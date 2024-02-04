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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.interactions.HasInputDevices;
//import org.openqa.selenium.interactions.Keyboard;
//import org.openqa.selenium.interactions.Mouse;
//import org.openqa.selenium.interactions.TouchScreen;
//import org.openqa.selenium.internal.FindsByClassName;
//import org.openqa.selenium.internal.FindsByCssSelector;
//import org.openqa.selenium.internal.FindsById;
//import org.openqa.selenium.internal.FindsByLinkText;
//import org.openqa.selenium.internal.FindsByName;
//import org.openqa.selenium.internal.FindsByTagName;
//import org.openqa.selenium.internal.FindsByXPath;

import com.qmetry.qaf.automation.ui.JsToolkit;
import com.qmetry.qaf.automation.ui.UiDriver;
import com.qmetry.qaf.automation.util.StringMatcher;

public interface QAFWebDriver extends UiDriver, WebDriver, TakesScreenshot, JavascriptExecutor, FindsByCustomStretegy, HasCapabilities{
		// HasInputDevices, HasCapabilities {

	public QAFWebElement findElement(By by);

	public List<QAFWebElement> getElements(By by);

	public QAFWebElement findElement(String locator);

	public List<QAFWebElement> findElements(String locator);

//	public Mouse getMouse();
//
//	public Keyboard getKeyboard();
//
//	public TouchScreen getTouchScreen();
	
	
	public void waitForAjax(JsToolkit toolkit, long... timeout);

	public void waitForAjax(long... timeout);

	public void waitForAnyElementPresent(QAFWebElement... elements);

	public void waitForAllElementPresent(QAFWebElement... elements);

	public void waitForAnyElementVisible(QAFWebElement... elements);

	public void waitForAllElementVisible(QAFWebElement... elements);
	
	
	public void waitForWindowTitle(StringMatcher titlematcher, long... timeout);
	
	public void waitForCurrentUrl(StringMatcher matcher, long... timeout) ;
	
	public void waitForNoOfWindows(int count, long... timeout);
	
	public boolean verifyTitle(StringMatcher text, long... timeout);

	public boolean verifyCurrentUrl(StringMatcher text, long... timeout);

	public boolean verifyNoOfWindows(int count, long... timeout);

	public void assertTitle(StringMatcher text, long... timeout);
	
	
	public void assertCurrentUrl(StringMatcher text, long... timeout);

}
