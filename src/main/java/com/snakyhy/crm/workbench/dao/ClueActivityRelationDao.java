package com.snakyhy.crm.workbench.dao;

import com.snakyhy.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation car);
}
