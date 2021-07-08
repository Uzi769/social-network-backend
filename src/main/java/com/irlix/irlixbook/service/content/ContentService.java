package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.dao.model.post.PostSearch;

import javax.validation.Valid;
import java.util.List;

public interface ContentService {
    List<PostOutput> save(PostInput postInput);

    PostOutput findById(Long id);

    List<PostOutput> findAll();

    Content getById(Long id);

    List<PostOutput> search(PostSearch dto, PageableInput pageable);

    List<PostOutput>  update(Long id, @Valid PostInput postInput);

    List<PostOutput>  delete(Long id);
}
