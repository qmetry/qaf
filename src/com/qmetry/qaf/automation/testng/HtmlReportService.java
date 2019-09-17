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
package com.qmetry.qaf.automation.testng;

import org.testng.ITestResult;
import org.testng.Reporter;

import com.qmetry.qaf.automation.util.StringUtil;

public class HtmlReportService {
	public static final String SCREENSHOT_DIR = "../../../";

	public HtmlReportService() {

	}

	public static String formatFailMessage(String message) {
		return "<span style=\"color:red\">F</span> " + message;
	}

	public static String formatSuccessMessage(String message) {
		return "<span style=\"color:green\">P</span> " + message;
	}

	public void attachScreenShotLink(ITestResult tr, String file) {

		Reporter.setCurrentTestResult(tr);
		Reporter.log("<a href=\"" + file + "\" target=\"_blank\">View Screenshot</a><br>");

	}

	public static void setSeleniumLog(ITestResult tr, String log) {
		String id = StringUtil.createRandomString("sLog");
		Reporter.log("<a href=\"javascript:toggleElement('" + id + "', 'block')\">View SeleniumLog</a>" + "\n");
		Reporter.log("<div id=\"" + id
				+ "\" style=\"display:none;position:absolute;overflow: auto; opacity: 0.95;filter:alpha(opacity=95);background-color:#eeeeee;height:300px;z-lineNo: 9002;border:1px outset;\">"
				+ log + "</div><br>");
	}

	public String toggle(String div) {
		String fld = "document.getElementById('" + div + "').style.display";
		return fld + "=='none'?" + fld + "='':" + fld + "='none'";
	}
}
