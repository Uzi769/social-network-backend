package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.ContentCommunity;
import com.irlix.irlixbook.dao.entity.enams.ContentTypeEnum;
import com.irlix.irlixbook.dao.entity.enams.PeriodTypeEnum;
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

    List<ContentResponse> findByEventDateForPeriod(LocalDate start, PeriodTypeEnum periodTypeEnum);

    Collection<String> findByEventDateForMonth(LocalDate start);

    List<ContentResponse> findAll();

    List<ContentResponse> search(ContentTypeEnum contentTypeEnum, String name, int page, int size);

    List<ContentResponse> findByType(ContentTypeEnum contentTypeEnum, int page, int size);

    ContentResponse update(Long id, @Valid ContentPersistRequest contentPersistRequest);

    void delete(Long id);

    void deleteAll();

    List<ContentResponse> getFavorites(ContentTypeEnum contentTypeEnum, int page, int size);

    List<ContentResponse> findImportant(ContentTypeEnum type, int page, int size);

    List<ContentCommunity> addContentsToContentCommunity(List<Long> contentsIdList, Community community);

}
