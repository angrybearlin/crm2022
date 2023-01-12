package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.DicValue;
import com.study.crm.settings.domain.User;
import com.study.crm.settings.mapper.DicValueMapper;
import com.study.crm.settings.service.DicValueService;
import com.study.crm.settings.service.UserService;
import com.study.crm.workbench.domain.Activity;
import com.study.crm.workbench.domain.Clue;
import com.study.crm.workbench.domain.ClueActivityRelation;
import com.study.crm.workbench.domain.ClueRemark;
import com.study.crm.workbench.service.ActivityService;
import com.study.crm.workbench.service.ClueActivityRelationService;
import com.study.crm.workbench.service.ClueRemarkService;
import com.study.crm.workbench.service.ClueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller("clueController")
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellations = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStates = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList", userList);
        request.setAttribute("appellations", appellations);
        request.setAttribute("clueStates", clueStates);
        request.setAttribute("sources", sources);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateUtil.formatDateTime(new Date()));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setCreateBy(user.getId());
        RetValue retValue = new RetValue();
        try {
            int count = clueService.saveCreateClue(clue);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试");
        }
        return retValue;
    }

    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname, String company, String phone, String source, String owner,
                                              String mphone, String state, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("pageSize", pageSize);
        int startIndex = (pageNo - 1) * pageSize;
        map.put("startIndex", startIndex);
        Map<String, Object> resultMap = new HashMap<>();
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        resultMap.put("clueList", clueList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    @RequestMapping("/workbench/clue/clueDetail.do")
    public String clueDetail(String clueId, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(clueId);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(clueId);
        request.setAttribute("clue", clue);
        request.setAttribute("clueRemarkList", clueRemarkList);
        request.setAttribute("activityList", activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/selectActivityForDetailByNameExcludeClueId.do")
    @ResponseBody
    public Object selectActivityForDetailByNameExcludeClueId(String activityName, String clueId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityName", activityName);
        params.put("clueId", clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByNameExcludeClueId(params);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(activityList);
        return retValue;
    }

    @RequestMapping("/workbench/clue/insertBundRelation.do")
    @ResponseBody
    public Object insertBundRelation(String[] activityIds, String clueId) {
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for(String id: activityIds) {
            ClueActivityRelation relation = new ClueActivityRelation();
            relation.setActivityId(id);
            relation.setClueId(clueId);
            relation.setId(UUIDUtil.getUUID());
            relationList.add(relation);
        }
        RetValue retValue = new RetValue();
        try {
            int count = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if (count > 0) {
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityIds);
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
                retValue.setData(activityList);
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

    @RequestMapping("/workbench/clue/unBundRelation.do")
    @ResponseBody
    public Object unBundRelation(ClueActivityRelation relation) {
        RetValue retValue = new RetValue();
        try {
            int count = clueActivityRelationService.deleteRelationByClueIdActivityId(relation);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/selectActivityForConvertByNameIncludeClueId.do")
    @ResponseBody
    public Object selectActivityForConvertByNameIncludeClueId(String activityName, String clueId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityName", activityName);
        params.put("clueId", clueId);
        List<Activity> activityList = activityService.queryActivityForConvertByNameIncludeClueId(params);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(activityList);
        return retValue;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId, String money, String name, String expectedDate, String stage, String activityId,
                              HttpSession session, String isCreateTran) {
        Map<String, Object> params = new HashMap<>();
        params.put("clueId", clueId);
        params.put("name", name);
        params.put("money", money);
        params.put("expectedDate", expectedDate);
        params.put("stage", stage);
        params.put("activityId", activityId);
        params.put("isCreateTran", isCreateTran);
        params.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));
        RetValue retValue = new RetValue();
        try {
            clueService.saveClueConvert(params);
            retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retValue.setMsg("线索转换成功");
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }
}
