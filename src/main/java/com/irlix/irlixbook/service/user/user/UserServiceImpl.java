package com.irlix.irlixbook.service.user.user;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.*;
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

import javax.annotation.PostConstruct;
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
    private final ContentUserRepository contentUserRepository;
    private final RoleStatusUserCommunityRepository roleStatusUserCommunityRepository;
    private final CommunityRepository communityRepository;
    private final StatusRepository statusRepository;

    @Autowired
    @Qualifier("mailSenderImpl")
    private MessageSender messageSender;

    @PostConstruct
    public void init() {

        List<User> users = roleStatusUserCommunityRepository.findByCommunityName("start").stream()
                .map(RoleStatusUserCommunity::getUser).collect(Collectors.toList());

        List<User> allUsers = userRepository.findAll();

        boolean raper = allUsers.removeAll(users);

        if (raper & allUsers.size() != 0) {
            for (User user : allUsers) {
                RoleStatusUserCommunity roleStatusUserCommunity = RoleStatusUserCommunity.builder()
                        .role(user.getRole())
                        .status(statusRepository.findByName(StatusEnum.NEW_MEMBER))
                        .user(user)
                        .community(communityRepository.findByName("start"))
                        .Id(new RoleStatusUserCommunityId(
                                user.getRole().getId(),
                                statusRepository.findByName(StatusEnum.NEW_MEMBER).getId(),
                                user.getId(),
                                communityRepository.findByName("start").getId()))
                        .build();
                roleStatusUserCommunityRepository.save(roleStatusUserCommunity);
                User savedUser = userRepository.save(user);
                log.info(allUsers.size() + " users added to \"start\" community");
            }
        }
    }

    @Override
    public User findById(UUID id) {
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
        User user = findById(id);

        return conversionService.convert(user, UserEntityOutput.class);

    }

    @Override
    public List<UserEntityOutput> findByComplexQuery(UserSearchInput input) {
        List<User> users = userRepositorySummary.search(input);
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
        return userRepository.findAll()
                .stream()
                .map(user -> conversionService.convert(user, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserEntityOutput updateUser(UserUpdateInput userUpdateInput, UUID id) {

        User userEntity;
        if (id == null) {
            userEntity = SecurityContextUtils.getUserFromContext();
        } else {
            userEntity = findById(id);
        }

        User userForUpdate = conversionService.convert(userUpdateInput, User.class);
        if (userForUpdate == null) {
            throw new BadRequestException(CONVERSION_ERROR);
        }

        checkingUpdatedData(userEntity, userForUpdate);
        if (userUpdateInput.getRole() != null) {
            roleRepository.findByName(userUpdateInput.getRole())
                    .ifPresent(userEntity::setRole);
        }

        User user = userRepository.save(userEntity);
        log.info(USER_UPDATED);
        return conversionService.convert(user, UserEntityOutput.class);

    }

    @Override
    @Transactional
    public UserEntityOutput blockedUser(UUID id) {

        User user = findById(id);
        user.setBlocked(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info(USER_BLOCKED);
        return conversionService.convert(savedUser, UserEntityOutput.class);

    }

    @Override
    @Transactional
    public UserEntityOutput unblockedUser(UUID id) {

        User user = findById(id);
        user.setBlocked(null);
        User savedUser = userRepository.save(user);
        log.info(USER_UNBLOCKED);
        return conversionService.convert(savedUser, UserEntityOutput.class);

    }

    @Override
    @Transactional
    public UserEntityOutput deletedUser(UUID id) {

        User user = findById(id);
        Collection<User> forDelete = new HashSet<>();
        forDelete.add(user);

        if (user != null) {
            userRepository.save(user);
            deleteRoleStatusUserCommunities(user);
            userRepository.deleteInBatch(forDelete);
            userRepository.flush();
            log.info(USER_DELETED);
            return conversionService.convert(user, UserEntityOutput.class);
        } else {
            throw new NotFoundException("User with id " + id + " not found.");
        }

    }

    @Override
    public List<UserEntityOutput> search(String surname, String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        List<User> userList;

        if (StringUtils.hasLength(surname)) {
            userList = userRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else {
            userList = userRepository.findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(name, surname, pageRequest);
        }

        return userList.stream()
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public UserEntityOutput assignRole(RoleEnum roleEnum) {

        User user = SecurityContextUtils.getUserFromContext();
        Optional<User> persistedUserOptional = userRepository.findById(user.getId());

        if (persistedUserOptional.isPresent()) {
            User persistedUser = persistedUserOptional.get();

            roleRepository.findByName(roleEnum).ifPresent(user::setRole);
            userRepository.save(user);
            log.info("User : {} assigned role: {}", persistedUser.getEmail(), roleEnum);
//            StatusEnum newStatus = roleEnum.getStatus(persistedUser.getRegistrationDate());
//            persistedUser.setStatus(newStatus);

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
    public User findUserForAuth(AuthRequest request) {

        String email = request.getEmail();

        if (email != null) {
            return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        }

        return null;

    }

    @Override
    @Transactional
    public User addFavorites(Long favoritesContentId) {

        Content content = contentService.findByIdOriginal(favoritesContentId);
        User userFromContext = SecurityContextUtils.getUserFromContext();
        Optional<User> currentUser = userRepository.findById(userFromContext.getId());

        if (currentUser.isPresent()) {

            User user = currentUser.get();
            List<ContentUser> contentUsers = user.getContentUsers();
            List<Content> favoritesContents = contentUsers != null ? contentUsers.stream().map(ContentUser::getContent).collect(Collectors.toList()) : new ArrayList<>();
            boolean alreadyFavorites = favoritesContents.stream().anyMatch(c -> c.getId().equals(favoritesContentId));

            if (alreadyFavorites) {
                throw new BadRequestException("Content with id: " + favoritesContentId + " already added to favorites");
            } else {

                contentUserRepository.save(ContentUser.builder()
                        .content(content)
                        .user(user)
                        .createdOn(LocalDateTime.now())
                        .id(new ContentUserId(content.getId(), user.getId()))
                        .build());
                return userRepository.findById(userFromContext.getId()).get();

            }

        } else {
            throw new NotFoundException("User with id : " + userFromContext.getId() + " doesn't exist");
        }

    }

    @Override
    @Transactional
    public User deleteFavorites(Long favoritesContentId) {

        Content content = contentService.findByIdOriginal(favoritesContentId);

        if (content != null) {

            User userFromContext = SecurityContextUtils.getUserFromContext();
            Optional<User> currentUser = userRepository.findById(userFromContext.getId());

            if (currentUser.isPresent()) {

                User user = currentUser.get();
                List<ContentUser> contentUsers = user.getContentUsers();

                Optional<ContentUser> first = contentUsers.stream().filter(c -> c.getContent().getId().equals(favoritesContentId)).findFirst();

                if (first.isPresent()) {

                    ContentUser contentUser = first.get();
                    contentUsers.remove(contentUser);
                    content.getContentUsers().remove(contentUser);
                    contentUser.setContent(null);
                    contentUser.setUser(null);
                    return userRepository.save(user);

                } else {
                    throw new BadRequestException("Content with id: " + favoritesContentId + " is not favorite");
                }

            } else {
                throw new NotFoundException("User with id : " + userFromContext.getId() + " doesn't exist");
            }

        } else {
            throw new NotFoundException("Content with id : " + favoritesContentId + " doesn't exist");
        }

    }

    @Scheduled(cron = "1 1 */1 * * *")
    public void deleteUsers() {

        LocalDateTime blockedDate = LocalDateTime.now().minusMonths(2);
        List<User> blockedUsers = userRepository.findByBlockedLessThanEqual(blockedDate);

        for (User user : blockedUsers) {
            deleteRoleStatusUserCommunities(user);
        }
        userRepository.deleteAll(blockedUsers);
        userRepository.flush();
        log.info("DELETE {} USERS how was blocked : {}", blockedUsers.size(), blockedDate);

    }

    @Scheduled(cron = "1 1 */1 * * *")
    public void changeStatus() {

        LocalDateTime limitDate = LocalDateTime.now().minusMonths(2).plusDays(1);
        List<RoleStatusUserCommunity> users = roleStatusUserCommunityRepository.findByDateJoinedBefore(limitDate);

        users.forEach(u -> u.setStatus(statusRepository.findByName(StatusEnum.COMMUNITY_MEMBER)));
        roleStatusUserCommunityRepository.saveAll(users);
    }

    private void checkingPhoneForUniqueness(User user, String phone) {

        if (userRepository.findByPhone(phone).orElse(null) != null) { //todo add exception handling if a lot users with same number
            throw new BadRequestException(USER_WITH_PHONE_ALREADY_EXISTS);
        } else {
            user.setPhone(phone);
        }

    }

    private void checkingUpdatedData(User user, User userForUpdate) {

        if (userForUpdate.getSurname() != null && !userForUpdate.getSurname().equals(user.getSurname())) {
            user.setSurname(userForUpdate.getSurname());
        }
        if (userForUpdate.getName() != null && !userForUpdate.getName().equals(user.getName())) {
            user.setName(userForUpdate.getName());
        }
        if (userForUpdate.getPhone() != null && !userForUpdate.getPhone().equals(user.getPhone())) {
            checkingPhoneForUniqueness(user, userForUpdate.getPhone());
        }
        if (userForUpdate.getSkype() != null && !userForUpdate.getSkype().equals(user.getSkype())) {
            user.setSkype(userForUpdate.getSkype());
        }
        if (userForUpdate.getBirthDate() != null && !userForUpdate.getBirthDate().equals(user.getBirthDate())) {
            user.setBirthDate(userForUpdate.getBirthDate());
        }
        if (userForUpdate.getEmail() != null & !userForUpdate.getEmail().equals(user.getEmail())) {
            checkingMailForUniqueness(user, userForUpdate.getEmail());
        }
        if (userForUpdate.getGender() != null && !userForUpdate.getGender().equals(user.getGender())) {
            user.setGender(userForUpdate.getGender());
        }
        if (userForUpdate.getDescription() != null && !userForUpdate.getDescription().equals(user.getDescription())) {
            user.setDescription(userForUpdate.getDescription());
        }

        if (userForUpdate.getVk() != null && !userForUpdate.getVk().equals(user.getVk())) {
            user.setVk(userForUpdate.getVk());
        }
        if (userForUpdate.getFaceBook() != null && !userForUpdate.getFaceBook().equals(user.getFaceBook())) {
            user.setFaceBook(userForUpdate.getFaceBook());
        }
        if (userForUpdate.getSkype() != null && !userForUpdate.getSkype().equals(user.getSkype())) {
            user.setSkype(userForUpdate.getSkype());
        }
        if (userForUpdate.getTelegram() != null && !userForUpdate.getTelegram().equals(user.getTelegram())) {
            user.setTelegram(userForUpdate.getTelegram());
        }
        if (userForUpdate.getInstagram() != null && !userForUpdate.getInstagram().equals(user.getInstagram())) {
            user.setInstagram(userForUpdate.getInstagram());
        }
        if (userForUpdate.getLinkedIn() != null && !userForUpdate.getLinkedIn().equals(user.getLinkedIn())) {
            user.setLinkedIn(userForUpdate.getLinkedIn());
        }

    }

    private UserEntityOutput create(UserCreateInput userCreateInput) {

        User user = conversionService.convert(userCreateInput, User.class);

        if (user == null) {
            throw new ConflictException("Error conversion user. Class UserServiceImpl, method createUser");
        }

        checkingMailForUniqueness(user, userCreateInput.getEmail());
        String password = createPassword();
        messageSender.send("Your password", user.getEmail(), "Your password: " + password);
        user.setPassword(passwordEncoder.encode(password));

        roleRepository.findByName(userCreateInput.getRole()).ifPresent(user::setRole);
        Role role = user.getRole();

        userRepository.save(user);

        RoleStatusUserCommunity roleStatusUserCommunity = RoleStatusUserCommunity.builder()
                .role(role)
                .status(statusRepository.findByName(StatusEnum.NEW_MEMBER))
                .user(user)
                .community(communityRepository.findByName("start"))
                .Id(new RoleStatusUserCommunityId(
                        role.getId(),
                        statusRepository.findByName(StatusEnum.NEW_MEMBER).getId(),
                        user.getId(),
                        communityRepository.findByName("start").getId()))
                .build();
        roleStatusUserCommunityRepository.save(roleStatusUserCommunity);

        user.setRegistrationDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info(USER_SAVED);
        return conversionService.convert(savedUser, UserEntityOutput.class);

    }

    private void checkingMailForUniqueness(User user, String email) {

        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new BadRequestException(USER_WITH_EMAIL_ALREADY_EXISTS);
        } else {
            user.setEmail(email);
        }
    }

    private String createPassword() {
        return new Random().ints(8, 48, 57)
                .mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
    }

    @Override
    public List<RoleStatusUserCommunity> addUsersToRoleStatusUserCommunity(List<UUID> usersIdList,
                                                                Community community) {
        List<RoleStatusUserCommunity> roleStatusUserCommunities = new ArrayList<>();
        List<UUID> allUsersOfCommunity = roleStatusUserCommunityRepository.findByCommunityName(community.getName()).stream()
                        .map(r -> r.getUser().getId()).collect(Collectors.toList());
        usersIdList.removeAll(allUsersOfCommunity);

        if (usersIdList.size() != 0) {
            usersIdList.stream()
                    .map(id -> userRepository.findById(id).orElseThrow(() -> {
                        log.error(USER_NOT_FOUND);
                        return new ConflictException(USER_NOT_FOUND);
                    }))
                    .forEach(user -> {
                        RoleStatusUserCommunity roleStatusUserCommunity = RoleStatusUserCommunity.builder()
                                .role(user.getRole())
                                .status(statusRepository.findByName(StatusEnum.NEW_MEMBER))
                                .user(user)
                                .community(community)
                                .Id(new RoleStatusUserCommunityId(
                                        user.getRole().getId(),
                                        statusRepository.findByName(StatusEnum.NEW_MEMBER).getId(),
                                        user.getId(),
                                        community.getId()))
                                .build();
                        roleStatusUserCommunityRepository.save(roleStatusUserCommunity);
                        userRepository.save(user);
                        roleStatusUserCommunities.add(roleStatusUserCommunity);
                    });
        } else {
            log.error("All these users are members of community.");
            throw new BadRequestException("All these users are members of community.");
        }
        return roleStatusUserCommunities;
    }

    private void deleteRoleStatusUserCommunities(User user) {
        List<RoleStatusUserCommunity> forDelete = roleStatusUserCommunityRepository.findByUserName(user.getName());
        roleStatusUserCommunityRepository.deleteInBatch(forDelete);
    }
}
