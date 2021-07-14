package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;
    private final UserService userService;

    @GetMapping
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public List<ContentResponse> findAll() {
        return contentService.findAll();
    }

    @GetMapping("/{id}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ContentResponse findById(@PathVariable("id") Long id) {
        return contentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ContentResponse create(@RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        System.out.println();
        return contentService.save(contentPersistRequest);
    }

    @GetMapping("/favorites")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public List<ContentResponse> getFavorites(@RequestParam(required = false) ContentType contentType,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.getFavorites(contentType, page, size);
    }

    @GetMapping("/search/{contentType}/{name}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @PathVariable String name,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.search(contentType, name, page, size);
    }

    @GetMapping("/byType/{contentType}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.findByType(contentType, page, size);
    }

    @PutMapping("/{id}}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ContentResponse update(
            @PathVariable("id") Long id,
            @RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        return contentService.update(id, contentPersistRequest);
    }

    @DeleteMapping("/{id}}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        contentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/all")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ResponseEntity<?> deleteAll() {
        contentService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/favorites/{contentId}")
    @RoleAndPermissionCheck({RoleEnam.USER, RoleEnam.ADMIN})
    public ResponseEntity<UUID> addFavorites(@PathVariable("contentId") Long favoritesId) {
        UserEntity userEntity = userService.addFavorites(favoritesId);
        return new ResponseEntity<>(userEntity.getId(), HttpStatus.OK);
    }
}
