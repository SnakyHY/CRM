package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.workbench.dao.TranDao;
import com.snakyhy.crm.workbench.dao.TranHistoryDao;
import com.snakyhy.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {

    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


}
