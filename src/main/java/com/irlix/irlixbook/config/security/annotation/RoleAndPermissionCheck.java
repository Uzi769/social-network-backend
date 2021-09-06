package com.irlix.irlixbook.config.security.annotation;

import com.irlix.irlixbook.dao.entity.enams.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RoleAndPermissionCheck {

    RoleEnum value();
}
