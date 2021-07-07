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
    private String surname;
    @Length(min = 2, max = 128)
    @NotEmpty
    private String name;
    private LocalDate birthDate;
    private String gender;
    @Length(min = 3, max = 128)
    @Email
    private String email;

}
