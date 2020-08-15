package com.snakyhy.crm.settings.dao;

import com.snakyhy.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
