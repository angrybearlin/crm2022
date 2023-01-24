package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationService {
    int saveContactsActivityRelationByList(List<ContactsActivityRelation> list);

    int deleteContactsActivityRelationByActivityId(ContactsActivityRelation relation);
}
