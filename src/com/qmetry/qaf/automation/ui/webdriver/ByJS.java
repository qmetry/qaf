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

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * This class allows to find element using java script. In case of child
 * element, parent element object will be referenced as `document` in script. If
 * you don't want to consider parent element as `document` while finding child element, use `window.document`
 * element
 * <h5>Example script:</h5>
 * <ul>
 * <li>return document.querySelector(\"#eleid\").shadowRoot;

 * </ul>
 * <h5>Usage</h5> <code>
 * <li>
 * js=&lt;script&gt;
 * </code>
 * 
 * @author chirag.jayswal
 */
public class ByJS extends By {
	private String script;

	private static final String CHILD_QUERY = "return (function(document){%s}(arguments[0]));";

	public ByJS(String script) {
		this.script = script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WebElement> findElements(SearchContext context) {
		Object res;
		if (context instanceof RemoteWebElement) {
			res = ((JavascriptExecutor) ((RemoteWebElement) context).getWrappedDriver()).executeScript(String.format(CHILD_QUERY,script),
					context);
		} else {
			res = ((JavascriptExecutor) context).executeScript(script);
		}
		if(null==res) {
			return Collections.emptyList();
		}
		if(List.class.isAssignableFrom(res.getClass())) {
			return (List<WebElement>) res;
		}
		return (List<WebElement>) Arrays.asList((WebElement)res);
	}

	@Override
	public String toString() {
		return "Using JS: " + script;
	}

}
