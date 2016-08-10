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


package com.infostretch.automation.ui.webdriver;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.infostretch.automation.util.StringMatcher;

public interface QAFWebElement extends WebElement, FindsByCustomStretegy {

	// explicit wait
	void waitForVisible(long... timeout);

	void waitForNotVisible(long... timeout);

	void waitForDisabled(long... timeout);

	void waitForEnabled(long... timeout);

	void waitForPresent(long... timeout);

	void waitForNotPresent(long... timeout);

	void waitForText(String text, long... timeout);

	void waitForText(StringMatcher matcher, long... timeout);

	void waitForNotText(String text, long... timeout);

	void waitForNotText(StringMatcher matcher, long... timeout);

	void waitForValue(Object value, long... timeout);

	void waitForNotValue(Object value, long... timeout);

	void waitForSelected(long... timeout);

	void waitForNotSelected(long... timeout);

	void waitForAttribute(String attr, String value, long... timeout);

	void waitForNotAttribute(String attr, String value, long... timeout);

	void waitForAttribute(String attr, StringMatcher value, long... timeout);

	void waitForNotAttribute(String attr, StringMatcher value, long... timeout);

	void waitForCssClass(String className, long... timeout);

	void waitForNotCssClass(String className, long... timeout);

	void waitForCssStyle(String prop, String value, long... timeout);

	void waitForNotCssStyle(String prop, String value, long... timeout);

	// verifications
	boolean verifyPresent(String... label);

	boolean verifyNotPresent(String... label);

	boolean verifyVisible(String... label);

	boolean verifyNotVisible(String... label);

	boolean verifyEnabled(String... label);

	boolean verifyDisabled(String... label);

	boolean verifyText(String text, String... label);

	boolean verifyText(StringMatcher matcher, String... label);

	boolean verifyNotText(String text, String... label);

	boolean verifyNotText(StringMatcher matcher, String... label);

	<T> boolean verifyValue(T t, String... label);

	<T> boolean verifyNotValue(T t, String... label);

	boolean verifySelected(String... label);

	boolean verifyNotSelected(String... label);

	boolean verifyAttribute(String attr, String value, String... label);

	boolean verifyAttribute(String attr, StringMatcher matcher, String... label);

	boolean verifyNotAttribute(String attr, String value, String... label);

	boolean verifyNotAttribute(String attr, StringMatcher matcher, String... label);

	boolean verifyCssClass(String className, String... label);

	boolean verifyNotCssClass(String className, String... label);

	boolean verifyCssStyle(String prop, String value, String... label);

	boolean verifyNotCssStyle(String prop, String value, String... label);

	// preconditions
	void givenPresent();

	void givenNotPresent(String... label);

	// assertions
	void assertPresent(String... label);

	void assertNotPresent(String... label);

	void assertVisible(String... label);

	void assertNotVisible(String... label);

	void assertEnabled(String... label);

	void assertDisabled(String... label);

	void assertText(String text, String... label);

	void assertNotText(String text, String... label);

	void assertText(StringMatcher matcher, String... label);

	void assetNotText(StringMatcher matcher, String... label);

	<T> void assertValue(T t, String... label);

	<T> void assertNotValue(T t, String... label);

	void assertSelected(String... label);

	void assertNotSelected(String... label);

	void assertAttribute(String attr, String value, String... label);

	void assertAttribute(String attr, StringMatcher matcher, String... label);

	void assertNotAttribute(String attr, String value, String... label);

	void assertNotAttribute(String attr, StringMatcher matcher, String... label);

	void assertCssClass(String className, String... label);

	void assertNotCssClass(String className, String... label);

	void assertCssStyle(String prop, String value, String... label);

	void assertNotCssStyle(String prop, String value, String... label);

	// other
	void setAttribute(String attr, String value);

	boolean isPresent();

	QAFWebElement findElement(String loc);

	List<QAFWebElement> findElements(String loc);

	<T> T executeScript(String sctipt);

	<T> T executeAsyncScript(String sctipt);
}
