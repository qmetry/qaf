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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * com.qmetry.qaf.automation.ui.webdriver.ByJQuery
 * 
 * JQuery strategy locator strategy for application where JQuery used in client
 * side implementation. If JQuery is not used, then either a direct connection or
 * a proxy setting is needed to access
 * https://ajax.googleapis.com/ajax/libs/jquery in order for the JQuery JS to be
 * retrieved.
 * 
 * @author chirag.jayswal
 */
public class ByJQuery extends By {
	private String jQuerySelector;
	private static final String JQUERY_SELECTOR = "var lst = [];  $(arguments[0]).each( function(i,item) { lst.push(item); } ); return lst;";
	private static final String JQUERY_CHILD_SELECTOR = "var lst = [];  $(arguments[0]).find(arguments[1]).each( function(i,item) { lst.push(item); } ); return lst;";

	public ByJQuery(String jQuerySelector) {
		this.jQuerySelector = jQuerySelector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WebElement> findElements(SearchContext context) {
		JavascriptExecutor jse;
		String js;
		Object[] args;
		if (context instanceof RemoteWebElement) {
			jse = (JavascriptExecutor) ((RemoteWebElement) context).getWrappedDriver();
			js = JQUERY_CHILD_SELECTOR;
			args = new Object[] { context, jQuerySelector };
		} else {
			jse = (JavascriptExecutor) context;
			js = JQUERY_SELECTOR;
			args = new Object[] { jQuerySelector };
		}
		jse.executeScript(getJQueryInjectSnippet());
		List<WebElement> objects = (List<WebElement>) jse.executeScript(js, args);
		return objects;
	}

	@Override
	public String toString() {
		return "Using JQuery Selector: " + jQuerySelector;
	}

	private static String getJQueryInjectSnippet() {
		return "(function(callback) {" + " if (typeof jQuery == 'undefined') {"
				+ " var jqueryUrl = 'https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js';"
				+ "	var script = document.createElement('script');"
				+ "	var head = document.getElementsByTagName('head')[0];" + "	var done = false;"
				+ "   script.onload = script.onreadystatechange = (function() {"
				+ "            if (!done && (!this.readyState || this.readyState == 'loaded' || this.readyState == 'complete')) {"
				+ "	            done = true;" + "               script.onload = script.onreadystatechange = null;"
				+ "               head.removeChild(script);" + "               callback();" + "           }"
				+ "        });" + "  script.src = jqueryUrl;" + "  head.appendChild(script);"
				+ "  }})(arguments[arguments.length - 1]);";
	}
}
