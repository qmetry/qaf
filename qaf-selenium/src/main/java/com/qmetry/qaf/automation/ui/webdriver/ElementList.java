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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;

import com.qmetry.qaf.automation.ui.util.ExpectedCondition;
import com.qmetry.qaf.automation.ui.util.QAFWebDriverWait;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.qmetry.qaf.automation.ui.webdriver.List.java
 * 
 * @author chirag
 */
public class ElementList<T extends QAFWebElement> extends ArrayList<T> {
	private static final long serialVersionUID = -703633828271567272L;
	private SearchContext context;
	private String description;
	private boolean cacheable = false;
	private By by;

	public ElementList(SearchContext context, String loc) {
		this.context = context;
		initLoc(loc);
	}

	@Override
	public T get(final int index) {
		if (!cacheable) {
			clear();
		}
		if (isEmpty()) {
			waitForIndex(index);
		}
		return super.get(index);
	}

	@Override
	public boolean contains(Object o) {
		waitForIndex(0);
		return super.contains(o);
	}

	private void initLoc(String locator) {
		by = LocatorUtil.getBy(locator);

		if (JSONUtil.isValidJsonString(locator)) {
			try {
				Map<String, Object> map = JSONUtil.toMap(locator);
				description = map.containsKey("desc") ? (String) map.get("desc")
						: map.containsKey("description") ? (String) map.get("description")
								: by.toString();
				cacheable = map.containsKey("cacheable") ? (Boolean) map.get("cacheable")
						: false;
			} catch (JSONException e) {
			}
		}
	}

	public void waitForEmpty() {
		waitForIndex(-1);
	}

	public void waitForIndex(final int index) {
		new QAFWebDriverWait()
				.withMessage(String.format("Wait timeout for list of %s with size %d",
						description, index + 1))
				.until(new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
					@SuppressWarnings("unchecked")
					@Override
					public Boolean apply(QAFExtendedWebDriver driver) {
						try {
							clear();
							context.findElements(by).forEach(e -> add((T)e));
							//addAll((Collection<T>) context.findElements(by));
							return size() > index;
						} catch (WebDriverException e) {
							return false;
						}
					}
				});
	}
}
