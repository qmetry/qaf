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

package com.qmetry.qaf.automation.ui.util;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.SystemClock;

import com.google.common.collect.ImmutableList;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;

/**
 * A specialization of {@link FluentWait} that uses WebDriver instances.
 */
public class QAFWebDriverWait extends FluentWait<QAFExtendedWebDriver> {
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
		this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMiliSeconds,
				getDefaultInterval());
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
		this(driver, new SystemClock(), Sleeper.SYSTEM_SLEEPER, timeOutInMiliSeconds,
				sleepInMillis);
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

	/**
	 * @param driver
	 *            The WebDriver instance to pass to the expected conditions
	 * @param clock
	 *            The clock to use when measuring the timeout
	 * @param sleeper
	 *            Object used to make the current thread go to sleep.
	 * @param timeOutInSeconds
	 *            The timeout in seconds when an expectation is
	 * @param sleepTimeOut
	 *            The timeout used whilst sleeping. Defaults to 500ms called.
	 */
	protected QAFWebDriverWait(QAFExtendedWebDriver driver, Clock clock, Sleeper sleeper,
			long timeOutInMiliSeconds, long sleepTimeOut) {
		super(driver, clock, sleeper);
		withTimeout(timeOutInMiliSeconds, TimeUnit.MILLISECONDS);
		pollingEvery(sleepTimeOut, TimeUnit.MILLISECONDS);
		ignoring(StaleElementReferenceException.class);
	}

	/**
	 * @see #ignoreAll(Collection)
	 */
	public QAFWebDriverWait ignore(Class<? extends RuntimeException>... exceptionType) {
		return (QAFWebDriverWait) this.ignoreAll(
				ImmutableList.<Class<? extends RuntimeException>> copyOf(exceptionType));
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

	private static long getDefaultTimeout() {
		return getBundle().getLong("selenium.explicit.wait.timeout",
				ApplicationProperties.SELENIUM_WAIT_TIMEOUT.getIntVal(5000));
	}

	private static long getDefaultInterval() {
		return getBundle().getLong("selenium.explicit.wait.interval",
				getBundle().getLong("selenium.wait.interval", 1000));
	}

}
