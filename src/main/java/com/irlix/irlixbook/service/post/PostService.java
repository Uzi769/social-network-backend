package com.irlix.irlixbook.service.post;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.dao.model.post.PostSearch;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;

import java.util.List;

public interface PostService {
    void save(PostInput postInput);

    PostOutput findById(Long id);

    List<PostOutput> findAll();

    Post getById(Long id);

    List<PostOutput> search(PostSearch dto, PageableInput pageable);
}
