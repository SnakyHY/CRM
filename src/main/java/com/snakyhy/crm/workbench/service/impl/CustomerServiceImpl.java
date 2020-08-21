package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.workbench.dao.CustomerDao;
import com.snakyhy.crm.workbench.dao.CustomerRemarkDao;
import com.snakyhy.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);


    @Override
    public List<String> getCustomerName(String name) {

        List<String> sList=customerDao.getCustomerName(name);

        return sList;
    }
}
