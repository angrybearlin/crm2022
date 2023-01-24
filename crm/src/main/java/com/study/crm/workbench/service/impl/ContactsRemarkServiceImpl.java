package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.ContactsRemark;
import com.study.crm.workbench.mapper.ContactsRemarkMapper;
import com.study.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Override
    public List<ContactsRemark> queryContactRemarkByContactId(String contactId) {
        return contactsRemarkMapper.selectContactRemarkByContactId(contactId);
    }

    @Override
    public int insertContactRemark(ContactsRemark remark) {
        return contactsRemarkMapper.insertContactRemark(remark);
    }

    @Override
    public int updateContactRemark(ContactsRemark remark) {
        return contactsRemarkMapper.updateContactRemark(remark);
    }

    @Override
    public int deleteContactRemarkById(String id) {
        return contactsRemarkMapper.deleteContactRemarkById(id);
    }
}
