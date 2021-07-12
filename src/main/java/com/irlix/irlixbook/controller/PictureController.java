package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import com.irlix.irlixbook.service.picture.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picture")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PictureController {

    public final PictureService pictureService;

    @PostMapping(consumes = "multipart/form-data")
    public PictureOutput pictureUpload(@RequestParam("file") MultipartFile file) {
        return pictureService.uploading(file);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity deletePicture(@PathVariable("id") UUID id) {
        pictureService.deletePicture(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<PictureOutput> getAll() {
        return pictureService.getList();
    }

}
