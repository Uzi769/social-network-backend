package com.irlix.irlixbook.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityOutput {

    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String city;
    private String technologies;
    private String phone;
    private String anotherPhone;
    private String email;
    private String skype;
    private String telegram;

}
