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
package com.qmetry.qaf.automation.report;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.JSONUtil.getJsonObjectFromFile;
import static com.qmetry.qaf.automation.util.JSONUtil.writeJsonObjectToFile;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.integration.TestCaseResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseRunResult;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.testng.report.ClassInfo;
import com.qmetry.qaf.automation.testng.report.MetaInfo;
import com.qmetry.qaf.automation.testng.report.MethodInfo;
import com.qmetry.qaf.automation.testng.report.MethodResult;
import com.qmetry.qaf.automation.testng.report.Report;
import com.qmetry.qaf.automation.testng.report.ReportEntry;
import com.qmetry.qaf.automation.testng.report.ReporterUtil;
import com.qmetry.qaf.automation.testng.report.TestOverview;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * 
 * @author chirag.jayswal
 *
 */
public class JsonReporter implements TestCaseResultUpdator {
	private static final Log logger = LogFactoryImpl.getLog(JsonReporter.class);

	private static final String REPORT_DIR = ApplicationProperties.JSON_REPORT_DIR
			.getStringVal(ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results") + "/"
					+ DateUtil.getDate(0, "EdMMMyy_hhmmssa"));
	private static List<StatusCounter> suiteStatusCounters = new ArrayList<StatusCounter>();
	private static List<StatusCounter> testSetStatusCounters = new ArrayList<StatusCounter>();

	@Override
	public boolean updateResult(TestCaseRunResult result) {
		String suiteName = result.getExecutionInfo().getOrDefault("suiteName", "Default Suite").toString();
		String testName = StringUtil.toCamelCaseIdentifier((String) result.getExecutionInfo().getOrDefault("testName", "Default Set"));
		String suitReportDir = REPORT_DIR + "/" + StringUtil.toCamelCaseIdentifier(suiteName);
		String testReportDir = suitReportDir + "/" + testName;

		StatusCounter suiteStatusCounter = getStatusCounter(suiteStatusCounters,
				StatusCounter.of(suiteName).withFile(suitReportDir));
		StatusCounter testStatusCounter = getStatusCounter(testSetStatusCounters,
				StatusCounter.of(testName).withFile(testReportDir));
		suiteStatusCounter.add(result.getStatus());
		testStatusCounter.add(result.getStatus());

		// suite meta-info
		updateSuiteMetaData(result, suiteStatusCounter, testStatusCounter);

		// test overview
		updateTestOverView(result, testStatusCounter);
		
		addMethodResult(result, testStatusCounter);

		return true;
	}

	@Override
	public String getToolName() {
		return "QAF Json Reporter";
	}

	@Override
	public boolean allowConfigAndRetry() {
		return true;
	}

	@Override
	public boolean enabled() {
		return getBundle().getBoolean("disable.qaf.testng.reporter", false)
				&& getBundle().getBoolean("qaf.json.reporter", true);
	}

	@Override
	public boolean allowParallel() {
		return false;
	}

	public static List<StatusCounter> getSuiteStatusCounters() {
		return Collections.unmodifiableList(suiteStatusCounters) ;
	}
	
	public static List<StatusCounter> getTestSetStatusCounters() {
		return Collections.unmodifiableList(testSetStatusCounters);
	}
	
	private StatusCounter getStatusCounter(List<StatusCounter> statusCounters, StatusCounter statusCounter) {
		int index = statusCounters.indexOf(statusCounter);
		if (index >= 0) {
			return statusCounters.get(index);
		}
		statusCounters.add(statusCounter);
		FileUtil.checkCreateDir(statusCounter.toString());
		return statusCounter;
	}

	@SuppressWarnings("unchecked")
	private void addMethodResult(TestCaseRunResult result, 
			StatusCounter testStatusCounter) {
		// method details
		int index = 1;
		MethodResult methodResult = new MethodResult();
		methodResult.setSeleniumLog((List<LoggingBean>) result.getCommandLogs());
		methodResult.setCheckPoints((List<CheckpointResultBean>) result.getCheckPoints());
		methodResult.setThrowable(result.getThrowable());
	
		//calculate filename
		String identifierKey = ApplicationProperties.TESTCASE_IDENTIFIER_KEY.getStringVal("testCaseId");
		String methodResultFile = result.getMetaData().getOrDefault(identifierKey, "").toString();
		if (result.getTestData() != null && result.getTestData().size() > 0) {
			Object testData = result.getTestData().iterator().next();
			if (testData instanceof Map<?, ?>) {
				methodResultFile = ((Map<String, Object>) testData).getOrDefault(identifierKey, methodResultFile).toString();
				index = (int) ((Map<String, Object>) testData).getOrDefault("__index", 1);
			}
		}
		if (StringUtil.isBlank(methodResultFile)) {
			methodResultFile = result.getName();
		}
		methodResultFile = StringUtil.toTitleCaseIdentifier(methodResultFile);
		if (methodResultFile.length() > 45) {
			methodResultFile = methodResultFile.substring(0, 45);
		}
		
		String methodResultDir = testStatusCounter.toString() + "/" + result.getClassName();
		if (new File(methodResultDir, methodResultFile + ".json").exists()) {
			methodResultFile = methodResultFile + testStatusCounter.getTotal();
		}
		
		//write details
		writeJsonObjectToFile(methodResultDir + "/" + methodResultFile + ".json", methodResult);
	
		// update Class Meta Info meta-data
		String classInfoFile = methodResultDir + "/meta-info.json";
	
		ClassInfo classInfo = getJsonObjectFromFile(classInfoFile, ClassInfo.class);
	
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setStartTime(result.getStarttime());
		methodInfo.setDuration(result.getEndtime() - result.getStarttime());
		methodInfo.setArgs(result.getTestData().toArray());
		methodInfo.setIndex(index);
		methodInfo.setType(result.isTest() ? "test" : "config");
		methodInfo.setResult(result.getStatus().toQAF());
		
		Map<String, Object> metaData = result.getMetaData();
		metaData.put("name", result.getName());
		metaData.put("resultFileName", methodResultFile);
		methodInfo.setMetaData(metaData);
		int retryCount = (int) result.getExecutionInfo().getOrDefault("retryCount", 0);
		if (retryCount > 0) {
			methodInfo.setRetryCount(retryCount);
		}
		if (!classInfo.getMethods().contains(methodInfo)) {
			classInfo.getMethods().add(methodInfo);
			writeJsonObjectToFile(classInfoFile, classInfo);
		} else {
			logger.warn("methodInfo already wrritten for " + methodInfo.getName());
		}
	}

	private void updateSuiteMetaData(TestCaseRunResult result, StatusCounter suiteStatusCounter,
			StatusCounter testStatusCounter) {
		String file = suiteStatusCounter.toString() + "/meta-info.json";
		Report report;
		if (new File(file).exists()) {
			report = getJsonObjectFromFile(file, Report.class);
			report.getTests().add(testStatusCounter.getName());
		} else {
			report = new Report();
			report.setStartTime(result.getStarttime());
			Set<String> tests = new HashSet<String>();
			tests.add(testStatusCounter.getName());
			report.setTests(tests);
			report.setName(suiteStatusCounter.getName());

			ReportEntry reportEntry = new ReportEntry();
			reportEntry.setName(suiteStatusCounter.getName());
			reportEntry.setDir(suiteStatusCounter.toString());
			reportEntry.setStartTime(getBundle().getLong("execution.start.ts", result.getStarttime()));
	
			String reportMetaInfoFile = ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results")
					+ "/meta-info.json";
			MetaInfo metaInfo = getJsonObjectFromFile(reportMetaInfoFile, MetaInfo.class);
			// metaInfo.getReports().remove(reportEntry);
			metaInfo.getReports().add(reportEntry);
			writeJsonObjectToFile(reportMetaInfoFile, metaInfo);
		}
		report.setEndTime(result.getEndtime());
		report.setPass(suiteStatusCounter.getPass());
		report.setFail(suiteStatusCounter.getFail());
		report.setSkip(suiteStatusCounter.getSkip());
		report.setTotal(suiteStatusCounter.getTotal());
		report.setStatus(suiteStatusCounter.getStatus());
		writeJsonObjectToFile(file, report);
	}

	private void updateTestOverView(TestCaseRunResult result, StatusCounter testStatusCounter) {
		String file = testStatusCounter.toString() + "/overview.json";
		TestOverview testOverview;
		if (new File(file).exists()) {
			testOverview = getJsonObjectFromFile(file, TestOverview.class);
			testOverview.getClasses().add(result.getClassName());
			Object dc =  result.getExecutionInfo().get("driverCapabilities");
			if(null!=dc ) {
				testOverview.getEnvInfo().put("browser-desired-capabilities", getBundle().getObject("driver.desiredCapabilities"));
				testOverview.getEnvInfo().put("browser-actual-capabilities", dc);	
			}
		} else {
			testOverview = new TestOverview();
			testOverview.setStartTime(result.getStarttime());
			Set<String> classes = new HashSet<String>();
			classes.add(result.getClassName());
			testOverview.setClasses(classes);
	
			Map<String, Object> envInfo = new HashMap<String, Object>();
			Map<String, Object> executionEnvInfo = new HashMap<String, Object>();
			envInfo.put("execution-env-info", executionEnvInfo);
			testOverview.setEnvInfo(envInfo);
	
			envInfo.put("isfw-build-info", getBundle().getObject("isfw.build.info"));
			envInfo.put("run-parameters", result.getExecutionInfo().get("env"));
			Object dc =  result.getExecutionInfo().get("driverCapabilities");
			if(null!=dc ) {
				envInfo.put("browser-desired-capabilities", getBundle().getObject("driver.desiredCapabilities"));
				envInfo.put("browser-actual-capabilities", dc);	
			}
	
			executionEnvInfo.put("os.name", System.getProperty("os.name"));
			executionEnvInfo.put("os.version", System.getProperty("os.version"));
	
			executionEnvInfo.put("os.arch", System.getProperty("os.arch"));
			executionEnvInfo.put("java.version", System.getProperty("java.version"));
			executionEnvInfo.put("java.vendor", System.getProperty("java.vendor"));
			executionEnvInfo.put("java.arch", System.getProperty("sun.arch.data.model"));
	
			executionEnvInfo.put("user.name", System.getProperty("user.name"));
			try {
				executionEnvInfo.put("host", InetAddress.getLocalHost().getHostName());
			} catch (Exception e) {
				// This code added for MAC to fetch hostname
				String hostname = ReporterUtil.execHostName("hostname");
				executionEnvInfo.put("host", hostname);
			}
		}
		testOverview.setEndTime(result.getEndtime());
		testOverview.setPass(testStatusCounter.getPass());
		testOverview.setFail(testStatusCounter.getFail());
		testOverview.setSkip(testStatusCounter.getSkip());
		testOverview.setTotal(testStatusCounter.getTotal());
	
		writeJsonObjectToFile(file, testOverview);
	}
}
