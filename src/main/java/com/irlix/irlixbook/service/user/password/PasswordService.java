package com.irlix.irlixbook.service.user.password;

import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;

import java.util.UUID;

public interface PasswordService {

    UserEntityOutput updatePasswordByAdmin(UUID id, UserPasswordInput userPasswordInput);

    UserEntityOutput updatePasswordByUser(UserPasswordWithCodeInput userPasswordInput);

    void sendGeneratedCode(String email);
}
