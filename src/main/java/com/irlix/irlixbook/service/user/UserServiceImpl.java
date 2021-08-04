package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.entity.enams.StatusEnam;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.RoleRepository;
import com.irlix.irlixbook.repository.UserRepository;
import com.irlix.irlixbook.repository.summary.UserRepositorySummary;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.messaging.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.irlix.irlixbook.utils.Consts.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;
    private final ContentService contentService;
    private final UserRepositorySummary userRepositorySummary;

    @Autowired
    @Qualifier("mailSenderImpl")
    private MessageSender messageSender;

    @Override
    public UserEntity findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error(USER_NOT_FOUND);
            return new ConflictException(USER_NOT_FOUND);
        });
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntityOutput findUserOutputById(UUID id) {
        return userRepository.findById(id)
                .map(user -> conversionService.convert(user, UserEntityOutput.class))
                .orElseThrow(() -> {
                    log.error(USER_NOT_FOUND);
                    return new ConflictException(USER_NOT_FOUND);
                });
    }

    @Override
    public UserEntityOutput findUserFromContext() {
        UUID id = SecurityContextUtils.getUserFromContext().getId();
        UserEntity user = findById(id);
        return conversionService.convert(user, UserEntityOutput.class);
    }

    @Override
    public List<UserEntityOutput> findByComplexQuery(UserSearchInput input) {
        List<UserEntity> users = userRepositorySummary.search(input);
        return users.stream()
                .map(user -> conversionService.convert(user, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEntityOutput createUser(UserCreateInput userCreateInput) {
        return create(userCreateInput);
    }

    @Override
    public List<UserEntityOutput> findUsers() {
        return userRepository.findAll().stream()
                .filter(userEntity -> userEntity.getBlocked() == null)
                .map(user -> conversionService.convert(user, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEntityOutput updateUser(UserUpdateInput userUpdateInput, UUID id) {
        UserEntity userEntity;
        if (id == null) {
            userEntity = SecurityContextUtils.getUserFromContext();
        } else {
            userEntity = findById(id);
        }

        UserEntity userEntityForUpdate = conversionService.convert(userUpdateInput, UserEntity.class);
        if (userEntityForUpdate == null) {
            throw new BadRequestException(CONVERSION_ERROR);
        }

        checkingUpdatedData(userEntity, userEntityForUpdate);
        if (userUpdateInput.getRole() != null) {
            roleRepository.findByName(userUpdateInput.getRole())
                    .ifPresent(userEntity::setRole);
        }

        UserEntity user = userRepository.save(userEntity);
        log.info(USER_UPDATED);
        return conversionService.convert(user, UserEntityOutput.class);
    }

    @Override
    @Transactional
    public UserEntityOutput blockedUser(UUID id) {
        UserEntity userEntity = findById(id);
        userEntity.setBlocked(LocalDateTime.now());
        UserEntity savedUser = userRepository.save(userEntity);
        log.info(USER_BLOCKED);
        return conversionService.convert(savedUser, UserEntityOutput.class);
    }

    @Override
    @Transactional
    public UserEntityOutput unblockedUser(UUID id) {
        UserEntity userEntity = findById(id);
        userEntity.setBlocked(null);
        UserEntity savedUser = userRepository.save(userEntity);
        log.info(USER_UNBLOCKED);
        return conversionService.convert(savedUser, UserEntityOutput.class);
    }

    @Override
    @Transactional
    public UserEntityOutput deletedUser(UUID id) {
        UserEntity userEntity = findById(id);

        log.info(USER_DELETED);
        userRepository.delete(userEntity);
        return conversionService.convert(userEntity, UserEntityOutput.class);
    }

    @Override
    public List<UserEntityOutput> search(String surname, String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        List<UserEntity> userEntityList;
        if (StringUtils.hasLength(surname)) {
            userEntityList = userRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else {
            userEntityList = userRepository.findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname, pageRequest);
        }
        return userEntityList.stream()
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEntityOutput assignRole(RoleEnam roleEnam) {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        Optional<UserEntity> persistedUserOptional = userRepository.findById(user.getId());
        if (persistedUserOptional.isPresent()) {
            UserEntity persistedUser = persistedUserOptional.get();

            roleRepository.findByName(roleEnam).ifPresent(user::setRole);
            userRepository.save(user);
            log.info("User : {} assigned role: {}", persistedUser.getEmail(), roleEnam);
            StatusEnam newStatus = roleEnam.getStatus(persistedUser.getRegistrationDate());
            persistedUser.setStatus(newStatus);

            return conversionService.convert(persistedUser, UserEntityOutput.class);
        } else {
            log.error("User with id : {}, and email: {}, not found!!!", user.getId(), user.getEmail());
            throw new NotFoundException("User with id : " + user.getId() + ", and email: " + user.getEmail() + " not found!!!");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        UserDetails userDetails = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error(USER_NOT_FOUND);
            return new NotFoundException(USER_NOT_FOUND);
        });
        userDetails.getAuthorities();
        return userDetails;
    }

    @Override
    @Transactional
    public UserEntityOutput updatePasswordByAdmin(UUID id, UserPasswordInput userPasswordInput) {
        UserEntity user = findById(id);
        updatePassword(userPasswordInput, user);
        log.info(PASSWORD_RESET);
        messageSender.send("New password", user.getEmail(), "Your new password: " + userPasswordInput.getPassword());
        return conversionService.convert(user, UserEntityOutput.class);
    }


    @Override
    @Transactional
    public UserEntityOutput updatePasswordByUser(UserPasswordInput userPasswordInput) {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        updatePassword(userPasswordInput, user);
        log.info(PASSWORD_CHANGED);
        messageSender.send("New password", user.getEmail(), "Your new password: " + userPasswordInput.getPassword());
        return conversionService.convert(user, UserEntityOutput.class);
    }

    @Override
    public UserEntity findUserForAuth(AuthRequest request) {
        String email = request.getEmail();
        if (email != null) {
            return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        }
        return null;
    }

    @Override
    @Transactional
    public UserEntity addFavorites(Long favoritesContentId) {
        Content content = contentService.findByIdOriginal(favoritesContentId);
        UserEntity userFromContext = SecurityContextUtils.getUserFromContext();
        Optional<UserEntity> currentUser = userRepository.findById(userFromContext.getId());
        if (currentUser.isPresent()) {
            UserEntity userEntity = currentUser.get();
            List<Content> favoritesContents = userEntity.getFavoritesContents();
            favoritesContents = favoritesContents != null ? favoritesContents : new ArrayList<>();
            favoritesContents.add(content);
            userRepository.save(userEntity);
            return userEntity;
        } else {
            throw new NotFoundException("User with id : " + userFromContext.getId() + " doesn't exist");
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void deleteUsers() {
        LocalDateTime blockedDate = LocalDateTime.now().plusMonths(2);
        List<UserEntity> blockedUsers = userRepository.findByBlockedLessThanEqual(blockedDate);
        userRepository.deleteAll(blockedUsers);
        log.info("DELETE {} USERS how was blocked : {}", blockedUsers.size(), blockedDate);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void changeStatus() {
        LocalDateTime eventDate = LocalDateTime.now().plusMonths(2).plusDays(1);
        List<UserEntity> users = userRepository.findByRegistrationDateLessThan(eventDate);
        for (UserEntity user : users) {
            Role role = user.getRole();
            StatusEnam status = role.getName().getStatus(user.getRegistrationDate());
            user.setStatus(status);
        }
        userRepository.saveAll(users);
    }

    private void checkingPhoneForUniqueness(UserEntity userEntity, String phone) {
        if (userRepository.findByPhone(phone).orElse(null) != null) {
            throw new BadRequestException(USER_WITH_PHONE_ALREADY_EXISTS);
        } else {
            userEntity.setPhone(phone);
        }
    }

    private void checkingUpdatedData(UserEntity userEntity, UserEntity userEntityForUpdate) {
        if (userEntityForUpdate.getSurname() != null && !userEntityForUpdate.getSurname().equals(userEntity.getSurname())) {
            userEntity.setSurname(userEntityForUpdate.getSurname());
        }
        if (userEntityForUpdate.getName() != null && !userEntityForUpdate.getName().equals(userEntity.getName())) {
            userEntity.setName(userEntityForUpdate.getName());
        }
        if (userEntityForUpdate.getPhone() != null && !userEntityForUpdate.getPhone().equals(userEntity.getPhone())) {
            checkingPhoneForUniqueness(userEntity, userEntityForUpdate.getPhone());
        }
        if (userEntityForUpdate.getSkype() != null && !userEntityForUpdate.getSkype().equals(userEntity.getSkype())) {
            userEntity.setSkype(userEntityForUpdate.getSkype());
        }
        if (userEntityForUpdate.getBirthDate() != null && !userEntityForUpdate.getBirthDate().equals(userEntity.getBirthDate())) {
            userEntity.setBirthDate(userEntityForUpdate.getBirthDate());
        }
        if (userEntityForUpdate.getEmail() != null && !userEntityForUpdate.getEmail().equals(userEntity.getEmail())) {
            checkingMailForUniqueness(userEntity, userEntityForUpdate.getEmail());
        }
        if (userEntityForUpdate.getGender() != null && !userEntityForUpdate.getGender().equals(userEntity.getGender())) {
            userEntity.setGender(userEntityForUpdate.getGender());
        }
        if (userEntityForUpdate.getDescription() != null && !userEntityForUpdate.getDescription().equals(userEntity.getDescription())) {
            userEntity.setDescription(userEntityForUpdate.getDescription());
        }

        if (userEntityForUpdate.getVk() != null && !userEntityForUpdate.getVk().equals(userEntity.getVk())) {
            userEntity.setVk(userEntityForUpdate.getVk());
        }
        if (userEntityForUpdate.getFaceBook() != null && !userEntityForUpdate.getFaceBook().equals(userEntity.getFaceBook())) {
            userEntity.setFaceBook(userEntityForUpdate.getFaceBook());
        }
        if (userEntityForUpdate.getSkype() != null && !userEntityForUpdate.getSkype().equals(userEntity.getSkype())) {
            userEntity.setSkype(userEntityForUpdate.getSkype());
        }
        if (userEntityForUpdate.getTelegram() != null && !userEntityForUpdate.getTelegram().equals(userEntity.getTelegram())) {
            userEntity.setTelegram(userEntityForUpdate.getTelegram());
        }
        if (userEntityForUpdate.getInstagram() != null && !userEntityForUpdate.getInstagram().equals(userEntity.getInstagram())) {
            userEntity.setInstagram(userEntityForUpdate.getInstagram());
        }
        if (userEntityForUpdate.getLinkedIn() != null && !userEntityForUpdate.getLinkedIn().equals(userEntity.getLinkedIn())) {
            userEntity.setLinkedIn(userEntityForUpdate.getLinkedIn());
        }
    }

    private UserEntityOutput create(UserCreateInput userCreateInput) {
        UserEntity userEntity = conversionService.convert(userCreateInput, UserEntity.class);
        if (userEntity == null) {
            throw new ConflictException("Error conversion user. Class UserServiceImpl, method createUser");
        }

        checkingMailForUniqueness(userEntity, userCreateInput.getEmail());
        String password = createPassword();
        messageSender.send("Your password", userEntity.getEmail(), "Your password: " + password);
        userEntity.setPassword(passwordEncoder.encode(password));

        roleRepository.findByName(userCreateInput.getRole()).ifPresent(userEntity::setRole);

        Role role = userEntity.getRole();
        StatusEnam status = role.getName().getStatus(null);
        userEntity.setStatus(status);
        userEntity.setRegistrationDate(LocalDateTime.now());
        UserEntity savedUser = userRepository.save(userEntity);
        log.info(USER_SAVED);
        return conversionService.convert(savedUser, UserEntityOutput.class);
    }

    private void checkingMailForUniqueness(UserEntity userEntity, String email) {
        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new BadRequestException(USER_WITH_EMAIL_ALREADY_EXISTS);
        } else {
            userEntity.setEmail(email);
        }
    }

    private String createPassword() {
        return new Random().ints(8, 48, 57)
                .mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
    }

    private void updatePassword(UserPasswordInput userPasswordThrow, UserEntity user) {
        validPassword(userPasswordThrow);
        user.setPassword(passwordEncoder.encode(userPasswordThrow.getPassword()));
        userRepository.save(user);
    }

    private void validPassword(UserPasswordInput userPasswordInput) {
        if (userPasswordInput.getPassword() == null || userPasswordInput.getVerificationPassword() == null) {
            log.error(INCORRECT_PASSWORD);
            throw new BadRequestException(INCORRECT_PASSWORD);
        }
        if (!userPasswordInput.getPassword().equals(userPasswordInput.getVerificationPassword())) {
            log.error(MISMATCH_PASSWORDS);
            throw new BadRequestException(MISMATCH_PASSWORDS);
        }
    }
}
