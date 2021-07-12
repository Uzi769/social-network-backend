package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;

import javax.validation.Valid;
import java.util.List;

public interface ContentService {
    ContentResponse save(ContentPersistRequest contentPersistRequest);

    ContentResponse findById(Long id);

    Content findByIdOriginal(Long id);

    List<ContentResponse> findAll();

    List<ContentResponse> search(String name, int page, int size);

    ContentResponse update(Long id, @Valid ContentPersistRequest contentPersistRequest);

    void delete(Long id);
}
