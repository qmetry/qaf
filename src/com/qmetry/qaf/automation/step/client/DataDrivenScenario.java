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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;

/**
 * com.qmetry.qaf.automation.step.client.csv.CSVTest.java
 * 
 * @author chirag.jayswal
 */
public class DataDrivenScenario extends Scenario {

	public DataDrivenScenario(String testName, Collection<TestStep> steps) {
		super(testName, steps);
	}

	public DataDrivenScenario(String testName, Collection<TestStep> steps,
			Map<String, Object> metadata) {
		super(testName, steps, metadata);
	}

	@Override
	@Test(enabled = false)
	public void scenario() {

	}

	@QAFDataProvider
	@Test(groups = "scenario")
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
	
}
