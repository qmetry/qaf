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
package com.qmetry.qaf.automation.ui.locator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

@Test(groups="UI")
public class DescriptiveLocatorsTest extends WebDriverTestCase {

	String searchbox = "{'locator' : 'css=#q'; 'desc' : 'search box'}";
	String servicesTab = "{'locator' : 'css=#services'; 'desc' : 'services tab'}";
	String productsTab = "{'locator' : 'css=#products'; 'desc' : 'products tab'}";

	QAFWebElement services = new QAFExtendedWebElement(servicesTab);
	QAFWebElement searchBox = new QAFExtendedWebElement(searchbox);
	QAFWebElement products = new QAFExtendedWebElement(productsTab);

	String searchboxN = "{'locator' : 'css=#qa'; 'desc' : 'search box'}";
	String servicesTabN = "{'locator' : 'css=#servicesa'; 'desc' : 'services tab'}";
	String productsTabN = "{'locator' : 'css=#productsa'; 'desc' : 'products tab'}";

	QAFWebElement servicesN = new QAFExtendedWebElement(servicesTabN);
	QAFWebElement searchBoxN = new QAFExtendedWebElement(searchboxN);
	QAFWebElement productsN = new QAFExtendedWebElement(productsTabN);

	@BeforeClass
	// @BeforeMethod(groups = {"waitservice", "assertions",
	// "descriptiveLocators",
	// "verifications"})
	public void start() {
		// getDriver().get("/");
		getDriver().get("http://www.infostretch.com");

	}

	@Test(description = "test descriptive locators", groups = {"descriptiveLocators",""})
	public void DescLocators() {
		getDriver().get("http://www.infostretch.com");

		searchBox.verifyPresent();
		services.verifyPresent();
		products.verifyPresent();
	}

	@Test(description = "test descriptive locators", groups = "descriptiveLocators")
	public void DescLocatorsNegative() {
		servicesN.verifyNotPresent();
		searchBoxN.verifyNotPresent();
		productsN.verifyNotPresent();
	}

	@Test(description = "test assertions", groups = "assertions")
	public void AssertionsTC() {
		services.assertPresent();
		searchBox.assertPresent();
		products.assertPresent();
	}

	@Test(description = "test verifications", groups = "verifications")
	public void VerificationsTC() {
		searchBox.verifyPresent();
		services.verifyPresent();
		products.verifyPresent();
	}

	@Test(description = "test verifications", groups = "waitservice")
	public void WaitServiceTC() {
		services.waitForPresent();
	}
}
