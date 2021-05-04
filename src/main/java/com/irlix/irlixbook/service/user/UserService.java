package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;

import java.util.Optional;

public interface UserService {

    UserEntity getUserEntity(Long id);

    void deleteUser(Long id);

    String generateToken(AuthRequest request);

    Optional<UserEntity> findByEmail(String email);

    UserEntityOutput getUserInfo();

}
