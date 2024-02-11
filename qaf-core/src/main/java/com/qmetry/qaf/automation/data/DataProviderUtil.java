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
package com.qmetry.qaf.automation.data;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.CSVUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.PoiExcelUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Utility class for TestNG data providers.
 * <ul>
 * <li>Example Usage: @Test(dataProvider = "csvDataProvider", dataProviderClass
 * = DataProviderUtil.class)
 * <li>Excel Data provider:
 * <ol>
 * <li>excelDataProvider, excelDataProviderParallel <br>
 * Required properties:
 * <ol>
 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;optional sheet
 * name&gt; If the data is not in in first sheet of workbook then provide
 * sheet-name with datafile separated by comma
 * <li>test.&lt;method name&gt;.data.hasheader=true/false set true to skip
 * header row
 * </ol>
 * <li>excelTableDataProvider, excelTableDataProviderParallel<br>
 * provides data from excel sheet marked with data set name<br>
 * Required properties:
 * <ol>
 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;data set
 * name&gt;,&lt;optional sheet name&gt; If the data is not in in first sheet of
 * workbook then provide sheet-name with datafile separated by comma
 * </ol>
 * </ol>
 * <li>CSV Data provider:
 * <ol>
 * <li>csvDataProvider, csvDataProviderParallel <br>
 * Required property
 * <ol>
 * <li>test.&lt;method_name&gt;.datafile= &lt;file URL&gt;
 * </ol>
 * </ol>
 * </ul>
 * com.qmetry.qaf.automation.testng.dataprovider.DataProviderUtil.java
 * 
 * @author chirag.jayswal
 */
public class DataProviderUtil {
	private static final Log logger = LogFactoryImpl.getLog(DataProviderUtil.class);
	/**
	 * Represents meta-data for data provider
	 * 
	 * @author chirag
	 *
	 */
	public enum params {
		DATAFILE, SHEETNAME, KEY, HASHEADERROW, SQLQUERY, BEANCLASS, JSON_DATA_TABLE, DATAPROVIDER, DATAPROVIDERCLASS, FILTER, FROM, TO, INDICES;
	}

	/**
	 * Blank values will be considered as null as type is not be identifiable from blank
	 * @param key
	 * @param file
	 * @return
	 */
	public static List<Object[]> getDataSetAsMap(String key, String file) {

		Configuration config;
		if (StringUtil.isBlank(file)) {
			config = ConfigurationManager.getBundle().subset(key);
		} else {
			config = new PropertyUtil(file).subset(key);
		}
		ArrayList<Object[]> dataset = new ArrayList<Object[]>();
		if (config.isEmpty()) {
			logger.error("Missing data with key [" + key + "]. ");
			throw new DataProviderException("Not test data found with key:" + key);
		}
		int size = config.getList(config.getKeys().next().toString()).size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			Iterator<?> iter = config.getKeys();
			while (iter.hasNext()) {
				String dataKey = String.valueOf(iter.next());
				try {
					String value = config.getStringArray(dataKey)[i];
					map.put(dataKey, StringUtil.toObject(value));
				} catch (ArrayIndexOutOfBoundsException e) {
					logger.error("Missing entry for property " + dataKey
							+ ". Provide value for each property (or blank) in each data set in data file.", e);
					throw e;
				}
			}
			dataset.add(new Object[] { map });
		}
		return dataset;
	}
	
	/**
	 * 
	 * @param metadata
	 * @return
	 */
	public static Object[][] getData(Map<String, Object> metadata) {

		String query = (String) metadata.get(params.SQLQUERY.name());
		if (isNotBlank(query)) {
			return DatabaseUtil.getRecordDataAsMap(query);
		}

		String jsonTable = (String) metadata.get(params.JSON_DATA_TABLE.name());
		if (isNotBlank(jsonTable)) {
			return JSONUtil.getJsonArrayOfMaps(jsonTable);
		}

		String file = (String) metadata.get(params.DATAFILE.name());
		String key = (String) metadata.get(params.KEY.name());

		if (isNotBlank(file)) {
			if (file.endsWith("json")) {
				return JSONUtil.getJsonArrayOfMaps(file);
			}
			if (file.endsWith("xml")) {
				List<Object[]> mapData = DataProviderUtil.getDataSetAsMap(key, file);
				return mapData.toArray(new Object[][] {});
			}
			/*if (file.endsWith("xls")) {
				if (isNotBlank(key)) {
					return ExcelUtil.getTableDataAsMap(file, ((String) metadata.get(params.KEY.name())),
							(String) metadata.get(params.SHEETNAME.name()));
				}
				return ExcelUtil.getExcelDataAsMap(file, (String) metadata.get(params.SHEETNAME.name()));
			}*/
			if (file.endsWith("xlsx") || file.endsWith("xls")) {
				if (isNotBlank(key)) {
					return PoiExcelUtil.getTableDataAsMap(file, ((String) metadata.get(params.KEY.name())),
							(String) metadata.get(params.SHEETNAME.name()));
				}
				return PoiExcelUtil.getExcelDataAsMap(file, (String) metadata.get(params.SHEETNAME.name()));
			}
			// csv, text
			List<Object[]> csvData = CSVUtil.getCSVDataAsMap(file);
			return csvData.toArray(new Object[][] {});
		}
		if (isNotBlank(key)) {
			List<Object[]> mapData = DataProviderUtil.getDataSetAsMap(key, "");
			return mapData.toArray(new Object[][] {});
		}
		throw new RuntimeException("No data provider found");
	}



}
