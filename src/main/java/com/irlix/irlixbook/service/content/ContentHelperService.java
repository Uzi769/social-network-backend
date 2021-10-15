package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperRequest;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperSearchRequest;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;

import java.util.List;

public interface ContentHelperService {
    HelperResponse save(HelperRequest helperRequest, HelperEnum helperType);

    HelperResponse update(Long id, HelperRequest helperRequest);

    HelperResponse findById(Long id);

    List<HelperResponse> findHelpers(HelperEnum helperType, HelperSearchRequest helperRequest, int page, int size);
}
