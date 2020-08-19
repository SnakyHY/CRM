package com.snakyhy.crm.settings.service.impl;

import com.snakyhy.crm.settings.dao.DicTypeDao;
import com.snakyhy.crm.settings.dao.DicValueDao;
import com.snakyhy.crm.settings.domain.DicType;
import com.snakyhy.crm.settings.domain.DicValue;
import com.snakyhy.crm.settings.service.DicService;
import com.snakyhy.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    public Map<String, List<DicValue>> getAll() {

        //将字典类型列表取出
        List<DicType> dtList=dicTypeDao.getTypeList();

        Map<String, List<DicValue>> map=new HashMap<>();

        //将字典类型列表遍历
        for(DicType dt:dtList){

            String code=dt.getCode();

            //根据每一个字典类型来得到每一个类型对应的字典值表
            List<DicValue> dvList=dicValueDao.getListByCode(code);

            map.put(code+"List", dvList);
        }

        return map;
    }
}
