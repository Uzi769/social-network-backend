package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.service.user.avatar.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping
    @RoleAndPermissionCheck(RoleEnam.USER)
    public String getAvatar() {
        return avatarService.getAvatar();
    }

    @GetMapping("/userId")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public String getAvatarByUserId(@RequestParam UUID userId) {
        return avatarService.getAvatarByUserID(userId);
    }

    @PostMapping(consumes = "multipart/form-data")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public String pictureUpload(@RequestParam("file") MultipartFile file) {
        return avatarService.uploading(file);
    }

    @DeleteMapping
    @RoleAndPermissionCheck(RoleEnam.ADMIN)
    public void deletePicture() {
        avatarService.delete();
    }

    @PutMapping(consumes = "multipart/form-data")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public String updateAvatar(@RequestParam("file") MultipartFile file) {
        return avatarService.update(file);
    }
}
