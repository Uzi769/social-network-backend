package com.irlix.irlixbook.repository.summary;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.content.request.ContentSearchRequest;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ContentRepositorySummary {

    private final EntityManager entityManager;

    public List<Content> search(ContentSearchRequest dto, PageableInput pageable) {

        if (pageable.getPage() < 0 || pageable.getSize() < 0) {
            log.error("Pageable was negative.");
            throw new BadRequestException("Page or size can not be negative");
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Content> query = builder.createQuery(Content.class);
        Root<Content> root = query.from(Content.class);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasLength(dto.getName())) {
            predicates.add(builder.like(root.get("name"), "%" + dto.getName() + "%"));
        }
        if (StringUtils.hasLength(dto.getType())) {
            predicates.add(builder.like(root.get("type"), "%" + dto.getType() + "%"));
        }

//        if (!pageable.isSort()) {
//            query.orderBy(builder.desc(root.get("date")));
//        }
//        if (pageable.isSort()) {
//            query.orderBy(builder.asc(root.get("date")));
//        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Content> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(((pageable.getPage() + 1) * pageable.getSize()) - pageable.getSize());
        typedQuery.setMaxResults(pageable.getSize());

        List<Content> list = typedQuery.getResultList();

        if (list.isEmpty()) {
            throw new NotFoundException("Empty list. Class UserRepositorySummary, method search");
        }

        log.info("Create users list for searchWithPagination. Class UserRepositorySummary, method search");
        return list;
    }
}
