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


package com.infostretch.automation.ui;

import static com.infostretch.automation.util.Reporter.log;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.infostretch.automation.core.ConfigurationManager;
import com.infostretch.automation.core.MessageTypes;
import com.infostretch.automation.core.TestBaseProvider;
import com.infostretch.automation.integration.ResultUpdator;
import com.infostretch.automation.ui.api.UiTestBase;
import com.infostretch.automation.util.PropertyUtil;

/**
 * com.infostretch.automation.core.ui.AbstractTestCase.java
 * 
 * @author chirag.jayswal
 */
public abstract class AbstractTestCase<D, B extends UiTestBase<D>> {
	protected PropertyUtil props;
	protected final Log logger;
	protected ITestContext context;

	public AbstractTestCase() {
		props = ConfigurationManager.getBundle();
		logger = LogFactoryImpl.getLog(this.getClass());
	}

	public abstract B getTestBase();

	public D getDriver() {
		return getTestBase().getDriver();
	}

	@BeforeGroups(alwaysRun = true)
	@BeforeClass(alwaysRun = true)
	final public void setup(ITestContext context) {

		ConfigurationManager.getBundle().addProperty("tng.context", context);
		this.context = context;
	}

	@BeforeSuite(alwaysRun = true)
	final public void setupSuit(ITestContext context) {
		this.context = context;
		ConfigurationManager.getBundle().addProperty("tng.context", context);
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>(
				context.getSuite().getXmlSuite().getParameters());

		ConfigurationManager.addAll(params);
	}

	@BeforeTest(alwaysRun = true)
	final public void setupTest(ITestContext context) {
		ConfigurationManager.getBundle().addProperty("tng.context", context);
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>(
				context.getCurrentXmlTest().getAllParameters());

		ConfigurationManager.addAll(params);
		this.context = context;
	}

	@BeforeMethod(alwaysRun = true)
	final public void setupMethod(Method m, ITestContext context) {
		ConfigurationManager.getBundle().addProperty("tng.context", context);
		this.context = context;
	}

	@AfterMethod(alwaysRun = true)
	final public void afterMethod(ITestContext testContext, ITestResult tr) {
		tearDownPrrallelThreads(testContext, "m");
	}

	@AfterGroups(alwaysRun = true)
	final public void afterGroup(ITestContext testContext) {
		tearDownPrrallelThreads(testContext, "groups");
	}

	@AfterClass(alwaysRun = true)
	final public void afterClass(ITestContext testContext) {
		tearDownPrrallelThreads(testContext, "classes");

	}

	@AfterTest(alwaysRun = true)
	final public void afterTest(ITestContext testContext) {
		tearDownPrrallelThreads(testContext, "tests");
	}

	private void tearDownPrrallelThreads(ITestContext testContext, String type) {
		String useSingleSeleniumInstance = ConfigurationManager.getBundle().getString("selenium.singletone", "t");
		// match with first char only so m or method or methods is fine
		if (useSingleSeleniumInstance.toUpperCase().startsWith(type.substring(0, 1).toUpperCase())
				|| type.equalsIgnoreCase("tests")) {
			if (getTestBase() != null) {
				getTestBase().tearDown();
			}
		}
	}

	@AfterSuite
	final public void afterSuit(ITestContext testContext) {
		TestBaseProvider.instance().stopAll();
		ResultUpdator.awaitTermination();
	}

	public static boolean verifyTrue(boolean condition, String failMessage, String successMsg) {
		if (condition) {
			log(successMsg, MessageTypes.Pass);
		} else {
			log(failMessage, MessageTypes.Fail);
		}
		return condition;

	}

	public static boolean verifyFalse(boolean condition, String failMessage, String successMsg) {
		if (!condition) {
			log(successMsg, MessageTypes.Pass);
		} else {
			log(failMessage, MessageTypes.Fail);
		}
		return !condition;

	}

	public static void assertTrue(boolean condition, String failMessage, String successMsg) {
		if (!verifyTrue(condition, failMessage, successMsg)) {
			throw new AssertionError(failMessage);
		}
	}

	public static void assertFalse(boolean condition, String failMessage, String successMsg) {
		if (!verifyFalse(condition, failMessage, successMsg)) {
			throw new AssertionError(failMessage);
		}
	}

}
