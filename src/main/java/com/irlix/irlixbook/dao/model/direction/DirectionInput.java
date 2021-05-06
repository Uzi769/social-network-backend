package com.irlix.irlixbook.dao.model.direction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectionInput {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
