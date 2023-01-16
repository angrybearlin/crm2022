package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<Customer> queryCustomerNameByName(String customerName);

    List<Customer> queryCustomerByConditionForPages(Map<String, Object> map);

    int queryCountOfCustomerByCondition(Map<String, Object> map);

    int insertCustomer(Customer customer);

    int updateCustomer(Customer customer);

    Customer queryCustomerForUpdate(String id);

    int deleteCustomerById(String[] ids);

    Customer queryCustomerForDetailById(String id);
}
