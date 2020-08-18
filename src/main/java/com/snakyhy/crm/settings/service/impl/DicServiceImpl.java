package com.snakyhy.crm.settings.service.impl;

import com.snakyhy.crm.settings.dao.DicTypeDao;
import com.snakyhy.crm.settings.dao.DicValueDao;
import com.snakyhy.crm.settings.service.DicService;
import com.snakyhy.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
}
