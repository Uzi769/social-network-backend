package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntityOutput getUserEntity(Long id);

    void deleteUser(Long id);

    String generateToken(AuthRequest request);

    Optional<UserEntity> findByEmail(String email);

    UserEntityOutput getUserInfo();

    List<UserEntityOutput> searchWithPagination(UserInputSearch dto, PageableInput pageable);

    List<UserEntityOutput> getUserEntityList();
}
