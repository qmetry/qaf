/*******************************************************************************
 * Copyright (c) 2021 Infostretch Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.util;

import com.qmetry.qaf.automation.testng.DataProviderException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class PoiExcelUtil {
    private static int getFirstRow(Sheet s, boolean skipHeaderRow) {
        int row = s.getFirstRowNum();
        for (; row < s.getLastRowNum(); row++) {
            Row r = s.getRow(row);
            boolean isEmptyRow = true;
            if (r != null) {
                short minColIx = r.getFirstCellNum();
                short maxColIx = r.getLastCellNum();
                for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                    Cell cell = r.getCell(colIx);
                    if (StringUtil.isNotBlank(getCellContentAsString(cell))) {
                        isEmptyRow = false;
                        break;
                    }
                }
            }
            if (!isEmptyRow) {
                if (!skipHeaderRow) {
                    break;
                } else {
                    skipHeaderRow = false;
                }
            }
        }
        return row;
    }

    private static int getFirstCol(Sheet s) {
        int l = s.getLeftCol();
        Row row = s.getRow(getFirstRow(s, false));
        for (int col = row.getFirstCellNum(); col < row.getLastCellNum(); col++) {
            Cell cell = row.getCell(col);
            if (StringUtils.isNotBlank(getCellContentAsString(cell))) {
                return col;
            }
        }
        return 0;
    }

    public static Object[][] getExcelData(String file, boolean headerRow, String sheetName) {
        Object[][] retobj = null;
        Workbook workbook = null;
        try {
            File f = new File(file);
            if (!f.exists() || !f.canRead()) {
                logger.error(" Can not read file " + f.getAbsolutePath() + " Returning empty dataset1");
                return new Object[][]{};
            }
            workbook = new XSSFWorkbook(f);
            Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
            if (null == sheet) {
                throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
            }
            int firstRow, firstCol, lastRow, colsCnt;
            firstRow = getFirstRow(sheet, headerRow);
            firstCol = getFirstCol(sheet);
            lastRow = sheet.getLastRowNum();
            colsCnt = sheet.getRow(firstRow).getLastCellNum();
            logger.info("Rows : " + lastRow);
            logger.info("Columns : " + colsCnt);
            retobj = new Object[lastRow - firstRow][colsCnt - firstCol];
            for (int rIndex = firstRow; rIndex < lastRow; rIndex++) {
                Row row = sheet.getRow(rIndex);
                for (int col = firstCol; col < row.getLastCellNum(); col++) {
                    retobj[rIndex - firstRow][col - firstCol] = getCellContent(row.getCell(col));
                }
            }
        } catch (Exception e) {
            logger.error("Error while fetching data from " + file, e);
            throw new DataProviderException("Error while fetching data from " + file, e);
        } finally {
            try {
                workbook.close();
            } catch (Exception e2) {
                // skip exception
            }
        }
        return retobj;
    }

    private static final Log logger = LogFactoryImpl.getLog(ExcelUtil.class);

    public static Object[][] getExcelDataAsMap(String file, String sheetName) {
        Object[][] retobj = null;
        Workbook workbook = null;
        try {
            File f = new File(file);
            if (!f.exists() || !f.canRead()) {
                logger.error(" Can not read file " + f.getAbsolutePath() + " Returning empty dataset1");
                return new Object[][]{};
            }
            workbook = new XSSFWorkbook(f);
            Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
            if (null == sheet) {
                throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
            }
            int firstRow, firstCol, lastRow, colsCnt;
            firstRow = getFirstRow(sheet, false);
            firstCol = getFirstCol(sheet);
            lastRow = sheet.getLastRowNum();
            colsCnt = sheet.getRow(firstRow).getLastCellNum();
            String[] colNames = new String[colsCnt - firstCol];
            logger.info("Rows : " + lastRow);
            logger.info("Columns : " + colsCnt);
            retobj = new Object[lastRow - firstRow][1]; // skipped header
            // row
            for (int rIndex = firstRow; rIndex <= lastRow; rIndex++) {
                Row row = sheet.getRow(rIndex);
                if (rIndex == firstRow) {
                    for (int col = firstCol; col < row.getLastCellNum(); col++) {
                        colNames[col - firstCol] = getCellContentAsString(row.getCell(col));
                    }
                } else {
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    for (int col = firstCol; col < firstCol+colNames.length; col++) {
                        map.put(colNames[col - firstCol], getCellContent(row.getCell(col)));
                    }
                    retobj[rIndex - (firstRow + 1)][0] = map;
                }
            }

        } catch (Exception e) {
            logger.error("Error while fetching data from " + file, e);
            throw new DataProviderException("Error while fetching data from " + file, e);
        } finally {
            try {
                workbook.close();
            } catch (Exception e2) {
                // skip exception
            }
        }
        return retobj;

    }

    public static Object[][] getTableDataAsMap(String xlFilePath, String tableName, String sheetName) {
        Object[][] tabArray = null;
        Workbook workbook = null;
        try {
            File f = new File(xlFilePath);
            if (!f.exists() || !f.canRead()) {
                logger.error(" Can not read file " + f.getAbsolutePath() + " Returning empty dataset1");
                return new String[][]{};
            }
            workbook = new XSSFWorkbook(f);
            Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
            if (null == sheet) {
                throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
            }

            int startRow, startCol, endRow, endCol, ci, cj;
            Cell tableStart = findCell(sheet, tableName, sheet.getLeftCol(), sheet.getFirstRowNum());
            if (null == tableStart) {
                throw new RuntimeException(
                        "Lable " + tableName + " for starting data range not found in sheet " + sheet.getSheetName());
            }
            startRow = tableStart.getRowIndex();
            startCol = tableStart.getColumnIndex();
            Cell tableEnd = findCell(sheet, tableName, startCol + 1, startRow + 1);
            if (null == tableEnd) {
                throw new RuntimeException(
                        "Lable " + tableName + " for ending data range not found in sheet " + sheet.getSheetName());
            }
            endRow = tableEnd.getRowIndex();
            endCol = tableEnd.getColumnIndex();
            logger.debug("startRow=" + startRow + ", endRow=" + endRow + ", " + "startCol=" + startCol + ", endCol="
                    + endCol);
            tabArray = new Object[endRow - startRow][1];
            ci = 0;
            String[] colNames = new String[endCol - startCol - 1];

            for (int i = startRow; i <= endRow; i++) {
                cj = 0;
                if (i == (startRow)) {
                    for (int j = startCol + 1; j < endCol; j++, cj++) {
                        colNames[cj] = getCellContentAsString(sheet.getRow(i).getCell(j));
                        logger.debug("header[" + cj + "] : " + colNames[cj]);

                    }
                } else {
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    for (int j = startCol + 1; j < endCol; j++, cj++) {
                        map.put(colNames[cj], getCellContent(sheet.getRow(i).getCell(j)));
                    }
                    logger.debug("Record " + ci + ":" + map);
                    tabArray[ci++][0] = map;

                }
            }
        } catch (Exception e) {
            logger.error("error while fetching data from " + xlFilePath, e);
            throw new DataProviderException("Error while fetching data from " + xlFilePath, e);

        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception e2) {
                // skip exception
            }
        }

        return (tabArray);
    }

    public static Cell findCell(Sheet sheet, String searchText, int firstCol, int firstRow) {
        //Iterate rows
        for (int j = firstRow; j <= sheet.getLastRowNum(); j++) {
            Row row = sheet.getRow(j);
            if (row != null) {
                //Iterate columns
                firstCol = row.getFirstCellNum() > firstCol ? row.getFirstCellNum() : firstCol;
                for (int k = firstCol; k <= row.getLastCellNum(); k++) {
                    Cell cell = row.getCell(k);
                    if (cell != null && getCellContentAsString(cell).equals(searchText)) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    public static Object getCellContent(Cell cell) {
        if (cell != null) {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                return null;
            }
            switch (cellValue.getCellType()) {
                case NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        return DateUtil.getJavaDate(cellValue.getNumberValue());
                    } else {
                        return cellValue.getNumberValue();
                    }
                case BOOLEAN:
                    return cellValue.getBooleanValue();
                case BLANK:
                    return "";
                case STRING:
                    return cellValue.getStringValue();
                default:
                    return cellValue.formatAsString();
            }
        }
        return null;
    }

    public static String getCellContentAsString(Cell cell) {
        if (cell != null) {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                return "";
            }
            switch (cellValue.getCellType()) {
                case NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date date = DateUtil.getJavaDate(cellValue.getNumberValue());
                        String df = cell.getCellStyle().getDataFormatString();
                        return new CellDateFormatter(df).format(date);
                    } else {
                        return String.valueOf(cellValue.getNumberValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cellValue.getBooleanValue());
                case BLANK:
                    return "";
                case STRING:
                    return cellValue.getStringValue();
                default:
                    return cellValue.formatAsString();
            }
        }
        return null;
    }
}
