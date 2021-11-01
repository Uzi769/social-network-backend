package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.user.UserPasswordWithCodeInput;
import com.irlix.irlixbook.dao.model.user.input.*;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.password.PasswordService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;

    @GetMapping(value = "/{id}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public UserEntityOutput getUserEntity(@PathVariable("id") UUID id) {
        return userService.findUserOutputById(id);
    }

    @GetMapping("/me")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public UserEntityOutput getUserFromContext() {
        return userService.findUserFromContext();
    }

    @GetMapping("/complex/search")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<UserEntityOutput> findComplex(UserSearchInput searchInput) {

        System.out.println(searchInput);
        return userService.findByComplexQuery(searchInput);

    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnum.USER)
    public UserEntityOutput createUser(@RequestBody @Valid UserCreateInput create) {
        return userService.createUser(create);
    }

    @GetMapping(value = "/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<UserEntityOutput> getUsers() {
        return userService.findUsers();
    }

    @PutMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public UserEntityOutput updateUser(@RequestBody @Valid UserUpdateInput update) {
        return userService.updateUser(update, null);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput updateUserByAdmin(@RequestBody @Valid UserUpdateInput update, @PathVariable("id") UUID id) {
        return userService.updateUser(update, id);
    }

    @PutMapping(value = "/block/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput blockedUser(@PathVariable("id") UUID id) {
        return userService.blockedUser(id);
    }

    @PutMapping(value = "/unblock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput unblockedUser(@PathVariable("id") UUID id) {
        return userService.unblockedUser(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput deletedUser(@PathVariable("id") UUID id) {
        return userService.deletedUser(id);
    }

    @GetMapping(value = "/search")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<UserEntityOutput> searchUser(@RequestParam(required = false) String surname,
                                             @RequestParam String name,
                                             @RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return userService.search(surname, name, page, size);
    }

    @PutMapping(value = "/assign-role/{role}")
    @ResponseStatus(HttpStatus.CREATED)
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput assignRole(@PathVariable RoleEnum role) {
        return userService.assignRole(role);
    }

    @PutMapping("/update-password/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public UserEntityOutput updatePasswordByAdmin(@PathVariable("id") UUID id, @RequestBody @Valid UserPasswordInput userPasswordInput) {
        return passwordService.updatePasswordByAdmin(id, userPasswordInput);
    }

    @PutMapping("/update-password")
    public UserEntityOutput updatePassword(@RequestBody @Valid UserPasswordWithCodeInput userPasswordInput) {
        return passwordService.updatePasswordByUser(userPasswordInput);
    }

    @PostMapping("/code")
    public void updatePassword(@RequestBody UserEmailInput userEmailInput) {
        passwordService.sendGeneratedCode(userEmailInput.getEmail());
    }

}
