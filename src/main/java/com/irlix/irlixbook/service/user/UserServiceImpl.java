package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserBirthdaysOutput;
import com.irlix.irlixbook.dao.model.user.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.UserUpdateInput;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.RoleRepository;
import com.irlix.irlixbook.repository.UserRepository;
import com.irlix.irlixbook.repository.summary.UserRepositorySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRepositorySummary userRepositorySummary;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_ROLE = "USER";
    private static final String USER_NOT_FOUND = "User not found";

    @Override
    public List<UserBirthdaysOutput> getUserWithBirthDays() {
        return userRepository.findByBirthDate()
                .stream()
                .map(user -> conversionService.convert(user, UserBirthdaysOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntityOutput getUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .orElseThrow(() -> {
                    log.error(USER_NOT_FOUND);
                    return new NotFoundException(USER_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            log.error(USER_NOT_FOUND);
            return new NotFoundException(USER_NOT_FOUND);
        });

        userEntity.setDelete(true);
        userRepository.save(userEntity);
        log.info("Soft delete user. Class UserServiceImpl, method deleteUser");
    }

    @Override
    public UserEntity findUserForAuth(AuthRequest request) {
        String email = request.getEmail();
        String phone = request.getPhone();

        if (email != null) {
            return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        }
        if (phone != null) {
            return userRepository.findByPhone(phone).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        } else throw new BadRequestException("Email and Phone are null");
    }

    @Override
    public UserEntityOutput getUserInfo() {
        Long id = SecurityContextUtils.getUserFromContext().getId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return conversionService.convert(userEntity, UserEntityOutput.class);
    }

    @Override
    public List<UserEntityOutput> searchWithPagination(UserSearchInput userSearchInput, PageableInput pageable) {
        List<UserEntity> userEntityList = userRepositorySummary.search(userSearchInput, pageable);
        return userEntityList.stream()
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntityOutput> getUserEntityList() {
        return userRepository.findAll().stream()
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .collect(Collectors.toList());
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
    public void createUser(UserCreateInput userCreateInput) {
        UserEntity userEntity = conversionService.convert(userCreateInput, UserEntity.class);
        if (userEntity == null) {
            throw new NotFoundException("Error conversion user. Class UserServiceImpl, method createUser");
        }
        List<Role> roles = Collections.singletonList(roleRepository.findByName(USER_ROLE)
                .orElseThrow(() -> {
                    log.error("Role not found by name. Class UserServiceImpl, method createUser");
                    return new NotFoundException("Role not found by name");
                }));
        checkingMailForUniqueness(userEntity, userCreateInput.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userCreateInput.getPassword()));
        userEntity.setDelete(false);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        log.info("Save user. Class UserServiceImpl, method createUser");
    }

    private void checkingMailForUniqueness(UserEntity userEntity, String email) {
        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new BadRequestException("User with this email already exists");
        } else {
            userEntity.setEmail(email);
        }
    }

    @Override
    @Transactional
    public void updatePassword(UserPasswordInput userPasswordInput) {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        validPassword(userPasswordInput, user);
        user.setPassword(passwordEncoder.encode(userPasswordInput.getPassword()));
        userRepository.save(user);
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
    @Transactional
    public void updateUser(UserUpdateInput userUpdateInput) {
        if (userUpdateInput == null) {
            throw new NotFoundException("Error input data. Class UserServiceImpl, method updateUser");
        }
        UserEntity userEntity = SecurityContextUtils.getUserFromContext();
        UserEntity userEntityForUpdate = conversionService.convert(userUpdateInput, UserEntity.class);
        if (userEntityForUpdate == null) {
            throw new NotFoundException("Error conversion user. Class UserServiceImpl, method updateUser");
        }
        if (userEntityForUpdate.getEmail() != null && !userEntityForUpdate.getEmail().equals(userEntity.getEmail())) {
            checkingMailForUniqueness(userEntity, userEntityForUpdate.getEmail());
        }
        if (userEntityForUpdate.getPhone() != null && !userEntityForUpdate.getPhone().equals(userEntity.getPhone())) {
            checkingPhoneForUniqueness(userEntity, userEntityForUpdate.getPhone());
        }
        if (userEntityForUpdate.getFullName() != null) {
            userEntity.setFullName(userEntityForUpdate.getFullName());
        }
        if (userEntityForUpdate.getBirthDate() != null) {
            userEntity.setBirthDate(userEntityForUpdate.getBirthDate());
        }
        if (userEntityForUpdate.getCity() != null) {
            userEntity.setCity(userEntityForUpdate.getCity());
        }
        if (userEntityForUpdate.getSkype() != null) {
            userEntity.setSkype(userEntityForUpdate.getSkype());
        }
        if (userEntityForUpdate.getTechnologies() != null) {
            userEntity.setTechnologies(userEntityForUpdate.getTechnologies());
        }
        if (userEntityForUpdate.getTelegram() != null) {
            userEntity.setTelegram(userEntityForUpdate.getTelegram());
        }
        if (userEntityForUpdate.getAnotherPhone() != null) {
            checkingAnotherPhoneForUniqueness(userEntity, userEntityForUpdate.getAnotherPhone());
        }
        userRepository.save(userEntity);
        log.info("Update user. Class UserServiceImpl, method updateUser");
    }

    private void checkingAnotherPhoneForUniqueness(UserEntity userEntity, String phone) {
        if (userRepository.findByAnotherPhone(phone).orElse(null) != null) {
            throw new BadRequestException("User with this phone already exists");
        } else {
            userEntity.setAnotherPhone(phone);
        }
    }

    private void checkingPhoneForUniqueness(UserEntity userEntity, String phone) {
        if (userRepository.findByPhone(phone).orElse(null) != null) {
            throw new BadRequestException("User with this phone already exists");
        } else {
            userEntity.setPhone(phone);
        }
    }
}
