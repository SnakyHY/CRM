package com.snakyhy.crm.settings.dao;

import com.snakyhy.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
