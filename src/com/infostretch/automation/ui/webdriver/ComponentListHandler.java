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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.infostretch.automation.core.ConfigurationManager;
import com.infostretch.automation.ui.WebDriverTestBase;
import com.infostretch.automation.ui.util.ExpectedCondition;
import com.infostretch.automation.ui.util.QAFWebDriverWait;
import com.infostretch.automation.util.JSONUtil;
import com.infostretch.automation.util.LocatorUtil;
import com.thoughtworks.selenium.SeleniumException;

/**
 * com.infostretch.automation.ui.webdriver.extended.IsWebElementList.java
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

	public ComponentListHandler(SearchContext context, String loc, Class<? extends QAFExtendedWebElement> cls,
			Object declaringclassObj) {
		this.context = context;
		componentClass = cls;
		this.declaringclassObj = declaringclassObj;
		init(loc);
	}

	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		if (context == null) {
			context = new WebDriverTestBase().getDriver();
		}
		final List<WebElement> elements = new ArrayList<WebElement>();
		if (method.getName().equalsIgnoreCase("get")) {
			final int index = (Integer) objects[0];
			new QAFWebDriverWait()
					.withMessage(String.format("Wait timeout for list of %s with size %d", description, index + 1))
					.until(new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
						@Override
						public Boolean apply(QAFExtendedWebDriver driver) {
							try {
								elements.clear();
								elements.addAll(context.findElements(by));
								return elements.size() > index;
							} catch (NoSuchElementException e) {
								return false;
							} catch (SeleniumException se) {
								return false;
							} catch (StaleElementReferenceException sr) {
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
				Object component = ComponentFactory.getObject(componentClass, loc, declaringclassObj, context);
				QAFExtendedWebElement extendedWebElement = (QAFExtendedWebElement) component;
				extendedWebElement.setId(((QAFExtendedWebElement) element).getId());
				extendedWebElement.cacheable = true;
				extendedWebElement.getMetaData().put("pageClass", declaringclassObj.getClass());

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
				description = map.containsKey("desc") ? (String) map.get("desc")
						: map.containsKey("description") ? (String) map.get("description")
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
