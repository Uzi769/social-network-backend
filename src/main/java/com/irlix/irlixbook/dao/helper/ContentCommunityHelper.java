package com.irlix.irlixbook.dao.helper;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.ContentCommunity;
import com.irlix.irlixbook.repository.ContentCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ContentCommunityHelper {

    private final ContentCommunityRepository contentCommunityRepository;

    public List<ContentCommunity> findByCommunityName(String name) {
        return contentCommunityRepository.findByCommunityName(name);
    }

    public void save(ContentCommunity contentCommunity) {
        contentCommunityRepository.save(contentCommunity);
    }

    public List<ContentCommunity> findByContent(Content content) {
        return contentCommunityRepository.findByContent(content);
    }

    public void deleteInBatch(List<ContentCommunity> content) {
        contentCommunityRepository.deleteInBatch(content);
    }
}
