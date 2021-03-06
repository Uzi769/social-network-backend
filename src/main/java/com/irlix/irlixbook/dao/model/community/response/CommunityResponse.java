package com.irlix.irlixbook.dao.model.community.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityResponse {

    private UUID id;

    private String name;

    private String shortDescription;

    private String description;

    private String registrationLink;

    private String deepLink;

    private List<Long> contents;

    private String admin;

    private List<String> users;

    private String creator;
}
