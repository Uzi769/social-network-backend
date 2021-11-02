package com.irlix.irlixbook.service.auth;

import com.irlix.irlixbook.config.security.utils.JwtProvider;
import com.irlix.irlixbook.dao.entity.Token;
import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.auth.AuthResponse;
import com.irlix.irlixbook.dao.model.user.output.UserAuthOutput;
import com.irlix.irlixbook.dao.model.user.output.UserAuthResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.TokenRepository;
import com.irlix.irlixbook.service.user.appcode.UserAppCodeService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final ConversionService conversionService;
    private final UserAppCodeService userAppCodeService;

    @Override
    public AuthResponse authUser(AuthRequest request, String appCode) {
        String password = request.getPassword();
        UserEntity userEntity = userService.findUserForAuth(request);
        if (passwordEncoder.matches(password, userEntity.getPassword()) && userEntity.getBlocked() == null) {
            userAppCodeService.addNewCode(UserAppCode.builder()
                    .email(userEntity.getEmail())
                    .userId(userEntity.getId())
                    .codes(Collections.singleton(appCode))
                    .build());

            String value = JwtProvider.generateToken(userEntity.getEmail());
            if (tokenRepository.findByValue(value).isPresent()) {
                return AuthResponse.builder()
                        .token(value)
                        .userAuthResponse(conversionService.convert(userEntity, UserAuthResponse.class))
                        .build();
            }
            Token token = Token.builder()
                    .value(value)
                    .build();
            tokenRepository.save(token);
            return AuthResponse.builder()
                    .token(token.getValue())
                    .userAuthResponse(conversionService.convert(userEntity, UserAuthResponse.class))
                    .build();
        } else throw new BadRequestException("User not active or Wrong Password");
    }

    @Override
    @Transactional
    public void logout(String value) {
        Token token = tokenRepository.findByValue(value.substring(7))
                .orElseThrow(() -> new NotFoundException("There is no token"));
        tokenRepository.delete(token);
    }

}
