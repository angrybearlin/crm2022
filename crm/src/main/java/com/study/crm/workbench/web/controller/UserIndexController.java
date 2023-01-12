package com.study.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserIndexController {

    /**
     * 跳转到/workbench下的index.jsp
     * @return
     */
    @RequestMapping("/workbench/index.do")
    public String index() {
        return "workbench/index";
    }
}
