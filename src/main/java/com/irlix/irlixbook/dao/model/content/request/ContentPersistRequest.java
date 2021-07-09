package com.irlix.irlixbook.dao.model.content.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentPersistRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String type;
    private String shortDescription;
    private String description;
    private String registrationLink;
    @NotBlank
    private String stickerName;
}
