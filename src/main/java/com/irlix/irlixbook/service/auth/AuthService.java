package com.irlix.irlixbook.service.auth;

import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.auth.AuthResponse;

public interface AuthService {

    AuthResponse authUser(AuthRequest request);

    void logout(String value);
}
