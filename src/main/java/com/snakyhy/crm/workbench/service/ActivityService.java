package com.snakyhy.crm.workbench.service;

import com.snakyhy.crm.vo.PaginationVO;
import com.snakyhy.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    boolean save(Activity act);

    PaginationVO<Activity> pageList(Map<String, Object> map);
}
