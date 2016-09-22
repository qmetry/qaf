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
