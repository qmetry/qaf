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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.dataproviders;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.testng.pro.DataProviderUtil;
import com.qmetry.qaf.automation.util.CSVUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.ExcelUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.testng.dataprovider.DataProviderFactory.java
 * 
 * @author chirag
 */
public class DataProviderFactory {

	public static final String getDataProvider(Method method) {
		Map<String, String> map = getParameters(method);
		return getDataProvider(map);
	}

	public static Object[][] getData(Map<String, String> param) {
		String dataproviderName = getDataProvider(param);
		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_excel.name())) {
			return ExcelUtil.getExcelDataAsMap(param.get(params.DATAFILE.name()), param.get(params.SHEETNAME.name()));
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_excel_table.name())) {
			return ExcelUtil.getTableDataAsMap(param.get(params.DATAFILE.name()), (param.get(params.KEY.name())),
					param.get(params.SHEETNAME.name()));
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_csv.name())) {
			return CSVUtil.getCSVDataAsMap(param.get(params.DATAFILE.name())).toArray(new Object[][] {});
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_database.name())) {
			String query = param.get(params.SQLQUERY.name());

			return DatabaseUtil.getRecordDataAsMap(query);

		}
		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_property.name())) {
			List<Object[]> mapData = DataProviderUtil.getDataSetAsMap(param.get(params.KEY.name()),"");

			return mapData.toArray(new Object[][] {});

		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_json.name())) {
			
			if(param.containsKey(params.JSON_DATA_TABLE)){
				return JSONUtil.getJsonArrayOfMaps(param.get(params.JSON_DATA_TABLE.name()));
			}
//			@SuppressWarnings("unchecked")
//			List<Map<String, Object>> mapData = JSONUtil.getJsonObjectFromFile(param.get(params.DATAFILE.name()),
//					List.class);
//
//			return mapData.toArray(new Object[][] {});
			return JSONUtil.getJsonArrayOfMaps(param.get(params.DATAFILE.name()));


		}
		throw new RuntimeException("No data provider found");
	}

	public static final String getDataProvider(Map<String, String> map) {
		if ((null == map) || map.isEmpty()) {
			return "";
		}
		if(map.containsKey(params.JSON_DATA_TABLE.name())){
			return dataproviders.isfw_json.name();
		}
		String f = map.get(params.DATAFILE.name());

		if (StringUtil.isNotBlank(f)) {
			if (f.endsWith(".xls")) {
				return StringUtil.isNotBlank(map.get(params.KEY.name())) ? dataproviders.isfw_excel_table.name()
						: dataproviders.isfw_excel.name();
			} else if (f.endsWith(".json")) {
				return dataproviders.isfw_json.name();
			} else {
				return dataproviders.isfw_csv.name();
			}
		}
		return StringUtil.isNotBlank(map.get(params.SQLQUERY.name())) ? dataproviders.isfw_database.name()
				: StringUtil.isNotBlank(map.get(params.KEY.name())) ? dataproviders.isfw_property.name() : "";
	}

	protected static Map<String, String> getParameters(Method method) {
		PropertyUtil props = ConfigurationManager.getBundle();
		String key = method.getName() + ".testdata";

		if (props.containsKey(key)) {
			return StringUtil.toMap(props.getStringArray(key), true);
		} else if (props.containsKey("global.testdata")) {
			Map<String, String> map = StringUtil.toMap(props.getStringArray("global.testdata"), true);
			String cls = method.getDeclaringClass().getSimpleName();
			String mtd = method.getName();
			String val = props.getSubstitutor().replace(map.get(params.DATAFILE.name()));

			map.put(params.DATAFILE.name(), val.replace("${class}", cls).replace("${method}", mtd));
			if (StringUtil.isNotBlank(map.get(params.SHEETNAME.name()))) {
				val = props.getSubstitutor().replace(map.get(params.SHEETNAME.name()));
				map.put(params.SHEETNAME.name(), val.replace("${class}", cls).replace("${method}", mtd));
			}
			if (StringUtil.isNotBlank(map.get(params.KEY.name()))) {
				val = props.getSubstitutor().replace(map.get(params.KEY.name()));
				map.put(params.KEY.name(), val.replace("${class}", cls).replace("${method}", mtd));
			}
			return map;
		} else if (props.containsKey("test." + method.getName() + ".datafile")) {
			Map<String, String> map = new WeakHashMap<String, String>();
			String[] oldStlyVal = props.getString("test." + method.getName() + ".datafile").split(",");
			map.put(params.DATAFILE.name(), oldStlyVal[0]);
			if (oldStlyVal.length > 1) {
				map.put(params.SHEETNAME.name(), oldStlyVal[1]);
			}
			return map;
		}
		if (method.isAnnotationPresent(QAFDataProvider.class)) {
			Map<String, String> map = new WeakHashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			QAFDataProvider dp = method.getAnnotation(QAFDataProvider.class);
			sb.append(params.DATAFILE.name() + "=" + dp.dataFile());
			sb.append(";");
			sb.append(params.SHEETNAME.name() + "=" + dp.sheetName());
			sb.append(";");
			sb.append(params.KEY.name() + "=" + dp.key());
			sb.append(";");
			sb.append(params.HASHEADERROW.name() + "=" + dp.hasHeaderRow());
			sb.append(";");
			sb.append(params.SQLQUERY.name() + "=" + dp.sqlQuery());

			props.setProperty(key, sb.toString());

			map.put(params.DATAFILE.name(), dp.dataFile());
			map.put(params.KEY.name(), dp.key());
			map.put(params.SQLQUERY.name(), dp.sqlQuery());

			return map;
		}

		return null;
	}

}
