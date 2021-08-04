package com.irlix.irlixbook.dao.model.user.output;

import com.irlix.irlixbook.dao.entity.enams.StatusEnam;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityOutput {

    private UUID id;
    private String surname;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String description;
    private String vk;
    private String phone;
    private String faceBook;
    private String email;
    private String skype;
    private String telegram;
    private String linkedIn;
    private String avatar;
    private String instagram;
    private String role;
    private boolean blocked;
    private LocalDateTime registrationDate;
    private StatusEnam status;

}
