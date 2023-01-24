package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.DicValue;
import com.study.crm.settings.domain.User;
import com.study.crm.settings.service.DicValueService;
import com.study.crm.settings.service.UserService;
import com.study.crm.workbench.domain.*;
import com.study.crm.workbench.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller("tranController")
public class TranController {
    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

    /**
     * 跳转到交易页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/transaction/index";
    }

    /**
     * 跳转到创建交易页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/transaction/save";
    }

    /**
     * 跳转到修改交易页面
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/workbench/transaction/toEdit.do")
    public String toEdit(HttpServletRequest request, String id) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        Map<String, Object> map = tranService.queryTranForUpdateById(id);
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString((String) map.get("stage"));
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("tran", map);
        request.setAttribute("possibility", possibility);
        request.setAttribute("stageId", tranService.queryStageIdOfTran(id));
        return "workbench/transaction/edit";
    }

    /**
     * 创建交易时根据名称查询市场活动
     * @param activityName
     * @return
     */
    @RequestMapping("/workbench/transaction/searchActivity.do")
    @ResponseBody
    public Object searchActivity(String activityName) {
        List<Activity> activityList = activityService.queryActivityByName(activityName);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(activityList);
        return retValue;
    }

    /**
     * 创建交易时根据名称查询联系人
     * @param contactsName
     * @return
     */
    @RequestMapping("/workbench/transaction/searchContacts.do")
    @ResponseBody
    public Object searchContacts(String contactsName) {
        List<Contacts> contactsList = contactsService.queryContactsByName(contactsName);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(contactsList);
        return retValue;
    }

    /**
     * 根据阶段名称从配置文件获取对应的可能性
     * @param stageValue
     * @return
     */
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    @ResponseBody
    public Object getPossibilityByStageValue(String stageValue) {
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(possibility);
        return retValue;
    }

    /**
     * 查询所有客户名称
     * @return
     */
    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String customerName) {
        List<Customer> customerList = customerService.queryCustomerNameByName(customerName);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(customerList);
        return retValue;
    }

    /**
     * 保存创建交易
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String, Object> map, HttpSession session) {
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));
        RetValue retValue = new RetValue();
        try {
            tranService.saveCreateTran(map);
            retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retValue.setMsg("OK");
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试。。。");
        }
        return retValue;
    }

    /**
     * 按条件分页查询交易
     * @param source
     * @param stage
     * @param name
     * @param type
     * @param owner
     * @param customerName
     * @param contactsName
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping("/workbench/transaction/queryTranByConditionForPages.do")
    @ResponseBody
    public Object queryTranByConditionForPages(String source, String stage, String name, String type, String owner,
                                               String customerName, String contactsName, int pageSize, int pageNo) {
        int startIndex = (pageNo - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("stage", stage);
        params.put("name", name);
        params.put("type", type);
        params.put("owner", owner);
        params.put("customerName", customerName);
        params.put("contactsName", contactsName);
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
        List<Tran> tranList = tranService.queryTranByConditionForPages(params);
        int totalRows = tranService.queryCountOfTranByCondition(params);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("tranList", tranList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    /**
     * 跳转到交易明细页面
     * @param tranId
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/toTranDetail.do")
    public String toTranDetail(String tranId, HttpServletRequest request) {
        Tran tran = tranService.queryTranForDetailById(tranId);
        List<TranRemark> tranRemarkList = tranRemarkService.queryTranRemarkByTranId(tranId);
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryByTranId(tranId);

        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("tran", tran);
        request.setAttribute("tranRemarkList", tranRemarkList);
        request.setAttribute("tranHistoryList", tranHistoryList);
        request.setAttribute("stageList", stageList);
        return "workbench/transaction/detail";
    }

    /**
     * 按阶段分组查询每组中交易的数量
     * @return
     */
    @RequestMapping("/workbench/chart/transaction/selectCountOfTranGroupByStage.do")
    @ResponseBody
    public Object selectCountOfTranGroupByStage() {
        List<FunnelVO> funnelVOList = tranService.queryCountOfTranGroupByStage();
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(funnelVOList);
        return retValue;
    }

    /**
     * 根据id删除交易
     * @param id
     * @return
     */
    @RequestMapping("/workbench/transaction/deleteTranById.do")
    @ResponseBody
    public Object deleteTranById(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = tranService.deleteTranById(id);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("删除成功");
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
     * 修改交易
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/editTran.do")
    @ResponseBody
    public Object editTran(@RequestParam Map<String, Object> map, HttpSession session) {
        Tran tran = new Tran();
        tran.setId((String) map.get("id"));
        tran.setOwner((String) map.get("owner"));
        tran.setMoney((String) map.get("money"));
        tran.setName((String) map.get("name"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        Customer customer = customerService.queryCustomerByName((String) map.get("customerName"));
        tran.setCustomerId(customer.getId());
        tran.setStage((String) map.get("stage"));
        tran.setType((String) map.get("type"));
        tran.setSource((String) map.get("source"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setDescription((String) map.get("description"));
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        tran.setEditBy(user.getId());
        tran.setEditTime(DateUtil.formatDateTime(new Date()));
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setStage((String) map.get("stage"));
        tranHistory.setTranId((String) map.get("id"));
        tranHistory.setMoney((String) map.get("money"));
        tranHistory.setCreateTime(DateUtil.formatDateTime(new Date()));
        tranHistory.setExpectedDate((String) map.get("expectedDate"));
        RetValue retValue = new RetValue();
        try {
            int count = tranService.updateTranById(tran, tranHistory);
            if (count == 2) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("修改成功");
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
     * 创建交易备注
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/transaction/insertTranRemark.do")
    @ResponseBody
    public Object insertTranRemark(TranRemark remark, HttpSession session) {
        RetValue retValue = new RetValue();
        remark.setId(UUIDUtil.getUUID());
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtil.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_HAS_NOT_EDIT);
        try {
            int count = tranRemarkService.insertTranRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("创建成功");
                retValue.setData(remark);
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
     * 修改交易备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/updateTranRemark.do")
    @ResponseBody
    public Object updateTranRemark(TranRemark remark, HttpSession session) {
        RetValue retValue = new RetValue();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtil.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_HAS_EDITED);
        try {
            int count = tranRemarkService.updateTranRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("修改成功");
                retValue.setData(remark);
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
     * 删除交易备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/transaction/deleteTranRemark.do")
    @ResponseBody
    public Object deleteTranRemark(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = tranRemarkService.deleteTranRemarkById(id);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("删除成功");
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
}
