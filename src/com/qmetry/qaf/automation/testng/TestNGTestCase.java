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

	@AfterSuite(alwaysRun = true)
	final public void afterSuit(ITestContext testContext) {
		TestBaseProvider.instance().stopAll();
		ResultUpdator.awaitTermination();
	}

	public PropertyUtil getProps() {
		return ConfigurationManager.getBundle();
	}

}
