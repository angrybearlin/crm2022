package com.study.crm.workbench.service.impl;

import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.workbench.domain.Contacts;
import com.study.crm.workbench.domain.Customer;
import com.study.crm.workbench.mapper.*;
import com.study.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Override
    public List<Contacts> queryContactsByName(String contactsName) {
        return contactsMapper.selectContactsByName(contactsName);
    }

    @Override
    public List<Contacts> queryContactsByCustomerId(String customerId) {
        return contactsMapper.selectContactsByCustomerId(customerId);
    }

    @Override
    public int insertContactOnCustomerDetail(Contacts contacts) {
        return contactsMapper.insertContactOnCustomerDetail(contacts);
    }

    @Override
    public void deleteContactById(String id) {
        contactsRemarkMapper.deleteContactRemarkByContactsId(id);
        tranMapper.deleteTranByContactsId(id);
        contactsActivityRelationMapper.deleteConActRelationByContactsId(id);
        contactsMapper.deleteContactById(id);
    }

    @Override
    public List<Contacts> queryContactByConditionForPages(Map<String, Object> map) {
        List<Contacts> contactsList = contactsMapper.selectContactByConditionForPages(map);
        for (Contacts con: contactsList) {
            if (con.getCustomerId() == null) {
                con.setCustomerId("无");
            }
        }
        return contactsList;
    }

    @Override
    public int queryCountOfContactByCondition(Map<String, Object> map) {
        return contactsMapper.selectCountOfContactByCondition(map);
    }

    @Override
    public int insertContact(Map<String, Object> map) {
        Contacts contacts = (Contacts) map.get("contacts");
        User user = (User) map.get("user");
        String customerName = contacts.getCustomerId();
        if (!"".equals(customerName)) {
            Customer customer = customerMapper.selectCustomerByName(customerName);
            // 如果按名字没有查询到客户信息，新建一个客户
            if (customer == null) {
                customer = new Customer();
                customer.setId(UUIDUtil.getUUID());
                customer.setOwner(user.getId());
                customer.setName(customerName);
                customer.setCreateBy(user.getId());
                customer.setCreateTime(DateUtil.formatDateTime(new Date()));
                customerMapper.insert(customer);
            }
            contacts.setCustomerId(customer.getId());
        }
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtil.formatDateTime(new Date()));
        return contactsMapper.insertContactOnCustomerDetail(contacts);
    }

    @Override
    public int updateContact(Map<String, Object> map) {
        Contacts contacts = (Contacts) map.get("contacts");
        User user = (User) map.get("user");
        String customerName = contacts.getCustomerId();
        if (!"".equals(customerName)) {
            Customer customer = customerMapper.selectCustomerByName(customerName);
            // 如果按名字没有查询到客户信息，新建一个客户
            if (customer == null) {
                customer = new Customer();
                customer.setId(UUIDUtil.getUUID());
                customer.setOwner(user.getId());
                customer.setName(customerName);
                customer.setCreateBy(user.getId());
                customer.setCreateTime(DateUtil.formatDateTime(new Date()));
                customerMapper.insert(customer);
            }
            contacts.setCustomerId(customer.getId());
        } else {
            contacts.setCustomerId("");
        }
        return contactsMapper.updateContact(contacts);
    }

    @Override
    public Contacts queryContactById(String id) {
        return contactsMapper.selectContactById(id);
    }

    @Override
    public Contacts queryContactByIdForDetail(String id) {
        return contactsMapper.selectContactByIdForDetail(id);
    }

    @Override
    public void deleteContactsByIds(String[] ids) {
        String contactsId = null;
        for (String id: ids) {
            contactsId = id;
            contactsRemarkMapper.deleteContactRemarkByContactsId(contactsId);
            tranMapper.deleteTranByContactsId(contactsId);
            contactsActivityRelationMapper.deleteConActRelationByContactsId(contactsId);
            contactsMapper.deleteContactById(id);
        }
    }
}
