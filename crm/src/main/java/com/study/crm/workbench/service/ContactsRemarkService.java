package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkService {
    List<ContactsRemark> queryContactRemarkByContactId(String contactId);

    int insertContactRemark(ContactsRemark remark);

    int updateContactRemark(ContactsRemark remark);

    int deleteContactRemarkById(String id);
}
