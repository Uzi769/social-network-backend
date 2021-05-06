package com.irlix.irlixbook.repository.summary;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.post.PostSearch;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

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
public class PostRepositorySummary {

    private final EntityManager entityManager;
    private final TagService tagService;

    public List<Post> search(PostSearch dto, PageableInput pageable) {

        if (pageable.getPage() < 0 || pageable.getSize() < 0) {
            log.error("Pageable was negative.");
            throw new BadRequestException("Page or size can not be negative");
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getTopic() != null) {
            predicates.add(builder.like(root.get("topic"), "%" + dto.getTopic() + "%"));
        }
        if (dto.getContent() != null) {
            predicates.add(builder.like(root.get("content"), "%" + dto.getContent() + "%"));
        }
        if (dto.getDate() != null) {
            predicates.add(builder.equal(root.get("date"), dto.getDate()));
        }
        if (dto.getTag() != null) {
            predicates.add(builder.isMember(tagService.getByName(dto.getTag()), root.get("tags")));
        }
        if (!pageable.isSort()) {
            query.orderBy(builder.desc(root.get("date")));
        }
        if (pageable.isSort()) {
            query.orderBy(builder.asc(root.get("date")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Post> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(((pageable.getPage() + 1) * pageable.getSize()) - pageable.getSize());
        typedQuery.setMaxResults(pageable.getSize());

        List<Post> list = typedQuery.getResultList();

        if (list.isEmpty()) {
            throw new NotFoundException("Empty list. Class UserRepositorySummary, method search");
        }

        log.info("Create users list for searchWithPagination. Class UserRepositorySummary, method search");
        return list;
    }
}
