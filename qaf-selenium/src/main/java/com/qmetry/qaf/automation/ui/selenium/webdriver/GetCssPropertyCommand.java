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
package com.qmetry.qaf.automation.ui.selenium.webdriver;

import static org.apache.commons.lang.StringUtils.isNumeric;

import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.SeleneseCommand;

/**
 * com.qmetry.qaf.automation.ui.selenium.webdriver.DriverSkippedCommand.java
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
		if (!(isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
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
