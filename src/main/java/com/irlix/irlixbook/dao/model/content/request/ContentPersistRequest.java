package com.irlix.irlixbook.dao.model.content.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private String stickerName;
    private LocalDateTime eventDate;
    private String author;
    private List<UUID> picturesId;
}
