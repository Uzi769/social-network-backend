package com.irlix.irlixbook.service.user.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity findById(UUID id);

    UserEntityOutput findUserOutputById(UUID id);

    UserEntityOutput findUserFromContext();

    List<UserEntityOutput> findByComplexQuery(UserSearchInput input);

    List<UserEntityOutput> findUsers();

    UserEntityOutput createUser(UserCreateInput userCreateInput);

    UserEntityOutput updateUser(UserUpdateInput userUpdateInput, UUID id);

    UserEntityOutput blockedUser(UUID id);

    UserEntityOutput unblockedUser(UUID id);

    UserEntityOutput deletedUser(UUID id);

    List<UserEntityOutput> search(String surname, String name, int page, int size);

    UserEntityOutput assignRole(RoleEnum roleEnum);

    UserEntity findUserForAuth(AuthRequest request);

    UserEntity addFavorites(Long favoritesContentId);

    UserEntity deleteFavorites(Long favoritesContentId);

}
