package com.irlix.irlixbook.controllerV1;

import com.irlix.irlixbook.dao.model.auth.AuthRequest;
import com.irlix.irlixbook.dao.model.auth.AuthResponse;
import com.irlix.irlixbook.service.auth.AuthService;
import com.irlix.irlixbook.service.messaging.MessageSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/sign-in")
    public ResponseEntity<String> auth(
            @RequestHeader(value = "user-app-code", required = false) String code,
            @RequestBody AuthRequest request) {

        AuthResponse authResponse = authService.authUser(request, code);

        return ResponseEntity.ok()
                .header("Authorization", authResponse.getToken())
                .body(authResponse.getToken());

    }

    @Operation(summary = "Logout in POST request")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String value) {

        authService.logout(value);
        return ResponseEntity.ok()
                .body("Logout");
    }

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @PostMapping("/send")
    public void send(@RequestParam String code, @RequestParam String text, @RequestParam String title) {
        messageSender.send(title, code, text);
    }

}
