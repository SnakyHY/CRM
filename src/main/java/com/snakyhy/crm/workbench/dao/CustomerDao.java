package com.snakyhy.crm.workbench.dao;

import com.snakyhy.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getByName(String name);

    int save(Customer cus);

    List<String> getCustomerName(String name);
}
