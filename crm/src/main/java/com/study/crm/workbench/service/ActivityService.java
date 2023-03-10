package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    List<Activity> selectAllActivities();

    List<Activity> selectActivityByIds(String[] ids);

    int insertActivitiesByList(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityForDetailByNameExcludeClueId(Map<String, Object> map);

    List<Activity> queryActivityForDetailByIds(String[] activityIds);

    List<Activity> queryActivityForConvertByNameIncludeClueId(Map<String, Object> map);

    List<Activity> queryActivityByName(String activityName);

    List<Activity> queryActivityByContactId(String contactId);

    List<Activity> queryActivityByNameExcludeContactsId(Map<String, Object> map);
}
