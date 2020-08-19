package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.workbench.dao.ClueDao;
import com.snakyhy.crm.workbench.domain.Clue;
import com.snakyhy.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    public List<User> getUserList() {
        return null;
    }

    public boolean save(Clue c) {

        boolean flag=true;

        int count=clueDao.save(c);

        if(count!=1){

            flag=false;

        }

        return flag;
    }


    public Clue detail(String id) {

        Clue c=clueDao.detail(id);

        return c;
    }
}
