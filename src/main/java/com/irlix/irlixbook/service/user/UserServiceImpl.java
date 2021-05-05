package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.config.security.utils.JwtProvider;
import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.RoleRepository;
import com.irlix.irlixbook.repository.UserRepository;
import com.irlix.irlixbook.repository.summary.UserRepositorySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Override
    public UserEntity getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found");
                    return new NotFoundException("User not found");
                });
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntityOutput getUserEntity(Long id) {
        return userRepository.findById(id)
                .map(userEntity -> conversionService.convert(userEntity, UserEntityOutput.class))
                .orElseThrow(() -> {
                    log.error("User not found by id " + id + ". Class UserServiceImpl, method getUserEntity");
                    return new NotFoundException("User not found by id" + id);
                });
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found by id: " + id + ". Class UserServiceImpl, method deleteUser");
            return new NotFoundException("User not found by id: " + id);
        });

        userEntity.setDelete(true);
        userRepository.save(userEntity);
        log.info("Soft delete user by id: " + id + ". Class UserServiceImpl, method deleteUser");
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
            return JwtProvider.generateToken(userEntity.getEmail());
        } else {
            throw new NotFoundException("Wrong password");
        }
    }

    @Override
    public UserEntityOutput getUserInfo() {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        return conversionService.convert(user, UserEntityOutput.class);
    }

    @Override
    public List<UserEntityOutput> searchWithPagination(UserInputSearch dto, PageableInput pageable) {
        List<UserEntity> userEntityList = userRepositorySummary.search(dto, pageable);
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
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not find by email " + email);
            return new UsernameNotFoundException("User not find by email " + email);
        });
        userDetails.getAuthorities();
        return userDetails;
    }
}
