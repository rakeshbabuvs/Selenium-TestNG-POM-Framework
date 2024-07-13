package com.qa.opencart.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelUtil {

    private static final String TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/OpenCartTestData.xlsx";
    private static Workbook book;
    private static Sheet sheet;

    public static Object[][] getTestData(String sheetName) {
        Object data[][] = null;

        try {
            FileInputStream ip = new FileInputStream(TEST_DATA_SHEET_PATH);
            book = WorkbookFactory.create(ip);
            sheet = book.getSheet(sheetName.trim());

            data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

            for(int i=0; i<sheet.getLastRowNum(); i++) {
                for(int j=0; j<sheet.getRow(0).getLastCellNum(); j++) {
                    data[i][j] = sheet.getRow(i+1).getCell(j).toString();
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException | org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }

    public static void attachTestData() {
        File file = new File(TEST_DATA_SHEET_PATH);
        try (FileInputStream fis = new FileInputStream(file)) {
            Allure.addAttachment("Test Data", fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
