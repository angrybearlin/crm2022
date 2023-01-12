package com.study.crm.workbench.service.impl;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.workbench.domain.Customer;
import com.study.crm.workbench.domain.FunnelVO;
import com.study.crm.workbench.domain.Tran;
import com.study.crm.workbench.domain.TranHistory;
import com.study.crm.workbench.mapper.CustomerMapper;
import com.study.crm.workbench.mapper.TranHistoryMapper;
import com.study.crm.workbench.mapper.TranMapper;
import com.study.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerName");
        User user = (User) map.get(Contants.SESSION_USER);
        Customer customer = customerMapper.selectCustomerByName(customerName);

        // 如果按名字没有查询到客户信息，新建一个客户
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtil.formatDateTime(new Date()));
            customerMapper.insert(customer);
        }

        // 创建交易
        Tran tran = new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtil.getUUID());
        tran.setOwner((String) map.get("owner"));
        tran.setName((String) map.get("name"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setContactsId((String) map.get("contactsId"));
        tran.setCreateTime(DateUtil.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        tranMapper.insert(tran);

        // 增加交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateTime(DateUtil.formatDateTime(new Date()));
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public List<Tran> queryTranByConditionForPages(Map<String, Object> map) {
        List tranList = tranMapper.selectTranByConditionForPages(map);
        System.out.println("------------------------------------------");
        System.out.println(tranList);
        for (int i=0; i<tranList.size(); i++) {
            if (((Map)tranList.get(i)).get("source") == null) {
                ((Map)tranList.get(i)).put("source", "无");
            }
            if (((Map)tranList.get(i)).get("type") == null) {
                ((Map)tranList.get(i)).put("type", "无");
            }
        }
        return tranList;
    }

    @Override
    public int queryCountOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTranByCondition(map);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
