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
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.qmetry.qaf.automation.ui.webdriver.extended.IsWebElementList.java
 * 
 * @author chirag
 */
public class QAFExtendedWebElementListHandler implements InvocationHandler {
	private final SearchContext context;
	private final By by;

	public QAFExtendedWebElementListHandler(SearchContext context, By by) {
		this.context = context;
		this.by = by;
	}

	public QAFExtendedWebElementListHandler(SearchContext context, String loc) {
		this(context, LocatorUtil.getBy(loc));
	}

	public QAFExtendedWebElementListHandler(By by) {
		this(new WebDriverTestBase().getDriver(), by);
	}

	public QAFExtendedWebElementListHandler(String loc) {
		this(new WebDriverTestBase().getDriver(), loc);
	}

	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		List<? extends WebElement> elements = context.findElements(by);
		try {
			return method.invoke(elements, objects);
		} catch (Exception e) {
			throw e.getCause();
		}
	}

}
