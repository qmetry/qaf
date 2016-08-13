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


package com.infostretch.automation.ui.selenium.webdriver;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.SeleneseCommand;

/**
 * com.infostretch.automation.ui.selenium.webdriver.DriverSkippedCommand.java
 * 
 * @author chirag
 */
public class GetCssPropertyCommand extends SeleneseCommand<String> {
	private final ElementFinder finder;
	private final JavascriptLibrary js;

	public GetCssPropertyCommand(JavascriptLibrary js, ElementFinder finder) {
		this.js = js;
		this.finder = finder;
	}

	@Override
	protected String handleSeleneseCommand(WebDriver driver, String attributeLocator, String value) {
		int atSign = attributeLocator.lastIndexOf("@");
		String elementLocator = attributeLocator.substring(0, atSign);
		String property = attributeLocator.substring(atSign + 1);
		if (!(StringUtils.isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
				|| value.startsWith("\""))) {
			value = "'" + value + "'";
		}
		Object res = js.executeScript(driver,
				"var res  = arguments[0].currentStyle? arguments[0].currentStyle['" + property
						+ "']:window.getComputedStyle? document.defaultView.getComputedStyle(arguments[0],null).getPropertyValue('"
						+ property + "'): ''; return res;",
				finder.findElement(driver, elementLocator));
		return (String) res;
	}

}
