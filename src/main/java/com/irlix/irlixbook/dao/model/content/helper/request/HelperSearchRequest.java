package com.irlix.irlixbook.dao.model.content.helper.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperSearchRequest {

    private boolean showMyHelpers;

    private boolean showTodayHelpers;
}
