package com.irlix.irlixbook.dao.model.user.output;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irlix.irlixbook.dao.entity.enams.StatusEnam;
import com.irlix.irlixbook.utils.serdes.CustomLocalDateTimeDeserializer;
import com.irlix.irlixbook.utils.serdes.CustomLocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Schema(type = "string", example = "2018-01-01T15:02:01")
    private LocalDateTime registrationDate;
    private StatusEnam status;

}
