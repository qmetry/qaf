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
