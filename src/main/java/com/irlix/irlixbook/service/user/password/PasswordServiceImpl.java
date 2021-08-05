package com.irlix.irlixbook.service.user.password;

import com.irlix.irlixbook.dao.entity.PasswordRecoveryEntity;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.PasswordRecoveryRepository;
import com.irlix.irlixbook.repository.UserRepository;
import com.irlix.irlixbook.service.messaging.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.irlix.irlixbook.utils.Consts.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    @Qualifier("mailSenderImpl")
    private MessageSender messageSender;

    @Override
    @Transactional
    public UserEntityOutput updatePasswordByAdmin(UUID id, UserPasswordInput input) {
        UserEntity user = findById(id);
        updatePassword(input.getPassword(), input.getVerificationPassword(), user);
        log.info(PASSWORD_RESET);
        messageSender.send("New password", user.getEmail(), "Your new password: " + input.getPassword());
        return conversionService.convert(user, UserEntityOutput.class);
    }


    @Override
    @Transactional
    public UserEntityOutput updatePasswordByUser(UserPasswordWithCodeInput input) {
        Optional<PasswordRecoveryEntity> byId = passwordRecoveryRepository.findById(input.getCode());
        if (byId.isPresent()) {
            PasswordRecoveryEntity entity = byId.get();
            UserEntity user = findById(entity.getUserId());
            updatePassword(input.getPassword(), input.getVerificationPassword(), user);
            log.info(PASSWORD_CHANGED);
            passwordRecoveryRepository.delete(entity);
            messageSender.send("New password", user.getEmail(), "Your new password: " + input.getPassword());
            return conversionService.convert(user, UserEntityOutput.class);
        } else {
            throw new BadRequestException("Code is not valid");
        }
    }

    @Override
    @Transactional
    public void sendGeneratedCode(String email) {
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            UserEntity userEntity = byEmail.get();
            String code = this.generateCode(email);
            PasswordRecoveryEntity recoveryEntity = PasswordRecoveryEntity.builder()
                    .email(userEntity.getEmail())
                    .userId(userEntity.getId())
                    .createDate(LocalDateTime.now())
                    .id(code)
                    .build();
            passwordRecoveryRepository.save(recoveryEntity);
            messageSender.send("Recovery password", email, "Your code for recovery password: " + code);
        } else {
            throw new NotFoundException("User with email: " + email + " not found");
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void deleteUsers() {
        LocalDateTime deleteDate = LocalDateTime.now().minusDays(1);
        List<PasswordRecoveryEntity> list = passwordRecoveryRepository.findByCreateDateLessThan(deleteDate);
        passwordRecoveryRepository.deleteAll(list);
        log.info("DELETE {} PASSWORD RECOVERY: {}", list.size(), deleteDate);
    }

    private String generateCode(String email) {
        String encode = passwordEncoder.encode(email);
        return encode.replaceAll("[^A-Za-z]", "").toLowerCase();
    }

    private void updatePassword(String pwd, String pwdVerify, UserEntity user) {
        validPassword(pwd, pwdVerify);
        user.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(user);
    }

    private void validPassword(String pwd, String pwdVerify) {
        if (pwd == null || pwdVerify == null) {
            log.error(INCORRECT_PASSWORD);
            throw new BadRequestException(INCORRECT_PASSWORD);
        }
        if (!pwd.equals(pwdVerify)) {
            log.error(MISMATCH_PASSWORDS);
            throw new BadRequestException(MISMATCH_PASSWORDS);
        }
    }

    private UserEntity findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error(USER_NOT_FOUND);
            return new ConflictException(USER_NOT_FOUND);
        });
    }
}
