package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.auth.AuthResponse;
import com.irlix.irlixbook.dao.model.user.output.UserCreateOutput;
import com.irlix.irlixbook.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "Sign up controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Sign in POST request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "You are successfully logged in!",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthResponse.class))))
    })
    @CrossOrigin
    @PostMapping("/sign-in")
    public ResponseEntity<UserCreateOutput> auth(@RequestBody AuthRequest request) {
        return ResponseEntity.ok()
                .header("Authorization", authService.authUser(request).getToken())
                .body(authService.authUser(request).getUserCreateOutput());
    }

    @Operation(summary = "Logout in POST request")
    @CrossOrigin
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String value) {
        authService.logout(value);
        return ResponseEntity.ok().body("Logout");
    }
}
