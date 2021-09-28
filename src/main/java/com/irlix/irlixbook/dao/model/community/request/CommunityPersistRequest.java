package com.irlix.irlixbook.dao.model.community.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
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
}
