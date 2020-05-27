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
package com.qmetry.qaf.automation.integration;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
/**
 * Bean Class that provides required details to {@link TestCaseResultUpdator}
 * 
 * @author chirag.jayswal
 */
public class TestCaseRunResult {
	private Collection<CheckpointResultBean> checkPoints;
	private Collection<LoggingBean> commandLogs;
	private Status status;
	private Map<String, Object> metaData;
	private Collection<Object> testData;
	private Collection<String> steps;
	private long starttime;
	private long endtime;
	//to hold execution details like suite name, XML test name, environment
	private Map<String, Object> executionInfo;
	//java class name or BDD file name
	private String className;
	private boolean willRetry;
	private boolean isTest;
	private Throwable throwable;


	public TestCaseRunResult(Status status, Map<String, Object> metaData, Object[] testData,
			Map<String, Object> executionInfo, Collection<String> steps, long starttime, boolean willRetry, boolean isTest) {
		this.endtime = System.currentTimeMillis();
		this.starttime = starttime;
		QAFTestBase testBase = TestBaseProvider.instance().get();
		checkPoints = new ArrayList<CheckpointResultBean>(testBase.getCheckPointResults());
		commandLogs = new ArrayList<LoggingBean>(testBase.getLog());
		this.status=status;
		this.willRetry=willRetry;
		this.isTest=isTest;
		this.metaData = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		try {
			metaData.values().removeAll(Collections.singleton(null));
			metaData.remove(params.JSON_DATA_TABLE.name());
		} catch (Throwable e) {
		}
		if (null != metaData) {
			this.metaData.putAll(metaData);
		}
		this.testData = new ArrayList<Object>();
		if (null != testData && testData.length>0) {
			this.testData.addAll(Arrays.asList(testData));
		}
		this.executionInfo = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		if (null != executionInfo) {
			this.executionInfo.putAll(executionInfo);
		}
		Map<String, String> cap = getActualCapabilities();
		if(!cap.isEmpty()) {
			this.executionInfo.put("driverCapabilities", cap);
		}
		this.steps = new ArrayList<String>(steps);
		
		//assign configuration from current thread which is running the test
	}

	public Collection<CheckpointResultBean> getCheckPoints() {
		return checkPoints;
	}

	public void setCheckPoints(Collection<CheckpointResultBean> checkPoints) {
		this.checkPoints = checkPoints;
	}

	public Collection<LoggingBean> getCommandLogs() {
		return commandLogs;
	}

	public void setCommandLogs(Collection<LoggingBean> commandLogs) {
		this.commandLogs = commandLogs;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData.clear();
		this.metaData.putAll(metaData);
	}

	public Collection<Object> getTestData() {
		return testData;
	}

	public void setTestData(Object[] testData) {
		this.testData.clear();
		if (null != testData && testData.length>0) {
			this.testData.addAll(Arrays.asList(testData));
		}
	}

	public Collection<String> getSteps() {
		return steps;
	}

	public void setSteps(Collection<String> steps) {
		this.steps.clear();
		this.steps.addAll(steps);
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String getClassName() {
		return className;
	}
	
	public String getName() {
		return (String) getMetaData().get("name");
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, Object> getExecutionInfo() {
		return executionInfo;
	}

	public void setExecutionInfo(Map<String, Object> executionInfo) {
		this.executionInfo.clear();
		this.executionInfo.putAll(executionInfo);
	}

	public boolean willRetry() {
		return willRetry;
	}

	public boolean isTest() {
		return isTest;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	/**
	 *
	 * Useful to convert result to test management tool specific string
	 * @author chirag.jayswal
	 *
	 */
	public enum Status {
		PASS("Pass", "pass", "Passed", "Passed","pass"), FAIL("Fail", "fail", "Failed", "Failed","fail"), SKIPPED("Not Testable",
				"notrun", "No Run", "Not Run","skip");
		private String rally, qmetry, qc, qmetry6,qaf;

		private Status(String toRally, String toQmetry, String toQC, String toQmetry6,String toQAF) {
			rally = toRally;
			qmetry = toQmetry;
			qmetry6 = toQmetry6;
			qc = toQC;
			qaf = toQAF;
		}

		public String toRally() {
			return rally;
		}

		public String toQmetry() {
			return qmetry;
		}

		public String toQC() {
			return qc;
		}

		public String toQmetry6() {
			return qmetry6;
		}
		
		public String toQAF() {
			return qaf;
		}
	}
	
	private static Map<String, String> getActualCapabilities() {
		@SuppressWarnings("unchecked")
		Map<String, Object> map =
				(Map<String, Object>) getBundle().getObject("driver.actualCapabilities");
		Map<String, String> newMap = new HashMap<String, String>();
		if (null != map) {
			for (String key : map.keySet()) {
				try {
					newMap.put(key, String.valueOf(map.get(key)));
				} catch (Exception e) {

				}
			}
		}
		return newMap;
	}
}
