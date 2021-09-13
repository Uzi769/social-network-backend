package com.irlix.irlixbook.repository.summary;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.input.UserSearchInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public List<UserEntity> search(UserSearchInput userSearchInput) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (userSearchInput.getSurname() != null) {
            predicates.add(builder.like(root.get("surname"), "%" + userSearchInput.getSurname() + "%"));
        }
        if (userSearchInput.getName() != null) {
            predicates.add(builder.like(root.get("name"), "%" + userSearchInput.getName() + "%"));
        }
        if(userSearchInput.getEmail() != null){
            predicates.add(builder.like(root.get("email"), "%" + userSearchInput.getEmail() + "%"));
        }
        if(userSearchInput.getPhone() != null){
            predicates.add(builder.like(root.get("phone"), "%" + userSearchInput.getPhone() + "%"));
        }
        Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);

        query.where(builder.and(predicatesArray));

        return entityManager.createQuery(query)
                .setFirstResult(userSearchInput.getPage())
                .setMaxResults(userSearchInput.getSize())
                .getResultList();
    }
}
