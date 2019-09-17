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
package com.qmetry.qaf.automation.testng;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.StringUtil.toStringWithSufix;

import java.util.List;

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

			// correct failed invocation numbers for data driven test case.
			List<Integer> failedInvocations = result.getMethod().getFailedInvocationNumbers();
			if (null != failedInvocations && !failedInvocations.isEmpty()) {
				int lastFailedIndex = failedInvocations.size() - 1;
				failedInvocations.remove(lastFailedIndex);
			}

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
