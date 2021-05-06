package com.irlix.irlixbook.dao.model.user.output;

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
public class UserBirthdaysOutput {

    private String fullName;
    private LocalDate birthDate;
    private String photo;
}
