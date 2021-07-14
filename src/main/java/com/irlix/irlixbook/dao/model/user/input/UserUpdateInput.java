package com.irlix.irlixbook.dao.model.user.input;


import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateInput {

    @Size(min = 2, max = 128)
    private String surname;
    @Size(min = 2, max = 128)
    private String name;
    @Size(min = 11, max = 12)
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String phone;
    @Length(min = 2, max = 128)
    @Email
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate birthDate;
    private String gender;
    private String description;
    private String vk;
    private String faceBook;
    private String skype;
    private String telegram;
    private String linkedIn;
    private String instagram;
    private List<RoleEnam> roles;
}
