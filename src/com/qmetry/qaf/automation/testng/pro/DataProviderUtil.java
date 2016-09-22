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


package com.qmetry.qaf.automation.testng.pro;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.annotations.DataProvider;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.testng.DataProviderException;
import com.qmetry.qaf.automation.testng.dataprovider.DataProviderFactory;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.util.CSVUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.ExcelUtil;
import com.qmetry.qaf.automation.util.JSONUtil;

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
public class DataProviderUtil extends DataProviderFactory {
	private static final Log logger = LogFactoryImpl.getLog(DataProviderUtil.class);

	/**
	 * <p>
	 * Use as TestNG dataProvider.
	 * <p>
	 * data-provider name: <code>isfw-database</code>
	 * <p>
	 * Required properties:
	 * <ol>
	 * <li>test.&lt;method name&gt;.query=&lt;sql query string&gt;
	 * </ol>
	 * 
	 * @param m
	 * @return
	 */
	@DataProvider(name = "isfw_database")
	public static Object[][] getDataFromDB(Method m) {
		String query = getParameters(m).get(params.SQLQUERY.name());
		Class<?> types[] = m.getParameterTypes();

		if ((types.length == 1) && Map.class.isAssignableFrom(types[0])) {
			// will consider first row as header row
			return DatabaseUtil.getRecordDataAsMap(query);
		}
		return DatabaseUtil.getData(query);
	}

	/**
	 * <p>
	 * Use as TestNG dataProvider.
	 * <p>
	 * data-provider name: <code>isfw-excel</code>
	 * <p>
	 * Required properties:
	 * <ol>
	 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;optional
	 * sheet name&gt; If the data is not in sheet 1 then provide sheet-name with
	 * datafile separated by semicolon (;)
	 * <li>test.&lt;method name&gt;.data.hasheader=true/false set true to skip
	 * header row
	 * </ol>
	 * 
	 * @param m
	 * @return
	 */

	@DataProvider(name = "isfw_excel")
	public static Object[][] getExcelData(Method m) {
		Map<String, String> param = getParameters(m);

		Class<?> types[] = m.getParameterTypes();
		if ((types.length == 1) && Map.class.isAssignableFrom(types[0])) {
			// will consider first row as header row
			return ExcelUtil.getExcelDataAsMap(param.get(params.DATAFILE.name()), param.get(params.SHEETNAME.name()));
		}
		return ExcelUtil
				.getExcelData(param.get(params.DATAFILE.name()),
						(param.get(params.HASHEADERROW.name()) != null)
								&& Boolean.valueOf(param.get(params.HASHEADERROW.name())),
						param.get(params.SHEETNAME.name()));
	}

	/**
	 * <p>
	 * Use as TestNG dataProvider.
	 * <p>
	 * data-provider name: <code>isfw-excel-table</code>
	 * <p>
	 * provides data from excel sheet marked with data set name<br>
	 * Required properties:
	 * <ol>
	 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;data set
	 * name&gt;,&lt;optional sheet name&gt; If the data is not in in first sheet
	 * of workbook then provide sheet-name with datafile separated by comma
	 * </ol>
	 * 
	 * @param m
	 * @return
	 */
	@DataProvider(name = "isfw_excel_table")
	public static Object[][] getExcelTableData(Method m) {
		Map<String, String> param = getParameters(m);

		Class<?> types[] = m.getParameterTypes();
		if ((types.length == 1) && Map.class.isAssignableFrom(types[0])) {
			// will consider first row as header row
			return ExcelUtil.getTableDataAsMap(param.get(params.DATAFILE.name()), (param.get(params.KEY.name())),
					param.get(params.SHEETNAME.name()));
		}
		return ExcelUtil.getTableData(param.get(params.DATAFILE.name()), (param.get(params.KEY.name())),
				param.get(params.SHEETNAME.name()));
	}

	/**
	 * <p>
	 * Use as TestNG dataProvider.
	 * <p>
	 * data-provider name: <code>isfw-csv</code>
	 * <p>
	 * csv data provider utility method for testNG<br>
	 * Required property
	 * <ol>
	 * <li>test.&lt;method_name&gt;.datafile= &lt;file URL&gt;
	 * </ol>
	 * 
	 * @param method
	 * @return
	 */
	@DataProvider(name = "isfw_csv")
	public static final Object[][] getCSVData(Method method) {
		Class<?> types[] = method.getParameterTypes();
		if ((types.length == 1) && Map.class.isAssignableFrom(types[0])) {
			// will consider first row as header row
			return CSVUtil.getCSVDataAsMap(getParameters(method).get(params.DATAFILE.name()))
					.toArray(new Object[][] {});
		}

		return CSVUtil.getCSVData(getParameters(method).get(params.DATAFILE.name())).toArray(new Object[][] {});
	}

	/**
	 * <p>
	 * Use as TestNG dataProvider.
	 * <p>
	 * data-provider name: <code>isfw-csv1</code>
	 * <p>
	 * csv data provider utility method for testNG Aspects property
	 * test.&lt;method_name&gt;.datafile property to hold csv file name.
	 * 
	 * @param method
	 * @return
	 */
	@DataProvider(name = "csvDataProvider")
	public static final Object[][] getCSVData1(Method method) {
		return getCSVData(method);
	}

	/*
	 * <p> Use as TestNG dataProvider. <p> data-provider name:
	 * <code>isfw-json</code> <p> json data provider utility method for testNG
	 * Aspects property test.&lt;method_name&gt;.datafile property to hold file
	 * name.
	 * 
	 * @param method
	 * 
	 * @return
	 */
	@DataProvider(name = "isfw_json")
	public static final Object[][] getJsonData(Method method) {

		return JSONUtil.getJsonArrayOfMaps(getParameters(method).get(params.DATAFILE.name()));
	}

	/*
	 * <p> Use as TestNG dataProvider. <p> data-provider name:
	 * <code>isfw-property</code> <p> csv data provider utility method for
	 * testNG<br> Required property <ol> <li>test.&lt;method_name&gt;.datafile=
	 * &lt;file URL&gt; </ol>
	 * 
	 * @param method
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@DataProvider(name = "isfw_property")
	public static final Object[][] getDataFromProp(Method method) {
		List<Object[]> mapData = getDataSetAsMap(getParameters(method).get(params.KEY.name()));
		Class<?> types[] = method.getParameterTypes();
		if ((types.length == 1) && Map.class.isAssignableFrom(types[0])) {
			return mapData.toArray(new Object[][] {});
		} else {
			List<Object[]> data = new ArrayList<Object[]>();
			Iterator<Object[]> mapDataIter = mapData.iterator();
			while (mapDataIter.hasNext()) {
				Map<String, String> map = (Map<String, String>) mapDataIter.next()[0];
				data.add(map.values().toArray());
			}
			return data.toArray(new Object[][] {});
		}
	}

	public static List<Object[]> getDataSetAsMap(String key) {
		Configuration config = ConfigurationManager.getBundle().subset(key);
		ArrayList<Object[]> dataset = new ArrayList<Object[]>();
		if(config.isEmpty()){
			logger.error("Missing data with key [" + key
					+ "]. ");
			throw new DataProviderException("Not test data found with key:" + key);
		}
		int size = config.getList(config.getKeys().next().toString()).size();
		for (int i = 0; i < size; i++) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			Iterator<?> iter = config.getKeys();
			while (iter.hasNext()) {
				String dataKey = String.valueOf(iter.next());
				try {
					map.put(dataKey, config.getStringArray(dataKey)[i]);
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

}
