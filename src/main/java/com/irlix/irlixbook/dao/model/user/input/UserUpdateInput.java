package com.irlix.irlixbook.dao.model.user.input;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateInput {

    @Size(min = 2, max = 128, message = "Surname must be in range 2 - 128")
    private String surname;

    @Size(min = 2, max = 128, message = "Name must be in range 2 - 128")
    private String name;

    @Size(min = 11, max = 12, message = "Phone must be 11 or 12 chars")
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)", message = "Phone must start from '+7' or '8'")
    private String phone;

    @Length(min = 2, max = 128, message = "Email must be in range 2 - 128")
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
    private RoleEnum role;

}
