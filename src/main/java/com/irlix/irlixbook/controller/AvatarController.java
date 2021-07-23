package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.service.user.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getAvatar(@RequestParam String avatarId) {
        return avatarService.getAvatar(avatarId);
    }

    @PostMapping(consumes = "multipart/form-data")
    public String pictureUpload(@RequestParam("file") MultipartFile file) {
        return avatarService.uploading(file);
    }

    @DeleteMapping
    public void deletePicture() {
        avatarService.deletePicture();
    }

    @PutMapping(consumes = "multipart/form-data")
    public String updateAvatar(@RequestParam("file") MultipartFile file) {
        return avatarService.update(file);
    }
}
