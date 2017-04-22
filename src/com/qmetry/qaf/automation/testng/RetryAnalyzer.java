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


package com.qmetry.qaf.automation.testng;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.StringUtil.toStringWithSufix;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.StepInvocationException;
import com.qmetry.qaf.automation.step.client.TestNGScenario;

/**
 * com.qmetry.qaf.automation.testng.RetryAnalyzer.java
 * 
 * @author chirag.jayswal
 */
public class RetryAnalyzer implements IRetryAnalyzer {
	public static String RETRY_INVOCATION_COUNT = "retry.invocation.count";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 */
	@Override
	public boolean retry(ITestResult result) {

		boolean shouldRetry = shouldRetry(result);
		if (shouldRetry) {
			try {
				if (result.getMethod() instanceof TestNGScenario) {
					((TestNGScenario) result.getMethod()).decAndgetCurrentInvocationCount();
				}
			} catch (Exception e) {
				System.err.println(e);
			}
			int retryInvocationCount = getRetryCount() + 1;
			System.err.println(
					"Retrying [" + result.getName() + "]" + toStringWithSufix(retryInvocationCount) + " time.");

			getBundle().addProperty(RETRY_INVOCATION_COUNT, retryInvocationCount);

		} else {
			getBundle().clearProperty(RETRY_INVOCATION_COUNT);
		}

		return shouldRetry;
	}

	public boolean shouldRetry(ITestResult result) {
		Throwable reason = result.getThrowable();
		int retryCount = getRetryCount();
		boolean shouldRetry = (result.getStatus() == ITestResult.FAILURE) && reason != null
				&& !(reason instanceof AutomationError || reason instanceof StepInvocationException
						|| reason instanceof AssertionError)
				&& (ApplicationProperties.RETRY_CNT.getIntVal(0) > retryCount);

		return shouldRetry;
	}

	protected int getRetryCount() {
		return getBundle().getInt(RETRY_INVOCATION_COUNT, 0);
	}
}
