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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.DriverFactory;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.ui.api.UiTestBase;

/**
 * com.qmetry.qaf.automation.core.ui.AbstractTestBase.java
 * 
 * @author chirag
 */
public abstract class AbstractTestBase<D extends UiDriver> implements UiTestBase<D> {
	protected String browser = "", browserToLaunch = "";
	protected String baseUrl;
	protected final Log logger;
	protected boolean prepareForShutDown;
	protected boolean inUse;
	protected D driver;
	protected Method method;
	String stbargs[];

	public AbstractTestBase(DriverFactory<D> factory) {
		logger = LogFactory.getLog(this.getClass());
		getBase().setDriverFactory(factory);
	}

	protected String getDefaultBrowser() {
		return "";
	}

	protected void setTestDone() {
		inUse = false;
	}

	protected abstract void launch(String baseurl);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#getDriver()
	 */
	@Override
	public abstract D getDriver();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase #isPreparedForShutDown()
	 */
	@Override
	public boolean isPreparedForShutDown() {
		return prepareForShutDown;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#prepareForShutDown()
	 */
	@Override
	public void prepareForShutDown() {
		this.prepareForShutDown = true;

	}

	public String getBaseUrl() {
		return getBase().getBaseUrl();
	}

	@Override
	public String getBrowser() {
		return getBase().getBrowser();
	}

	@Override
	public void tearDown() {
		getBase().tearDown();

	}

	protected QAFTestBase getBase() {
		return TestBaseProvider.instance().get();
	}

	/**
	 * UiTestBase provider can check this flag timely basis and can remove
	 * unused reference
	 * 
	 * @return
	 */
	protected boolean isInUse() {
		return inUse;
	}

	@Override
	public void addTestStepLog(String msg) {
		getBase().addAssertionLog(msg, MessageTypes.TestStep);
	}

	@Override
	public void addAssertionsLog(String msg) {
		getBase().addAssertionLog(msg, MessageTypes.Info);
	}

	@Override
	public void addAssertionsLog(String msg, MessageTypes type) {
		getBase().addAssertionLog(msg, type);
	}

}
