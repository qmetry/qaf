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

package com.qmetry.qaf.automation.ui.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * com.ispl.automation.sample.test.ByAny.java
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
