package com.study.crm.workbench.mapper;

import com.study.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    int insert(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    int insertSelective(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    ContactsActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    int updateByPrimaryKeySelective(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Mon Jan 09 11:45:17 CST 2023
     */
    int updateByPrimaryKey(ContactsActivityRelation record);

    /**
     * 通过列表新增联系人市场活动关联关系
     * @param conActRelationList
     * @return
     */
    int insertConActRelationByList(List<ContactsActivityRelation> conActRelationList);

    /**
     * 通过activityId删除联系人市场活动备注
     * @param relation
     * @return
     */
    int deleteConActRelationByActivityId(ContactsActivityRelation relation);

    /**
     * 根据contactsId删除和该联系人所有关联市场活动的关联关系
     * @param contactsId
     * @return
     */
    int deleteConActRelationByContactsId(String contactsId);
}
