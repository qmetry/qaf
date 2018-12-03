/**
 * 
 */
package com.qmetry.qaf.automation.testng.dataprovider;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.ClassHelper;
import org.testng.internal.Configuration;
import org.testng.internal.MethodInvocationHelper;
import org.testng.internal.annotations.IAnnotationFinder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.data.DataBean;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.testng.pro.DataProviderUtil;
import com.qmetry.qaf.automation.util.CSVUtil;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.ExcelUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.ListUtils;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag
 *
 */
public class QAFInetrceptableDataProvider {

	public static final String QAF_DATA_PROVIDER = "qaf-data-provider";

	@DataProvider(name = QAF_DATA_PROVIDER)
	public static Iterator<Object[]> interceptedDataProvider(ITestNGMethod method, ITestContext c) {
		TestNGScenario scenario = (TestNGScenario) method;
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) getParameters(scenario);

		Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		metadata.putAll(parameters);

		// override from properties if provided
		List<Object[]> dataList = null;
		boolean hasCustomDataProvider = metadata.containsKey(params.DATAPROVIDERCLASS.name());
		if (hasCustomDataProvider) {
			// get data provider from description!...
			String dataProvider = (String) metadata.get(params.DATAPROVIDER.name());
			String dataProviderClass = (String) metadata.get(params.DATAPROVIDERCLASS.name());
			Iterator<Object[]> testData = invokeCustomDataProvider(method, c, dataProvider, dataProviderClass);
			dataList = ListUtils.toList(testData);
		} else {
			Object[][] testData = getData(metadata);
			dataList = ListUtils.toList(testData);
		}

		// listner
		List<Object[]> interceptedData = intercept(method, c, dataList, metadata);
		return interceptedData.iterator();
	}

	private static Map<?, ?> getParameters(TestNGScenario scenario) {
		Map<String, Object> methodParameters = scenario.getMetaData();
		String description = (String) methodParameters.get("description");
		if (StringUtil.isNotBlank(description) && JSONUtil.isValidJsonString(description)) {
			@SuppressWarnings("unchecked")
			Map<String, String> paramsFromDesc = new Gson().fromJson(description, Map.class);
			methodParameters.putAll(paramsFromDesc);
		}

		// highest priority test data overridden through property with test name
		// prefix
		String testParameters = getBundle().getString(scenario.getMethodName() + ".testdata");
		if (StringUtil.isBlank(testParameters)) {
			boolean hasDataProvider = false;
			for (params param : params.values()) {
				if (methodParameters.containsKey(param.name())) {
					hasDataProvider = true;
					break;
				}
			}
			if (hasDataProvider) {
				testParameters = new Gson().toJson(methodParameters);
			} else {
				// lowest priority to global test data
				testParameters = getBundle().getString("global.testdata");
			}
		}
		String cls = scenario.getMethod().getDeclaringClass().getSimpleName();
		String mtd = scenario.getMethodName();
		testParameters = testParameters.replace("${class}", cls);
		testParameters = testParameters.replace("${method}", mtd);
		testParameters = getBundle().getSubstitutor().replace(testParameters);

		try {
			return new Gson().fromJson(testParameters, Map.class);
		} catch (JsonSyntaxException e) {
			// old way of setting global data or testdata using key=value
			return StringUtil.toMap(testParameters, true);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Object[]> intercept(ITestNGMethod method, ITestContext c, List<Object[]> testdata,
			Map<?, ?> metadata) {
		Class<?>[] paramTypes = method.getConstructorOrMethod().getParameterTypes();

		// list of only map object
		if (null != testdata && !testdata.isEmpty() && testdata.get(0).length == 1
				&& Map.class.isAssignableFrom(testdata.get(0)[0].getClass())) {
			
			//filter records using key include/exclude if provided in data
			Iterator<Object[]> iter = testdata.iterator();
			while (iter.hasNext()) {
				Map<String, Object> record = (Map<String, Object>) iter.next()[0];
				if ((record.containsKey("exclude") && Boolean.valueOf(record.get("exclude").toString()))
						|| (record.containsKey("include") && !Boolean.valueOf(record.get("include").toString()))) {
					iter.remove();
				}
			}
			
			//process parameters 
			for (int i = 0; i < testdata.size(); i++) {
				Map<String, Object> record = (Map<String, Object>) testdata.get(i)[0];
				if (paramTypes.length > 1) {
					Object[] values = record.values().toArray();
					if (paramTypes.length == values.length) {
						testdata.set(i, values);
					} else {
						Object[] params = new Object[paramTypes.length];
						for (int pi = 0; pi < paramTypes.length; pi++) {
							try {
								if (values.length > pi && paramTypes[pi].isAssignableFrom(values[pi].getClass())) {
									params[pi] = values[pi];
								} else if (DataBean.class.isAssignableFrom(paramTypes[pi])) {
									DataBean bean = (DataBean) paramTypes[pi].newInstance();
									bean.fillData(record);
									params[pi] = bean;

								} else {
									Object bean = paramTypes[pi].newInstance();
									BeanUtils.populate(bean, record);
								}
							} catch (Exception e) {
								throw new AutomationError("Unable to populate data" + paramTypes, e);
							}
						}
						testdata.set(i, params);
					}
				} else if (!Map.class.isAssignableFrom(paramTypes[0])) {
					if (DataBean.class.isAssignableFrom(paramTypes[0])) {
						try {
							DataBean bean = (DataBean) paramTypes[0].newInstance();
							bean.fillData(record);
							testdata.set(i, new Object[] { bean });

						} catch (Exception e) {
							throw new AutomationError("Unable to populate databean", e);
						}
					} else {
						Gson gson = new Gson();
						String seralizedObj = gson.toJson(record);
						Object obj = new Gson().fromJson(seralizedObj, paramTypes[0]);
						testdata.set(i, new Object[] { obj });
					}
				} else {
					record.put("__index", i + 1);
					if (!record.containsKey("testCaseId")) {
						record.put("testCaseId", method.getMethodName() + "-" + (i + 1));
					}
				}
			}
		}

		int from = 0;
		int to = testdata.size() - 1;
		if (metadata.containsKey("from") || metadata.containsKey("to")) {
			if (metadata.containsKey("to") && (int) metadata.get("to") < to) {
				to = (int) metadata.get("to");
			}
			if (metadata.containsKey("from")) {
				from = (int) metadata.get("from");
			}
			return testdata.subList(from, to);
		}

		if (metadata.containsKey("indices")) {
			List<?> indices = (List<?>) metadata.get("indices");
			List<Object[]> filteredList = new ArrayList<Object[]>();
			for (Object i : indices) {
				filteredList.add(testdata.get((int) i));
			}
			return filteredList;
		}
		return testdata;
	}

	public static Object[][] getData(Map<?, ? extends Object> param) {

		if (param.containsKey(params.SQLQUERY.name())) {
			String query = (String) param.get(params.SQLQUERY.name());
			return DatabaseUtil.getRecordDataAsMap(query);
		}
		if (param.containsKey(params.JSON_DATA_TABLE.name())) {
			return JSONUtil.getJsonArrayOfMaps((String) param.get(params.JSON_DATA_TABLE.name()));
		}
		if (param.containsKey(params.DATAFILE.name())) {
			String file = (String) param.get(params.DATAFILE.name());
			if (file.endsWith("json")) {
				return JSONUtil.getJsonArrayOfMaps(file);
			}
			if (file.endsWith("xml")) {
				List<Object[]> mapData = DataProviderUtil.getDataSetAsMap((String) param.get(params.KEY.name()), file);
				return mapData.toArray(new Object[][] {});
			}
			if (file.endsWith("xls")) {
				if (param.containsKey(params.KEY.name())) {
					return ExcelUtil.getTableDataAsMap(file, ((String) param.get(params.KEY.name())),
							(String) param.get(params.SHEETNAME.name()));
				}
				return ExcelUtil.getExcelDataAsMap(file, (String) param.get(params.SHEETNAME.name()));
			}
			// csv, text
			List<Object[]> csvData = CSVUtil.getCSVDataAsMap(file);
			return csvData.toArray(new Object[][] {});
		}
		if (param.containsKey(params.KEY.name())) {
			List<Object[]> mapData = DataProviderUtil.getDataSetAsMap((String) param.get(params.KEY.name()), "");
			return mapData.toArray(new Object[][] {});
		}
		throw new RuntimeException("No data provider found");
	}

	private static Iterator<Object[]> invokeCustomDataProvider(ITestNGMethod tm, ITestContext c, String dp,
			String dpc) {
		if (StringUtil.isBlank(dpc)) {
			dpc = getBundle().getString("global.dataproviderclass", getBundle().getString("dataproviderclass"));
		}
		if (StringUtil.isNotBlank(dpc)) {
			Method m = getDataProviderMethod(dp, dpc);
			;
			Object instanceToUse = ClassHelper.newInstanceOrNull(m.getDeclaringClass());
			return InvocatoinHelper.invokeDataProvider(instanceToUse, m, tm, c, null,
					new Configuration().getAnnotationFinder());
		} else {
			throw new AutomationError(
					"Data-provider class not found. Please provide fully qualified class name as dataProviderClass");
		}
	}

	private static Method getDataProviderMethod(String dp, String dpc) {
		try {
			Class<?> dpClass = Class.forName(dpc);
			Set<Method> dpMethods = ClassUtil.getAllMethodsWithAnnotation(dpClass, DataProvider.class);
			for (Method m : dpMethods) {
				DataProvider dpObj = ClassUtil.getAnnotation(m, DataProvider.class);
				if (dp.equalsIgnoreCase(dpObj.name())) {
					// this is the mehod we are lo
					return m;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new AutomationError("Data-provider class " + dpc
					+ " not found. Please provide fully qualified class name as dataProviderClass");
		}
		throw new AutomationError("Data-provider: '" + dp + "' not found in class: '" + dpc
				+ "'. Please provide valid data provider name as dataProvider");
	}

	private static class InvocatoinHelper extends MethodInvocationHelper {
		protected static Iterator<Object[]> invokeDataProvider(Object instance, Method dataProvider,
				ITestNGMethod method, ITestContext testContext, Object fedInstance,
				IAnnotationFinder annotationFinder) {
			return MethodInvocationHelper.invokeDataProvider(instance, dataProvider, method, testContext, fedInstance,
					annotationFinder);
		}
	}

	public static void setQAFDataProvider(ITestAnnotation testAnnotation, Method method) {
		if (method.getAnnotation(QAFDataProvider.class) != null && (null != method)
				&& null != method.getParameterTypes() && (method.getParameterTypes().length > 0)) {
			String dataProvider = testAnnotation.getDataProvider();
			boolean hasDataProvider = StringUtil.isNotBlank(dataProvider);

			// other than qaf data provider
			if (hasDataProvider && !dataProvider.equalsIgnoreCase(QAF_DATA_PROVIDER)) {
				// keep actual data-provider details with description
				Map<String, String> desc = new HashMap<String, String>();
				desc.put("description", testAnnotation.getDescription());
				desc.put("dataProvider", testAnnotation.getDataProvider());
				Class<?> dpClass = testAnnotation.getDataProviderClass();
				if (null != dpClass) {
					desc.put("dataProviderClass", dpClass.getName());
				} else {
					desc.put("dataProviderClass", method.getDeclaringClass().getName());
				}
				testAnnotation.setDescription(new Gson().toJson(desc));
			}

			testAnnotation.setDataProvider(QAF_DATA_PROVIDER);
			testAnnotation.setDataProviderClass(QAFInetrceptableDataProvider.class);
		}
	}

}
