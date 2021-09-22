package com.irlix.irlixbook.dao.model.user.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSearchInput {

    private String surname;
    private String name;
    private String phone;
    private String email;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;
}
