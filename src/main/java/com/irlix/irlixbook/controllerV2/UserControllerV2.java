package com.irlix.irlixbook.controllerV2;

import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.password.PasswordService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserControllerV2 {

    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/code")
    public void updatePassword(@RequestBody UserEmailInput userEmailInput) {
        passwordService.sendGeneratedCode(userEmailInput.getEmail());
    }

    @PutMapping("/update-password")
    public UserEntityOutput updatePassword(@RequestBody @Valid UserPasswordWithCodeInput userPasswordInput) {
        return passwordService.updatePasswordByUser(userPasswordInput);
    }
}
