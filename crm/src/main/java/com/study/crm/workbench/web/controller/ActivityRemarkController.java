package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.workbench.domain.ActivityRemark;
import com.study.crm.workbench.service.ActivityRemarkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     * 添加市场活动备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    @ResponseBody
    public Object saveActivityRemark(ActivityRemark remark, HttpSession session) {
        RetValue retValue = new RetValue();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateTime(DateUtil.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.ACTIVITY_REMARK_HAS_NOT_EDIT);
        try {
            int count = activityRemarkService.saveActivityRemark(remark);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
                retValue.setData(remark);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }

    /**
     * 删除市场活动备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityRemark.do")
    @ResponseBody
    public Object deleteActivityRemark(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = activityRemarkService.deleteActivityRemarkById(id);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后再试");
            }
        } catch (Exception e) {
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试");
        }
        return retValue;
    }

    /**
     * 保存修改后的市场活动备注
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark, HttpSession session) {
        RetValue retValue = new RetValue();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditTime(DateUtil.formatDateTime(new Date()));
        remark.setEditBy(user.getId());
        remark.setEditFlag(Contants.ACTIVITY_REMARK_HAS_EDITED);
        try {
            int count = activityRemarkService.saveEditActivityRemark(remark);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
                retValue.setData(remark);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后再试。。。");
            }
        } catch (Exception e) {
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试。。。");
        }
        return retValue;
    }
}
