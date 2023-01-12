package com.study.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * 关于excel文件操作的工具类
 */
public class HSSFUtil {
    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell) {
        String ret = "";
        if (cell.getCellType() == CellType.STRING) {
            ret = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            ret = cell.getNumericCellValue() + "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            ret = cell.getBooleanCellValue() + "";
        } else if (cell.getCellType() == CellType.FORMULA) {
            ret = cell.getCellFormula();
        } else {
            ret = "";
        }
        return ret;
    }
}
