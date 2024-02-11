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

import com.qmetry.qaf.automation.core.DriverFactory;
import com.qmetry.qaf.automation.ui.selenium.IsSelenium;
import com.qmetry.qaf.automation.ui.selenium.QAFCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.webdriver.GetEvalListener;
import com.qmetry.qaf.automation.ui.selenium.webdriver.QAFWebDriverBackedSelenium;
import com.qmetry.qaf.automation.ui.selenium.webdriver.QAFWebDriverCommandProcessor;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;

/**
 * com.qmetry.qaf.automation.SeleniumTestBase.java
 * 
 * @author chirag
 */
public class SeleniumTestBase extends AbstractTestBase<IsSelenium> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#tearDown()
	 */

	public SeleniumTestBase() {
		super(new UiDriverFactory<IsSelenium>());
	}

	@Override
	public IsSelenium getDriver() {
		UiDriver selenium = getBase().getUiDriver();
		if (selenium instanceof IsSelenium) {
			return (IsSelenium) getBase().getUiDriver();
		}
		QAFCommandProcessor commandProcessor = new QAFWebDriverCommandProcessor(getBaseUrl(),
				(QAFExtendedWebDriver) getBase().getUiDriver());

		commandProcessor.addListener(new GetEvalListener());

		return new QAFWebDriverBackedSelenium(commandProcessor);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void launch(String baseurl) {
		getDriver().open(baseurl);

	}

}
