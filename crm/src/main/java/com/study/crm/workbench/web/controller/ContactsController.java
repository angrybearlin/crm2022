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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class ContactsController {
    @Autowired
    private ContactsService contactsService;

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ContactsRemarkService contactsRemarkService;

    @Autowired
    private TranService tranService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsActivityRelationService contactsActivityRelationService;

    /**
     * 跳转到联系人页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        request.setAttribute("userList", userList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("appellationList", appellationList);
        return "workbench/contacts/index";
    }

    /**
     * 条件查询联系人
     * @param owner
     * @param fullname
     * @param customerName
     * @param source
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/contacts/queryContactByCondition.do")
    @ResponseBody
    public Object queryContactByCondition(String owner, String fullname, String customerName, String source, int pageNo,
                                          int pageSize) {
        Map<String, Object> params = new HashMap<>();
        int startIndex = (pageNo -1 ) * pageSize;
        params.put("owner", owner);
        params.put("fullname", fullname);
        params.put("customerName", customerName);
        params.put("source", source);
        params.put("startIndex", startIndex);
        params.put("pageSize", pageSize);
        List<Contacts> contactsList = contactsService.queryContactByConditionForPages(params);
        int totalRows = contactsService.queryCountOfContactByCondition(params);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("contactsList", contactsList);
        resultMap.put("totalRows", totalRows);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(resultMap);
        return retValue;
    }

    /**
     * 创建联系人
     * @param contacts
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/createContact.do")
    @ResponseBody
    public Object createContact(Contacts contacts, HttpSession session) {
        RetValue retValue = new RetValue();
        Map<String, Object> params = new HashMap<>();
        params.put("contacts", contacts);
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        params.put("user", user);
        try {
            int count = contactsService.insertContact(params);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("创建成功");
                retValue.setData(contacts);
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
     * 修改联系人
     * @param contacts
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/updateContact.do")
    @ResponseBody
    public Object updateContact(Contacts contacts, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        contacts.setEditBy(user.getId());
        contacts.setEditTime(DateUtil.formatDateTime(new Date()));
        Map<String, Object> params = new HashMap<>();
        params.put("contacts", contacts);
        params.put("user", user);
        RetValue retValue = new RetValue();
        try {
            int count = contactsService.updateContact(params);
            if (count == 1) {
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
     * 根据id查询联系人信息展示在更新页面
     * @param id
     * @return
     */
    @RequestMapping("/workbench/contacts/selectContactForUpdate.do")
    @ResponseBody
    public Object selectContactForUpdate(String id) {
        Contacts contacts = contactsService.queryContactById(id);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(contacts);
        return retValue;
    }

    /**
     * 跳转到联系人详情页
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/detail.do")
    public String detail(String id, HttpServletRequest request) {
        Contacts contacts = contactsService.queryContactByIdForDetail(id);
        List<ContactsRemark> contactsRemarkList = contactsRemarkService.queryContactRemarkByContactId(id);
        List<Tran> tranList = tranService.queryTranByContactId(id);
        List<Activity> activityList = activityService.queryActivityByContactId(id);
        request.setAttribute("contacts", contacts);
        request.setAttribute("contactsRemarkList", contactsRemarkList);
        request.setAttribute("tranList", tranList);
        request.setAttribute("activityList", activityList);
        return "workbench/contacts/detail";
    }

    /**
     * 新增联系人备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/createContactRemark.do")
    @ResponseBody
    public Object createContactRemark(ContactsRemark remark, HttpSession session) {
        RetValue retValue = new RetValue();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtil.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_HAS_NOT_EDIT);
        try {
            int count = contactsRemarkService.insertContactRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("创建成功");
                retValue.setData(remark);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }

    /**
     * 修改联系人备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/updateContactRemark.do")
    @ResponseBody
    public Object updateContactRemark(ContactsRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtil.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_HAS_EDITED);
        RetValue retValue = new RetValue();
        try {
            int count = contactsRemarkService.updateContactRemark(remark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("修改成功");
                retValue.setData(remark);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }

    /**
     * 删除联系人备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/contacts/deleteContactRemark.do")
    @ResponseBody
    public Object deleteContactRemark(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = contactsRemarkService.deleteContactRemarkById(id);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("删除成功");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }

    /**
     * 跳转到创建交易页面
     * @param contactName
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/toSaveTran.do")
    public String toSaveTran(String contactName, String contactId, HttpServletRequest request) {
        request.setAttribute("contactName", contactName);
        request.setAttribute("contactId", contactId);
        return "forward:/workbench/transaction/toSave.do";
    }

    /**
     * 批量给联系人绑定市场活动
     * @param activityIds
     * @param contactsId
     * @return
     */
    @RequestMapping("/workbench/contacts/bundContactsActivityRelation.do")
    @ResponseBody
    public Object bundContactsActivityRelation(String[] activityIds, String contactsId) {
        List<ContactsActivityRelation> relationList = new ArrayList<>();
        ContactsActivityRelation relation = null;
        for (String activityId: activityIds) {
            relation = new ContactsActivityRelation();
            relation.setActivityId(activityId);
            relation.setContactsId(contactsId);
            relation.setId(UUIDUtil.getUUID());
            relationList.add(relation);
        }
        RetValue retValue = new RetValue();
        try {
            int count = contactsActivityRelationService.saveContactsActivityRelationByList(relationList);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("绑定成功");
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityIds);
                retValue.setData(activityList);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        }
        return retValue;
    }

    /**
     * 根据名字查询市场活动，并筛选出没有和当前联系人绑定的市场活动
     * @param activityName
     * @param contactsId
     * @return
     */
    @RequestMapping("/workbench/contacts/selectActivityByNameExcludeContactsId.do")
    @ResponseBody
    public Object selectActivityByNameExcludeContactsId(String activityName, String contactsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("activityName", activityName);
        params.put("contactsId", contactsId);
        List<Activity> activityList = activityService.queryActivityByNameExcludeContactsId(params);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(activityList);
        return retValue;
    }

    /**
     * 解除绑定联系人市场活动关系
     * @param activityId
     * @return
     */
    @RequestMapping("/workbench/contacts/unbundContactsActivityRelation.do")
    @ResponseBody
    public Object unbundContactsActivityRelation(String activityId, String contactsId) {
        RetValue retValue = new RetValue();
        ContactsActivityRelation relation = new ContactsActivityRelation();
        relation.setActivityId(activityId);
        relation.setContactsId(contactsId);
        try {
            int count = contactsActivityRelationService.deleteContactsActivityRelationByActivityId(relation);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("解除绑定成功");
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
     * 根据id删除联系人
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/contacts/deleteContacts.do")
    @ResponseBody
    public Object deleteContacts(String[] ids) {
        RetValue retValue = new RetValue();
        try {
            contactsService.deleteContactsByIds(ids);
            retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retValue.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试。。。");
        }
        return retValue;
    }
}
