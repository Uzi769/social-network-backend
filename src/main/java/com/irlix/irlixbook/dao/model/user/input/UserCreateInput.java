package com.irlix.irlixbook.dao.model.user.input;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateInput {

    @Length(min = 2, max = 128)
    @NotEmpty
    private String fullName;
    @Size(min = 11, max = 12)
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String phone;
    @Length(min = 3, max = 128)
    @Email
    private String email;
    private String password;
    private String city;
    private String technologies;
    private LocalDate birthDate;
}