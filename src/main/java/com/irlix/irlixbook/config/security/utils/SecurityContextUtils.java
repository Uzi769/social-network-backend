package com.irlix.irlixbook.config.security.utils;

import com.irlix.irlixbook.dao.entity.UserEntity;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class SecurityContextUtils {

    public static UserEntity getUserFromContext() {
        return ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}