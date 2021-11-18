package com.irlix.irlixbook.controllerV2;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.avatar.AvatarService;
import com.irlix.irlixbook.service.user.password.PasswordService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserControllerV2 {

    private final AvatarService avatarService;
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

    @PostMapping(path = "/avatar", consumes = "multipart/form-data")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public String pictureUpload(@RequestParam("file") MultipartFile file) {
        return avatarService.uploading(file);
    }
}
