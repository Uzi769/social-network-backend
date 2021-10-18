package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.content.comment.CommentRequest;
import com.irlix.irlixbook.dao.model.content.comment.CommentResponse;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperRequest;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperSearchRequest;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.service.comment.CommentService;
import com.irlix.irlixbook.service.content.ContentHelperService;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.NonNull;
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
    private final ContentHelperService contentHelperService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/{type}/important")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity findImportant(@PathVariable ContentType type,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        List<ContentResponse> list = contentService.findImportant(type, page, size);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ContentResponse> findAll() {
        return contentService.findAll();
    }

    @GetMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.GUEST)
    public ContentResponse findById(@PathVariable("id") Long id) {
        return contentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ContentResponse create(@RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        return contentService.save(contentPersistRequest);
    }

    @GetMapping("/event/{periodType}/byEventDate") //2021-07-20
    @RoleAndPermissionCheck(RoleEnum.GUEST)
    public List<ContentResponse> findByEventDateForWeek(@RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate,
                                                        @PathVariable PeriodType periodType) {
        return contentService.findByEventDateForPeriod(searchDate, periodType);
    }

    @GetMapping("/event/month/byEventDate/dates") //2021-07-20
    @RoleAndPermissionCheck(RoleEnum.USER)
    public Collection<String> findByEventDateForMonth(@RequestParam(required = false)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate) {
        return contentService.findByEventDateForMonth(searchDate);
    }

    @GetMapping("/search/{contentType}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @RequestParam String name,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {

        this.validateSearchName(name);
        return contentService.search(contentType, name, page, size);
    }

    @GetMapping("/byType/{contentType}")
    @RoleAndPermissionCheck(RoleEnum.GUEST)
    public List<ContentResponse> search(@PathVariable ContentType contentType,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.findByType(contentType, page, size);
    }

    @PutMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ContentResponse update(
            @PathVariable("id") Long id,
            @RequestBody @Valid ContentPersistRequest contentPersistRequest) {
        return contentService.update(id, contentPersistRequest);
    }

    @DeleteMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        contentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/all")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ResponseEntity<?> deleteAll() {
        contentService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/favorites")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ContentResponse> getFavorites(@RequestParam(required = false) ContentType contentType,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        return contentService.getFavorites(contentType, page, size);
    }

    @PostMapping("/favorites/{contentId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public void addFavorites(@PathVariable("contentId") Long favoritesId) {
        userService.addFavorites(favoritesId);
    }

    @DeleteMapping("/favorites/{contentId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public void deleteFavorites(@PathVariable("contentId") Long favoritesId) {
        userService.deleteFavorites(favoritesId);
    }

    private void validateSearchName(String name) {

        if (!StringUtils.hasLength(name))
            throw new BadRequestException("Param name is null or empty");

        if (!Pattern.matches("[a-zA-ZА-Яа-я ]+", name.trim()))
            throw new BadRequestException("Search param contains not valid chars: \'" + name + "\'");

    }

    // ================================================================================ HELPER METHODS

    @PostMapping("/helper/{helperType}")
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnum.USER)
    public HelperResponse createHelper(@PathVariable("helperType") HelperEnum helperType,
                                       @RequestBody @Valid HelperRequest helperRequest) {
        return contentHelperService.save(helperRequest, helperType);
    }

    @GetMapping("/helper/{id}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public HelperResponse findHelperById(@NonNull @PathVariable("id") Long id) {
        return contentHelperService.findById(id);
    }

    @PostMapping("/helper/search/{helperType}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<HelperResponse> findHelpers(@PathVariable("helperType") HelperEnum helperType,
                                      @RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                            @RequestBody(required = false) @Valid HelperSearchRequest helperRequest) {
        return contentHelperService.findHelpers(helperType, helperRequest, page, size);
    }

    @GetMapping("/helper/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<HelperResponse> findAllHelpers(@RequestParam HelperEnum helperType,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return contentHelperService.findAllHelpers(helperType, page, size);
    }
}
