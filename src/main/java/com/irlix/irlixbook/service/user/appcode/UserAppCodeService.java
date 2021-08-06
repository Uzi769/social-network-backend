package com.irlix.irlixbook.service.user.appcode;

import com.irlix.irlixbook.dao.entity.UserAppCode;

public interface UserAppCodeService {

    UserAppCode addNewCode(UserAppCode appCode);

    UserAppCode findByEmail(String email);
}
