package com.snakyhy.crm.workbench.service.impl;

import com.snakyhy.crm.settings.domain.User;
import com.snakyhy.crm.utils.DateTimeUtil;
import com.snakyhy.crm.utils.SqlSessionUtil;
import com.snakyhy.crm.utils.UUIDUtil;
import com.snakyhy.crm.workbench.dao.*;
import com.snakyhy.crm.workbench.domain.*;
import com.snakyhy.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


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

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        boolean flag=true;

        String createTime= DateTimeUtil.getSysTime();

        //根据id查单条
        Clue c=clueDao.getById(clueId);

        //根据公司名称创建客户(判断是否存在该客户)
        String company=c.getCompany();

        Customer cus=customerDao.getByName(company);

        if(cus==null){

            //没有该客户，需要新建
            cus=new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());

            //增加新客户
            int count1=customerDao.save(cus);

            if(count1!=1){

                flag=false;
            }

        }

        //通过线索对象联系人新增联系人
        Contacts con=new Contacts();

        con.setId(UUIDUtil.getUUID());
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        //添加联系人
        int count2=contactsDao.save(con);

        if(count2!=1){

            flag=false;
        }

        //将线索的备注转换为客户以及联系人的备注
        //查询到与线索关联的备注列表
        List<ClueRemark> clueRemarkList=clueRemarkDao.getListByClueId(clueId);

        for(ClueRemark clueRemark:clueRemarkList){

            //主要取出备注信息
            String noteContent=clueRemark.getNoteContent();

            //创建客户备注对象
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);

            int count3=customerRemarkDao.save(customerRemark);
            if(count3!=1){

                flag=false;

            }

            //创建联系人备注对象
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);

            int count4=contactsRemarkDao.save(contactsRemark);
            if(count4!=1){

                flag=false;

            }
        }

        //转移市场活动与线索的联系到联系人关系表
        //查询关联关系列表
        List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.getListByClueId(clueId);
        //遍历每一条关联的信息
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            String activityId=clueActivityRelation.getActivityId();

            //创建联系人与市场活动的关联关系对象
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            //添加关联关系列表
            int count5=contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){

                flag=false;

            }
        }


        //如果有交易创建请求，创建一条交易
        if(t!=null){

            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());

            //添加交易
            int count6=tranDao.save(t);
            if(count6!=1){

                flag=false;

            }

            //如果创建了交易,创建交易历史
            TranHistory th=new TranHistory();
            th.setTranId(t.getId());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setId(UUIDUtil.getUUID());
            th.setExpectedDate(t.getExpectedDate());
            th.setCreateTime(createTime);
            th.setCreateBy(createBy);

            int count7=tranHistoryDao.save(th);
            if(count7!=1){

                flag=false;

            }


        }


        //删除相关操作
        //删除线索对应的备注
        for(ClueRemark clueRemark:clueRemarkList){

            int count8=clueRemarkDao.delete(clueRemark);
            if(count8!=1){

                flag=false;
            }
        }

        //删除市场和线索关联关系
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            int count9=clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){

                flag=false;
            }
        }

        //删除线索
        int count10=clueDao.delete(clueId);
        if(count10!=1){

            flag=false;

        }


        return flag;
    }


}
