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


package com.qmetry.qaf.automation.step.client;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.ClassHelper;
import org.testng.internal.Configuration;
import org.testng.internal.MethodInvocationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.collections.ArrayIterator;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.dataproviders;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.testng.pro.DataProviderUtil;
import com.qmetry.qaf.automation.util.CSVUtil;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.ExcelUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.client.csv.CSVTest.java
 * 
 * @author chirag.jayswal
 */
public class DataDrivenScenario extends Scenario {
	private String dataProviderDesc;

	public DataDrivenScenario(String testName, Collection<TestStep> steps, String dataProviderDesc) {
		super(testName, steps);
		this.dataProviderDesc = dataProviderDesc;
	}

	public DataDrivenScenario(String testName, Collection<TestStep> steps, String dataProviderDesc,
			Map<String, Object> metadata) {
		super(testName, steps, metadata);
		this.dataProviderDesc = dataProviderDesc;

	}

	@Override
	@Test(enabled = false)
	public void scenario() {

	}

	@Test(dataProvider = "scenariodp", groups = "scenario")
	public void scenario(Map<String, String> testData) {
		beforeScanario();
		logger.info("Test Data" + String.format("%s", testData));

		Map<String, Object> context = new HashMap<String, Object>(testData);
		context.put("${args[0]}", testData);
		context.put("args[0]", testData);

		execute(getStepsToExecute(context), context);

	}

	protected String comuteSign() {
		return getPackage() + "." + scenarioName + "( " + Map.class.getName() + ")";
	}

	@DataProvider(name = "scenariodp", parallel=true)
	public Iterator<Object[]> dp(ITestNGMethod tm, ITestContext c) {
		//System.out.println(dataProviderDesc);
		dataProviderDesc = getBundle().getSubstitutor().replace(dataProviderDesc);
		Map<String, String> param = StringUtil
				.toMap(StringUtil.parseCSV(dataProviderDesc, getBundle().getListDelimiter()), true);
		//System.out.println(param);

		if(param.containsKey(params.DATAPROVIDER.name())){
			return invokeCustomDataProvider(tm,  c, param.get(params.DATAPROVIDER.name()), param.get(params.DATAPROVIDERCLASS.name()));
		}
		String dataproviderName = DataProviderUtil.getDataProvider(param);
		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_excel.name())) {
			return new ArrayIterator(ExcelUtil.getExcelDataAsMap(param.get(params.DATAFILE.name()), param.get(params.SHEETNAME.name())));
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_excel_table.name())) {
			return new ArrayIterator(ExcelUtil.getTableDataAsMap(param.get(params.DATAFILE.name()), (param.get(params.KEY.name())),
					param.get(params.SHEETNAME.name())));
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_csv.name())) {
			return CSVUtil.getCSVDataAsMap(param.get(params.DATAFILE.name())).iterator();
		}

		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_database.name())) {
			String query = param.get(params.SQLQUERY.name());
			return new ArrayIterator(DatabaseUtil.getRecordDataAsMap(query));
		}
		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_property.name())) {
			List<Object[]> mapData = DataProviderUtil.getDataSetAsMap(param.get(params.KEY.name()));

			return mapData.iterator();

		}
		if (dataproviderName.equalsIgnoreCase(dataproviders.isfw_json.name())) {
			if(param.containsKey(params.JSON_DATA_TABLE.name()))
				return new ArrayIterator(JSONUtil.getJsonArrayOfMaps((String)getMetadata().get(params.JSON_DATA_TABLE.name())));
			
			return new ArrayIterator(JSONUtil.getJsonArrayOfMaps(param.get(params.DATAFILE.name())));
		}
		throw new RuntimeException("No data provider found");
	}

	private Iterator<Object[]> invokeCustomDataProvider(ITestNGMethod tm, ITestContext c, String dp, String dpc) {
		if(StringUtil.isBlank(dpc)){
			dpc=getBundle().getString("global.dataproviderclass",getBundle().getString("dataproviderclass"));
		}
		if(StringUtil.isNotBlank(dpc)){
			Method m = getDataProviderMethod(dp, dpc);
			Object instanceToUse = ClassHelper.newInstanceOrNull(m.getDeclaringClass());
			return InvocatoinHelper.invokeDataProvider(instanceToUse, m, tm, c, null, new Configuration().getAnnotationFinder());
		}else{
			throw new AutomationError("Data-provider class not found. Please provide fully qualified class name as dataProviderClass");
		}
	}

	private Method getDataProviderMethod(String dp, String dpc){
		try {
			Class<?> dpClass = Class.forName(dpc);
			Set<Method> dpMethods = ClassUtil.getAllMethodsWithAnnotation(dpClass, DataProvider.class);
			for(Method m : dpMethods){
				DataProvider dpObj = ClassUtil.getAnnotation(m, DataProvider.class);
				if(dp.equalsIgnoreCase(dpObj.name())){
					//this is the mehod we are lo
					return m;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new AutomationError("Data-provider class " + dpc + " not found. Please provide fully qualified class name as dataProviderClass");
		}
		throw new AutomationError("Data-provider: '" + dp + "' not found in class: '"+ dpc+ "'. Please provide valid data provider name as dataProvider");
	}
	private TestStep[] getStepsToExecute(Map<String, Object> context) {
		TestStep[] proxySteps = new TestStep[steps.size()];
		int stepIndex = 0;
		for (TestStep testStep : steps) {

			StringTestStep proxy = new StringTestStep(testStep.getName(), context, testStep.getActualArgs());
			proxy.setLineNumber(testStep.getLineNumber());
			proxy.setFileName(testStep.getFileName());

			proxySteps[stepIndex++] = proxy;
		}

		return proxySteps;
	}
	
	private static class InvocatoinHelper extends MethodInvocationHelper{
		  protected static Iterator<Object[]> invokeDataProvider(Object instance, Method dataProvider,
			      ITestNGMethod method, ITestContext testContext, Object fedInstance,
			      IAnnotationFinder annotationFinder) {
			  return MethodInvocationHelper.invokeDataProvider(instance, dataProvider, method, testContext, fedInstance, annotationFinder);
		  }
	}

}
