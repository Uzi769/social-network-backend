package com.irlix.irlixbook.service.direction;

import com.irlix.irlixbook.dao.model.direction.DirectionInput;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;

import java.util.List;

public interface DirectionService {
    List<DirectionOutput>  save(DirectionInput directionInput);

    DirectionOutput findById(Long id);

    List<DirectionOutput> findAll();
}
