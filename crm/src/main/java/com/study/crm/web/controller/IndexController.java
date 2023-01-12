package com.study.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    /**
     * 访问http://127.0.0.1:8080/crm/跳转到网站首页index.jsp页面
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
