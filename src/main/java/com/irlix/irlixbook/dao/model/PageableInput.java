package com.irlix.irlixbook.dao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableInput {

    @Positive
    @Schema(example = "5", required = true)
    private int size;
    @PositiveOrZero
    private int page;
    private boolean sort = true;
}
