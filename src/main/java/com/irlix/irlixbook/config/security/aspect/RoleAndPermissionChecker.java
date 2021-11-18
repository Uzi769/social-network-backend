package com.irlix.irlixbook.config.security.aspect;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.exception.ForbiddenException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class RoleAndPermissionChecker {

    @Before("@annotation(com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck)")
    public void check(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RoleAndPermissionCheck myAnnotation = method.getAnnotation(RoleAndPermissionCheck.class);
        RoleEnum availableRole = myAnnotation.value();

        User userFromContext = SecurityContextUtils.getUserFromContext();
        Role role = userFromContext.getRole();

        RoleEnum[] includeRoles = role.getName().includeRoles();
        RoleEnum[] availableRoles;

        if (includeRoles.length > 0) {

            availableRoles = new RoleEnum[includeRoles.length + 1];

            for (int i = 0; i < includeRoles.length; i++)
                availableRoles[i] = includeRoles[i];

            availableRoles[includeRoles.length] = role.getName();

        } else {
            availableRoles = new RoleEnum[]{role.getName()};
        }

        boolean isNotValidRole = Arrays
                .stream(availableRoles)
                .noneMatch(ar -> ar == availableRole);

        if (isNotValidRole)
            throw new ForbiddenException("User " + userFromContext.getEmail() + ", has not role or permission on method: " + method.getName());

    }

}
