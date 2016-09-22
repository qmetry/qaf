/*******************************************************************************
 * Copyright 2016 Infostretch Corporation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
