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

import com.qmetry.qaf.automation.testng.DataProviderException;

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
