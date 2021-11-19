package com.irlix.irlixbook.dao.helper;

import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class UserAppCodeHelper {

    private final UserAppCodeRepository userAppCodeRepository;

    public List<UserAppCode> findAll() {
        return userAppCodeRepository.findAll();
    }
}
