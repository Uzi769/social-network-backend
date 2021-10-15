package com.irlix.irlixbook.dao.model.content.helper.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class HelperRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String text;

    private int like;

//    private List<UUID> pictureId
}
