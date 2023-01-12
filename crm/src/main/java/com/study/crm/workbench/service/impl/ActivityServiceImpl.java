package com.study.crm.workbench.service.impl;

import com.study.crm.workbench.domain.Activity;
import com.study.crm.workbench.mapper.ActivityMapper;
import com.study.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.saveUpdateActivity(activity);
    }

    @Override
    public List<Activity> selectAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> selectActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public int insertActivitiesByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    @Override
    public List<Activity> queryActivityForDetailByClueId(String clueId) {
        return activityMapper.selectActivityForDetailByClueId(clueId);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameExcludeClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameExcludeClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] activityIds) {
        return activityMapper.selectActivityForDetailByIds(activityIds);
    }

    @Override
    public List<Activity> queryActivityForConvertByNameIncludeClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForConvertByNameIncludeClueId(map);
    }

    @Override
    public List<Activity> queryActivityByName(String activityName) {
        return activityMapper.selectActivityByName(activityName);
    }
}
