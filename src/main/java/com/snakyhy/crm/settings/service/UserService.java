package com.snakyhy.crm.settings.service;

import com.snakyhy.crm.exception.LoginException;
import com.snakyhy.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
