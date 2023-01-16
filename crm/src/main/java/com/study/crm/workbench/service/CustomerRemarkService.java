package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {
    List<CustomerRemark> queryCustomerRemarkByCustomerId(String customerId);

    int insertCustomerRemark(CustomerRemark customerRemark);

    int updateCustomerRemark(CustomerRemark customerRemark);

    int deleteCustomerRemarkById(String id);
}
