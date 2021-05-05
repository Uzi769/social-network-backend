package com.irlix.irlixbook.dao.model.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
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
public class UserUpdateInput {

    @Size(min = 2, max = 128)
    private String fullName;
    @Size(min = 11, max = 12)
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String phone;
    @Length(min = 2, max = 128)
    @Email
    private String email;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String city;
    private String technologies;
    @Pattern(regexp = "(^\\+?[78][-\\(]?\\d{3}\\)?-?\\d{3}-?\\d{2}-?\\d{2}$)")
    private String anotherPhone;
    private String skype;
    private String telegram;
}
