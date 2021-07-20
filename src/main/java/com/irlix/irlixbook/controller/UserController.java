package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import com.irlix.irlixbook.dao.model.user.input.UserPasswordInput;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}")
    public UserEntityOutput getUserEntity(@PathVariable("id") UUID id) {
        return userService.findUserOutputById(id);
    }

    @GetMapping("/me")
    public UserEntityOutput getUserFromContext() {
        return userService.findUserFromContext();
    }

    @GetMapping("/complex/search")
    public List<UserEntityOutput> findComplex(UserSearchInput searchInput) {
        System.out.println(searchInput);
        return userService.findByComplexQuery(searchInput);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityOutput createUser(@RequestBody @Valid UserCreateInput create) {
        return userService.createUser(create);
    }

    @GetMapping(value = "/all")
    public List<UserEntityOutput> getUsers() {
        return userService.findUsers();
    }

    @PutMapping
    public UserEntityOutput updateUser(@RequestBody @Valid UserUpdateInput update) {
        return userService.updateUser(update, null);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public UserEntityOutput updateUserByAdmin(@RequestBody @Valid UserUpdateInput update, @PathVariable("id") UUID id) {
        return userService.updateUser(update, id);
    }

    @PutMapping(value = "/block/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput blockedUser(@PathVariable("id") UUID id) {
        return userService.blockedUser(id);
    }

    @PutMapping(value = "/unblock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput unblockedUser(@PathVariable("id") UUID id) {
        return userService.unblockedUser(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityOutput deletedUser(@PathVariable("id") UUID id) {
        return userService.deletedUser(id);
    }

    @GetMapping(value = "/search")
    public List<UserEntityOutput> searchUser(@RequestParam(required = false) String surname,
                                             @RequestParam String name,
                                             @RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return userService.search(surname, name, page, size);
    }

    @PutMapping(value = "/assign-role/{role}}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityOutput assignRole(@PathVariable RoleEnam role) {
        return userService.assignRole(role);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update-password/{id}")
    public UserEntityOutput updatePasswordByAdmin(@PathVariable("id") UUID id, @RequestBody @Valid UserPasswordInput userPasswordInput) {
        return userService.updatePasswordByAdmin(id, userPasswordInput);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/update-password")
    public UserEntityOutput updatePassword(@RequestBody @Valid UserPasswordInput userPasswordInput) {
        return userService.updatePasswordByUser(userPasswordInput);
    }


    @PostMapping(value = "/upload-photo", consumes = "multipart/form-data")
    public String pictureUpload(@RequestParam("file") MultipartFile file) {
        return userService.uploading(file);
    }

    @DeleteMapping("/delete-photo/{id}")
    public void deletePicture(@PathVariable("id") UUID id) {
        userService.deletePicture(id);
    }
}
