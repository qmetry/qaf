/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


package com.qmetry.qaf.automation.util;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.testng.DataProviderException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * com.qmetry.qaf.automation.util.ExcelUtil.java
 * 
 * @author chirag
 */
public class ExcelUtil {
	private static int getFirstRow(Sheet s, boolean skipHeaderRow) {
		int row = 0;
		int l = s.getRows();
		for (row = 0; row < l; row++) {
			Cell[] cells = s.getRow(row);
			boolean isEmptyRow = true;
			for (Cell cell : cells) {
				if (StringUtil.isNotBlank(cell.getContents())) {
					isEmptyRow = false;
					break;
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
		int l = s.getColumns();
		Cell[] cells = s.getRow(getFirstRow(s, false));
		for (int col = 0; col < l; col++) {
			Cell cell = cells[col];
			if (StringUtils.isNotBlank(cell.getContents())) {
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
				return new Object[][] {};
			}
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheet(0);
			if (null == sheet) {
				throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
			}
			int firstRow, firstCol, lastRow, colsCnt;
			firstRow = getFirstRow(sheet, headerRow);
			firstCol = getFirstCol(sheet);
			lastRow = sheet.getRows();
			colsCnt = sheet.getColumns();
			logger.info("Rows : " + lastRow);
			logger.info("Columns : " + colsCnt);
			retobj = new Object[lastRow - firstRow][colsCnt - firstCol];
			for (int row = firstRow; row < lastRow; row++) {
				Cell[] cells = sheet.getRow(row);
				for (int col = firstCol; col < cells.length; col++) {
					retobj[row - firstRow][col - firstCol] = cells[col].getContents();
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

	/**
	 * @param file
	 * @param headerRow
	 * @return
	 */
	public static Object[][] getExcelData(String file, boolean headerRow) {
		return getExcelData(file, headerRow, "");
	}

	public static String[][] getTableData(String xlFilePath, String tableName, String sheetName) {
		String[][] tabArray = null;
		Workbook workbook = null;
		try {
			File f = new File(xlFilePath);
			if (!f.exists() || !f.canRead()) {
				logger.error(" Can not read file " + f.getAbsolutePath() + " Returning empty dataset1");
				return new String[][] {};
			}
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheet(0);
			if (null == sheet) {
				throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
			}

			int startRow, startCol, endRow, endCol, ci, cj;
			Cell tableStart = sheet.findCell(tableName);
			if (null == tableStart) {
				throw new RuntimeException(
						"Lable " + tableName + " for starting data range not found in sheet " + sheet.getName());
			}

			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();
			Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 100, 64000, false);
			if (null == tableEnd) {
				throw new RuntimeException(
						"Lable " + tableName + " for ending data range not found in sheet " + sheet.getName());
			}
			endRow = tableEnd.getRow();
			endCol = tableEnd.getColumn();
			logger.debug("startRow=" + startRow + ", endRow=" + endRow + ", " + "startCol=" + startCol + ", endCol="
					+ endCol);
			tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
			ci = 0;

			for (int i = startRow + 1; i < endRow; i++, ci++) {
				cj = 0;
				for (int j = startCol + 1; j < endCol; j++, cj++) {
					tabArray[ci][cj] = sheet.getCell(j, i).getContents();
				}
			}
		} catch (Exception e) {
			logger.error("error while fetching data from " + xlFilePath, e);
			throw new DataProviderException("Error while fetching data from " + xlFilePath, e);

		} finally {
			try {
				workbook.close();
			} catch (Exception e2) {
				// skip exception
			}
		}

		return (tabArray);
	}

	private static final Log logger = LogFactoryImpl.getLog(ExcelUtil.class);

	public static Object[][] getExcelDataAsMap(String file, String sheetName) {
		Object[][] retobj = null;
		Workbook workbook = null;
		try {
			File f = new File(file);
			if (!f.exists() || !f.canRead()) {
				logger.error(" Can not read file " + f.getAbsolutePath() + " Returning empty dataset1");
				return new Object[][] {};
			}
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheet(0);
			if (null == sheet) {
				throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
			}
			int firstRow, firstCol, lastRow, colsCnt;
			firstRow = getFirstRow(sheet, false);
			firstCol = getFirstCol(sheet);
			lastRow = sheet.getRows();
			colsCnt = sheet.getColumns();
			String[] colNames = new String[colsCnt - firstCol];

			logger.info("Rows : " + lastRow);
			logger.info("Columns : " + colsCnt);
			retobj = new Object[lastRow - (firstRow + 1)][1]; // skipped header
																// row
			for (int row = firstRow; row < lastRow; row++) {
				Cell[] cells = sheet.getRow(row);
				if (row == firstRow) {
					for (int col = firstCol; col < (firstCol + cells.length); col++) {
						colNames[col - firstCol] = cells[col].getContents().trim();
					}
				} else {
					Map<String, String> map = new LinkedHashMap<String, String>();
					for (int col = firstCol; col < (firstCol + cells.length); col++) {
						map.put(colNames[col - firstCol], cells[col].getContents());
					}
					retobj[row - (firstRow + 1)][0] = map;
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
				return new String[][] {};
			}
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = StringUtils.isNotBlank(sheetName) ? workbook.getSheet(sheetName) : workbook.getSheet(0);
			if (null == sheet) {
				throw new RuntimeException("Worksheet " + sheetName + " not found in " + f.getAbsolutePath());
			}

			int startRow, startCol, endRow, endCol, ci, cj;
			Cell tableStart = sheet.findCell(tableName);
			if (null == tableStart) {
				throw new RuntimeException(
						"Lable " + tableName + " for starting data range not found in sheet " + sheet.getName());
			}

			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();
			Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 100, 64000, false);
			if (null == tableEnd) {
				throw new RuntimeException(
						"Lable " + tableName + " for ending data range not found in sheet " + sheet.getName());
			}
			endRow = tableEnd.getRow();
			endCol = tableEnd.getColumn();
			logger.debug("startRow=" + startRow + ", endRow=" + endRow + ", " + "startCol=" + startCol + ", endCol="
					+ endCol);
			tabArray = new Object[endRow - startRow][1];
			ci = 0;
			String[] colNames = new String[endCol - startCol - 1];

			for (int i = startRow; i <= endRow; i++) {
				cj = 0;
				if (i == (startRow)) {
					for (int j = startCol + 1; j < endCol; j++, cj++) {
						colNames[cj] = sheet.getCell(j, i).getContents().trim();
						logger.debug("header[" + cj + "] : " + colNames[cj]);

					}
				} else {
					Map<String, String> map = new LinkedHashMap<String, String>();
					for (int j = startCol + 1; j < endCol; j++, cj++) {
						map.put(colNames[cj], sheet.getCell(j, i).getContents());
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
				workbook.close();
			} catch (Exception e2) {
				// skip exception
			}
		}

		return (tabArray);
	}

}
