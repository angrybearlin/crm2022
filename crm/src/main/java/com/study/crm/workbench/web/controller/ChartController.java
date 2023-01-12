package com.study.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChartController {

    /**
     * 跳转到交易统计图表页面
     * @return
     */
    @RequestMapping("/workbench/chart/transactionIndex.do")
    public String transactionIndex() {
        return "workbench/chart/transaction/index";
    }
}
