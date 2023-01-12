package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.TranHistory;
import com.study.crm.workbench.mapper.TranHistoryMapper;
import com.study.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<TranHistory> queryTranHistoryByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryByTranId(tranId);
    }
}
