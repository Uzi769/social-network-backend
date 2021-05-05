package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.user.UserBirthdaysOutput;
import com.irlix.irlixbook.dao.model.user.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;
import com.irlix.irlixbook.dao.model.user.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.UserUpdateInput;
import com.irlix.irlixbook.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    @CrossOrigin
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
    @CrossOrigin
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
    @CrossOrigin
    @GetMapping(value = "/info/{id}")
    public UserEntityOutput getUserEntity(
            @Parameter(description = "User to get all info. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = Long.class))
            @PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Get user information", description = "Returns single user information", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't get users information"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
//    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER'})")
    @CrossOrigin
    @GetMapping(value = "/birthdays/")
    public List<UserBirthdaysOutput> getUserWithBirthdays() {
        return userService.getUserWithBirthDays();
    }

    @Operation(summary = "Get user information", description = "Returns single user information", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't get users information"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
//    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER'})")
    @GetMapping(value = "/all")
    @CrossOrigin
    public List<UserEntityOutput> getUserEntityList() {
        return userService.getUserEntityList();
    }

    @Operation(summary = "Find users", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't get users via search"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
//    @PreAuthorize("hasAuthority('ADMIN')")
    @CrossOrigin
    @GetMapping(value = "/search")
    public List<UserEntityOutput> searchUser(
            @Parameter(description = "User search parameters. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = UserInputSearch.class))
                    UserInputSearch dto, PageableInput pageable) {
        return userService.searchWithPagination(dto, pageable);
    }

    @Operation(summary = "Add new User", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't add new user"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
//    @PreAuthorize("hasAuthority('ADMIN')")
    @CrossOrigin
    @PostMapping(value = "/create", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(
            @Parameter(description = "User to add. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = UserCreateInput.class))
            @RequestBody @Valid UserCreateInput create) {
        userService.createUser(create);
    }

    @Operation(summary = "Update an existing user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't update existing users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAuthority('ADMIN')")
    @CrossOrigin
    @PutMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public void updateUser(
            @Parameter(description = "User to update. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = UserUpdateInput.class))
            @RequestBody @Valid UserUpdateInput update) {
        userService.updateUser(update);
    }

    @Operation(summary = "Update password for user", tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden. You can't update password"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAuthority('USER')")
    @CrossOrigin
    @PutMapping("/update-password")
    public void updatePassword(
            @Parameter(description = "Cannot be null or empty.")
            @RequestBody @Valid UserPasswordInput userPasswordInput) {
        userService.updatePassword(userPasswordInput);
    }

}
