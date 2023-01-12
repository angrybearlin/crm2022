package com.study.crm.workbench.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.HSSFUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.settings.service.UserService;
import com.study.crm.workbench.domain.Activity;
import com.study.crm.workbench.domain.ActivityRemark;
import com.study.crm.workbench.service.ActivityRemarkService;
import com.study.crm.workbench.service.ActivityService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     * 跳转到市场活动页面/workbench/activity/index.jsp
     * @param request
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    /**
     * 保存创建的市场活动
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        RetValue retValue = new RetValue();
        // 为新增的市场活动设置id
        activity.setId(UUIDUtil.getUUID());
        // 设置创建时间
        activity.setCreateTime(DateUtil.formatDateTime(new Date()));
        // 设置创建人，取当前session登录用户的id
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        try {
            int count = activityService.saveCreateActivity(activity);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("新增成功");
            } else {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                retValue.setMsg("系统繁忙，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统繁忙，请稍后再试");
        }
        return retValue;
    }

    /**
     * 条件查询市场活动并分页
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("owner", owner);
        paramsMap.put("startDate", startDate);
        paramsMap.put("endDate", endDate);
        paramsMap.put("beginNo", (pageNo - 1) * pageSize);
        paramsMap.put("pageSize", pageSize);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(paramsMap);
        int totalRows = activityService.queryCountOfActivityByCondition(paramsMap);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("activityList", activityList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    /**
     * 删除市场活动
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivities.do")
    @ResponseBody
    public Object deleteActivities(String[] ids) {
        RetValue retValue = new RetValue();
        try {
            int count = activityService.deleteActivityByIds(ids);
            if (count > 0) {
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
     * 根据id查询市场活动信息
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        RetValue retValue = new RetValue();
        activity.setEditTime(DateUtil.formatDateTime(new Date()));
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        try {
            int count = activityService.saveEditActivity(activity);
            if (count > 0) {
                retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retValue.setMsg("OK");
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

    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception{
        // 设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 获取输出流
        ServletOutputStream out = response.getOutputStream();

        // 浏览器接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息；即使打不开，也会调用应用程序打开，只有实在打不开，才会激活文件下载
        // 可以设置响应头信息，使浏览器接收到响应信息后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");
        // 读取excel文件(inputStream)，输出到浏览器（outputStream）
        InputStream is = new FileInputStream("/Users/linkexuan/Downloads/studentList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        // 关闭资源
        is.close();
        out.flush();
    }

    /**
     * 导出所有市场活动
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws Exception{
        List<Activity> activityList = activityService.selectAllActivities();
        response.setContentType("application/octet-stream;charset=UTF-8");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改人");

        if (activityList != null && activityList.size() >0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        /*OutputStream os = new FileOutputStream("/crm-project/crm/src/docs/activityList.xls");

        wb.write(os);
        os.flush();
        os.close();
        wb.close();*/

        // 获取输出流
        ServletOutputStream out = response.getOutputStream();
        // 设置响应头信息
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");

        /*InputStream is = new FileInputStream("/crm-project/crm/src/docs/activityList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        is.close();*/
        wb.write(out);

        out.flush();
        wb.close();
    }

    /**
     * 根据id选择性导出部分市场活动
     * @return
     */
    @RequestMapping("/workbench/activity/exportActivitiesByIds.do")
    public void exportActivitiesByIds(String[] ids, HttpServletResponse response) throws Exception{
        List<Activity> activityList = activityService.selectActivityByIds(ids);
        response.setContentType("application/octet-stream;charset=UTF-8");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改人");

        if (activityList != null && activityList.size() >0) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        // 获取输出流
        ServletOutputStream out = response.getOutputStream();
        // 设置响应头信息
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");

        wb.write(out);

        out.flush();
        wb.close();
    }

    /**
     * 配置springmvc的文件上传解析器
     * @param username
     * @param myFile
     * @return
     *//*
    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(String username, MultipartFile myFile) throws Exception{
        // 把文本数据打印到控制台
        System.out.println("username=" + username);
        // 把文件在服务器指定的目录中生成一个同样的文件
        String originalFilename = myFile.getOriginalFilename();
        File file = new File("/crm-project/crm/src/docs/", originalFilename);
        myFile.transferTo(file);

        // 返回响应信息
        RetValue retValue = new RetValue();
        retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        retValue.setMsg("OK");
        return retValue;
    }*/

    /**
     * 导入市场活动
     * @param activityFile
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        RetValue retValue = new RetValue();
        InputStream is = null;
        HSSFWorkbook wb = null;
        try {
            /*String originalFilename = activityFile.getOriginalFilename();
            File file = new File("/crm-project/crm/src/docs/", originalFilename);
            activityFile.transferTo(file);*/
            // is = new FileInputStream(file);
            is = activityFile.getInputStream();
            wb = new HSSFWorkbook(is);
            // 根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = wb.getSheetAt(0); // 页的下标，从0开始，依次增加
            HSSFRow row = null;
            List<Activity> activityList = new ArrayList<>();
            // 根据sheet获取HSSFRow对象，封装了一行的所有信息
            for (int i=1; i<=sheet.getLastRowNum(); i++) { // sheet.getLastRowNum()最后一行的下标
                row = sheet.getRow(i); // 行的下标，从0开始，依次增加
                Activity activity = new Activity();
                activity.setId(UUIDUtil.getUUID());
                User user = (User) session.getAttribute(Contants.SESSION_USER);
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtil.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j=0; j<row.getLastCellNum(); j++) { // row.getLastCellNum()最后一列的下标+1
                    // 根据row获取HSSFCell对象，封装了一列的所有信息
                    if (j == 0) {
                        activity.setName(HSSFUtil.getCellValueForStr(row.getCell(j)));
                    } else if (j == 1) {
                        activity.setStartDate(HSSFUtil.getCellValueForStr(row.getCell(j)));
                    } else if (j == 2) {
                        activity.setEndDate(HSSFUtil.getCellValueForStr(row.getCell(j)));
                    } else if (j == 3) {
                        activity.setCost(HSSFUtil.getCellValueForStr(row.getCell(j)));
                    } else if (j == 4) {
                        activity.setDescription(HSSFUtil.getCellValueForStr(row.getCell(j)));
                    }
                }
                // 每一行中数据获取完之后，将这个activity对象封装到List集合中
                activityList.add(activity);
            }
            int count = activityService.insertActivitiesByList(activityList);
            retValue.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            retValue.setMsg("OK");
            retValue.setData("成功插入" + count + "条数据");
        } catch (Exception e) {
            e.printStackTrace();
            retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            retValue.setMsg("系统忙，请稍后重试。。。");
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retValue;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request) {
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        request.setAttribute("activity", activity);
        request.setAttribute("activityRemarks", activityRemarks);
        return "workbench/activity/detail";
    }
}
