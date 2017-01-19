/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.testng.report;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.JSONUtil.getJsonObjectFromFile;
import static com.qmetry.qaf.automation.util.JSONUtil.writeJsonObjectToFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.RetryAnalyzer;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.testng.report.ReporterUtil.java
 * 
 * @author chirag.jayswal
 */
public class ReporterUtil {
	private static final Log logger = LogFactoryImpl.getLog(ReporterUtil.class);

	public static void updateMetaInfo(ISuite suite) {
		createMetaInfo(suite, false);
	}

	public static void createMetaInfo(ISuite suite) {
		createMetaInfo(suite, true);

	}

	private static void createMetaInfo(ISuite suite, boolean listEntry) {
		List<XmlTest> tests = suite.getXmlSuite().getTests();
		List<String> testNames = new ArrayList<String>();
		for (XmlTest test : tests) {
			testNames.add(getTestName(test.getName()));
		}
		String dir = ApplicationProperties.JSON_REPORT_DIR.getStringVal();
		Report report = new Report();

		if (!getBundle().containsKey("suit.start.ts")) {
			dir = ApplicationProperties.JSON_REPORT_DIR
					.getStringVal(ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal(
							"test-results") + "/" + DateUtil.getDate(0, "EdMMMyy_hhmma"));
			getBundle().setProperty(ApplicationProperties.JSON_REPORT_DIR.key, dir);
			FileUtil.checkCreateDir(ApplicationProperties.JSON_REPORT_ROOT_DIR
					.getStringVal("test-results"));
			FileUtil.checkCreateDir(dir);
			getBundle().setProperty("suit.start.ts", System.currentTimeMillis());
		} else {
			report.setEndTime(System.currentTimeMillis());
		}
		report.setName(suite.getName());
		report.setTests(testNames);
		report.setDir(dir);

		int pass = 0, fail = 0, skip = 0, total = 0;
		Iterator<ISuiteResult> iter = suite.getResults().values().iterator();
		while (iter.hasNext()) {
			ITestContext context = iter.next().getTestContext();
			pass += getPassCnt(context);
			skip += getSkipCnt(context);
			fail += getFailCnt(context) + getFailWithPassPerCnt(context);
			total += getTotal(context);
		}
		report.setPass(pass);
		report.setFail(fail);
		report.setSkip(skip);
		report.setTotal((pass + fail + skip) > total ? pass + fail + skip : total);
		report.setStatus(fail > 0 ? "fail" : pass > 0 ? "pass" : "unstable");
		report.setStartTime(getBundle().getLong("suit.start.ts", 0));

		appendReportInfo(report);
		if (listEntry) {
			ReportEntry reportEntry = new ReportEntry();
			reportEntry.setName(suite.getName());
			reportEntry.setStartTime(getBundle().getLong("suit.start.ts", 0));
			reportEntry.setDir(dir);
			appendMetaInfo(reportEntry);
		}
	}

	public static synchronized void updateOverview(ITestContext context,
			ITestResult result) {
		try {
			String file = ApplicationProperties.JSON_REPORT_DIR.getStringVal() + "/"
					+ getTestName(context) + "/overview.json";
			TestOverview overview = getJsonObjectFromFile(file, TestOverview.class);
			if (null == result) {
				Map<String, Object> runPrams = new HashMap<String, Object>(
						context.getCurrentXmlTest().getAllParameters());
				Configuration env = getBundle().subset("env");
				Iterator<?> iter = env.getKeys();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					runPrams.put(key, env.getString(key));
				}
				Map<String, Object> envInfo = new HashMap<String, Object>();
				envInfo.put("isfw-build-info", getBundle().getObject("isfw.build.info"));
				envInfo.put("run-parameters", runPrams);
				envInfo.put("browser-desired-capabilities",
						getBundle().getObject("driver.desiredCapabilities"));
				envInfo.put("browser-actual-capabilities", getActualCapabilities());

				overview.setEnvInfo(envInfo);
				Map<String, Object> executionEnvInfo = new HashMap<String, Object>();
				executionEnvInfo.put("os.name", System.getProperty("os.name"));
				executionEnvInfo.put("os.version", System.getProperty("os.version"));

				executionEnvInfo.put("os.arch", System.getProperty("os.arch"));
				executionEnvInfo.put("java.version", System.getProperty("java.version"));
				executionEnvInfo.put("java.vendor", System.getProperty("java.vendor"));
				executionEnvInfo.put("java.arch",
						System.getProperty("sun.arch.data.model"));

				executionEnvInfo.put("user.name", System.getProperty("user.name"));
				try {
					executionEnvInfo.put("host",
							InetAddress.getLocalHost().getHostName());
				} catch (Exception e) {
					// This code added for MAC to fetch hostname
					String hostname = execHostName("hostname");
					executionEnvInfo.put("host", hostname);
				}

				envInfo.put("execution-env-info", executionEnvInfo);

			}

			int pass = getPassCnt(context);
			int fail = getFailCnt(context) + getFailWithPassPerCnt(context);
			int skip = getSkipCnt(context);
			int total = getTotal(context);

			overview.setTotal(total > (pass + fail + skip) ? total : pass + fail + skip);
			overview.setPass(pass);
			overview.setSkip(skip);
			overview.setFail(fail);
			if (null != result) {
				overview.getClasses().add(result.getTestClass().getName());
			}
			if ((overview.getStartTime() > 0)) {
				overview.setEndTime(System.currentTimeMillis());
			} else {
				overview.setStartTime(System.currentTimeMillis());
			}
			writeJsonObjectToFile(file, overview);
			updateMetaInfo(context.getSuite());
		} catch (Exception e) {
			logger.debug(e);
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

	/**
	 * should be called on test method completion
	 * 
	 * @param context
	 * @param result
	 */
	public static void createMethodResult(ITestContext context, ITestResult result,
			List<LoggingBean> logs, List<CheckpointResultBean> checkpoints) {

		try {
			String dir = getClassDir(context, result);

			MethodResult methodResult = new MethodResult();

			methodResult.setSeleniumLog(logs);
			methodResult.setCheckPoints(checkpoints);
			methodResult.setThrowable(result.getThrowable());

			updateOverview(context, result);

			String fileName = getMethodName(result);
			String methodResultFile = dir + "/" + fileName;

			File f = new File(methodResultFile + ".json");
			if (f.exists()) {
				// if file already exists then it will append some random
				// character as suffix
				fileName += StringUtil.getRandomString("aaaaaaaaaa");
				// add updated file name as 'resultFileName' key in metaData
				methodResultFile = dir + "/" + fileName;
				updateClassMetaInfo(context, result, fileName);
			} else {
				updateClassMetaInfo(context, result, "");
			}

			writeJsonObjectToFile(methodResultFile + ".json", methodResult);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

	}

	/**
	 * should be called on test method completion
	 * 
	 * @param context
	 * @param result
	 */
	private static synchronized void updateClassMetaInfo(ITestContext context,
			ITestResult result, String methodfname) {
		String dir = getClassDir(context, result);
		String file = dir + "/meta-info.json";
		FileUtil.checkCreateDir(dir);

		ClassInfo classInfo = getJsonObjectFromFile(file, ClassInfo.class);

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setStartTime(result.getStartMillis());
		methodInfo.setDuration(result.getEndMillis() - result.getStartMillis());

		if (result.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
			// methodResult.setPassPer(result.getMethod().getCurrentInvocationCount())
		}
		Map<String, Object> metadata;
		if (result.getMethod().isTest()) {
			methodInfo.setIndex(result.getMethod().getCurrentInvocationCount());
			int retryCount = getBundle().getInt(RetryAnalyzer.RETRY_INVOCATION_COUNT, 0);
			if (retryCount > 0) {
				methodInfo.setRetryCount(retryCount);
			}
			methodInfo.setArgs(result.getParameters());

			if (result.getMethod() instanceof TestNGScenario) {
				TestNGScenario scenario = (TestNGScenario) result.getMethod();
				metadata = scenario.getMetaData();
				metadata.put("description", scenario.getDescription());
				metadata.put("groups", scenario.getGroups());
			} else {
				String desc = ApplicationProperties.CURRENT_TEST_DESCRIPTION
						.getStringVal(result.getMethod().getDescription());
				metadata = new HashMap<String, Object>();
				metadata.put("groups", result.getMethod().getGroups());
				metadata.put("description", desc);
			}
			metadata.put("name", getMethodName(result));
			methodInfo.setMetaData(metadata);
			getBundle().clearProperty(ApplicationProperties.CURRENT_TEST_DESCRIPTION.key);

			Test test = result.getMethod().getConstructorOrMethod().getMethod()
					.getAnnotation(Test.class);
			if (((test.dependsOnMethods() != null)
					&& (test.dependsOnMethods().length > 0))
					|| ((test.dependsOnGroups() != null)
							&& (test.dependsOnGroups().length > 0))) {
				String[] depends =
						{"Methods: " + Arrays.toString(test.dependsOnMethods()),
								"Groups: " + Arrays.toString(test.dependsOnGroups())};
				methodInfo.setDependsOn(depends);
			}
			methodInfo.setType("test");
		} else { // config method
			String name = getMethodName(result);
			logger.debug("config method:  " + name);

			metadata = new HashMap<String, Object>();
			metadata.put("groups", result.getMethod().getGroups());
			metadata.put("description", result.getMethod().getDescription());
			metadata.put("name", name);

			methodInfo.setMetaData(metadata);
			methodInfo.setType("config");

		}
		methodInfo.setResult(getResult(result.getStatus()));

		if (StringUtil.isNotBlank(methodfname)) {
			metadata.put("resultFileName", methodfname);
		}

		if (!classInfo.getMethods().contains(methodInfo)) {
			logger.debug("method:  result: " + methodInfo.getResult() + " groups: "
					+ methodInfo.getMetaData());
			classInfo.getMethods().add(methodInfo);
			writeJsonObjectToFile(file, classInfo);
		} else {
			logger.warn("methodInfo already wrritten for " + methodInfo.getName());
		}

	}

	private static String getMethodName(ITestResult result) {
		return result.getName();
	}

	private static String getClassDir(ITestContext context, ITestResult result) {
		String testName = getTestName(context);
		return ApplicationProperties.JSON_REPORT_DIR.getStringVal() + "/" + testName + "/"
				+ result.getTestClass().getName();
	}

	private static void appendReportInfo(Report report) {

		String file = report.getDir() + "/meta-info.json";
		writeJsonObjectToFile(file, report);
	}

	private static void appendMetaInfo(ReportEntry report) {

		String file =
				ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results")
						+ "/meta-info.json";
		MetaInfo metaInfo = getJsonObjectFromFile(file, MetaInfo.class);
		metaInfo.getReports().remove(report);
		metaInfo.getReports().add(report);
		writeJsonObjectToFile(file, metaInfo);
	}

	private static String getResult(int res) {
		switch (res) {
			case ITestResult.SUCCESS :
				return "pass";
			case ITestResult.FAILURE :
				return "fail";
			case ITestResult.SKIP :
				return "skip";
			case ITestResult.SUCCESS_PERCENTAGE_FAILURE :
				return "pass";
			default :
				return "";
		}
	}

	private static String getTestName(ITestContext context) {
		if (context == null) {
			context = (ITestContext) ConfigurationManager.getBundle()
					.getObject(ApplicationProperties.CURRENT_TEST_CONTEXT.key);
		}
		return getTestName(context.getName());

	}

	private static int getPassCnt(ITestContext context) {
		if ((context != null) && (context.getPassedTests() != null)) {
			if (context.getPassedTests().getAllResults() != null) {
				return context.getPassedTests().getAllResults().size();
			}
			return context.getPassedTests().size();
		}
		return 0;
	}

	private static int getFailCnt(ITestContext context) {
		int failCnt = 0;

		if ((context != null) && (context.getFailedTests() != null)) {
			Collection<ITestNGMethod> allFailedTests =
					context.getFailedTests().getAllMethods();
			// get unique failed test methods
			Set<ITestNGMethod> set = new HashSet<ITestNGMethod>(allFailedTests);
			if (ApplicationProperties.RETRY_CNT.getIntVal(0) > 0)
				set.removeAll(context.getPassedTests().getAllMethods());
			Iterator<ITestNGMethod> iter = set.iterator();

			while (iter.hasNext()) {
				ITestNGMethod m = iter.next();
				// get failed invocations (remove duplicate because of retry)
				// for data driven, in case not data driven test invocations
				// will be 0
				Set<Integer> invocationNumbers =
						new HashSet<Integer>(m.getFailedInvocationNumbers());
				int invocatons = invocationNumbers.size();
				failCnt += invocatons > 0 ? invocatons : 1;
			}
		}
		return failCnt;
	}

	private static int getFailWithPassPerCnt(ITestContext context) {
		if ((context != null)
				&& (context.getFailedButWithinSuccessPercentageTests() != null)) {
			if (context.getFailedButWithinSuccessPercentageTests()
					.getAllResults() != null) {
				return context.getFailedButWithinSuccessPercentageTests().getAllResults()
						.size();
			}
			return context.getFailedButWithinSuccessPercentageTests().size();
		}
		return 0;
	}

	private static int getSkipCnt(ITestContext context) {
		if ((context != null) && (context.getSkippedTests() != null)) {
			if (context.getSkippedTests().getAllResults() != null) {
				return context.getSkippedTests().getAllResults().size();
			}
			return context.getSkippedTests().size();
		}
		return 0;
	}

	private static int getTotal(ITestContext context) {

		return (context == null) || (null == context.getAllTestMethods()) ? 0
				: context.getAllTestMethods().length;

	}

	private static String getTestName(String testname) {
		return StringUtil.isBlank(testname) ? ""
				: testname.replaceAll("[^a-zA-Z0-9_]+", "");
	}

	public static String execHostName(String execCommand) {
		InputStream stream;
		Scanner s;
		try {
			Process proc = Runtime.getRuntime().exec(execCommand);
			stream = proc.getInputStream();
			if (stream != null) {
				s = new Scanner(stream);
				s.useDelimiter("\\A");
				String val = s.hasNext() ? s.next() : "";
				stream.close();
				s.close();
				return val;
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return "";
	}
}
