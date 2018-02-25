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

package com.qmetry.qaf.automation.ui.util;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;

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
	
	public static ExpectedCondition<QAFExtendedWebDriver, Boolean> windowTitle(final String title) {
		return new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebDriver driver) {
				try {
					return title.equalsIgnoreCase(driver.getTitle());
				} catch (Exception e) {
				}
				return false;
			}
		};
	}
}
