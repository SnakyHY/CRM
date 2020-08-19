package com.snakyhy.crm.workbench.dao;

import com.snakyhy.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);
}
