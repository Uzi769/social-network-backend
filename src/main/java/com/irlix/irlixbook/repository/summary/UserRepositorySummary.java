package com.irlix.irlixbook.repository.summary;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
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
public class UserRepositorySummary {

    private final EntityManager entityManager;

    public List<UserEntity> search(UserInputSearch dto, PageableInput pageable) {

        if (pageable.getPage() < 0 || pageable.getSize() < 0) {
            log.error("Pageable was negative. Class UserServiceImpl, method searchWithPagination");
            throw new BadRequestException("Page or size can not be negative");
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.isDelete()) {
            predicates.add(builder.equal(root.get("delete"), dto.isDelete()));
        }
        if (!dto.getEmail().isEmpty()) {
            predicates.add(builder.equal(root.get("email"), dto.getEmail()));
        }
        if (!dto.getPhone().isEmpty()) {
            predicates.add(builder.equal(root.get("phone"), dto.getPhone()));
        }
        if (!dto.getFullName().isEmpty()) {
            predicates.add(builder.like(root.get("fullName"), "%" + dto.getFullName() + "%"));
        }
        if (!pageable.isSort()) {
            query.orderBy(builder.desc(root.get("fullName")));
        }
        if (pageable.isSort()) {
            query.orderBy(builder.asc(root.get("fullName")));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<UserEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(((pageable.getPage() + 1) * pageable.getSize()) - pageable.getSize());
        typedQuery.setMaxResults(pageable.getSize());

        List<UserEntity> list = typedQuery.getResultList();

        if (list.isEmpty()) {
            throw new NotFoundException("Empty list. Class UserRepositorySummary, method search");
        }

        log.info("Create users list for searchWithPagination. Class UserRepositorySummary, method search");
        return list;
    }
}