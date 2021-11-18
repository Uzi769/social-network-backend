package com.irlix.irlixbook.controllerV1;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.sticker.input.StickerUpdateRequest;
import com.irlix.irlixbook.dao.model.sticker.output.StickerResponse;
import com.irlix.irlixbook.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sticker")
@RequiredArgsConstructor
public class StickerController {

    private final StickerService stickerService;

    @GetMapping("/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<StickerResponse> findAll() {
        return stickerService.findAll();
    }

    @GetMapping("/{name}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public StickerResponse findByName(@PathVariable String name) {
        return stickerService.findByName(name);
    }

    @PostMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public StickerResponse save(@RequestParam String name) {
        return stickerService.save(name);
    }

    @PutMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public StickerResponse update(@RequestBody StickerUpdateRequest request) {
        return stickerService.update(request);
    }

    @DeleteMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public void delete(@PathVariable Long id) {
        stickerService.deleteById(id);
    }

}
