package com.irlix.irlixbook.dao.model.auth;

import com.irlix.irlixbook.dao.model.user.output.UserCreateOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {
    private String token;
    private UserCreateOutput userCreateOutput;
}
