package com.irlix.irlixbook.service.post;

import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;

import java.util.List;

public interface PostService {
    void save(PostInput postInput);

    PostOutput findById(Long id);

    List<PostOutput> findAll();
}
