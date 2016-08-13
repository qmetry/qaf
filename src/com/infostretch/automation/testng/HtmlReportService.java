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


package com.infostretch.automation.testng;

import org.testng.ITestResult;
import org.testng.Reporter;

import com.infostretch.automation.util.StringUtil;

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
