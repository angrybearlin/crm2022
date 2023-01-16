package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.Contacts;
import com.study.crm.workbench.mapper.ContactsMapper;
import com.study.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Override
    public List<Contacts> queryContactsByName(String contactsName) {
        return contactsMapper.selectContactsByName(contactsName);
    }

    @Override
    public List<Contacts> queryContactsByCustomerId(String customerId) {
        return contactsMapper.selectContactsByCustomerId(customerId);
    }
}
