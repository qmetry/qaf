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
package com.qmetry.qaf.automation.ui.selenium;

import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Helper class for generating js statements
 * 
 * @author chirag
 */
public class JavaScriptHelper {
	private static final String window = "selenium.browserbot.getCurrentWindow()";

	public static String checkElementStyle(String ele, String style) {
		return getElementById(ele) + ".style." + style;
	}

	public static String getExpression(String expr) {

		return String.format("with(%s){%s}", window, expr);
	}

	public static String getElementById(String id) {
		return window + ".document.getElementById('" + id + "')";
	}

	public static String checkElementInvisible(String ele) {
		return checkElementStyle(ele, "display=='none'");
	}

	public static String checkElementVisible(String ele) {
		return checkElementStyle(ele, "display!='none'");
	}

	public static String checkElementAttribute(String ele, String attr, String value) {
		return getElementById(ele) + "." + attr + value;
	}

	public static String hasCSSClass(String ele, String cls) {
		return getElementById(ele) + ".className.indexOf('" + cls + "')>=0";
	}

	public static String getConditionforCSSClassNotExist(String ele, String className) {
		if (StringUtil.isXpath(ele)) {
			ele = StringUtil.getWellFormedXPATH(ele);
		}
		return String.format("var attr=selenium.getAttribute(\"%s@class\");attr.indexOf('%s')<=0;", ele, className);
	}

	public static String getConditionforCSSClassExist(String ele, String className) {
		if (StringUtil.isXpath(ele)) {
			ele = StringUtil.getWellFormedXPATH(ele);
		}
		return String.format("var attr=selenium.getAttribute(\"%s@class\");attr.indexOf('%s')>=0;", ele, className);
	}

	public static String getConditionforImageLoaded(String imgLoc) {

		return String.format("selenium.browserbot.findElement(\"%s\").complete;", imgLoc);
	}

	public static String waitForConditionForElementPresetn(String ele) {
		if (StringUtil.isXpath(ele)) {
			ele = StringUtil.getWellFormedXPATH(ele);
		}
		return String.format("selenium.isElementPresent(\"%s\")", ele);
	}

	public static String waitForConditionForTextPresetn(String text) {
		return String.format("selenium.isTextPresent(\"%s\")", text);
	}

	public static String getJQueryInjectSnippet() {
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
