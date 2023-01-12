package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.TranRemark;
import com.study.crm.workbench.mapper.TranRemarkMapper;
import com.study.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<TranRemark> queryTranRemarkByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkByTranId(tranId);
    }
}
