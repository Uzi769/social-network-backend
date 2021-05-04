package com.irlix.irlixbook.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInputSearch {

    private String fullName = "";
    private String phone = "";
    private String email = "";
    private boolean delete = false;

}
