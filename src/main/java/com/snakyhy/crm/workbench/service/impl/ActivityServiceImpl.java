package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.workbench.dao.ActivityDao;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


    public boolean save(Activity act) {

        boolean flag=true;

        int count=activityDao.save(act);

        if(count!=1){

            flag=false;
        }
        return flag;
    }
}
