package com.irlix.irlixbook.dao.model.content.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irlix.irlixbook.utils.transformation.CustomLocalDateTimeDeserializer;
import com.irlix.irlixbook.utils.transformation.CustomLocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    private String stickerName;

    private String registrationLink;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Schema(type = "string", example = "2018-01-01T15:02:01")
    private LocalDateTime eventDate;

    private String author;

    private List<UUID> picturesId;
}
