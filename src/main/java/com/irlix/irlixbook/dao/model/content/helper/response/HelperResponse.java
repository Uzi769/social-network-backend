package com.irlix.irlixbook.dao.model.content.helper.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import com.irlix.irlixbook.utils.serdes.CustomLocalDateTimeDeserializer;
import com.irlix.irlixbook.utils.serdes.CustomLocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperResponse {

    private Long id;
    private String title;
    private HelperEnum helperType;
    private String author;
    private String avatar;
    private String description;
    private String deepLink;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Schema(type = "string", example = "2018-01-01T15:02:01")
    private LocalDateTime creatingDate;
    private List<Long> comments;
    private int like;
}
