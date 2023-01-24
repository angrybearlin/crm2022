package com.study.crm.workbench.service;

import com.study.crm.commons.contants.Contants;
import com.study.crm.workbench.domain.Contacts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    List<Contacts> queryContactsByName(String contactsName);

    List<Contacts> queryContactsByCustomerId(String customerId);

    int insertContactOnCustomerDetail(Contacts contacts);

    void deleteContactById(String id);

    List<Contacts> queryContactByConditionForPages(Map<String, Object> map);

    int queryCountOfContactByCondition(Map<String, Object> map);

    int insertContact(Map<String, Object> map);

    int updateContact(Map<String, Object> map);

    Contacts queryContactById(String id);

    Contacts queryContactByIdForDetail(String id);

    void deleteContactsByIds(String[] ids);
}
