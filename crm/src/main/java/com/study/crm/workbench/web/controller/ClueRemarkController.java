package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.workbench.domain.ClueRemark;
import com.study.crm.workbench.service.ClueRemarkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ClueRemarkController {
    @Autowired
    private ClueRemarkService clueRemarkService;

    /**
     * 新增线索备注
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/clue/insertClueRemark.do")
    @ResponseBody
    public Object insertClueRemark(ClueRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtil.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_HAS_NOT_EDIT);
        RetValue retValue = new RetValue();
        try {
            int count = clueRemarkService.insertClueRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("新增备注成功");
                retValue.setData(remark);
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
     * 修改线索备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/updateClueRemark.do")
    @ResponseBody
    public Object updateClueRemark(ClueRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditFlag(Contants.REMARK_HAS_EDITED);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtil.formatDateTime(new Date()));
        RetValue retValue = new RetValue();
        try {
            int count = clueRemarkService.updateClueRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("更新备注成功");
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

    /**
     * 删除线索备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueRemark.do")
    @ResponseBody
    public Object deleteClueRemark(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = clueRemarkService.deleteClueRemarkById(id);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("删除成功");
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
