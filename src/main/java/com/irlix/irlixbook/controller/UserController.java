package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.UserService;
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
    @PutMapping(value = "/user-unblocked/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput unblockedUser(@PathVariable("id") UUID id) {
        return userService.unblockedUser(id);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput deletedUser(@PathVariable("id") UUID id) {
        return userService.deletedUser(id);
    }

    //    @PreAuthorize("hasAnyAuthority({'ADMIN', 'USER', 'MODERATOR'})")
    @GetMapping(value = "/user/search")
    public List<UserEntityOutput> searchUser(UserSearchInput dto) {
        return userService.search(dto);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/assign-role/{id}}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityOutput assignRole(@PathVariable UUID id) {

        return userService.assignRole(id);
    }


    //    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("user/update-password/{id}")
    public UserEntityOutput updatePasswordByAdmin(@PathVariable("id") UUID id, @RequestBody @Valid UserPasswordInput userPasswordInput) {
        return userService.updatePasswordByAdmin(id, userPasswordInput);
    }

    //    @PreAuthorize("hasAuthority('USER', 'MODERATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("user/update-password")
    public UserEntityOutput updatePassword(@RequestBody @Valid UserPasswordInput userPasswordInput) {
        return userService.updatePasswordByUser(userPasswordInput);
    }
}
