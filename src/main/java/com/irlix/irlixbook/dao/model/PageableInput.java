package com.irlix.irlixbook.dao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableInput {

    @Positive
    @Schema(example = "5", required = true)
    private int size;

    @PositiveOrZero
    private int page;
    private boolean sort;

}
