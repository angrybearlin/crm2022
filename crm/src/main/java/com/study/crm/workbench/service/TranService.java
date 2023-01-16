package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.FunnelVO;
import com.study.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    void saveCreateTran(Map<String, Object> map);

    List<Tran> queryTranByConditionForPages(Map<String, Object> map);

    int queryCountOfTranByCondition(Map<String, Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

    List<Tran> queryTranByCustomerId(String customerId);
}
