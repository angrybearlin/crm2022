package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.ClueRemark;
import com.study.crm.workbench.mapper.ClueRemarkMapper;
import com.study.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }

    @Override
    public int insertClueRemark(ClueRemark remark) {
        return clueRemarkMapper.insertClueRemark(remark);
    }

    @Override
    public int updateClueRemark(ClueRemark remark) {
        return clueRemarkMapper.updateClueRemark(remark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteByPrimaryKey(id);
    }
}
