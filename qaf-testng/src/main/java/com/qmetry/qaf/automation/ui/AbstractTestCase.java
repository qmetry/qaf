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
package com.qmetry.qaf.automation.ui;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.testng.TestNGTestCase;
import com.qmetry.qaf.automation.ui.api.UiTestBase;
import com.qmetry.qaf.automation.util.PropertyUtil;

/**
 * com.qmetry.qaf.automation.core.ui.AbstractTestCase.java
 * 
 * @author chirag.jayswal
 */
public abstract class AbstractTestCase<D extends UiDriver, B extends UiTestBase<D>> extends TestNGTestCase {
	/**
	 * @deprecated use {@link #getProps()} instead
	 */
	protected PropertyUtil props;


	public AbstractTestCase() {
		props = ConfigurationManager.getBundle();
	}

	public abstract B getTestBase();

	public D getDriver() {
		return getTestBase().getDriver();
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
		String useSingleSeleniumInstance =
				ConfigurationManager.getBundle().getString("selenium.singletone", "t");
		// match with first char only so m or method or methods is fine
		if (useSingleSeleniumInstance.toUpperCase().startsWith(
				type.substring(0, 1).toUpperCase()) || type.equalsIgnoreCase("tests")) {
			if (getTestBase() != null) {
				getTestBase().tearDown();
			}
		}
	}
}
