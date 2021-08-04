package com.irlix.irlixbook.dao.model.user.input;


import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateInput {

    @Length(min = 2, max = 128)
    @NotEmpty
    private String surname;
    @Length(min = 2, max = 128)
    @NotEmpty
    private String name;
    private LocalDate birthDate;
    private String gender;
    @Length(min = 3, max = 128)
    @Email
    private String email;
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String phone;
    private RoleEnam role;
}
