package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.web.multipart.MultipartFile;

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

    UserEntityOutput unblockedUser(UUID id);

    UserEntityOutput deletedUser(UUID id);

    List<UserEntityOutput> search(UserSearchInput userSearchInput);

    UserEntityOutput assignRole(RoleEnam roleEnam);

    UserEntityOutput updatePasswordByAdmin(UUID id, UserPasswordInput userPasswordInput);

    UserEntityOutput updatePasswordByUser(UserPasswordInput userPasswordInput);

    String uploading(MultipartFile file);

    void deletePicture(UUID id);

    UserEntity findUserForAuth(AuthRequest request);

    UserEntity addFavorites(Long favoritesContentId);

}
