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

		SearchPage spPage = new SearchPage();
		System.out.println(spPage.isPageActive(null));

		spPage.waitForPageToLoad();
		Validator.verifyThat(ele, Matchers.instanceOf(UiElement.class));
		System.out.println("element is implementation of" + ele.getClass());
		
		QAFExtendedWebElement eleFromList = (QAFExtendedWebElement) new WebDriverTestBase().getDriver().findElements(By.name("q")).get(0);
		eleFromList.verifyPresent();
		Validator.verifyThat(eleFromList, Matchers.instanceOf(UiElement.class));

		System.out.println("element From List is implementation of" + eleFromList.getClass());
		
		QAFExtendedWebElement childEleFromList = (QAFExtendedWebElement) new WebDriverTestBase().getDriver().findElement("tagName=body").findElements(By.name("q")).get(0);
		childEleFromList.verifyPresent();
		Validator.verifyThat(childEleFromList, Matchers.instanceOf(UiElement.class));
		childEleFromList.verifyVisible();
		

		System.out.println("child element From List is implementation of" + childEleFromList.getClass());
		QAFWebElement ele1 = ElementFactory.$("name=q");
		Validator.verifyThat(ele1, Matchers.instanceOf(UiElement.class));

		System.out.println("element is implementation of " + ele1.getClass());
		ele1.verifyPresent();
		new WebDriverTestBase().getDriver().waitForAnyElementVisible(childEleFromList,ele,ele1);

		new WebDriverTestBase().getDriver().waitForAllElementVisible(childEleFromList,ele,ele1);

		SearchPage searchPage = new SearchPage();
		System.out.println("element is implementation of " + searchPage.getSearchInput().getClass());
		Validator.verifyThat(searchPage.getSearchInput(), Matchers.instanceOf(UiElement.class));

		searchPage.getSearchInput().verifyPresent();
		
		System.out.println("element From page List is implementation of " + searchPage.getSearchInputList().get(0).getClass());
		Validator.verifyThat(searchPage.getSearchInputList(), Matchers.instanceOf(UiElement.class));

	}

}
