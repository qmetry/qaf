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

import com.qmetry.qaf.automation.util.StringUtil;
import org.openqa.selenium.WebDriver;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.SeleneseCommand;

/**
 * com.qmetry.qaf.automation.ui.selenium.webdriver.DriverSkippedCommand.java
 * 
 * @author chirag
 */
public class SetAttributeCommand extends SeleneseCommand<Void> {
	private final ElementFinder finder;
	private final JavascriptLibrary js;

	public SetAttributeCommand(JavascriptLibrary js, ElementFinder finder) {
		this.js = js;
		this.finder = finder;
	}

	@Override
	protected Void handleSeleneseCommand(WebDriver driver, String attributeLocator, String value) {
		int atSign = attributeLocator.lastIndexOf("@");
		String elementLocator = attributeLocator.substring(0, atSign);
		String attributeName = attributeLocator.substring(atSign + 1);
		if (!(StringUtil.isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
				|| value.startsWith("\""))) {
			value = "\"" + value + "\"";
		}
		try {
			((QAFExtendedWebDriver) driver).findElement(elementLocator)
					.executeScript("setAttribute('" + attributeName + "'," + value + ")");
		} catch (Exception e) {
			if (attributeName.equalsIgnoreCase("style")) {
				((QAFExtendedWebDriver) driver).findElement(elementLocator)
						.executeScript("style.setAttribute('cssText'," + value + ")");
			} else if (attributeName.equalsIgnoreCase("class")) {
				((QAFExtendedWebDriver) driver).findElement(elementLocator).executeScript("className=" + value);
			} else {
				js.executeScript(driver, "arguments[0]." + attributeName + " = arguments[1]",
						finder.findElement(driver, elementLocator), value);
			}
		}

		return null;
	}
}
