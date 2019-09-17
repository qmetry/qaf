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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.ispl.automation.sample.test.ByAny.java
 * 
 * @author chirag
 */
public class ByAny extends By {
	private String[] locators;

	public ByAny(String... locator) {
		locators = locator;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		List<WebElement> elements = findElements(context);
		if (elements.isEmpty()) {
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		}
		return elements.get(0);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (locators.length == 0) {
			return new ArrayList<WebElement>();
		}
		List<WebElement> elems = new ArrayList<WebElement>();

		for (String locator : locators) {
			By by = LocatorUtil.getBy(locator);
			elems.addAll(by.findElements(context));
			if (!elems.isEmpty()) {
				break;
			}
		}

		return elems;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("Using any of");
		stringBuilder.append(Arrays.toString(locators));

		return stringBuilder.toString();
	}

}
