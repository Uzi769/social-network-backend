package com.irlix.irlixbook.service.direction;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Direction;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.dao.model.comment.CommentSearch;
import com.irlix.irlixbook.dao.model.direction.DirectionInput;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.CommentRepository;
import com.irlix.irlixbook.repository.DirectionRepository;
import com.irlix.irlixbook.repository.summary.CommentRepositorySummary;
import com.irlix.irlixbook.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class DirectionServiceImpl implements DirectionService {

    private final ConversionService conversionService;
    private final DirectionRepository directionRepository;

    private static final String DIRECTION_NOT_FOUND = "Direction not found";

    @Override
    @Transactional
    public List<DirectionOutput> save(DirectionInput directionInput) {
         if (directionInput == null) {
            log.error("DirectionInput cannot be null");
            throw new NullPointerException("DirectionInput cannot be null");
        }

        Direction direction = conversionService.convert(directionInput, Direction.class);

        directionRepository.save(direction);
        log.info("Direction saved. Class DirectionServiceImpl, method save");

        return findAll();
    }

    @Override
    public DirectionOutput findById(Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(DIRECTION_NOT_FOUND);
                    return new NotFoundException(DIRECTION_NOT_FOUND);
                });
        DirectionOutput directionOutput = conversionService.convert(direction, DirectionOutput.class);
        log.info("Return Direction by id. Class DirectionServiceImpl, method findById");
        return directionOutput;
    }

    @Override
    public List<DirectionOutput> findAll() {
        return null;
    }
}
