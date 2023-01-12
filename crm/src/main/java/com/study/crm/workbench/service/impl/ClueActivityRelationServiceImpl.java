package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.ClueActivityRelation;
import com.study.crm.workbench.mapper.ClueActivityRelationMapper;
import com.study.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> relationList) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(relationList);
    }

    @Override
    public int deleteRelationByClueIdActivityId(ClueActivityRelation relation) {
        return clueActivityRelationMapper.deleteRelationByClueIdActivityId(relation);
    }
}
