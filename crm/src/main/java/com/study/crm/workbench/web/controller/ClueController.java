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
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * 跳转到线索页面
     * @param request
     * @return
     */
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

    /**
     * 保存新增线索
     * @param clue
     * @param session
     * @return
     */
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

    /**
     * 条件查询线索并分页
     * @param fullname
     * @param company
     * @param phone
     * @param source
     * @param owner
     * @param mphone
     * @param state
     * @param pageNo
     * @param pageSize
     * @return
     */
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

    /**
     * 跳转到线索详情页
     * @param clueId
     * @param request
     * @return
     */
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

    /**
     * 查询和当前线索不关联的其他市场活动
     * @param activityName
     * @param clueId
     * @return
     */
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

    /**
     * 新增线索和市场活动绑定关系
     * @param activityIds
     * @param clueId
     * @return
     */
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

    /**
     * 解除线索和市场活动绑定关系
     * @param relation
     * @return
     */
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

    /**
     * 跳转到线索转换页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);
        return "workbench/clue/convert";
    }

    /**
     * 查询和当前线索绑定的市场活动
     * @param activityName
     * @param clueId
     * @return
     */
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

    /**
     * 转换线索
     * @param clueId
     * @param money
     * @param name
     * @param expectedDate
     * @param stage
     * @param activityId
     * @param session
     * @param isCreateTran
     * @return
     */
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

    /**
     * 修改线索
     * @param params
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/updateClue.do")
    @ResponseBody
    public Object updateClue(@RequestParam Map<String, Object> params, HttpSession session) {
        Clue clue = new Clue();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setId((String) params.get("id"));
        clue.setAddress((String) params.get("address"));
        clue.setAppellation((String) params.get("appellation"));
        clue.setCompany((String) params.get("company"));
        clue.setContactSummary((String) params.get("contactSummary"));
        clue.setDescription((String) params.get("description"));
        clue.setEmail((String) params.get("email"));
        clue.setFullname((String) params.get("fullname"));
        clue.setJob((String) params.get("job"));
        clue.setMphone((String) params.get("mphone"));
        clue.setNextContactTime((String) params.get("nextContactTime"));
        clue.setOwner((String) params.get("owner"));
        clue.setSource((String) params.get("source"));
        clue.setState((String) params.get("state"));
        clue.setWebsite((String) params.get("website"));
        clue.setPhone((String) params.get("phone"));
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtil.formatDateTime(new Date()));
        RetValue retValue = new RetValue();
        try {
            int count = clueService.updateClue(clue);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("更新成功");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后再试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试。。。");
        }
        return retValue;
    }

    /**
     * 查询出线索的信息展示在线索修改页面
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/selectClueDetailForUpdate.do")
    @ResponseBody
    public Object selectClueDetailForUpdate(String clueId) {
        Clue clue = clueService.queryClueDetailById(clueId);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(clue);
        return retValue;
    }

    @RequestMapping("/workbench/clue/deleteClue.do")
    @ResponseBody
    public Object deleteClue(String[] ids) {
        RetValue retValue = new RetValue();
        try {
            clueService.deleteClueByIds(ids);
            retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retValue.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }
}
