package com.irlix.irlixbook.service;

import com.irlix.irlixbook.dao.model.PostInput;
import com.irlix.irlixbook.dao.model.PostOutput;

import java.util.List;

public interface PostService {
    void save(PostInput postInput);

    PostOutput findById(Long id);

    List<PostOutput> findAll();
}
