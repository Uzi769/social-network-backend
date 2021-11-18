package com.irlix.irlixbook.config.security.utils;

import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.exception.UnauthorizedException;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class SecurityContextUtils {

    public static User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
            throw new UnauthorizedException("Unauthorized");
        }
        return (User) authentication.getPrincipal();
    }
}