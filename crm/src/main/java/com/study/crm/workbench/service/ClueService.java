package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String, Object> map);

    int queryCountOfClueByCondition(Map<String, Object> map);

    Clue queryClueForDetailById(String id);

    void saveClueConvert(Map<String, Object> map);

    int updateClue(Clue clue);

    Clue queryClueDetailById(String id);

    void deleteClueByIds(String[] ids);
}
