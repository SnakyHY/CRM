package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.settings.dao.UserDao;
import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.vo.PaginationVO;
import com.snakyhy.crm.workbench.dao.ActivityDao;
import com.snakyhy.crm.workbench.dao.ActivityRemarkDao;
import com.snakyhy.crm.workbench.domain.Activity;
import com.snakyhy.crm.workbench.domain.ActivityRemark;
import com.snakyhy.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


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


    public boolean delete(String[] ids) {

        boolean flag=true;

        //查询出需要删除的备注数量
        int count1=activityRemarkDao.getCountByAids(ids);

        //查询出实际删除的备注数量
        int count2=activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){
            flag=false;
        }

        //删除市场条目
        int count3=activityDao.delete(ids);

        if(count3!=ids.length){
            flag=false;
        }

        return flag;
    }


    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList=userDao.getUserList();

        //取a
        Activity a=activityDao.getById(id);

        //打包成map
        Map<String,Object>map=new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);

        return map;
    }


    public boolean update(Activity act) {

        boolean flag=true;

        int count=activityDao.update(act);

        if(count!=1){

            flag=false;
        }
        return flag;

    }


    public Activity detail(String id) {

        Activity a=activityDao.detail(id);

        return a;
    }


    public List<ActivityRemark> getRemarkListByAid(String id) {

        List<ActivityRemark> arList=activityRemarkDao.getRemarkListByAid(id);

        return arList;
    }


    public boolean deleteRemark(String id) {

        boolean flag=true;

        int count=activityRemarkDao.deleteById(id);

        if(count!=1){
            flag=false;
        }

        return flag;
    }


    public boolean saveRemark(ActivityRemark ar) {

        boolean flag=true;

        int count=activityRemarkDao.saveRemark(ar);

        if(count!=1){

            flag=false;

        }

        return flag;
    }


    public boolean updateRemark(ActivityRemark ar) {

        boolean flag=true;

        int count=activityRemarkDao.updateRemark(ar);

        if(count!=1){

            flag=false;

        }

        return flag;
    }
}
