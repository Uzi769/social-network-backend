package com.irlix.irlixbook.dao.entity.enams;

import java.time.LocalDateTime;

public enum RoleEnam {

    ADMIN,
    USER;

    public StatusEnam getStatus(LocalDateTime registrationDate) {
        if (this == ADMIN) {
            return StatusEnam.COMMUNITY_LEADER;
        } else {
            if (registrationDate != null) {
                LocalDateTime eventDate = registrationDate.plusMonths(2).plusDays(1);
                if (eventDate.isAfter(LocalDateTime.now())) {
                    return StatusEnam.COMMUNITY_MEMBER;
                } else {
                    return StatusEnam.NEW_MEMBER;
                }
            } else {
                return StatusEnam.NEW_MEMBER;
            }
        }
    }

    public RoleEnam[] includeRoles() {
        if (this == ADMIN) {
            return new RoleEnam[]{USER};
        } else {
            return new RoleEnam[0];
        }
    }
}
