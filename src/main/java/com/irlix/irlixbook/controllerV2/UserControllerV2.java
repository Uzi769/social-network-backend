package com.irlix.irlixbook.controllerV2;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.password.PasswordService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

}
