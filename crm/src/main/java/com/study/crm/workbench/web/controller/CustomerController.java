package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.settings.service.UserService;
import com.study.crm.workbench.domain.Contacts;
import com.study.crm.workbench.domain.Customer;
import com.study.crm.workbench.domain.CustomerRemark;
import com.study.crm.workbench.domain.Tran;
import com.study.crm.workbench.service.ContactsService;
import com.study.crm.workbench.service.CustomerRemarkService;
import com.study.crm.workbench.service.CustomerService;
import com.study.crm.workbench.service.TranService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRemarkService customerRemarkService;

    @Autowired
    private TranService tranService;

    @Autowired
    private ContactsService contactsService;

    /**
     * 跳转到客户页面
     * @return
     */
    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);
        return "workbench/customer/index";
    }

    /**
     * 条件查询客户并分页
     * @param name
     * @param owner
     * @param phone
     * @param website
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerByConditionForPages.do")
    @ResponseBody
    public Object selectCustomerByConditionForPages(String name, String owner, String phone, String website, int pageNo,
                                                    int pageSize) {
        Map<String, Object> map = new HashMap<>();
        int startIndex = (pageNo -  1) * pageSize;
        map.put("startIndex", startIndex);
        map.put("name", name);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("website", website);
        map.put("pageSize", pageSize);
        List<Customer> customerList = customerService.queryCustomerByConditionForPages(map);
        int totalRows = customerService.queryCountOfCustomerByCondition(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("customerList", customerList);
        resultMap.put("totalRows", totalRows);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(resultMap);
        return retValue;
    }

    /**
     * 创建客户
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomer.do")
    @ResponseBody
    public Object insertCustomer(Customer customer, HttpSession session) {
        customer.setId(UUIDUtil.getUUID());
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtil.formatDateTime(new Date()));
        RetValue retValue = new RetValue();
        try {
            int count = customerService.insertCustomer(customer);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("创建成功");
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
     * 修改客户
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/updateCustomer.do")
    @ResponseBody
    public Object updateCustomer(Customer customer, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtil.formatDateTime(new Date()));
        RetValue retValue = new RetValue();
        try {
            int count = customerService.updateCustomer(customer);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("修改成功");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试");
        }
        return retValue;
    }

    /**
     * 查询出客户信息并展示在修改页面上
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerForUpdate.do")
    @ResponseBody
    public Object selectCustomerForUpdate(String id) {
        Customer customer = customerService.queryCustomerForUpdate(id);
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        retValue.setData(customer);
        return retValue;
    }

    /**
     * 通过id删除客户
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomer.do")
    @ResponseBody
    public Object deleteCustomer(String[] ids) {
        RetValue retValue = new RetValue();
        try {
            int count = customerService.deleteCustomerById(ids);
            if (count > 0) {
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
     * 跳转到客户详情页
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/customer/detail.do")
    public String detail(String id, HttpServletRequest request) {
        Customer customer = customerService.queryCustomerForDetailById(id);
        List<CustomerRemark> customerRemarkList = customerRemarkService.queryCustomerRemarkByCustomerId(id);
        List<Tran> tranList = tranService.queryTranByCustomerId(id);
        List<Contacts> contactsList = contactsService.queryContactsByCustomerId(id);
        request.setAttribute("customer", customer);
        request.setAttribute("customerRemarkList", customerRemarkList);
        request.setAttribute("tranList", tranList);
        request.setAttribute("contactsList", contactsList);
        return "workbench/customer/detail";
    }

    /**
     * 新增客户备注
     * @param customerRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomerRemark.do")
    @ResponseBody
    public Object insertCustomerRemark(CustomerRemark customerRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customerRemark.setId(UUIDUtil.getUUID());
        customerRemark.setCreateBy(user.getId());
        customerRemark.setCreateTime(DateUtil.formatDateTime(new Date()));
        customerRemark.setEditFlag(Contants.ACTIVITY_REMARK_HAS_NOT_EDIT);
        RetValue retValue = new RetValue();
        try {
            int count = customerRemarkService.insertCustomerRemark(customerRemark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("创建成功");
                retValue.setData(customerRemark);
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
     * 修改客户备注
     * @param customerRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/updateCustomerRemark.do")
    @ResponseBody
    public Object updateCustomerRemark(CustomerRemark customerRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customerRemark.setEditBy(user.getId());
        customerRemark.setEditTime(DateUtil.formatDateTime(new Date()));
        customerRemark.setEditFlag(Contants.ACTIVITY_REMARK_HAS_EDITED);
        RetValue retValue = new RetValue();
        try {
            int count = customerRemarkService.updateCustomerRemark(customerRemark);
            if (count == 1) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("修改成功");
                retValue.setData(customerRemark);
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统忙，请稍后再试。。。");
            }
        }catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后再试。。。");
        }
        return retValue;
    }

    /**
     * 删除客户备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomerRemark.do")
    @ResponseBody
    public Object deleteCustomerRemark(String id) {
        RetValue retValue = new RetValue();
        try {
            int count = customerRemarkService.deleteCustomerRemarkById(id);
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
        return  retValue;
    }
}
