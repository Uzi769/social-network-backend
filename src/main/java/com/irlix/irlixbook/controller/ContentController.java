package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;
    private final UserService userService;

    @GetMapping("/{type}/important")
    public ResponseEntity findImportant(@PathVariable ContentType type,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size
    ) {

        List<ContentResponse> list = contentService.findImportant(type, page, size);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<ContentResponse> findAll() {
        return contentService.findAll();
    }

    @GetMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public ContentResponse findById(@PathVariable("id") Long id) {
        return contentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnam.USER)
    public ContentResponse create(@RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        return contentService.save(contentPersistRequest);
    }

    @GetMapping("/event/{periodType}/byEventDate")//2021-07-20
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<ContentResponse> findByEventDateForWeek(@RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate,
                                                        @PathVariable PeriodType periodType) {
        return contentService.findByEventDateForPeriod(searchDate, periodType);
    }

    @GetMapping("/event/month/byEventDate/dates")//2021-07-20
    @RoleAndPermissionCheck(RoleEnam.USER)
    public Collection<String> findByEventDateForMonth(@RequestParam(required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate) {
        return contentService.findByEventDateForMonth(searchDate);
    }

    @GetMapping("/search/{contentType}/{name}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @PathVariable String name,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        this.validateSearchName(name);
        return contentService.search(contentType, name, page, size);
    }

    @GetMapping("/byType/{contentType}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.findByType(contentType, page, size);
    }

    @PutMapping("/{id}}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public ContentResponse update(
            @PathVariable("id") Long id,
            @RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        return contentService.update(id, contentPersistRequest);
    }

    @DeleteMapping("/{id}}")
    @RoleAndPermissionCheck(RoleEnam.ADMIN)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        contentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/all")
    @RoleAndPermissionCheck(RoleEnam.ADMIN)
    public ResponseEntity<?> deleteAll() {
        contentService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/favorites")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public List<ContentResponse> getFavorites(@RequestParam(required = false) ContentType contentType,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.getFavorites(contentType, page, size);
    }

    @PostMapping("/favorites/{contentId}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public void addFavorites(@PathVariable("contentId") Long favoritesId) {
        userService.addFavorites(favoritesId);
    }

    @DeleteMapping("/favorites/{contentId}")
    @RoleAndPermissionCheck(RoleEnam.USER)
    public void deleteFavorites(@PathVariable("contentId") Long favoritesId) {
        userService.deleteFavorites(favoritesId);
    }

    private void validateSearchName(String name) {
        if (!StringUtils.hasLength(name)) {
            throw new BadRequestException("Param name is null or empty");
        }
        if (!Pattern.matches("[a-zA-ZА-Яа-я ]+", name.trim())) {
            throw new BadRequestException("Search param contains not valid chars: \'" + name + "\'");
        }
    }
}
