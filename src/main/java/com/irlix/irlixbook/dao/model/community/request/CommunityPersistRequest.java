package com.irlix.irlixbook.dao.model.community.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPersistRequest {

    @NotBlank
    private String name;

    private String shortDescription;
    private String description;
    private String registrationLink;
    private String admin;
    private List<UUID> usersId;
    private List<Long> contentsId;
}
