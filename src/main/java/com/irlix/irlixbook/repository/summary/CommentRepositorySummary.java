package com.irlix.irlixbook.repository.summary;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.comment.CommentSearch;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.service.post.PostService;
import com.irlix.irlixbook.service.user.UserService;
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
public class CommentRepositorySummary {

    private final EntityManager entityManager;
    private final PostService postService;
    private final UserService userService;

    public List<Comment> search(CommentSearch dto, PageableInput pageable) {

        if (pageable.getPage() < 0 || pageable.getSize() < 0) {
            log.error("Pageable was negative.");
            throw new BadRequestException("Page or size can not be negative");
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getPostId() != null) {
            predicates.add(builder.equal(root.get("post"), postService.getById(dto.getPostId())));
        }
        if (dto.getUserId() != null) {
            predicates.add(builder.equal(root.get("user"), userService.findUserById(dto.getUserId())));
        }
        if (dto.getDate() != null) {
            predicates.add(builder.equal(root.get("date"), dto.getDate()));
        }

        if (!pageable.isSort()) {
            query.orderBy(builder.desc(root.get("date")));
        }
        if (pageable.isSort()) {
            query.orderBy(builder.asc(root.get("date")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Comment> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(((pageable.getPage() + 1) * pageable.getSize()) - pageable.getSize());
        typedQuery.setMaxResults(pageable.getSize());

        List<Comment> list = typedQuery.getResultList();

        if (list.isEmpty()) {
            throw new NotFoundException("Empty list. Class UserRepositorySummary, method search");
        }

        log.info("Create users list for searchWithPagination. Class UserRepositorySummary, method search");
        return list;
    }
}
