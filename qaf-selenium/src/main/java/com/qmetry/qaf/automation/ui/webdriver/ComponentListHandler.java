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
package com.qmetry.qaf.automation.ui.webdriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.util.ExpectedCondition;
import com.qmetry.qaf.automation.ui.util.QAFWebDriverWait;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.qmetry.qaf.automation.ui.webdriver.extended.IsWebElementList.java
 * 
 * @author chirag
 */
public class ComponentListHandler implements InvocationHandler {
	private SearchContext context;
	private String loc;
	private Class<? extends QAFExtendedWebElement> componentClass;
	private Object declaringclassObj;
	private String description;
	private By by;

	public ComponentListHandler(SearchContext context, String loc,
			Class<? extends QAFExtendedWebElement> cls, Object declaringclassObj) {
		this.context = context;
		componentClass = cls;
		this.declaringclassObj = declaringclassObj;
		init(loc);
	}

	public Object invoke(Object object, Method method, Object[] objects)
			throws Throwable {
		if (context == null) {
			context = new WebDriverTestBase().getDriver();
		}
		final List<WebElement> elements = new ArrayList<WebElement>();
		if (method.getName().equalsIgnoreCase("get")) {
			final int index = (Integer) objects[0];
			new QAFWebDriverWait()
					.withMessage(String.format("Wait timeout for list of %s with size %d",
							description, index + 1))
					.until(new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
						@Override
						public Boolean apply(QAFExtendedWebDriver driver) {
							try {
								elements.clear();
								elements.addAll(context.findElements(by));
								return elements.size() > index;
							} catch (WebDriverException e) {
								return false;
							}
						}
					});
		} else {
			elements.clear();
			elements.addAll((context).findElements(by));
		}

		List<Object> components = new ArrayList<Object>();

		if ((elements != null) && !elements.isEmpty()) {
			for (WebElement element : elements) {
				Object component = ComponentFactory.getObject(componentClass, loc,
						declaringclassObj, context);
				QAFExtendedWebElement extendedWebElement =
						(QAFExtendedWebElement) component;
				extendedWebElement.setId(((QAFExtendedWebElement) element).getId());
				extendedWebElement.cacheable = true;
				extendedWebElement.getMetaData().put("pageClass",
						declaringclassObj.getClass());

				if ((null != context) && (context instanceof QAFExtendedWebElement)) {
					extendedWebElement.parentElement = (QAFExtendedWebElement) context;
				}

				components.add(component);
			}

		}

		try {
			return method.invoke(components, objects);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

	// TODO: fix map.get("child") as string case.....
	private void init(String locator) {
		loc = ConfigurationManager.getBundle().getString(locator, locator);
		loc = ConfigurationManager.getBundle().getSubstitutor().replace(loc);
		by = LocatorUtil.getBy(loc);
		if (JSONUtil.isValidJsonString(loc)) {
			try {
				Map<String, Object> map = JSONUtil.toMap(loc);
				description =
						map.containsKey("desc") ? (String) map.get("desc")
								: map.containsKey("description")
										? (String) map.get("description")
										: (String) map.get("locator");
				if (map.containsKey("child") && !(Boolean) map.get("child")) {
					context = null;
				}

			} catch (JSONException e) {
				description = loc;
			}
		}
	}

}
