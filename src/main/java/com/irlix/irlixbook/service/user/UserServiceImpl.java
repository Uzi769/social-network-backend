package com.irlix.irlixbook.service.user;

import com.coworking.demo.config.security.JwtProvider;
import com.coworking.demo.config.security.utils.SecurityContextUtils;
import com.coworking.demo.dao.entity.ActivationCode;
import com.coworking.demo.dao.entity.Role;
import com.coworking.demo.dao.entity.UserEntity;
import com.coworking.demo.dao.model.PageableInput;
import com.coworking.demo.dao.model.admin.UserCreateInput;
import com.coworking.demo.dao.model.admin.UserEntityForAdminOutput;
import com.coworking.demo.dao.model.admin.UserUpdateInput;
import com.coworking.demo.dao.model.auth.AuthRequest;
import com.coworking.demo.dao.model.user.UserEntityOutput;
import com.coworking.demo.dao.model.user.UserInputSearch;
import com.coworking.demo.dao.model.user.UserPasswordInput;
import com.coworking.demo.dao.model.user.UserPasswordThrow;
import com.coworking.demo.exception.BadRequestException;
import com.coworking.demo.exception.NotFoundException;
import com.coworking.demo.exception.PasswordActiveException;
import com.coworking.demo.repository.ActivationCodeRepository;
import com.coworking.demo.repository.RoleRepository;
import com.coworking.demo.repository.UserRepository;
import com.coworking.demo.repository.summary.UserRepositorySummary;
import com.coworking.demo.service.mail.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConversionService conversionService;
    private final UserRepositorySummary userRepositorySummary;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MailSenderService mailSenderService;
    private final ActivationCodeRepository activationCodeRepository;

    private final String USER_ROLE = "USER";

    @Override
    public void updatePassword(UserPasswordInput userPasswordInput) {
        UserEntity user = getUserFromContext();
        validPassword(userPasswordInput, user);
        user.setPassword(passwordEncoder.encode(userPasswordInput.getPassword()));
        user.setPasswordActive(true);
        userRepository.save(user);
        log.info("Create new password for user by id " + user.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<UserEntityForAdminOutput> getAllUsers(PageableInput pageable) {

        checkPageableOnNegativeParameters(pageable);

        Page<UserEntity> userList;
        Pageable pageableForSearch;

        pageableForSearch = pageable.isSort()
                ? PageRequest.of(pageable.getPage(), pageable.getSize(), Sort.by("fullName"))
                : PageRequest.of(pageable.getPage(), pageable.getSize(), Sort.by("fullName").descending());

        userList = userRepository.findAll(pageableForSearch);
        log.info("Create page list users with pageable. Class UserServiceImpl, method getAllUsers");

        if (userList.toList().isEmpty()) {
            throw new NotFoundException("Empty userList. Class UserServiceImpl, method getAllUsers");
        }

        List<UserEntityForAdminOutput> userDtoList = userList.stream()
                .map(userEntity -> conversionService
                        .convert(userEntity, UserEntityForAdminOutput.class))
                .collect(Collectors.toList());

        if (!userDtoList.isEmpty()) {
            log.info("Return List users with pagination. Class UserServiceImpl, method getAllUsers");
            return userDtoList;
        } else {
            throw new NotFoundException("Users not found");
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntity getUserEntity(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found by id " + id + ". Class UserServiceImpl, method getUserEntity");
            return new NotFoundException("User not found by id" + id);
        });
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntityForAdminOutput getUser(UUID id) {
        UserEntity userEntity = getUserEntity(id);
        UserEntityForAdminOutput userEntityForAdminOutput = conversionService.convert(userEntity, UserEntityForAdminOutput.class);
        if (userEntityForAdminOutput != null) {
            log.info("Return user by id: " + id + ". Class UserServiceImpl, method getUser");
            return userEntityForAdminOutput;
        } else {
            throw new NotFoundException("User not found by id: " + id);
        }
    }

    @Override
    @Transactional
    public void createUser(UserCreateInput dto) {
        if (dto == null) {
            throw new NotFoundException("Error input data. Class UserServiceImpl, method createUser");
        }
        UserEntity userEntity = conversionService.convert(dto, UserEntity.class);
        if (userEntity == null) {
            throw new NotFoundException("Error conversion user. Class UserServiceImpl, method createUser");
        }
        List<Role> roles = Collections.singletonList(roleRepository.findByName(USER_ROLE)
                .orElseThrow(() -> {
                    log.error("Role not found by name. Class UserServiceImpl, method createUser");
                    return new NotFoundException("Role not found by name");
                }));
        checkingMailForUniqueness(userEntity, dto.getEmail());
        String password = createPassword();
        mailSenderService.send(dto.getEmail(), mailSenderService.message(dto.getFullName(), password));
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setDelete(false);
        userEntity.setRoles(roles);
        userEntity.setMinutesForBooking(2400);
        userEntity.setPasswordActive(false);
        userRepository.save(userEntity);
        log.info("Save user. Class UserServiceImpl, method createUser");
    }

    @Override
    public void deleteUser(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found by id: " + id + ". Class UserServiceImpl, method deleteUser");
            return new NotFoundException("User not found by id: " + id);
        });

        userEntity.setDelete(true);
        userRepository.save(userEntity);
        log.info("Soft delete user by id: " + id + ". Class UserServiceImpl, method deleteUser");
    }

    @Override
    @Transactional
    public void updateUser(UUID id, UserUpdateInput dto) {
        if (dto == null) {
            throw new NotFoundException("Error input data. Class UserServiceImpl, method updateUser");
        }
        UserEntity userEntity = getUserEntity(id);
        UserEntity userEntityForUpdate = conversionService.convert(dto, UserEntity.class);

        if (userEntityForUpdate == null) {
            throw new NotFoundException("Error conversion user. Class UserServiceImpl, method updateUser");
        }

        if (userEntityForUpdate.getEmail() != null && !userEntityForUpdate.getEmail().equals(userEntity.getEmail())) {
            checkingMailForUniqueness(userEntity, userEntityForUpdate.getEmail());
        }
        if (userEntityForUpdate.getFullName() != null) {
            userEntity.setFullName(userEntityForUpdate.getFullName());
        }
        if (userEntityForUpdate.getPhone() != null && !userEntityForUpdate.getPhone().equals(userEntity.getPhone())) {
            checkingPhoneForUniqueness(userEntity, userEntityForUpdate.getPhone());
        }
        if (userEntity.isDelete() != userEntityForUpdate.isDelete()) {
            userEntity.setDelete(userEntityForUpdate.isDelete());
        }
        if (dto.getRoleName() != null) {
            Role role = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> {
                        log.error("Role not found by name. Class UserServiceImpl, method updateUser");
                        return new NotFoundException("Role not found by name");
                    });
            if (!userEntity.getRoles().contains(role)) {
                userEntity.getRoles().add(role);
            } else {
                throw new BadRequestException("This user has already been assigned a role " + dto.getRoleName());
            }
        }
        userRepository.save(userEntity);
        log.info("Update user by id: " + id + ". Class UserServiceImpl, method updateUser");
    }

    @Override
    public String generateToken(AuthRequest request) {
        String email = request.getEmail();
        String phone = request.getPhone();
        String password = request.getPassword();

        UserEntity userEntity = new UserEntity();
        if (email != null) {
            userEntity = findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        }
        if (phone != null) {
            userEntity = userRepository.findByPhone(phone).orElseThrow(() -> new NotFoundException("User not found"));
        }
        if (passwordEncoder.matches(password, userEntity.getPassword()) && !userEntity.isDelete()) {
            return jwtProvider.generateToken(userEntity.getEmail());
        } else {
            throw new NotFoundException("User not active");
        }
    }

    @Override
    public UserEntityOutput getUserInfo() {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        if (!user.isPasswordActive()) {
            log.info("Need reset password for user by id " + user.getId());
            throw new PasswordActiveException("Reset your password");
        }
        return conversionService.convert(user, UserEntityOutput.class);
    }

    @Override
    public List<UserEntityForAdminOutput> searchWithPagination(UserInputSearch dto, PageableInput pageable) {
        List<UserEntity> userEntityList = userRepositorySummary.search(dto, pageable);
        return userEntityList.stream()
                .map(userEntity -> conversionService.convert(userEntity, UserEntityForAdminOutput.class))
                .collect(Collectors.toList());
    }

    private void checkPageableOnNegativeParameters(PageableInput pageable) {
        if (pageable.getPage() < 0 || pageable.getSize() < 0) {
            log.error("Page or size was negative. Class UserServiceImpl, method checkPageableOnNegativeParameters");
            throw new BadRequestException("Page or size can not be negative");
        }
    }

    private String createPassword() {
        return new Random().ints(8, 48, 122)
                .mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
    }

    private void checkingMailForUniqueness(UserEntity userEntity, String email) {
        if (ifEmailIsAlreadyOnUse(email)) {
            throw new BadRequestException("User with this email already exists");
        } else {
            userEntity.setEmail(email);
        }
    }

    private boolean ifEmailIsAlreadyOnUse(String email) {
        return userRepository.findByEmail(email).orElse(null) != null;
    }

    private void checkingPhoneForUniqueness(UserEntity userEntity, String phone) {
        if (isPhoneAlreadyOnUse(phone)) {
            throw new BadRequestException("User with this phone already exists");
        } else {
            userEntity.setPhone(phone);
        }
    }

    private boolean isPhoneAlreadyOnUse(String phone) {
        return userRepository.findByPhone(phone).orElse(null) != null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createActivationCode (UserEntity user) {
        ActivationCode activationCode = new ActivationCode();
        activationCode.setUserEntity(user);
        activationCodeRepository.save(activationCode);
        user.setPasswordActive(false);
        userRepository.save(user);
    }

    @Override
    public void updatePasswordByAdmin(UserPasswordThrow userPasswordThrow) {
        UserEntity user = getUserEntity(userPasswordThrow.getUserId());
        createActivationCode(user);
        ActivationCode code = activationCodeRepository.findByUserEntity_Id(user.getId());
        String s = code.getId().toString();
        mailSenderService.send(user.getEmail(),
                mailSenderService.passwordUpdateMessage(user.getFullName(), s));
    }

    @Override
    @Transactional
    public void updatePasswordByLink(UUID id, UserPasswordInput userPasswordInput) {
        ActivationCode activationCode = activationCodeRepository.findById(id).orElseThrow(() -> {
            log.error("Activation code not found by id: " + id + ". Class UserServiceImpl, method updatePasswordByLink");
            return new NotFoundException("Activation code not found by id: " + id);
        });
        UserEntity user = activationCode.getUserEntity();
        validPassword(userPasswordInput, user);

        user.setPassword(passwordEncoder.encode(userPasswordInput.getPassword()));
        user.setPasswordActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        activationCodeRepository.delete(activationCode);
        log.info("Create new password for user by id " + user.getId());
    }

    private void validPassword(UserPasswordInput userPasswordInput, UserEntity user) {
        if (userPasswordInput == null) {
            log.error("Incorrect password for user by id " + user.getId());
            throw new BadRequestException("Incorrect password");
        }
        if (!userPasswordInput.getPassword().equals(userPasswordInput.getVerificationPassword())) {
            log.error("Passwords mismatch for user by id " + user.getId());
            throw new BadRequestException("Passwords mismatch");
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getUserFromContext() {
        return SecurityContextUtils.getUserFromContext();
    }
}
