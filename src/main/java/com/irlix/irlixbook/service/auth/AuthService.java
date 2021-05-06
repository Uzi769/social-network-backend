package com.irlix.irlixbook.service.auth;

import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.user.output.UserCreateOutput;

public interface AuthService {

    String authUser(AuthRequest request);

    UserCreateOutput getAuthUser(AuthRequest request);

    void logout(String value);
}
