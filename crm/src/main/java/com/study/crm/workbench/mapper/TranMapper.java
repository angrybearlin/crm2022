package com.study.crm.workbench.mapper;

import com.study.crm.workbench.domain.FunnelVO;
import com.study.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 12:04:13 CST 2023
     */
    int updateByPrimaryKey(Tran record);

    /**
     * 线索转换时新增一条交易记录
     * @param tran
     * @return
     */
    int insertTranOnClueConvert(Tran tran);

    /**
     * 条件查询交易并分页
     * @param map
     * @return
     */
    List<Tran> selectTranByConditionForPages(Map<String, Object> map);

    /**
     * 查询分页查询后的记录条数
     * @param map
     * @return
     */
    int selectCountOfTranByCondition(Map<String, Object> map);

    /**
     * 通过id查询交易明细
     * @param id
     * @return
     */
    Tran selectTranForDetailById(String id);

    /**
     * 查询处在各个阶段中交易的数量
     * @return
     */
    List<FunnelVO> selectCountOfTranGroupByStage();

    /**
     * 根据customerId查询
     * @param customerId
     * @return
     */
    List<Tran> selectTranByCustomerId(String customerId);

    /**
     * 根据id删除交易
     * @param id
     * @return
     */
    int deleteTranById(String id);

    /**
     * 根据customerId删除该客户的所有交易
     * @param customerId
     * @return
     */
    int deleteTranByCustomerId(String customerId);

    /**
     * 根据contactId查询该联系人下的所有交易
     * @param contactId
     * @return
     */
    List<Tran> selectTranByContactId(String contactId);

    /**
     * 根据contactsId删除该联系人下所有交易
     * @param contactsId
     * @return
     */
    int deleteTranByContactsId(String contactsId);

    /**
     * 根据id查询交易信息展示在修改页面
     * @param id
     * @return
     */
    Map<String, Object> selectTranForUpdateById(String id);

    /**
     * 根据id修改交易
     * @param tran
     * @return
     */
    int updateTranById(Tran tran);

    /**
     * 查询某一交易的阶段id
     * @param id
     * @return
     */
    String selectStageIdOfTran(String id);
}
