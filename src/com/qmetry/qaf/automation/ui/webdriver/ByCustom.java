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

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * @author Chirag
 */
public class ByCustom extends By {
	private static final Log logger = LogFactory.getLog(ByCustom.class);

	private By by;

	public ByCustom(String strategy, String loc) {
		by = getBy(strategy, loc);
	}

	@Override
	public WebElement findElement(SearchContext context) {
		return by.findElement(context);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
	 */
	@Override
	public List<WebElement> findElements(SearchContext context) {
		return by.findElements(context);
	}

	private By getBy(String srategy, String loc) {
		final String s = ConfigurationManager.getBundle().getString(srategy,srategy);
		try {
			@SuppressWarnings("unchecked")
			Class<? extends By> cls = (Class<? extends By>) Class.forName(s);
			try {
				Constructor<? extends By> con = cls.getConstructor(String.class);
				con.setAccessible(true);
				return con.newInstance(loc);
			} catch (Exception e) {
				throw new AutomationError("Unable to create By using class" + s + " for locator " + loc, e);
			}
		} catch (ClassNotFoundException e) {
			logger.info("No class registerd for strategy" + s + ". Will use '" + s + "' as custom strategy");

			return new By() {
				@Override
				public List<WebElement> findElements(SearchContext context) {
					return ((FindsByCustomStretegy) context).findElementsByCustomStretegy(s, loc);
				}

				@Override
				public WebElement findElement(SearchContext context) {
					return ((FindsByCustomStretegy) context).findElementByCustomStretegy(s, loc);
				}
				@Override
				public String toString() {
					return String.format("Using %s: %s", s, loc);
				}
			};
		}
	}
	
	@Override
	public String toString() {
		return by.toString();
	}
	
	/**
	 * @return 
	 */
	public By getBy() {
		return by;
	}
}
