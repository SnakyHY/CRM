package com.snakyhy.crm.workbench.dao;

import com.snakyhy.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getByName(String name);

    int save(Customer cus);
}
