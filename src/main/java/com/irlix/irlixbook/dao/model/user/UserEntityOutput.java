package com.irlix.irlixbook.dao.model.user;

import com.irlix.irlixbook.dao.entity.Photo;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private List<DirectionOutput> directionList;
    private List<String> photos;
}
