package com.irlix.irlixbook.dao.model.content.helper.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
