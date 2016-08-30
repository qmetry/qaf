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
