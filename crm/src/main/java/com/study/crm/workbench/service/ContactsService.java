package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.Contacts;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ContactsService {
    List<Contacts> queryContactsByName(String contactsName);
}
