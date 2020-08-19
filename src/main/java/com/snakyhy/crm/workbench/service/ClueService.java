package com.snakyhy.crm.workbench.service;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueService {
    List<User> getUserList();

    boolean save(Clue c);

    Clue detail(String id);
}
