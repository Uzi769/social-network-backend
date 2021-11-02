package com.irlix.irlixbook.controllerV2;

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
@RequestMapping("/api2/community")
public class CommunityControllerV2 {

    private final CommunityService communityService;

    @GetMapping("/{id}/users")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<UserEntityOutputWithStatus> findCommunityUsers(@PathVariable("id")UUID id,
                                                               @RequestParam(required = false, defaultValue = "0") int page,
                                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return communityService.findCommunityUsers(id, page, size);
    }

    @GetMapping("/{id}/contents")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ContentResponse> findCommunityContents(@PathVariable("id")UUID id,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return communityService.findCommunityContents(id, page, size);
    }
}
