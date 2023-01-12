package com.study.crm.workbench.service;

import com.study.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    int saveActivityRemark(ActivityRemark remark);

    int deleteActivityRemarkById(String id);

    int saveEditActivityRemark(ActivityRemark remark);
}
