package com.irlix.irlixbook.config.security.utils;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.exception.UnauthorizedException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class SecurityContextUtils {

    public static UserEntity getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new UnauthorizedException("Unauthorized");
        }
        return (UserEntity) authentication.getPrincipal();
    }
}