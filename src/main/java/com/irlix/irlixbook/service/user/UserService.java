package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity findById(UUID id);

    UserEntityOutput findUserOutputById(UUID id);

    UserEntityOutput findUserFromContext();

    List<UserEntityOutput> findUsers();

    UserEntityOutput createUser(UserCreateInput userCreateInput);

    UserEntityOutput updateUser(UserUpdateInput userUpdateInput, UUID id);

    UserEntityOutput blockedUser(UUID id);

    UserEntityOutput deletedUser(UUID id);







    UserEntity findUserForAuth(AuthRequest request);


    List<UserEntityOutput> searchWithPagination(UserSearchInput userSearchInput, PageableInput pageable);


    void createModerator(UserCreateInput userCreateInput);

    void updatePasswordByAdmin(UserPasswordThrow userPasswordThrow);

    void updatePasswordByUser(UserPasswordInput userPasswordInput);
}
