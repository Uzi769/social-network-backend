package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.UserContentCommunity;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ContentService {

    ContentResponse save(ContentPersistRequest contentPersistRequest);

    ContentResponse findById(Long id);

    Content findByIdOriginal(Long id);

    List<ContentResponse> findByEventDateForPeriod(LocalDate start, PeriodType periodType);

    Collection<String> findByEventDateForMonth(LocalDate start);

    List<ContentResponse> findAll();

    List<ContentResponse> search(ContentType contentType, String name, int page, int size);

    List<ContentResponse> findByType(ContentType contentType, int page, int size);

    ContentResponse update(Long id, @Valid ContentPersistRequest contentPersistRequest);

    void delete(Long id);

    void deleteAll();

    List<ContentResponse> getFavorites(ContentType contentType, int page, int size);

    List<ContentResponse> findImportant(ContentType type, int page, int size);

    List<Content> addContentsToUserContentCommunity(List<Long> contentsIdList, UserContentCommunity userContentCommunity);

}
