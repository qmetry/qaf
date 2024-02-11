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
package com.qmetry.qaf.automation.core;

import java.util.List;

import org.testng.ITestContext;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.QAFTestStepListener;
import com.qmetry.qaf.automation.step.StepExecutionTracker;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProviderIntercepter;
import com.qmetry.qaf.automation.ui.webdriver.CommandTracker;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriverCommandAdapter;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriverCommandListener;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElementCommandListener;
import com.qmetry.qaf.automation.util.PropertyUtil;

/**
 * Adapter class for all QAF listeners. Register listener using
 * {@link ApplicationProperties#QAF_LISTENERS}
 * 
 * @author chirag.jayswal
 */
public class QAFListenerAdapter extends QAFWebDriverCommandAdapter
		implements
			QAFWebElementCommandListener,
			QAFWebDriverCommandListener,
			QAFTestStepListener, QAFDataProviderIntercepter, QAFConfigurationListener {

	@Override
	public void onFailure(StepExecutionTracker stepExecutionTracker) {

	}

	@Override
	public void beforExecute(StepExecutionTracker stepExecutionTracker) {

	}

	@Override
	public void afterExecute(StepExecutionTracker stepExecutionTracker) {

	}

	@Override
	public void beforeCommand(QAFExtendedWebElement element,
			CommandTracker commandTracker) {

	}

	@Override
	public void afterCommand(QAFExtendedWebElement element,
			CommandTracker commandTracker) {

	}

	@Override
	public void onFailure(QAFExtendedWebElement element, CommandTracker commandTracker) {

	}

	@Override
	public List<Object[]> intercept(TestNGScenario scenario, ITestContext context, List<Object[]> testdata) {
		return testdata;
	}

	@Override
	public void beforeFech(TestNGScenario scenario, ITestContext context) {
		
	}

	@Override
	public void onLoad(PropertyUtil bundle) {
	}

	@Override
	public void onChange() {
	}
}
