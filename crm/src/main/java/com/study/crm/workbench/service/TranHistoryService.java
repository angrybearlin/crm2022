package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryByTranId(String tranId);
}
