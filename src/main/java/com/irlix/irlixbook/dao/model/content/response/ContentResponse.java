package com.irlix.irlixbook.dao.model.content.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irlix.irlixbook.utils.transformation.CustomLocalDateTimeDeserializer;
import com.irlix.irlixbook.utils.transformation.CustomLocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {

    private Long id;

    private String name;

    private String type;

    private String shortDescription;

    private String description;

    private String registrationLink;

    private String author;

    private String creator;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Schema(type = "string", example = "2018-01-01T15:02:01")
    private LocalDateTime eventDate;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Schema(type = "string", example = "2018-01-01T15:02:01")
    private LocalDateTime createDate;

    private List<String> users;

    private String stickerName;

    private List<String> pictures;

    private String deeplink;

    private boolean favorite;
}
