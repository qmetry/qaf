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
import org.testng.SkipException;
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

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.HtmlCheckpointResultFormatter;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.SkipTestException;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDTestFactory;
import com.qmetry.qaf.automation.testng.MethodPriorityComparator;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.ReportUtils;
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
		} catch (Throwable e) {
			logger.debug(e.getStackTrace());
			logger.info("Unable to create testng-failed-qas.xml: " + e.getMessage());
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
						.getDeclaringClass().getName()
				+ " is test:" + method.isTestMethod());
		// if (method.isTestMethod()) {
		processResult(tr, context);
		// }

		logger.debug("afterInvocation: Done");
	}



	protected void report(ITestResult tr) {
		String[] groups = tr.getMethod().getGroups();
		if (null != groups && Arrays.asList(groups).contains("cucumber"))
			return;
		QAFTestBase stb = TestBaseProvider.instance().get();
		tr.setAttribute("browser", stb.getDriverName());

		org.testng.Reporter.setCurrentTestResult(tr);
		if (getBundle().getBoolean("report.log.testngoutput", false)) {
			HtmlCheckpointResultFormatter checkpointResultFormatter = new HtmlCheckpointResultFormatter();
			org.testng.Reporter.log(checkpointResultFormatter.getResults(stb.getCheckPointResults()));
		}
	}

	protected void processResult(ITestResult tr, ITestContext context) {
		QAFTestBase stb = TestBaseProvider.instance().get();
		int varificationFailures = stb.getVerificationErrors();
		if (varificationFailures > 0
				&& (tr.getStatus() == ITestResult.SUCCESS)) {
			setFailure(tr, context);
			if(tr.getThrowable()==null ){
				// Gradle runner internal listener is throwing null pointer exception!... 
				tr.setThrowable(new AssertionError(varificationFailures + " verification failed."));
			}
		}
		if(tr.getThrowable() != null && tr.getThrowable() instanceof SkipTestException) {
			SkipTestException ste = (SkipTestException)tr.getThrowable();
			tr.setThrowable(new SkipException(ste.getMessage(), ste.getSelfOrCause()));
			setSkip(tr, context);
		}

		if (tr.getStatus() == ITestResult.FAILURE) {
			ReportUtils.setScreenshot(tr.getThrowable());
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
					xmlSuite.setParallel(XmlSuite.ParallelMode.getValidParallel(suite.getParallel()));
					
					xmlSuite.setThreadCount(suite.getXmlSuite().getThreadCount());
					xmlSuite.setListeners(suite.getXmlSuite().getListeners());
					xmlSuite.setParameters(suite.getXmlSuite().getParameters());
				}
				XmlTest test = new XmlTest(xmlSuite);
				test.setName(suiteResult.getValue().getTestContext().getName());
				test.setPreserveOrder(true);
				test.setParameters(suiteResult.getValue().getTestContext()
						.getCurrentXmlTest().getLocalParameters());
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
				setScript(xmlScript,scriptDataPart);
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
	
	private void setScript(XmlScript xmlScript, String script) {
		try {
			Method setter = null;
			try {
				setter = ClassUtil.getMethod(XmlScript.class, "setScript");
			} catch (NoSuchMethodException e) {
				//tng - 7.1.1+
				setter = ClassUtil.getMethod(XmlScript.class, "setExpression");
			}
			setter.invoke(xmlScript, script);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
