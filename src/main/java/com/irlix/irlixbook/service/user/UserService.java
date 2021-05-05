package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserBirthdaysOutput;
import com.irlix.irlixbook.dao.model.user.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;
import com.irlix.irlixbook.dao.model.user.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.UserUpdateInput;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserBirthdaysOutput> getUserWithBirthDays();

    UserEntityOutput getUserById(Long id);

    void deleteUser(Long id);

    String generateToken(AuthRequest request);

    Optional<UserEntity> findByEmail(String email);

    UserEntityOutput getUserInfo();

    List<UserEntityOutput> searchWithPagination(UserInputSearch dto, PageableInput pageable);

    List<UserEntityOutput> getUserEntityList();

    void createUser(UserCreateInput dto);

    void updatePassword(UserPasswordInput userPasswordInput);

    void updateUser(UserUpdateInput userUpdateInput);
}
