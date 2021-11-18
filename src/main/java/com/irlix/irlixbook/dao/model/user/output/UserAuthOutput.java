package com.irlix.irlixbook.dao.model.user.output;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthOutput {

    private UUID id;

    private String surname;

    private String name;

    private String avatar;
}
