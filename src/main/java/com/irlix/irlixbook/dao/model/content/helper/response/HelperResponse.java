package com.irlix.irlixbook.dao.model.content.helper.response;

import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
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
    private LocalDateTime creatingDate;
    private List<Long> comments;
    private int like;
}
