package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.workbench.dao.ClueActivityRelationDao;
import com.snakyhy.crm.workbench.dao.ClueDao;
import com.snakyhy.crm.workbench.domain.Clue;
import com.snakyhy.crm.workbench.domain.ClueActivityRelation;
import com.snakyhy.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

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

    @Override
    public boolean unbund(String id) {

        boolean flag=true;

        int count=clueActivityRelationDao.unbund(id);

        if (count!=1){

            flag=false;

        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag=true;

        for(String aid:aids){

            ClueActivityRelation car=new ClueActivityRelation();

            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            int count=clueActivityRelationDao.bund(car);

            if(count!=1){
                flag=false;
            }

        }

        return flag;
    }
}
