package com.study.test.docs;

import com.study.crm.commons.utils.HSSFUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;

public class ParseExcelTest {
    public static void main(String[] args) throws Exception{
        // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
        FileInputStream is = new FileInputStream("/Users/linkexuan/IdeaProjects/crm-project/crm/src/docs/studentList.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        // 根据wb获取HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet = wb.getSheetAt(0); // 页的下标，从0开始，依次增加
        HSSFRow row = null;
        HSSFCell cell = null;
        // 根据sheet获取HSSFRow对象，封装了一行的所有信息
        for (int i=0; i<=sheet.getLastRowNum(); i++) { // sheet.getLastRowNum()最后一行的下标
            row = sheet.getRow(i); // 行的下标，从0开始，依次增加
            for (int j=0; j<row.getLastCellNum(); j++) { // row.getLastCellNum()最后一列的下标+1
                // 根据row获取HSSFCell对象，封装了一列的所有信息
                cell = row.getCell(j); // 列的下标，从0开始，依次增加
                // 获取列中数值类型
                System.out.print(HSSFUtil.getCellValueForStr(cell) + " ");
            }
            // 每一行中所有列都打完，打印一个换行
            System.out.print("\n");
        }
        is.close();
    }


}
