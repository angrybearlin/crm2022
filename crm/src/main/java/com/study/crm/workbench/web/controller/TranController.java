package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
}
