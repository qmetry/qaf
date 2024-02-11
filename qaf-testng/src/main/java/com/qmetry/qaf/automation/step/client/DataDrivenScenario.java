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
