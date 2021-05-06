package com.irlix.irlixbook.service.auth;

import com.irlix.irlixbook.dao.model.auth.AuthRequest;

public interface AuthService {

    String authUser(AuthRequest request);

    void logout(String value);
}
