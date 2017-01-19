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

package com.qmetry.qaf.automation.testng.pro;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.IInvokedMethod;
import org.testng.IMethodInstance;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlMethodSelector;
import org.testng.xml.XmlMethodSelectors;
import org.testng.xml.XmlScript;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.HtmlCheckpointResultFormatter;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDTestFactory;
import com.qmetry.qaf.automation.testng.MethodPriorityComparator;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * All in one Listener for ISFW.
 * 
 * @author Chirag Jayswal.
 */
public class QAFTestNGListener {
	// original listener is spited to separate classes to fix multiple time
	// execution of listener method
	private final Log logger = LogFactoryImpl.getLog(getClass());

	public void onStart(final ISuite suite) {
	}

	public void onFinish(ISuite suite) {
		try {
			generateTestFailedXML(suite);
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
			logger.info("Unable to create testng-failed-qas.xml");
		}
	}

	public void onTestStart(ITestResult paramITestResult) {
		logger.debug("onTestStart: start");

	}

	public void onStart(ITestContext testContext) {
	}

	public void onFinish(ITestContext testContext) {
		logger.debug("onfinish testcntext: start");

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
		onTestFailure(tr);
	}

	public void onTestFailure(ITestResult tr) {
		report(tr);
	}

	public void onTestSkipped(ITestResult tr) {
		report(tr);
	}

	public void onTestSuccess(ITestResult tr) {
		report(tr);
	}

	public void onConfigurationFailure(ITestResult itr) {
		// report all configuration failures even from the ISFW
		report(itr);
	}

	public void onConfigurationSkip(ITestResult itr) {
		if (!itr.getMethod().getConstructorOrMethod().getDeclaringClass().getPackage()
				.getName().startsWith("com.qmetry.qaf.automation")) {
			report(itr);
		}
	}

	public void onConfigurationSuccess(ITestResult itr) {
		if (!itr.getMethod().getConstructorOrMethod().getDeclaringClass().getPackage()
				.getName().startsWith("com.qmetry.qaf.automation")) {
			report(itr);
		}
	}

	public void transform(IDataProviderAnnotation arg0, Method method) {
		boolean parallel = false;
		if (getBundle().containsKey(method.getName() + ".parallel")) {
			parallel = getBundle().getBoolean(method.getName() + ".parallel");
		} else {
			parallel = getBundle().getBoolean("global.datadriven.parallel", false);
		}

		arg0.setParallel(parallel);
	}

	public void transform(IFactoryAnnotation factory, Method method) {
	}

	@SuppressWarnings("rawtypes")
	public void transform(IConfigurationAnnotation iConfigurationAnnotation, Class arg1,
			Constructor arg2, Method arg3) {
	}

	public List<IMethodInstance> intercept(List<IMethodInstance> lst,
			ITestContext context) {
		logger.debug("Method Order interceptor called");
		String order = context.getCurrentXmlTest().getParameter("groupOrder");
		MethodPriorityComparator comparator = new MethodPriorityComparator(order);
		Collections.sort(lst, comparator);
		return lst;
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult tr,
			ITestContext context) {
		QAFTestBase stb = TestBaseProvider.instance().get();
		stb.getLog().clear();
		stb.clearVerificationErrors();
		stb.getCheckPointResults().clear();
		logger.debug("beforeInvocation: " + method.getTestMethod().getMethodName());
		tr.setAttribute("context", context);
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.CURRENT_TEST_CONTEXT.key, context);

		ConfigurationManager.getBundle().setProperty(ApplicationProperties.CURRENT_TEST_NAME.key,
				tr.getName());
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.CURRENT_TEST_DESCRIPTION.key,
				tr.getMethod().getDescription());
		ConfigurationManager.getBundle().setProperty(ApplicationProperties.CURRENT_TEST_RESULT.key,
				tr);
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult tr) {
	}

	public void afterInvocation(IInvokedMethod method, ITestResult tr) {

	}

	public void afterInvocation(final IInvokedMethod method, final ITestResult tr,
			final ITestContext context) {
		logger.debug("afterInvocation: " + method.getTestMethod().getMethodName()
				+ " - " + method.getTestMethod().getConstructorOrMethod()
						.getDeclaringClass().getPackage().getName()
				+ " is test:" + method.isTestMethod());
		// if (method.isTestMethod()) {
		processResult(tr, context);
		// }

		logger.debug("afterInvocation: Done");
	}

	public CheckpointResultBean getLastFailedCheckpointResultBean(
			List<CheckpointResultBean> checkPointResults) {
		if ((null == checkPointResults) || checkPointResults.isEmpty()) {
			return null;
		}

		// There may be not run check point at the end. Visit form bottom, we
		// need to visit up to failure check point found.
		for (int index = checkPointResults.size() - 1; index >= 0; index--) {
			CheckpointResultBean checkpointResultBean = checkPointResults.get(index);
			MessageTypes type = MessageTypes.valueOf(checkpointResultBean.getType());
			if (type.isFailure())
				return checkpointResultBean;
		}

		return null;
	}

	protected void report(ITestResult tr) {
		String[] groups = tr.getMethod().getGroups();
		if (null != groups && Arrays.asList(groups).contains("cucumber"))
			return;
		QAFTestBase stb = TestBaseProvider.instance().get();
		tr.setAttribute("browser", stb.getBrowser());
		final List<CheckpointResultBean> checkpoints =
				new ArrayList<CheckpointResultBean>(stb.getCheckPointResults());

		org.testng.Reporter.setCurrentTestResult(tr);

		HtmlCheckpointResultFormatter checkpointResultFormatter =
				new HtmlCheckpointResultFormatter();
		org.testng.Reporter.log(checkpointResultFormatter.getResults(checkpoints));
	}

	protected void processResult(ITestResult tr, ITestContext context) {
		QAFTestBase stb = TestBaseProvider.instance().get();

		if ((stb.getVerificationErrors() > 0)
				&& (tr.getStatus() == ITestResult.SUCCESS)) {
			setFailure(tr, context);
		}

		if (tr.getStatus() == ITestResult.FAILURE) {
			String failiremsg = getFailureMessage(tr.getThrowable());
			CheckpointResultBean lastFailedChkPoint =
					getLastFailedCheckpointResultBean(stb.getCheckPointResults());

			// not an assertion of verification failure
			if (null != lastFailedChkPoint) {
				// ensure last failed check-point has screenshot

				if (StringUtil.isBlank(lastFailedChkPoint.getScreenshot())) {
					// get last failed sub-checkpoint
					CheckpointResultBean lastFailedSubChkPoint =
							getLastFailedCheckpointResultBean(
									lastFailedChkPoint.getSubCheckPoints());

					if (lastFailedSubChkPoint != null && StringUtil
							.isNotBlank(lastFailedSubChkPoint.getScreenshot())) {
						lastFailedChkPoint
								.setScreenshot(lastFailedSubChkPoint.getScreenshot());

					} else {
						stb.takeScreenShot();
						lastFailedChkPoint.setScreenshot(stb.getLastCapturedScreenShot());
					}
				}
			} else if (StringUtil.isNotBlank(failiremsg)) {
				logger.error(tr.getThrowable());

				// stb.addAssertionLogWithScreenShot(failiremsg,
				// MessageTypes.Fail);
				stb.takeScreenShot();
				CheckpointResultBean stepResultBean = new CheckpointResultBean();
				stepResultBean.setMessage(failiremsg);
				stepResultBean.setType(MessageTypes.Fail);
				stepResultBean.setScreenshot(stb.getLastCapturedScreenShot());
				stb.getCheckPointResults().add(stepResultBean);

			}
			// discontinue support for "selenium.wait.failure.setskip". Use QAS
			// listener instead
			// if ((tr.getThrowable() instanceof WaitTimedOutException)
			// && (getBundle().getBoolean("selenium.wait.failure.setskip"))) {
			// setSkip(tr, context);
			// }
		}
	}

	protected void setFailure(ITestResult tr, ITestContext context) {

		if (getBundle().getInt("testng.version", 6) > 5) {
			// Fix for testNG 6
			// IResultMap resultMap = context.getPassedTests();
			// resultMap.
			if ((null != context.getPassedTests())) {

				if (context.getPassedTests().getResults(tr.getMethod()).size() > 1) {
					context.getPassedTests().removeResult(tr);
				} else {
					context.getPassedTests().removeResult(tr.getMethod());
				} // removeResult(tr.getMethod());
			}
			tr.setStatus(ITestResult.FAILURE);
			if (null != context.getFailedTests()) {
				context.getFailedTests().addResult(tr, tr.getMethod());
			}
		} else {
			tr.setStatus(ITestResult.FAILURE);

		}
	}

	protected void setSkip(ITestResult tr, ITestContext context) {

		if (getBundle().getInt("testng.version", 6) > 5) {

			// Fix for testNG 6
			if ((null != context.getFailedTests())) {
				if (((null != context.getFailedTests().getResults(tr.getMethod())))
						&& (context.getFailedTests().getResults(tr.getMethod())
								.size() > 1)) {
					context.getFailedTests().getResults(tr.getMethod()).remove(tr);
				} else {
					context.getFailedTests().removeResult(tr.getMethod());
				}
			}
			tr.setStatus(ITestResult.SKIP);
			if (null != context.getSkippedTests()) {
				context.getSkippedTests().addResult(tr, tr.getMethod());
			}
		} else {
			tr.setStatus(ITestResult.SKIP);
		}
	}

	/**
	 * @param suite
	 * @throws IOException
	 *             This method creates testng-failed-qas.xml file, which is
	 *             useful to rerun failed testcases for BDD scenarios.
	 */
	private void generateTestFailedXML(ISuite suite) throws IOException {
		Map<String, ISuiteResult> suiteResults = suite.getResults();
		XmlSuite xmlSuite = null;
		for (Entry<String, ISuiteResult> suiteResult : suiteResults.entrySet()) {
			IResultMap failedTests =
					suiteResult.getValue().getTestContext().getFailedTests();
			IResultMap skippedTests =
					suiteResult.getValue().getTestContext().getSkippedTests();
			Set<ITestNGMethod> failedOrSkipppedMethods = new HashSet<ITestNGMethod>();
			failedOrSkipppedMethods.addAll(failedTests.getAllMethods());
			failedOrSkipppedMethods.addAll(skippedTests.getAllMethods());
			if (!failedOrSkipppedMethods.isEmpty() && Scenario.class.isAssignableFrom(
					failedOrSkipppedMethods.iterator().next().getRealClass())) {
				if (xmlSuite == null) {
					xmlSuite = new XmlSuite();
					xmlSuite.setName(suite.getName());
					xmlSuite.setParallel(suite.getParallel());
					xmlSuite.setThreadCount(suite.getXmlSuite().getThreadCount());
					xmlSuite.setListeners(suite.getXmlSuite().getListeners());
					xmlSuite.setParameters(suite.getXmlSuite().getParameters());
				}
				XmlTest test = new XmlTest(xmlSuite);
				test.setName(suiteResult.getValue().getTestContext().getName());
				test.setPreserveOrder("true");
				test.setParameters(suiteResult.getValue().getTestContext()
						.getCurrentXmlTest().getTestParameters());
				XmlMethodSelectors methodSelectors = new XmlMethodSelectors();
				XmlMethodSelector methodSelector = new XmlMethodSelector();
				methodSelectors.setMethodSelector(methodSelector);
				XmlScript xmlScript = new XmlScript();
				xmlScript.setLanguage("beanshell");
				String scriptDataPart = "";
				for (ITestNGMethod testMethod : failedOrSkipppedMethods) {
					if (Scenario.class.isAssignableFrom(testMethod.getRealClass())) {
						if (StringUtil.isNotEmpty(scriptDataPart)) {
							scriptDataPart = scriptDataPart + "||";
						}
						if (testMethod.getInstance() instanceof Scenario) {
							Scenario scenario = (Scenario) testMethod.getInstance();
							scriptDataPart = scriptDataPart + String.format(
									"testngMethod.getMethodName().equalsIgnoreCase(\"%s\")",
									scenario.getTestName());
						}
					}
				}
				xmlScript.setScript(scriptDataPart);
				methodSelector.setScript(xmlScript);
				ArrayList<XmlMethodSelector> selectors =
						new ArrayList<XmlMethodSelector>();
				selectors.add(methodSelector);
				XmlClass testClass = null;
				ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
				ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
				testClass = new XmlClass();
				testClass.setName(BDDTestFactory.class.getName());
				testClass.setIncludedMethods(methodsToRun);
				classes.add(testClass);
				test.setMethodSelectors(selectors);
				test.setXmlClasses(classes);
			}
		}
		if (xmlSuite != null) {
			File file = new File(suite.getOutputDirectory().replace(suite.getName(), "")
					+ "testng-failed-qas.xml");
			FileWriter writer = new FileWriter(file);
			writer.write(xmlSuite.toXml());
			writer.close();
		}
	}

	private String getFailureMessage(Throwable t) {
		if (null == t)
			return "";
		String msg = t.getMessage();
		if (StringUtil.isNotBlank(msg))
			return msg;
		return t.toString();
	}

}
