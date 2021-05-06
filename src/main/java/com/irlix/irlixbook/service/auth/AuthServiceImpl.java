package com.irlix.irlixbook.service.auth;

import com.irlix.irlixbook.config.security.utils.JwtProvider;
import com.irlix.irlixbook.dao.entity.Token;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.TokenRepository;
import com.irlix.irlixbook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;


    @Override
    public String authUser(AuthRequest request) {
        String password = request.getPassword();
        UserEntity userEntity = userService.findUserForAuth(request);
        if (passwordEncoder.matches(password, userEntity.getPassword()) && !userEntity.isDelete()) {
            String value = JwtProvider.generateToken(userEntity.getEmail());
            if (tokenRepository.findByValue(value).isPresent()) {
                return value;
            }
            Token token = Token.builder()
                    .value(value)
                    .build();
            tokenRepository.save(token);
            return token.getValue();
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
