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
package com.qmetry.qaf.automation.ui.util;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.util.StringMatcher;

/**
 * com.qmetry.qaf.automation.core.ui.QAFWebDriverExpectedConditions.java
 * 
 * @author chirag
 */
public class QAFWebDriverExpectedConditions {
	// Restricted to create objects
	private QAFWebDriverExpectedConditions() {

	}

	/**
	 * @param snippet
	 *            JS snippet returning boolean value.
	 * @return
	 */
	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> jsCondition(final String snippet) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				Object res = driver.executeScript(snippet);
				return (Boolean) res;
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> elementPresent(final By locator) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				try {
					driver.findElement(locator);
					return true;
				} catch (RuntimeException e) {
					return false;
				}
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> elementNotPresent(final By locator) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				try {
					driver.findElement(locator);

				} catch (RuntimeException e) {
					return true;
				}
				return false;
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebDriver, Alert> alertPresent() {
		return new ExpectedCondition<QAFExtendedWebDriver, Alert>() {
			@Override
			public Alert apply(QAFExtendedWebDriver driver) {
				try {
					Alert alert = driver.switchTo().alert();
					alert.getText();
					return alert;
				} catch (NullPointerException e) {
				}
				return null;
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> noOfwindowsPresent(final int count) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				try {
					return driver.getWindowHandles().size()>=count;
				} catch (Exception e) {
				}
				return false;
			}
		};
	}
	
	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> windowTitle(final StringMatcher title) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				try {
					return title.match(driver.getTitle());
				} catch (Exception e) {

				}
				return false;
			}
		};
	}
	
	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> currentURL(final StringMatcher url) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
					return url.match(driver.getCurrentUrl());
			}
		};
	}
}
