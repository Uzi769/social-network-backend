package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    //    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER', 'MODERATOR'})")
    @GetMapping(value = "/user/{id}")
    public UserEntityOutput getUserEntity(@PathVariable("id") UUID id) {
        return userService.findUserOutputById(id);
    }

    //    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER', 'MODERATOR'})")
    @GetMapping(value = "/user")
    public UserEntityOutput getUserFromContext() {
        return userService.findUserFromContext();
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/user/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityOutput createUser(@RequestBody @Valid UserCreateInput create) {
        return userService.createUser(create);
    }

    //    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER', 'MODERATOR'})")
    @GetMapping(value = "/users")
    public List<UserEntityOutput> getUsers() {
        return userService.findUsers();
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/user")
    public UserEntityOutput updateUser(@RequestBody @Valid UserUpdateInput update) {
        return userService.updateUser(update, null);
    }


    //    @PreAuthorize("hasAuthority('USER', 'MODERATOR')")
    @PutMapping(value = "/user/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public UserEntityOutput updateUserByAdmin(@RequestBody @Valid UserUpdateInput update, @PathVariable("id") UUID id) {
        return userService.updateUser(update, id);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/user-blocked/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput blockedUser(@PathVariable("id") UUID id) {
        return userService.blockedUser(id);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput deletedUser(@PathVariable("id") UUID id) {
        return userService.deletedUser(id);
    }











    //    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER', 'MODERATOR'})")
    @GetMapping(value = "/search")
    public List<UserEntityOutput> searchUser(
            @Parameter(description = "User search parameters. Cannot be null or empty.", required = true,
                    schema = @Schema(implementation = UserSearchInput.class))
                    UserSearchInput dto, PageableInput pageable) {
        return userService.searchWithPagination(dto, pageable);
    }


    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create-moderator", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createModerator(@RequestBody @Valid UserCreateInput create) {
        userService.createModerator(create);
    }


    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAuthority('USER', 'MODERATOR')")
    @PutMapping("/update-password")
    public void updatePassword(
            @Parameter(description = "Cannot be null or empty.")
            @RequestBody @Valid UserPasswordInput userPasswordInput) {
        userService.updatePasswordByUser(userPasswordInput);
    }

    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-user-password")
    public void updatePasswordByAdmin(
            @Parameter(description = "Cannot be null or empty.")
            @RequestBody @Valid UserPasswordThrow userPasswordThrow) {
        userService.updatePasswordByAdmin(userPasswordThrow);
    }

}
