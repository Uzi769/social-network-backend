package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.community.request.CommunityContentsRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityUsersRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutputWithStatus;
import com.irlix.irlixbook.service.community.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public CommunityResponse create(@RequestBody @Valid CommunityPersistRequest communityPersistRequest) {
        return communityService.save(communityPersistRequest);
    }

    @GetMapping("/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<CommunityResponse> findAll() {
        return communityService.findAll();
    }

    @GetMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public CommunityResponse findById(@PathVariable("id")UUID id) {
        return communityService.findById(id);
    }

    @GetMapping()
    @RoleAndPermissionCheck(RoleEnum.USER)
    public CommunityResponse findByName(@RequestParam(value = "name") String name) {
        return communityService.findByName(name);
    }

    @GetMapping("/{name}/users")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<UserEntityOutputWithStatus> findCommunityUsers(@PathVariable("name")String name,
                                                               @RequestParam(required = false, defaultValue = "0") int page,
                                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return communityService.findCommunityUsers(name, page, size);
    }

    @GetMapping("/{name}/contents")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ContentResponse> findCommunityContents(@PathVariable("name")String name,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return communityService.findCommunityContents(name, page, size);
    }

    //todo parameter users
    @PutMapping("/users")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public CommunityResponse addUsers(@RequestBody @Valid CommunityUsersRequest communityUsersRequest) {
        return communityService.addUsers(communityUsersRequest);
    }

    //todo parameter contents
    @PostMapping("/contents")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public CommunityResponse addContents(@RequestBody @Valid CommunityContentsRequest communityContentsRequest) {
        return communityService.addContents(communityContentsRequest);
    }

    @DeleteMapping("/{name}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ResponseEntity<?> delete(@PathVariable("name") String name) {
        communityService.delete(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
