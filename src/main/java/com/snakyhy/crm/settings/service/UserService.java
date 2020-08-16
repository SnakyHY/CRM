package com.snakyhy.crm.settings.service;

import com.snakyhy.crm.exception.LoginException;
import com.snakyhy.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
