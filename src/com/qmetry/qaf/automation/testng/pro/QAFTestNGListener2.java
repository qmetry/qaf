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

package com.qmetry.qaf.automation.testng.pro;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.testng.dataprovider.DataProviderFactory.getDataProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.IInvokedMethod;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.integration.ResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseRunResult;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.RetryAnalyzer;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.testng.dataprovider.QAFInetrceptableDataProvider;
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
				if ((method.getAnnotation(QAFDataProvider.class) != null) && (null != method.getParameterTypes())
						&& (method.getParameterTypes().length > 0)) {
					QAFInetrceptableDataProvider.setQAFDataProvider(testAnnotation, method);
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
		if (method.isTestMethod() && !shouldRetry(tr)) {
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
		QAFTestBase stb = TestBaseProvider.instance().get();

		try {
			if ((tr.getMethod() instanceof TestNGScenario) && ((tr.getStatus() == ITestResult.FAILURE)
					|| (tr.getStatus() == ITestResult.SUCCESS || tr.getStatus() == ITestResult.SKIP))) {

				ConstructorOrMethod testCase = tr.getMethod().getConstructorOrMethod();

				testCase.getMethod().getAnnotation(Test.class);
				TestCaseRunResult result = tr.getStatus() == ITestResult.SUCCESS ? TestCaseRunResult.PASS
						: tr.getStatus() == ITestResult.FAILURE ? TestCaseRunResult.FAIL : TestCaseRunResult.SKIPPED;

				// String method = testCase.getName();
				String updator = getBundle().getString("result.updator");

				if (StringUtil.isNotBlank(updator)) {
					Class<?> updatorCls = Class.forName(updator);

					TestCaseResultUpdator updatorObj = (TestCaseResultUpdator) updatorCls.newInstance();

					TestNGScenario scenario = (TestNGScenario) tr.getMethod();
					Map<String, Object> params = new HashMap<String, Object>(scenario.getMetaData());
					params.put("duration", tr.getEndMillis() - tr.getStartMillis());

					ResultUpdator.updateResult(result, stb.getHTMLFormattedLog() + stb.getAssertionsLog(), updatorObj,
							params);
				}

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
