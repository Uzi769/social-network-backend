package com.irlix.irlixbook.dao.helper;

import com.irlix.irlixbook.dao.entity.ContentUser;
import com.irlix.irlixbook.dao.entity.enams.ContentTypeEnum;
import com.irlix.irlixbook.repository.ContentUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ContentUserHelper {

    private final ContentUserRepository contentUserRepository;

    public void deleteByContentId(Long contentId) {
        contentUserRepository.deleteByContentId(contentId);
    }

    public List<ContentUser> findByUserId(UUID userId, Pageable pageable) {
        return contentUserRepository.findByUserId(userId, pageable);
    }

    public List<ContentUser> findByUserIdAndContent_Type(UUID userId, ContentTypeEnum contentTypeEnum, Pageable pageable) {
        return contentUserRepository.findByUserIdAndContent_Type(userId, contentTypeEnum, pageable);
    }
}
