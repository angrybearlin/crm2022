package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    int insertClueRemark(ClueRemark remark);

    int updateClueRemark(ClueRemark remark);

    int deleteClueRemarkById(String id);
}
