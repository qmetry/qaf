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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.SeleneseCommand;

/**
 * com.qmetry.qaf.automation.ui.selenium.webdriver.GetText.java
 * 
 * @author chirag
 */
public class GetText extends SeleneseCommand<String> {
	private final ElementFinder finder;

	public GetText(ElementFinder finder) {
		this.finder = finder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.openqa.selenium.internal.seleniumemulation.SeleneseCommand#
	 * handleSeleneseCommand(org.openqa.selenium.WebDriver, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	protected String handleSeleneseCommand(WebDriver driver, String locator, String ignored) {
		WebElement element = finder.findElement(driver, locator);
		return element.getText();
	}

}
