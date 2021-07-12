package com.irlix.irlixbook.config.security.aspect;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import com.irlix.irlixbook.exception.ForbiddenException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RoleAndPermissionChecker {

    @Before("@annotation(com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck)")
    public void check(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RoleAndPermissionCheck myAnnotation = method.getAnnotation(RoleAndPermissionCheck.class);
        RoleEnam[] availableRoles = myAnnotation.value();

        UserEntity userFromContext = SecurityContextUtils.getUserFromContext();
        List<Role> roles = userFromContext.getRoles();

        boolean isNotValidRole = roles.stream()
                .map(Role::getName)
                .noneMatch(r ->
                        Arrays.stream(availableRoles).anyMatch(ar -> ar == r)
                );

        if (isNotValidRole) {
            throw new ForbiddenException("User " + userFromContext.getEmail() + ", has not role or permission on method: " + method.getName());
        }
    }
}