package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.ContactsActivityRelation;
import com.study.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.study.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Override
    public int saveContactsActivityRelationByList(List<ContactsActivityRelation> list) {
        return contactsActivityRelationMapper.insertConActRelationByList(list);
    }

    @Override
    public int deleteContactsActivityRelationByActivityId(ContactsActivityRelation relation) {
        return contactsActivityRelationMapper.deleteConActRelationByActivityId(relation);
    }
}
