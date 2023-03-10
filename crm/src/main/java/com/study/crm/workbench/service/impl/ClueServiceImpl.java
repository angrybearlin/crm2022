package com.study.crm.workbench.service.impl;

import com.study.crm.commons.contants.Contants;
import com.study.crm.commons.utils.DateUtil;
import com.study.crm.commons.utils.UUIDUtil;
import com.study.crm.settings.domain.User;
import com.study.crm.workbench.domain.*;
import com.study.crm.workbench.mapper.*;
import com.study.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveClueConvert(Map<String, Object> map) {
        User user = (User) map.get(Contants.SESSION_USER);
        String clueId = (String) map.get("clueId");
        // ??????????????????????????????????????????????????????
        Clue clue = clueMapper.selectClueForConvertById(clueId);
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setId(UUIDUtil.getUUID());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtil.formatDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setName(clue.getCompany());
        customer.setOwner(clue.getOwner());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setNextContactTime(clue.getNextContactTime());
        customerMapper.insertCustomerByClue(customer);

        // ?????????????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtil.formatDateTime(new Date()));
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContactByClue(contacts);

        List<ClueRemark> remarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if (remarkList != null && remarkList.size() > 0) {
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            CustomerRemark customerRemark = null;
            ContactsRemark contactsRemark = null;
            for(ClueRemark remark: remarkList) {
                // ??????????????????????????????????????????????????????????????????
                customerRemark = new CustomerRemark();
                customerRemark.setCreateBy(remark.getCreateBy());
                customerRemark.setCreateTime(remark.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(remark.getEditBy());
                customerRemark.setEditFlag(remark.getEditFlag());
                customerRemark.setEditTime(remark.getEditTime());
                customerRemark.setNoteContent(remark.getNoteContent());
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemarkList.add(customerRemark);

                // ?????????????????????????????????????????????????????????????????????
                contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(remark.getCreateBy());
                contactsRemark.setCreateTime(remark.getCreateTime());
                contactsRemark.setEditBy(remark.getEditBy());
                contactsRemark.setEditTime(remark.getEditTime());
                contactsRemark.setNoteContent(remark.getNoteContent());
                contactsRemark.setEditFlag(remark.getEditFlag());
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemarkList.add(contactsRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByClueRemarkList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByClueRemarkList(contactsRemarkList);

            // ????????????????????????????????????????????????????????????????????????????????????????????????
            List<ClueActivityRelation> clueActRelationList = clueActivityRelationMapper.selectClueActRelationListByClueId(clueId);
            if (clueActRelationList != null && clueActRelationList.size() > 0) {
                List<ContactsActivityRelation> conActRelationList = new ArrayList<>();
                ContactsActivityRelation conActRelation = null;
                for(ClueActivityRelation clueActRelation: clueActRelationList) {
                    conActRelation = new ContactsActivityRelation();
                    conActRelation.setId(UUIDUtil.getUUID());
                    conActRelation.setActivityId(clueActRelation.getActivityId());
                    conActRelation.setContactsId(contacts.getId());
                    conActRelationList.add(conActRelation);
                }
                contactsActivityRelationMapper.insertConActRelationByList(conActRelationList);
            }

            if ("true".equals((String) map.get("isCreateTran"))) {
                // ???????????????????????????????????????????????????????????????
                Tran tran = new Tran();
                tran.setContactsId(contacts.getId());
                tran.setCreateBy(user.getId());
                tran.setCreateTime(DateUtil.formatDateTime(new Date()));
                tran.setCustomerId(customer.getId());
                tran.setExpectedDate((String) map.get("expectedDate"));
                tran.setId(UUIDUtil.getUUID());
                tran.setMoney((String) map.get("money"));
                tran.setName((String) map.get("name"));
                tran.setActivityId((String) map.get("activityId"));
                tran.setStage((String) map.get("stage"));
                tran.setOwner(user.getId());
                tranMapper.insertTranOnClueConvert(tran);

                // ????????????????????????????????????????????????????????????????????????????????????????????????
                if (remarkList != null && remarkList.size() > 0) {
                    List<TranRemark> tranRemarkList = new ArrayList<>();
                    TranRemark tranRemark = null;
                    for (ClueRemark clueRemark: remarkList) {
                        tranRemark = new TranRemark();
                        tranRemark.setCreateBy(clueRemark.getCreateBy());
                        tranRemark.setCreateTime(clueRemark.getCreateTime());
                        tranRemark.setTranId(tran.getId());
                        tranRemark.setEditBy(clueRemark.getEditBy());
                        tranRemark.setEditTime(clueRemark.getEditTime());
                        tranRemark.setEditFlag(clueRemark.getEditFlag());
                        tranRemark.setNoteContent(clueRemark.getNoteContent());
                        tranRemark.setId(UUIDUtil.getUUID());
                        tranRemarkList.add(tranRemark);
                    }
                    tranRemarkMapper.insertTranRemarkByClueRemark(tranRemarkList);
                }
            }
        }
        // ??????????????????????????????
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        // ?????????????????????????????????????????????
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        // ???????????????
        clueMapper.deleteByPrimaryKey(clueId);
    }

    @Override
    public int updateClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public Clue queryClueDetailById(String id) {
        return clueMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteClueByIds(String[] ids) {
        clueMapper.deleteClueByIds(ids);
        // ????????????????????????????????????????????????????????????
        for (String id: ids) {
            clueActivityRelationMapper.deleteClueActivityRelationByClueId(id);
            clueRemarkMapper.deleteClueRemarkByClueId(id);
        }
    }
}
