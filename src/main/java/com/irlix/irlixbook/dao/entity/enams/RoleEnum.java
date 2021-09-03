package com.irlix.irlixbook.dao.entity.enams;

import java.time.LocalDateTime;

public enum RoleEnum {

    ADMIN,
    USER,
    GUEST;

    public StatusEnam getStatus(LocalDateTime registrationDate) {
        if (this == ADMIN) {
            return StatusEnam.COMMUNITY_LEADER;
        } else if (this == USER){
            if (registrationDate != null) {
                LocalDateTime eventDate = LocalDateTime.now().plusMonths(2);
                if (eventDate.isBefore(registrationDate)) {
                    return StatusEnam.COMMUNITY_MEMBER;
                } else {
                    return StatusEnam.NEW_MEMBER;
                }
            } else {
                return StatusEnam.NEW_MEMBER;
            }
        } else {
            return null;
        }
    }

    public RoleEnum[] includeRoles() {
        if (this == ADMIN) {
            return new RoleEnum[]{USER, GUEST};
        } else if (this == USER){
            return new RoleEnum[]{GUEST};
        } else {
            return new RoleEnum[0];
        }
    }
}
