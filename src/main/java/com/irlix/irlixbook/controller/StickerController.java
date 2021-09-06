package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.sticker.input.StickerUpdateRequest;
import com.irlix.irlixbook.dao.model.sticker.output.StickerResponse;
import com.irlix.irlixbook.service.sticker.StickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sticker")
public class StickerController {

    @Autowired
    private StickerService stickerService;

    @GetMapping("/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity<List<StickerResponse>> findAll() {
        List<StickerResponse> all = stickerService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity<StickerResponse> findByName(@PathVariable String name) {
        StickerResponse byName = stickerService.findByName(name);
        return new ResponseEntity<>(byName, HttpStatus.OK);
    }

    @PostMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity<StickerResponse> save(@RequestParam String name) {
        StickerResponse save = stickerService.save(name);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @PutMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity<StickerResponse> update(@RequestBody StickerUpdateRequest request) {
        StickerResponse update = stickerService.update(request);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public void delete(@PathVariable Long id) {
        stickerService.deleteById(id);
    }
}
