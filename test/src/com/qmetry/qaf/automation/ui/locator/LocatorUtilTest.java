package com.qmetry.qaf.automation.ui.locator;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.Validator.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.ArrayList;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.By.ByTagName;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.webdriver.ByAny;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.util.LocatorUtil;

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
/**
 * .LocatorUtilTest.java
 * 
 * @author chirag
 */
public class LocatorUtilTest {

	@Test(dataProvider = "locatorDP")
	public void testLocator(String loc, Class<?> cls) {
		assertThat(LocatorUtil.getBy(loc), instanceOf(cls));
	}

	@Test
	public void testLocator() {
		getBundle().setProperty("test.loc",
				"{'locator':'css=a','desc':' Trip type check box'}");
		assertThat(LocatorUtil.getBy("test.loc"), instanceOf(ByCssSelector.class));
	}

	@Test(groups="UI")
	public void testLocatorKey() {
		getBundle().setProperty("test.loc",
				"{'locator':'css=a','desc':'Trip type check box'}");
		// System.out.println(ConfigurationManager.getBundle().getString("test.loc"));
		getBundle().setProperty(ApplicationProperties.DRIVER_NAME.key, "firefoxDriver");
		// getBundle().setProperty(ApplicationProperties.SELENIUM_BASE_URL.key,
		// "http://www.google.com");
		// new WebDriverTestBase().getDriver().get("/");
		QAFExtendedWebElement ele = new QAFExtendedWebElement("test.loc");

		assertThat(ele.getDescription(),
				Matchers.equalToIgnoringCase("Trip type check box"));
	}

	@DataProvider(name = "locatorDP")
	public static Iterator<Object[]> testData() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();

		data.add(new Object[]{"id=eleId", ById.class});
		data.add(new Object[]{"name=eleName", ByName.class});
		data.add(new Object[]{"css=#eleId.className", ByCssSelector.class});
		data.add(new Object[]{"tagName=div", ByTagName.class});
		data.add(new Object[]{"link=Link Text", ByLinkText.class});
		data.add(new Object[]{"partialLink=Link Text", ByPartialLinkText.class});
		data.add(new Object[]{"['css=#qa','name=eleName']", ByAny.class});

		// self descriptive
		data.add(new Object[]{"{'locator' : 'id=eleId'; 'desc' : 'locate element by id'}",
				ById.class});
		data.add(new Object[]{
				"{'locator' : 'name=eleName'; 'desc' : 'locate element by name'}",
				ByName.class});
		data.add(new Object[]{
				"{'locator' : 'css=#eleId.className'; 'desc' : 'locate element by css'}",
				ByCssSelector.class});

		data.add(new Object[]{
				"{'locator' : ['css=#qa','name=eleName']; 'desc' : 'locate element by css'}",
				ByAny.class});

		return data.iterator();
	}
}
