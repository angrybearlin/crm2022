package com.study.crm.workbench.mapper;

import com.study.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jan 04 15:10:55 CST 2023
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 新增一条线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 条件查询线索并分页
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map<String, Object> map);

    /**
     * 统计条件查询后线索的记录条数
     * @param map
     * @return
     */
    int selectCountOfClueByCondition(Map<String, Object> map);

    /**
     * 根据线索id查询线索
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    /**
     * 查询要转换的线索的信息
     * @param id
     * @return
     */
    Clue selectClueForConvertById(String id);

    /**
     * 修改线索
     * @param clue
     * @return
     */
    int updateClue(Clue clue);
}
