package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.vo.PaginationVO;
import com.snakyhy.crm.workbench.dao.ActivityDao;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

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


    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total=activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList=activityDao.getActivityListByCondition(map);

        //封装到vo中
        PaginationVO<Activity>vo=new PaginationVO<>();
        vo.setDataList(dataList);
        vo.setTotal(total);

        return vo;
    }
}
