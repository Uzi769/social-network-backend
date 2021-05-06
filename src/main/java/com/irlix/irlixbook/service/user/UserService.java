package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.output.UserBirthdaysOutput;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;

import java.util.List;

public interface UserService {

    List<UserBirthdaysOutput> getUserWithBirthDays();

    UserEntityOutput getUserById(Long id);

    void deleteUser(Long id);

    UserEntity findUserForAuth(AuthRequest request);

    UserEntityOutput getUserInfo();

    List<UserEntityOutput> searchWithPagination(UserSearchInput userSearchInputo, PageableInput pageable);

    List<UserEntityOutput> getUserEntityList();

    void createUser(UserCreateInput dto);

    void updatePassword(UserPasswordInput userPasswordInput);

    void updateUser(UserUpdateInput userUpdateInput);
}
