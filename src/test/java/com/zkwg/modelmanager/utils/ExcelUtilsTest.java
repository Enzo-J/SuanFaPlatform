package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.entity.ExcelUser;
import com.zkwg.modelmanager.entity.PurSelSto;
import com.zkwg.modelmanager.entity.TaxpayerAddress;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelUtilsTest {

    public static void main(String[] args) {
        File file = new File("D:\\Excel (2).xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            List<ExcelHead> excelHeads = new ArrayList<ExcelHead>();
            ExcelHead excelHead = new ExcelHead("姓名", "name");
            ExcelHead excelHead1 = new ExcelHead("性别", "sex");
            ExcelHead excelHead2 = new ExcelHead("年龄", "age");
            ExcelHead excelHead3 = new ExcelHead("地址", "address", true);
            excelHeads.add(excelHead);
            excelHeads.add(excelHead1);
            excelHeads.add(excelHead2);
            excelHeads.add(excelHead3);
            List<ExcelUser> list = ExcelUtils.readExcelToEntity(ExcelUser.class, in, file.getName(), excelHeads);
            for (ExcelUser excelUser : list) {
                System.out.println(excelUser.getName() + ":" + excelUser.getSex() + ":" + excelUser.getAge() + ":" + excelUser.getAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in!=null) {
                try {
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    void readExcelToEntity() {
        File file = new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\excel\\地址解析算法数据导入导出模板.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            ExcelHead excelHead = new ExcelHead("纳税人识别号", "taxpayerCode");
            ExcelHead excelHead1 = new ExcelHead("企业注册地址", "address");

            List<ExcelHead> excelHeads = new ArrayList<ExcelHead>();
            excelHeads.add(excelHead);
            excelHeads.add(excelHead1);

            List<TaxpayerAddress> list = ExcelUtils.readExcelToEntity(TaxpayerAddress.class, in, file.getName(), excelHeads);

            for (TaxpayerAddress taxpayerAddress : list) {
                System.out.println(taxpayerAddress.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in!=null) {
                try {
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    void testReadExcelToEntity() {
        File file = new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\excel\\进销匹配算法数据导入导出模板.xlsx");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            ExcelHead excelHead = new ExcelHead("纳税人识别号", "taxpayerCode");
            ExcelHead excelHead1 = new ExcelHead("进项货物品名", "purItemName");
            ExcelHead excelHead2 = new ExcelHead("进项货物金额", "purItemMoney");
            ExcelHead excelHead3 = new ExcelHead("销项货物品名", "sellItemName", true);
            ExcelHead excelHead4 = new ExcelHead("销项货物金额", "sellItemMoney", true);

            List<ExcelHead> excelHeads = new ArrayList<ExcelHead>();
            excelHeads.add(excelHead);
            excelHeads.add(excelHead1);
            excelHeads.add(excelHead2);
            excelHeads.add(excelHead3);
            excelHeads.add(excelHead4);

            List<PurSelSto> list = ExcelUtils.readExcelToEntity(PurSelSto.class, in, file.getName(), excelHeads);

            for (PurSelSto purSelSto : list) {
                System.out.println(purSelSto.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in!=null) {
                try {
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}