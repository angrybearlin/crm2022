package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> relationList);

    int deleteRelationByClueIdActivityId(ClueActivityRelation relation);
}
