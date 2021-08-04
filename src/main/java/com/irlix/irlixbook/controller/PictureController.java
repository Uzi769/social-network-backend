package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import com.irlix.irlixbook.service.picture.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picture")
public class PictureController {

    public final PictureService pictureService;

    @PostMapping(consumes = "multipart/form-data")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public PictureOutput pictureUpload(@RequestParam("file") MultipartFile file) {
        return pictureService.uploading(file);
    }

    @DeleteMapping({"/{id}"})
    @RoleAndPermissionCheck(RoleEnam.ADMIN)
    public void deletePicture(@PathVariable("id") UUID id) {
        pictureService.deletePicture(id);
    }

    @GetMapping
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<PictureOutput> getAll() {
        return pictureService.getList();
    }

}
