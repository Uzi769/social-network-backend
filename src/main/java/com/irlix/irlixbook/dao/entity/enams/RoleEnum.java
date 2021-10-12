package com.irlix.irlixbook.dao.entity.enams;

public enum RoleEnum {

    ADMIN,
    USER,
    GUEST;

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
