package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkService {
    List<TranRemark> queryTranRemarkByTranId(String tranId);
}
