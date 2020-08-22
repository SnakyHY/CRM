package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.utils.DateTimeUtil;
import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.workbench.dao.CustomerDao;
import com.snakyhy.crm.workbench.dao.TranDao;
import com.snakyhy.crm.workbench.dao.TranHistoryDao;
import com.snakyhy.crm.workbench.domain.Customer;
import com.snakyhy.crm.workbench.domain.Tran;
import com.snakyhy.crm.workbench.domain.TranHistory;
import com.snakyhy.crm.workbench.service.TranService;

import java.util.List;

public class TranServiceImpl implements TranService {

    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran t, String customerName) {

        boolean flag=true;

        //根据客户姓名进行精确查询，若没有客户，新建
        Customer cus = customerDao.getByName(customerName);

        if(cus==null){

            cus=new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1=customerDao.save(cus);
            if(count1!=1){

                flag=false;
            }

        }

        t.setCustomerId(cus.getId());

        //添加交易
        int count2=tranDao.save(t);
        if(count2!=1){

            flag=false;

        }

        //添加交易历史
        TranHistory th=new TranHistory();
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setId(UUIDUtil.getUUID());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getCreateTime());
        th.setCreateBy(t.getCreateBy());
        int count3=tranHistoryDao.save(th);
        if(count3!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t=tranDao.detail(id);

        return t;

    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList=tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag=true;

        //改变交易阶段
        int count1=tranDao.changeStage(t);

        if(count1!=1){

            flag=false;
        }

        //生成一条交易历史
        TranHistory th=new TranHistory();
        th.setPossibility(t.getPossibility());
        th.setCreateBy(t.getEditBy());
        th.setTranId(t.getId());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setId(UUIDUtil.getUUID());

        int count2=tranHistoryDao.save(th);

        if(count2!=1){

            flag=false;

        }

        return flag;
    }
}
