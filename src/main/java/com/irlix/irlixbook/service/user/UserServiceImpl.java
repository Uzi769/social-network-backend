package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.config.security.utils.JwtProvider;
import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.RoleRepository;
import com.irlix.irlixbook.repository.UserRepository;
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

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;
    private final String USER_ROLE = "USER";

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
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
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not find by email " + email);
            return new UsernameNotFoundException("User not find by email " + email);});
        userDetails.getAuthorities();
        return userDetails;
    }
}
