package com.irlix.irlixbook.dao.model.user;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordWithCodeInput {

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotEmpty
    @Size(min = 8)
    private String verificationPassword;

    @NotEmpty
    private String code;

}
