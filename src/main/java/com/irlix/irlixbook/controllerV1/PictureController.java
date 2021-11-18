package com.irlix.irlixbook.controllerV1;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
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
    @RoleAndPermissionCheck(RoleEnum.USER)
    public PictureOutput pictureUpload(@RequestParam("file") MultipartFile file) {
        return pictureService.uploading(file);
    }

    @DeleteMapping({"/{id}"})
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public void deletePicture(@PathVariable("id") UUID id) {
        pictureService.deletePicture(id);
    }

    @GetMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<PictureOutput> getAll() {
        return pictureService.getList();
    }

}
