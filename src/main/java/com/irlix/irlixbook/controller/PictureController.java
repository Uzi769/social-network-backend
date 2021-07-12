package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import com.irlix.irlixbook.service.picture.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public PictureOutput pictureUpload(@RequestParam("file") MultipartFile file) {
        return pictureService.uploading(file);
    }

    @DeleteMapping({"/{id}"})
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ResponseEntity deletePicture(@PathVariable("id") UUID id) {
        pictureService.deletePicture(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public List<PictureOutput> getAll() {
        return pictureService.getList();
    }

}
