package com.fuzamei.bonuspoint.util;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import com.xuxueli.poi.excel.util.FieldReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: data-output
 * @description:
 * @author: WangJie
 * @create: 2018-10-10 17:36
 **/
@Slf4j
public class ExcelUtil {

    /**
     * 导出Excel对象
     *
     * @param dataList  Excel数据
     * @return
     */
    public static Workbook exportWorkbook(Workbook workbook ,List dataList,String sheetName){

        // data array valid
        if (dataList==null || dataList.size()==0) {
            throw new RuntimeException(">>>>>>>>>>> excel export error, data array can not be empty.");
        }

        // sheet
        Class<?> sheetClass = dataList.get(0).getClass();
        ExcelSheet excelSheet = sheetClass.getAnnotation(ExcelSheet.class);

        HSSFColor.HSSFColorPredefined headColor = null;
        if (excelSheet != null) {
            if (StringUtils.isEmpty(sheetName)){
                sheetName = dataList.get(0).getClass().getSimpleName();
                if (excelSheet.name()!=null && excelSheet.name().trim().length()>0) {
                    sheetName = excelSheet.name().trim();
                }
            }
            headColor = excelSheet.headColor();
        }

        Sheet existSheet = workbook.getSheet(sheetName);
        if (existSheet != null) {
            for (int i = 2; i <= 1000; i++) {
                String newSheetName = sheetName.concat(String.valueOf(i));
                existSheet = workbook.getSheet(newSheetName);
                if (existSheet == null) {
                    sheetName = newSheetName;
                    break;
                } else {
                    continue;
                }
            }
        }

        Sheet sheet = workbook.createSheet(sheetName);

        // sheet field
        List<Field> fields = new ArrayList<Field>();
        if (sheetClass.getDeclaredFields()!=null && sheetClass.getDeclaredFields().length>0) {
            for (Field field: sheetClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
        }

        if (fields==null || fields.size()==0) {
            throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data field can not be empty.");
        }

        // sheet header row
        CellStyle headStyle = null;
        if (headColor != null) {
            headStyle = workbook.createCellStyle();

            headStyle.setFillForegroundColor(headColor.getIndex());
            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headStyle.setFillBackgroundColor(headColor.getIndex());
        }

        Row headRow = sheet.createRow(0);
        boolean ifSetWidth = false;
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String fieldName = (excelField!=null && excelField.name()!=null && excelField.name().trim().length()>0)?excelField.name():field.getName();
            int fieldWidth = (excelField!=null)?excelField.width():0;

            Cell cellX = headRow.createCell(i, CellType.STRING);
            if (headStyle != null) {
                cellX.setCellStyle(headStyle);
            }
            if (fieldWidth > 0) {
                sheet.setColumnWidth(i, fieldWidth);
                ifSetWidth = true;
            }
            cellX.setCellValue(String.valueOf(fieldName));
        }

        // sheet data rows
        for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
            int rowIndex = dataIndex+1;
            Object rowData = dataList.get(dataIndex);

            Row rowX = sheet.createRow(rowIndex);

            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(rowData);

                    String fieldValueString = FieldReflectionUtil.formatValue(field, fieldValue);

                    Cell cellX = rowX.createCell(i, CellType.STRING);
                    cellX.setCellValue(fieldValueString);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (!ifSetWidth) {
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn((short)i);
            }
        }

        return workbook;
    }
    /**
     * 导出Excel文件到磁盘
     *
     * @param filePath
     * @param workbook
     */
    public static void exportToFile(String filePath, Workbook workbook){


        FileOutputStream fileOutputStream = null;
        try {

            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);

            fileOutputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileOutputStream!=null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 导出Excel字节数据
     *
     * @param workbook
     * @return
     */
    public static byte[] exportToBytes(Workbook workbook){

        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] result = null;
        try {
            // workbook 2 ByteArrayOutputStream
            byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            // flush
            byteArrayOutputStream.flush();

            result = byteArrayOutputStream.toByteArray();
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

}

