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
			QAFTestStepListener, QAFDataProviderIntercepter {

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

}
