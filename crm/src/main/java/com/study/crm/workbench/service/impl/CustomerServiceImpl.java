package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.Customer;
import com.study.crm.workbench.mapper.ContactsMapper;
import com.study.crm.workbench.mapper.CustomerMapper;
import com.study.crm.workbench.mapper.CustomerRemarkMapper;
import com.study.crm.workbench.mapper.TranMapper;
import com.study.crm.workbench.service.ContactsService;
import com.study.crm.workbench.service.CustomerRemarkService;
import com.study.crm.workbench.service.CustomerService;
import com.study.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public List<Customer> queryCustomerNameByName(String customerName) {
        return customerMapper.selectCustomerNameByName(customerName);
    }

    @Override
    public List<Customer> queryCustomerByConditionForPages(Map<String, Object> map) {
        List<Customer> customerList = customerMapper.selectCustomerByConditionForPages(map);
        for (Customer customer: customerList) {
            if (customer.getPhone() == null) {
                customer.setPhone("无");
            }
            if (customer.getWebsite() == null) {
                customer.setWebsite("无");
            }
        }
        return customerList;
    }

    @Override
    public int queryCountOfCustomerByCondition(Map<String, Object> map) {
        return customerMapper.selectCountOfCustomerByCondition(map);
    }

    @Override
    public int insertCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public Customer queryCustomerForUpdate(String id) {
        return customerMapper.selectCustomerForUpdate(id);
    }

    @Override
    public void deleteCustomerById(String[] ids) {
        customerMapper.deleteCustomerById(ids);
        for (String id: ids) {
            customerRemarkMapper.deleteCustomerRemarkByCustomerId(id);
            tranMapper.deleteTranByCustomerId(id);
            contactsMapper.deleteContactByCustomerId(id);
        }
    }

    @Override
    public Customer queryCustomerForDetailById(String id) {
        return customerMapper.selectCustomerForDetailById(id);
    }

    @Override
    public Customer queryCustomerByName(String name) {
        return customerMapper.selectCustomerByName(name);
    }
}
