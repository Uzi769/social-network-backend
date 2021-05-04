package com.irlix.irlixbook.config.controller;

import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "User CRUD controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Delete an existing user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't delete users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "User to change status. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = Long.class))
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Get user information", description = "Returns single user information", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't get users information"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
//    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER'})")
    @GetMapping(value = "/info")
    public UserEntityOutput getUserInfo() {
        return userService.getUserInfo();
    }

    @Operation(summary = "Get user information", description = "Returns single user information", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't get users information"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
//    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER'})")
    @GetMapping(value = "/info/{id}")
    public UserEntityOutput getUserEntity(
            @Parameter(description = "User to get all info. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = Long.class))
            @PathVariable("id") Long id) {
        return userService.getUserEntity(id);
    }
}
