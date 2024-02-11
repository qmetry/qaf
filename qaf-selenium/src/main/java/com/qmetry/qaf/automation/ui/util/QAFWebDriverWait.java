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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.StaleElementReferenceException;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;

/**
 * A specialization of {@link DynamicWait} that uses WebDriver instances.
 */
public class QAFWebDriverWait extends DynamicWait<QAFExtendedWebDriver> {
	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param timeOutInMiliSeconds
	 *            The timeout in seconds when an expectation is called
	 * @see QAFWebDriverWait#ignoring(Class[]) equals
	 */
	public QAFWebDriverWait(QAFExtendedWebDriver driver, long timeOutInMiliSeconds) {
		super(driver);
		withTimeout(timeOutInMiliSeconds, TimeUnit.MILLISECONDS);
		ignoring(StaleElementReferenceException.class);
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param timeOutInMiliSeconds
	 *            The timeout in seconds when an expectation is called
	 * @param sleepInMillis
	 *            The duration in milliseconds to sleep between polls.
	 * @see QAFWebDriverWait#ignoring(Class[]) equals
	 */
	public QAFWebDriverWait(QAFExtendedWebDriver driver, long timeOutInMiliSeconds,
			long sleepInMillis) {
		this(driver,timeOutInMiliSeconds);
		pollingEvery(sleepInMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered
	 * (thrown) by default in the 'until' condition, and immediately propagate
	 * all others. You can add more to the ignore list by calling
	 * ignoring(exceptions to add).
	 * 
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @see QAFWebDriverWait#ignoring(Class[]) equals
	 */
	public QAFWebDriverWait(long... timeout) {
		this(new WebDriverTestBase().getDriver(), timeout);
	}

	public QAFWebDriverWait(QAFExtendedWebDriver driver, long... timeout) {
		this(driver, getTimeout(timeout), getInterval(timeout));
	}


	private static long getTimeout(long... timeout) {
		if ((null == timeout) || (timeout.length < 1) || (timeout[0] <= 0)) {
			return getDefaultTimeout();
		}
		return timeout[0];
	}

	private static long getInterval(long... timeout) {
		if ((null == timeout) || (timeout.length < 2) || (timeout[1] <= 0)) {
			return getDefaultInterval();
		}
		return timeout[1];
	}
}
