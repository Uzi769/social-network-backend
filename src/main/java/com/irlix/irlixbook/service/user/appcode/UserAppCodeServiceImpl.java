package com.irlix.irlixbook.service.user.appcode;

import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserAppCodeServiceImpl implements UserAppCodeService {

    @Autowired
    private UserAppCodeRepository repository;

    @Override
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
    public UserAppCode findByEmail(String email) {
        return repository.findById(email)
                .orElseThrow(() -> new NotFoundException("User code by email: " + email + " not found!!!"));
    }
}
