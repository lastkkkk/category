package com.example.category.utils;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by happy on 2019/06/05
 * In order to transform excel to sql/hsql,
 * otherwise, as same with sql/hsql to excel.
 */
public class ExcelSQLTransform {

    /**
     *
     * @param filePath
     * @param storePath
     * @return
     * @throws Exception
     */
    public static List<Map> readExcel(String filePath,String storePath) throws Exception{
        File file=new File(filePath);
        System.out.println(file);
        if (!file.exists()) {
            System.out.println("no1");
            return null;
        }

        /**
         * Get the suffix of the file
         */
        String suffix= filePath.substring(filePath.lastIndexOf("."));

        /**
         * if suffix cannot equals .xls && .xlsx ,return null
         */
        if(!".xls".equals(suffix) && !".xlsx".equals(suffix)){
            System.out.println("no");
            return null;
        }

        //返回值列
        List<Map> reaultList=new ArrayList<Map>();
        if(".xls".equals(suffix)){
            reaultList=readExcel2007(filePath,storePath);
        }else if(".xlsx".equals(suffix)){
            reaultList=readExcel2007(filePath,storePath);
        }
        System.out.println("yes1");
        return reaultList;
    }

    /**
     *
     * @param filePath
     * @param storePath
     * @return
     * @throws IOException
     */
    public static List<Map> readExcel2007(String filePath,String storePath) throws IOException{
        List<Map> valueList=new ArrayList<Map>();
        FileInputStream fis =null;
        try {
            fis =new FileInputStream(filePath);
            XSSFWorkbook xwb = new XSSFWorkbook(fis);       // 构造 XSSFWorkbook 对象，strPath 传入文件路径

            int sheetNum = xwb.getNumberOfSheets();
            /**
             *  foreach to read sheet.
             */
            XSSFSheet sheet;
            XSSFRow row;
            String sql;

            for (int s=0;s<=sheetNum-1;s++){
                sheet = xwb.getSheetAt(s);
                String table_name           = sheet.getSheetName();
                int totalRow                = sheet.getLastRowNum();
                sql = "CREATE TABLE IF NOT EXISTS "+table_name+"(";
                for (int i=1;i<=totalRow;i++){
                    row      = sheet.getRow(i);
                    if (i==totalRow){
                        String a = row.getCell(1)+" "+row.getCell(2)+");";
                        sql = sql+a;
                    }else {
                        String a  = row.getCell(1)+" "+row.getCell(2)+",";
                        sql = sql+a;
                    }
                }
                new SaveDataToFile().appendFile(storePath,sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fis.close();
        }
        return valueList;
    }

    /**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String readurl  = "E:\\2020学习\\实验室\\质监局\\映射表\\list1.xlsx";  //读取excel文件的路径
        String storeurl = "E:\\2020学习\\实验室\\质监局\\映射表\\list1.sql";//保存sql命令的路径
        System.out.println("yes");
        readExcel(readurl,storeurl);
    }

}
