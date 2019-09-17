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
package com.qmetry.qaf.automation.ui.webdriver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;

import com.qmetry.qaf.automation.ui.annotations.FindBy;

/**
 * com.qmetry.qaf.automation.ui.webdriver.custom.Component.java
 * 
 * @author chirag
 */
public abstract class QAFWebComponent extends QAFExtendedWebElement {
	protected final Log logger;

	public QAFWebComponent(String locator) {
		super(locator);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}

	public QAFWebComponent(QAFExtendedWebDriver driver, By by) {
		super(driver, by);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}
	
	protected QAFWebComponent(QAFExtendedWebDriver driver){
		super(driver);
		logger = LogFactory.getLog(this.getClass());
	}

	/**
	 * call this constructor for component having parent element. Such component
	 * will not supported by {@link FindBy} annotation.
	 * 
	 * @param parent
	 * @param locator
	 */
	public QAFWebComponent(QAFExtendedWebElement parent, String locator) {
		super(parent, locator);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}

	private void initFields() {
		ElementFactory elementFactory = new ElementFactory(this);
		elementFactory.initFields(this);
	}

}
