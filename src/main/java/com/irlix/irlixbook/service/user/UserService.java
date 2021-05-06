package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordThrow;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateByAdminInput;
import com.irlix.irlixbook.dao.model.user.output.UserBirthdaysOutput;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;

import java.util.List;

public interface UserService {

    List<UserBirthdaysOutput> findUserWithBirthDays();

    UserEntity findById(Long id);

    UserEntityOutput findUserOutputById(Long id);

    void deleteUser(Long id);

    UserEntity findUserForAuth(AuthRequest request);

    UserEntityOutput findUserInfo();

    List<UserEntityOutput> searchWithPagination(UserSearchInput userSearchInput, PageableInput pageable);

    List<UserEntityOutput> findUserEntityList();

    void createUser(UserCreateInput userCreateInput);

    void createModerator(UserCreateInput userCreateInput);

    void updatePasswordByAdmin(UserPasswordThrow userPasswordThrow);

    void updatePasswordByUser(UserPasswordInput userPasswordInput);

    void updateUserByUser(UserUpdateInput userUpdateInput);

    void updateUserByAdmin(UserUpdateByAdminInput userUpdateInput);
}
