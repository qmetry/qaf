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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.IInvokedMethod;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Parameters;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.integration.ResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseRunResult;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.RetryAnalyzer;
import com.qmetry.qaf.automation.testng.dataprovider.DataProviderUtil;
import com.qmetry.qaf.automation.testng.report.ReporterUtil;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * All in one Listener for ISFW. If this listener is added, you don't required
 * to add any other ISFW specific listener.
 * 
 * @author Chirag Jayswal.
 */
public class QAFTestNGListener2 extends QAFTestNGListener
// implements
// IAnnotationTransformer2,
// IMethodInterceptor,
// IResultListener,
// ISuiteListener,
// IInvokedMethodListener2,
// IMethodSelector
{
	private final Log logger = LogFactoryImpl.getLog(getClass());

	public QAFTestNGListener2() {
		logger.debug("QAFTestNGListener registered!...");

	}

	@Override
	public void onStart(final ISuite suite) {
		if (skipReporting())
			return;
		super.onStart(suite);
		ReporterUtil.createMetaInfo(suite);
	}

	@Override
	public void onFinish(ISuite suite) {
		if (skipReporting())
			return;
		super.onFinish(suite);
		logger.debug("onFinish: start");
		ReporterUtil.createMetaInfo(suite);
		logger.debug("onFinish: done");

	}

	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		if (!skipReporting()) {
			ReporterUtil.updateOverview(testContext, null);
		}
	}

	@Override
	public void onFinish(ITestContext testContext) {
		if (skipReporting())
			return;

		super.onFinish(testContext);
		ReporterUtil.updateOverview(testContext, null);
	}

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation testAnnotation, Class clazz, Constructor arg2, Method method) {
		try {
			if (null != method) {
				if (null != method.getParameterTypes() && (method.getParameterTypes().length > 0)
						&& !method.isAnnotationPresent(Parameters.class)) {
					DataProviderUtil.setQAFDataProvider(testAnnotation, method);
				}

				String tmtURL = getBundle().getString(method.getName() + ".testspec.url");
				if (StringUtil.isNotBlank(tmtURL)) {
					String desc = String.format("%s<br/><a href=\"%s\">[test-spec]</a>",
							testAnnotation.getDescription(), tmtURL);
					testAnnotation.setDescription(desc);
				}
				if (getBundle().getBoolean("report.javadoc.link", false)) {
					String linkRelPath = String.format("%s%s.html#%s",
							getBundle().getString("javadoc.folderpath", "../../../docs/tests/"),
							method.getDeclaringClass().getCanonicalName().replaceAll("\\.", "/"),
							ClassUtil.getMethodSignture(method, false));

					String desc = String.format(
							"%s " + getBundle().getString("report.javadoc.link.format",
									"<a href=\"%s\" target=\"_blank\">[View-doc]</a>"),
							testAnnotation.getDescription(), linkRelPath);
					testAnnotation.setDescription(desc);
				}
				testAnnotation.setDescription(getBundle().getSubstitutor().replace(testAnnotation.getDescription()));

				testAnnotation.setRetryAnalyzer(Class
						.forName(ApplicationProperties.RETRY_ANALYZER.getStringVal(RetryAnalyzer.class.getName())));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterInvocation(final IInvokedMethod method, final ITestResult tr, final ITestContext context) {
		super.afterInvocation(method, tr, context);
		// pro
		if (!skipReporting()) {
			deployResult(tr, context);
		}
	}

	private boolean shouldRetry(ITestResult tr) {
		IRetryAnalyzer retryAnalyzer = tr.getMethod().getRetryAnalyzer();
		boolean shouldRetry = false;
		if (null != retryAnalyzer && retryAnalyzer instanceof RetryAnalyzer) {
			shouldRetry = (((RetryAnalyzer) retryAnalyzer).shouldRetry(tr));
		}
		return shouldRetry;
	}

	@Override
	protected void report(ITestResult tr) {
		super.report(tr);
		if (skipReporting())
			return;
		QAFTestBase stb = TestBaseProvider.instance().get();
		final List<CheckpointResultBean> checkpoints = new ArrayList<CheckpointResultBean>(stb.getCheckPointResults());

		// pro
		final List<LoggingBean> logs = new ArrayList<LoggingBean>(stb.getLog());
		ITestContext testContext = (ITestContext) tr.getAttribute("context");
		ReporterUtil.createMethodResult(testContext, tr, logs, checkpoints);
		if (tr.getStatus() != ITestResult.SKIP) {
			getBundle().clearProperty(RetryAnalyzer.RETRY_INVOCATION_COUNT);
		}
	}

	private void deployResult(ITestResult tr, ITestContext context) {
		try {
			if (ResultUpdator.getResultUpdatorsCnt()>0 && (tr.getMethod() instanceof TestNGScenario) && ((tr.getStatus() == ITestResult.FAILURE)
					|| (tr.getStatus() == ITestResult.SUCCESS || tr.getStatus() == ITestResult.SKIP))) {

				TestCaseRunResult.Status status = tr.getStatus() == ITestResult.SUCCESS ? TestCaseRunResult.Status.PASS
						: tr.getStatus() == ITestResult.FAILURE ? TestCaseRunResult.Status.FAIL
								: TestCaseRunResult.Status.SKIPPED;

				TestNGScenario scenario = (TestNGScenario) tr.getMethod();
				Map<String, Object> params = new HashMap<String, Object>(scenario.getMetaData());
				params.put("duration", tr.getEndMillis() - tr.getStartMillis());

				Map<String, Object> executionInfo = new HashMap<String, Object>();
				executionInfo.put("testName", tr.getTestContext().getCurrentXmlTest().getName());
				executionInfo.put("suiteName", tr.getTestContext().getSuite().getName());
				executionInfo.put("env", ConfigurationConverter.getMap(getBundle().subset("env")));

				TestCaseRunResult testCaseRunResult = new TestCaseRunResult(status, scenario.getMetaData(),
						tr.getParameters(), executionInfo, scenario.getSteps(), tr.getStartMillis(),shouldRetry(tr),scenario.isTest() );
				testCaseRunResult.setClassName(scenario.getClassOrFileName());
				if (scenario.getGroups() != null && scenario.getGroups().length > 0) {
					testCaseRunResult.getMetaData().put("groups", scenario.getGroups());
				}
				testCaseRunResult.getMetaData().put("description",scenario.getDescription());
				testCaseRunResult.setThrowable(tr.getThrowable());
				ResultUpdator.updateResult(testCaseRunResult);
			}
		} catch (Exception e) {
			logger.warn("Unable to deploy result", e);
		}
	}

	private boolean skipReporting() {
		return getBundle().getBoolean("disable.qaf.testng.reporter", false)
				|| getBundle().getBoolean("cucumber.run.mode", false);
	}
}
