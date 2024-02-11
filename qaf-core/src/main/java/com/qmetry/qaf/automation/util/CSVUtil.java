/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.data.DataProviderException;

/**
 * com.qmetry.qaf.automation.util.CSVUtil.java
 * 
 * @author chirag
 */
public class CSVUtil {
	private static final Log logger = LogFactoryImpl.getLog(CSVUtil.class);

	public static List<Object[]> getCSVData(String strFile) {
		return getCSVData(strFile, ',');
	}

	public static List<Object[]> getCSVData(String strFile, char separatorChar) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		File csvFile;
		BufferedReader br = null;
		try {

			logger.info("loading csv data file: " + strFile);
			csvFile = new File(strFile);
			br = new BufferedReader(new FileReader(csvFile));
			String strLine = "";
			// read comma separated file line by line
			// exclude blank lines and comments
			while ((strLine = br.readLine()) != null) {
				if (!("".equalsIgnoreCase(strLine.trim()) || "#!".contains("" + strLine.trim().charAt(0)))) {
					Object[] cols = StringUtil.parseCSV(strLine, separatorChar);// strLine.split(separatorChar);
					rows.add(cols);
				} else if (strLine.contains("col.separator")) {
					separatorChar = strLine.split("=")[1].trim().charAt(0);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while reading csv file: " + strFile + e);
			throw new DataProviderException("Error while fetching data from " + strFile, e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return rows;
	}

	@Deprecated
	public static List<Object[]> getCSVData(String strFile, Map<String, Integer> headers) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		int cnt = 0;
		BufferedReader br = null;
		try {

			logger.info("loading csv data file: " + strFile);
			File csvFile = new File(strFile);
			br = new BufferedReader(new FileReader(csvFile));
			String strLine = "";
			char separatorChar = ',';
			// read comma separated file line by line
			// exclude blank lines and comments

			while ((strLine = br.readLine()) != null) {
				if (!("".equalsIgnoreCase(strLine.trim()) || "#!".contains("" + strLine.trim().charAt(0)))) {
					Object[] cols = StringUtil.parseCSV(strLine, separatorChar);// strLine.split(separatorChar);
					if (cnt == 0) {
						for (int i = 0; i < cols.length; i++) {
							headers.put(cols[i].toString().replaceAll(" ", "").toUpperCase(), i);
						}
					} else {
						rows.add(cols);
					}
					cnt++;
				} else if (strLine.contains("col.separator")) {
					separatorChar = strLine.split("=")[1].trim().charAt(0);
				}
			}

		} catch (Exception e) {
			logger.error("Exception while reading csv file: " + strFile + e);
			throw new DataProviderException("Error while fetching data from " + strFile, e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		logger.info(cnt + " rows found");
		return rows;
	}

	/**
	 * Get csv file data as Iterator of Map for each row data with column names
	 * as key. It assumes first row as header row.
	 * 
	 * @param strFile
	 *            - csv file
	 * @return Iterator of object array which contains Map
	 */
	public static List<Object[]> getCSVDataAsMap(String strFile) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		BufferedReader br = null;
		try {

			logger.info("loading csv data file: " + strFile);
			File csvFile = new File(strFile);
			br = new BufferedReader(new FileReader(csvFile));
			String strLine = "";
			char separatorChar = ',';
			// read comma separated file line by line
			// exclude blank lines and comments
			Object[] colsNames = null;
			while ((strLine = br.readLine()) != null) {
				if (!("".equalsIgnoreCase(strLine.trim()) || "#!".contains("" + strLine.trim().charAt(0)))) {
					if (colsNames == null) {
						colsNames = StringUtil.parseCSV(strLine, separatorChar);
					} else {
						Object[] cols = StringUtil.parseCSV(strLine, separatorChar);
						Map<String, Object> map = new LinkedHashMap<String, Object>();
						for (int i = 0; i < cols.length; i++) {
							try {
								map.put(colsNames[i].toString().trim(), cols[i]);
							} catch (ArrayIndexOutOfBoundsException e) {
								logger.warn(String.format(
										"Missing column header for column[%d] in data file: %s. It will be included by lineNo",
										i + 1, strFile));
								map.put(String.valueOf(i), cols[i]);

							}
						}
						rows.add(new Object[] { map });

					}
				} else if (strLine.contains("col.separator")) {
					separatorChar = strLine.split("=")[1].trim().charAt(0);
				}
			}

		} catch (Exception e) {
			logger.error("Exception while reading csv file: " + strFile + e);
			throw new DataProviderException("Error while fetching data from " + strFile, e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return rows;
	}
}
