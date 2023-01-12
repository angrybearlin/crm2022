package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> queryCustomerNameByName(String customerName);
}
