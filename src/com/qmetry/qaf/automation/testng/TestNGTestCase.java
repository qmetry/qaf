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

package com.qmetry.qaf.automation.testng;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.integration.ResultUpdator;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author Chirag Jayswal
 *
 */
public abstract class TestNGTestCase extends Validator {
	protected final Log logger;
	protected ITestContext context;

	public TestNGTestCase() {
		logger = LogFactoryImpl.getLog(this.getClass());
	}

	@BeforeGroups(alwaysRun = true)
	@BeforeClass(alwaysRun = true)
	final public void setup(ITestContext context) {

		ConfigurationManager.getBundle().addProperty(ApplicationProperties.CURRENT_TEST_CONTEXT.key, context);
		this.context = context;
	}

	@BeforeSuite(alwaysRun = true)
	final public void setupSuit(ITestContext context) {
		this.context = context;
		ConfigurationManager.getBundle().addProperty(ApplicationProperties.CURRENT_TEST_CONTEXT.key, context);
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>(
				context.getSuite().getXmlSuite().getParameters());

		ConfigurationManager.addAll(params);
	}

	@BeforeTest(alwaysRun = true)
	final public void setupTest(ITestContext context) {
		ConfigurationManager.getBundle().addProperty(ApplicationProperties.CURRENT_TEST_CONTEXT.key, context);
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>(
				context.getCurrentXmlTest().getAllParameters());

		ConfigurationManager.addAll(params);
		this.context = context;
	}

	@BeforeMethod(alwaysRun = true)
	final public void setupMethod(Method m, ITestContext context) {
		ConfigurationManager.getBundle().addProperty(ApplicationProperties.CURRENT_TEST_CONTEXT.key, context);
		this.context = context;
	}

	@AfterSuite
	final public void afterSuit(ITestContext testContext) {
		TestBaseProvider.instance().stopAll();
		ResultUpdator.awaitTermination();
	}

	public PropertyUtil getProps() {
		return ConfigurationManager.getBundle();
	}

}
