package com.irlix.irlixbook.dao.model.content.helper.response;

import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class HelperResponse {

    private String name;
    private HelperEnum helperType;
    private String creator;
    private String text;
    private String deepLink;
    private LocalDateTime creatingDate;
    private List<Long> comments;
    private int like;
}
