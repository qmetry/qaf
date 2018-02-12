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

package com.qmetry.qaf.automation.ui.element;

import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.ElementFactory;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Validator;

/**
 * 
 * @author chirag jayswal
 *
 */
public class ElementTest{
	
	@Test(groups="ui-driver")
	public void defaultElemetnTest() {
		getBundle().setProperty("default.element.impl", UiElement.class.getCanonicalName());
		getBundle().setProperty("system.webdriver.gecko.driver", "C:/Users/chirag/Downloads/geckodriver-v0.18.0-win64/geckodriver.exe");
		getBundle().setProperty("system.webdriver.chrome.driver", "C:/Users/chirag/Downloads/chromedriver_win32/chromedriver.exe");

		getBundle().setProperty("driver.name", "chromeDriver");

		System.out.println(UiElement.class.getCanonicalName());
		
		new WebDriverTestBase().getDriver().get("http://www.google.com");
		QAFExtendedWebElement ele = new WebDriverTestBase().getDriver().findElement(By.name("q"));
		ele.verifyPresent();

		Validator.verifyThat(ele, Matchers.instanceOf(UiElement.class));
		System.out.println("element is implementation of" + ele.getClass());
		
		QAFExtendedWebElement eleFromList = (QAFExtendedWebElement) new WebDriverTestBase().getDriver().findElements(By.name("q")).get(0);
		eleFromList.verifyPresent();
		Validator.verifyThat(eleFromList, Matchers.instanceOf(UiElement.class));

		System.out.println("element From List is implementation of" + eleFromList.getClass());
		
		QAFExtendedWebElement childEleFromList = (QAFExtendedWebElement) new WebDriverTestBase().getDriver().findElement("tagName=body").findElements(By.name("q")).get(0);
		childEleFromList.verifyPresent();
		Validator.verifyThat(childEleFromList, Matchers.instanceOf(UiElement.class));

		System.out.println("child element From List is implementation of" + childEleFromList.getClass());
		QAFWebElement ele1 = ElementFactory.$("name=q ");
		Validator.verifyThat(ele1, Matchers.instanceOf(UiElement.class));

		System.out.println("element is implementation of " + ele1.getClass());
		ele1.verifyPresent();
		
		SearchPage searchPage = new SearchPage();
		System.out.println("element is implementation of " + searchPage.getSearchInput().getClass());
		Validator.verifyThat(searchPage.getSearchInput(), Matchers.instanceOf(UiElement.class));

		searchPage.getSearchInput().verifyPresent();
		
		System.out.println("element From page List is implementation of " + searchPage.getSearchInputList().get(0).getClass());
		Validator.verifyThat(searchPage.getSearchInputList(), Matchers.instanceOf(UiElement.class));

	}

}
