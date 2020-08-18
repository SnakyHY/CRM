package com.snakyhy.crm.workbench.dao;

import com.snakyhy.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String id);

    int deleteById(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
