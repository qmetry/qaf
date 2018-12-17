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

package com.qmetry.qaf.automation.testng.dataprovider;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.json.JSONObject;
import org.testng.annotations.ITestAnnotation;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.testng.DataProviderException;
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


	public static void setQAFDataProvider(ITestAnnotation testAnnotation, Method method) {
		if ((null != method) && null != method.getParameterTypes() && (method.getParameterTypes().length > 0)) {
			String dataProvider = testAnnotation.getDataProvider();
			boolean hasDataProvider = isNotBlank(dataProvider);

			// other than qaf data provider
			if (hasDataProvider && !dataProvider.startsWith(QAFDataProvider.NAME)) {
				// keep actual data-provider details with description
				Map<String, String> desc = new HashMap<String, String>();
				desc.put("description", testAnnotation.getDescription());
				desc.put("dataProvider", testAnnotation.getDataProvider());
				Class<?> dpClass = testAnnotation.getDataProviderClass();
				if (null != dpClass) {
					desc.put("dataProviderClass", dpClass.getName());
				}
				testAnnotation.setDescription(new JSONObject(desc).toString());
			}

			boolean globalParallelSetting = getBundle().getBoolean("global.datadriven.parallel", false);
			boolean parallel = getBundle().getBoolean(method.getName() + ".parallel", globalParallelSetting);
			dataProvider = parallel ? QAFDataProvider.NAME_PARALLEL : QAFDataProvider.NAME;

			testAnnotation.setDataProvider(dataProvider);
			testAnnotation.setDataProviderClass(QAFInetrceptableDataProvider.class);
		}
	}
}
