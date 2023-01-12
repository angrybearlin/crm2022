package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.Customer;
import com.study.crm.workbench.mapper.CustomerMapper;
import com.study.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> queryCustomerNameByName(String customerName) {
        return customerMapper.selectCustomerNameByName(customerName);
    }
}
