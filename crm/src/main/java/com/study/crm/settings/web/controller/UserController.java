package com.study.crm.settings.web.controller;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.domain.RetValue;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.settings.service.UserService;
import com.study.crm.settings.service.impl.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录页面login.jsp
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    /**
     * 根据前端传过来的账号密码，连接数据库判断是否能登录成功
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/settings/qx/user/login.do")
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        // 构造返回结果包装对象
        RetValue retValue = new RetValue();
        // 获取从数据库中查询到的数据
        User user = userService.queryUserByActAndPwd(map);
        // 先进行异常判断，先将code码置为失败
        retValue.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
        // 如果数据为空，用户名或密码错误
        if (user == null) {
            retValue.setMsg("用户名或密码错误");
        } else {
            // 当前时间大于用户到期时间，用户已到期
            String nowTime = DateUtil.formatDateTime(new Date());
            if (nowTime.compareTo(user.getExpireTime()) > 0) {
                retValue.setMsg("用户已到期");
            } else if ("0".equals(user.getLockState())) {
                // LockState为0 ，表示用户已被锁定
                retValue.setMsg("用户已被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                // 用户目前IP不在AllowIps内，则用户访问IP受限
                retValue.setMsg("用户访问IP受限");
            } else {
                // 以上条件都满足，用户登录成功，code码置为成功
                retValue.setCode("0");
                retValue.setMsg("OK");
                httpSession.setAttribute(Contants.SESSION_USER, user);
                Cookie c1 = null;
                Cookie c2 = null;
                // 如果isRemPwd为true，记住账号密码，写进cookie
                if ("true".equals(isRemPwd)) {
                    c1 = new Cookie("loginAct", loginAct);
                    c1.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c1);
                    c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c2);
                } else if ("false".equals(isRemPwd)){
                    c1 = new Cookie("loginAct", "");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    c2 = new Cookie("loginPwd", "");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return retValue;
    }

    /**
     * 安全退出功能
     * @param response
     * @param httpSession
     * @return
     */
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession httpSession) {
        // 清空cookie
        Cookie c1 = new Cookie("loginAct", "");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "");
        c2.setMaxAge(0);
        response.addCookie(c2);
        // 删除session
        httpSession.invalidate();
        return "redirect:/";
    }
}
