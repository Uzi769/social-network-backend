package com.irlix.irlixbook.dao.mapper.request.direction;

import com.irlix.irlixbook.dao.entity.Direction;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.direction.DirectionInput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import org.springframework.core.convert.converter.Converter;

public class DirectionInputToDirection implements Converter<DirectionInput, Direction> {

    @Override
    public Direction convert(DirectionInput directionInput) {
        return Direction.builder()
                .title(directionInput.getTitle())
                .description(directionInput.getDescription())
                .build();
    }
}
