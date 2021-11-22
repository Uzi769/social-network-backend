package com.irlix.irlixbook.service.user.appcode;

import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAppCodeServiceImpl implements UserAppCodeService {

    private final UserAppCodeRepository repository;

    @Override
    @Transactional
    public UserAppCode addNewCode(UserAppCode appCode) {
        Optional<UserAppCode> userAppCode = repository.findById(appCode.getEmail());
        if (userAppCode.isPresent()) {
            UserAppCode existedCode = userAppCode.get();
            existedCode.getCodes().addAll(appCode.getCodes());
            return repository.save(existedCode);
        } else {
            return repository.save(appCode);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserAppCode findByEmail(String email) {
        return repository.findById(email)
                .orElseThrow(() -> new NotFoundException("User code by email: " + email + " not found!!!"));
    }
}
