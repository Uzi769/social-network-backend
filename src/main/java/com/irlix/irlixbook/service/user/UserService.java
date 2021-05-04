package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserEntityForAdminOutput> getAllUsers(PageableInput pageable);

    UserEntityForAdminOutput getUser(UUID id);

    UserEntity getUserEntity(UUID id);

    void createUser(UserCreateInput dto);

    void deleteUser(UUID id);

    void updateUser(UUID id, UserUpdateInput dto);

    String generateToken(AuthRequest request);

    Optional<UserEntity> findByEmail(String email);

    UserEntityOutput getUserInfo();

    UserEntity getUserFromContext();
}
