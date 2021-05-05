package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ConversionService conversionService;


    @Override
    public void save(CommentInput commentInput) {
//TODO
    }

    @Override
    public CommentOutput findById(Long id) {
        CommentOutput commentOutput = conversionService.convert(commentRepository.findById(id), CommentOutput.class);
        log.info("Return Comment by id. Class CommentServiceImpl, method findById");
        return commentOutput;
    }

    @Override
    public List<CommentOutput> findAll() {
        List<CommentOutput> commentOutputs = commentRepository.findAll().stream()
                .map(comment -> conversionService.convert(comment, CommentOutput.class)).collect(Collectors.toList());

        return commentOutputs;
    }
}
