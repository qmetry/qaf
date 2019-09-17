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
package com.qmetry.qaf.automation.step;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

/**
 * This class will provide predefined steps to be used with web-element.
 * com.qmetry.qaf.automation.step.WebElementStepLib.java
 * 
 * @author chirag
 */
// @QAFTestStepProvider
public class WebElementStepLib {

	/***
	 * locate/find an element in browser
	 * 
	 * @param loc
	 * @return
	 */
	public static QAFWebElement defineElement(String loc) {
		return new QAFExtendedWebElement(loc);
	}

	/**
	 * To locate/find child element of an element
	 * 
	 * @param ele
	 * @param loc
	 * @return
	 */
	public static QAFWebElement defineChildElement(QAFExtendedWebElement ele, String loc) {
		return new QAFExtendedWebElement(ele, loc);
	}

	/**
	 * To enter text into field
	 * 
	 * @param ele
	 * @param s
	 */
	public static void sendKeysToElement(QAFWebElement ele, String s) {
		ele.sendKeys(s);
	}

	/**
	 * To verify element present
	 * 
	 * @param ele
	 * @return
	 */
	public static boolean verifyElementPresent(QAFWebElement ele) {
		return ele.verifyPresent();
	}

	/**
	 * To assert element present
	 * 
	 * @param ele
	 */
	public static void assertElementPresent(QAFWebElement ele) {
		ele.assertPresent();
	}

	/**
	 * To assert text present
	 * 
	 * @param ele
	 * @param text
	 */
	public static void assertElementText(QAFWebElement ele, String text) {
		ele.assertText(text);
	}

	/**
	 * To assert element attribute by passing attribute name & text expected
	 * 
	 * @param ele
	 * @param attr
	 * @param text
	 */
	public static void assertElementAttribute(QAFWebElement ele, String attr, String text) {
		ele.assertAttribute(attr, text);
	}

	/**
	 * To assert element by css class
	 * 
	 * @param ele
	 * @param arg1
	 * @param arg2
	 */
	public static void assertElementCssClass(QAFWebElement ele, String arg1, String arg2) {
		ele.assertCssClass(arg1, arg2);
	}

	/**
	 * To assert element by css style
	 * 
	 * @param ele
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void assertElementCssStyle(QAFWebElement ele, String arg1, String arg2, String arg3) {
		ele.assertCssStyle(arg1, arg2, arg3);
	}

	/**
	 * To assert element disabled
	 * 
	 * @param ele
	 * @param arg1
	 */
	public static void assertElementDisabled(QAFWebElement ele, String arg1) {
		ele.assertDisabled(arg1);
	}

	/**
	 * To assert enabled
	 * 
	 * @param ele
	 * @param arg1
	 */
	public static void assertElementEnabled(QAFWebElement ele, String arg1) {
		ele.assertEnabled(arg1);
	}

	/**
	 * To assert element not present by attribute value
	 * 
	 * @param ele
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void assertElementNotAttribute(QAFWebElement ele, String arg1, String arg2, String arg3) {
		ele.assertNotAttribute(arg1, arg2, arg3);
	}

	/**
	 * To assert element not present by css class
	 * 
	 * @param ele
	 * @param arg1
	 * @param arg2
	 */
	public static void assertElementNotCssClass(QAFWebElement ele, String arg1, String arg2) {
		ele.assertNotCssClass(arg1, arg2);
	}

	/**
	 * To assert element not present by css style
	 * 
	 * @param ele
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public static void assertElementNotCssStyle(QAFWebElement ele, String arg1, String arg2, String arg3) {
		ele.assertNotCssStyle(arg1, arg2, arg3);
	}

	/**
	 * To assert element not present
	 * 
	 * @param ele
	 * @param arg1
	 */
	public static void assertElementNotPresent(QAFWebElement ele, String arg1) {
		ele.assertNotPresent(arg1);
	}

	/**
	 * To assert element not selected
	 * 
	 * @param ele
	 * @param arg1
	 */
	public static void assertElementNotSelected(QAFWebElement ele, String arg1) {
		ele.assertNotSelected(arg1);
	}

	/**
	 * To assert element not value
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void assertElementNotValue(QAFWebElement ele, String arg0, String arg1) {
		ele.assertNotValue(arg0, arg1);
	}

	/**
	 * To assert element not visible
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void assertElementNotVisible(QAFWebElement ele, String arg0) {
		ele.assertNotVisible(arg0);
	}

	/**
	 * /** To assert element selected
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void assertElementSelected(QAFWebElement ele, String arg0) {
		ele.assertSelected(arg0);
	}

	/**
	 * To assert element value
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void assertElementValue(QAFWebElement ele, Object arg0, String arg1) {
		ele.assertValue(arg0, arg1);
	}

	/**
	 * To assert element visible
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void assertElementVisible(QAFWebElement ele, String arg0) {
		ele.assertVisible(arg0);
	}

	/**
	 * To assert element not text
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void assertElementNotText(QAFWebElement ele, String arg0) {
		ele.assertNotText(arg0);
	}

	/**
	 * To clear element
	 * 
	 * @param ele
	 */
	public static void clearElement(QAFWebElement ele) {
		ele.clear();
	}

	/**
	 * To click element
	 * 
	 * @param ele
	 */
	public static void clickElement(QAFWebElement ele) {
		ele.click();
	}

	/**
	 * To verify given element not present
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void givenElementNotPresent(QAFWebElement ele, String arg0) {
		ele.givenNotPresent(arg0);
	}

	/**
	 * To verify given element present
	 * 
	 * @param ele
	 */
	public static void givenElementPresent(QAFWebElement ele) {
		ele.givenPresent();
	}

	/**
	 * To set specified attribute of element
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void setElementAttribute(QAFWebElement ele, String arg0, String arg1) {
		ele.setAttribute(arg0, arg1);
	}

	/**
	 * To perform submit click
	 * 
	 * @param ele
	 */
	public static void submitElement(QAFWebElement ele) {
		ele.submit();
	}

	/**
	 * To verify specified attribute of element
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean verifyElementAttribute(QAFWebElement ele, String arg0, String arg1, String arg2) {
		return ele.verifyAttribute(arg0, arg1, arg2);
	}

	/**
	 * To verify element css class
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementCssClass(QAFWebElement ele, String arg0, String arg1) {
		return ele.verifyCssClass(arg0, arg1);
	}

	/**
	 * To verify element css style
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean verifyElementCssStyle(QAFWebElement ele, String arg0, String arg1, String arg2) {
		return ele.verifyCssStyle(arg0, arg1, arg2);
	}

	/**
	 * To verify element displayed
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementDisabled(QAFWebElement ele, String arg0) {
		return ele.verifyDisabled(arg0);
	}

	/**
	 * To verify element enabled
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementEnabled(QAFWebElement ele, String arg0) {
		return ele.verifyEnabled(arg0);
	}

	/**
	 * To verify element by not attribute
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean verifyElementNotAttribute(QAFWebElement ele, String arg0, String arg1, String arg2) {
		return ele.verifyNotAttribute(arg0, arg1, arg2);
	}

	/**
	 * To verify element by not css class
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementNotCssClass(QAFWebElement ele, String arg0, String arg1) {
		return ele.verifyNotCssClass(arg0, arg1);
	}

	/**
	 * To verify element by not css style
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean verifyElementNotCssStyle(QAFWebElement ele, String arg0, String arg1, String arg2) {
		return ele.verifyNotCssStyle(arg0, arg1, arg2);
	}

	/**
	 * To verify element not present
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementNotPresent(QAFWebElement ele, String arg0) {
		return ele.verifyNotPresent(arg0);
	}

	/**
	 * To verify element not present
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementNotSelected(QAFWebElement ele, String arg0) {
		return ele.verifyNotSelected(arg0);
	}

	/**
	 * To verify element text not present
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementNotText(QAFWebElement ele, String arg0, String arg1) {
		return ele.verifyNotText(arg0, arg1);
	}

	/**
	 * To verify not value
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementNotValue(QAFWebElement ele, Object arg0, String arg1) {
		return ele.verifyNotValue(arg0, arg1);
	}

	/**
	 * To verify element not visible
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementNotVisible(QAFWebElement ele, String arg0) {
		return ele.verifyNotVisible(arg0);
	}

	/**
	 * To verify element selected
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementSelected(QAFWebElement ele, String arg0) {
		return ele.verifySelected(arg0);
	}

	/**
	 * To verify text
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementText(QAFWebElement ele, String arg0, String arg1) {
		return ele.verifyText(arg0, arg1);
	}

	/**
	 * To verify value
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static boolean verifyElementValue(QAFWebElement ele, Object arg0, String arg1) {
		return ele.verifyValue(arg0, arg1);
	}

	/**
	 * To verify element visible
	 * 
	 * @param ele
	 * @param arg0
	 * @return
	 */
	public static boolean verifyElementVisible(QAFWebElement ele, String arg0) {
		return ele.verifyVisible(arg0);
	}

	/**
	 * waitForAttribute
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void waitForElementAttribute(QAFWebElement ele, String arg0, String arg1) {
		ele.waitForAttribute(arg0, arg1);
	}

	/**
	 * waitForCssClass
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void waitForElementCssClass(QAFWebElement ele, String arg0) {
		ele.waitForCssClass(arg0);
	}

	/**
	 * waitForCssStyle
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void waitForElementCssStyle(QAFWebElement ele, String arg0, String arg1) {
		ele.waitForCssStyle(arg0, arg1);
	}

	/**
	 * waitForDisabled
	 * 
	 * @param ele
	 */
	public static void waitForElementDisabled(QAFWebElement ele) {
		ele.waitForDisabled();
	}

	/**
	 * waitForEnabled
	 * 
	 * @param ele
	 */
	public static void waitForElementEnabled(QAFWebElement ele) {
		ele.waitForEnabled();
	}

	/**
	 * waitForNotAttribute
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void waitForElementNotAttribute(QAFWebElement ele, String arg0, String arg1) {
		ele.waitForNotAttribute(arg0, arg1);
	}

	/**
	 * waitForNotCssClass
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void waitForElementNotCssClass(QAFWebElement ele, String arg0) {
		ele.waitForNotCssClass(arg0);
	}

	/**
	 * waitForNotCssStyle
	 * 
	 * @param ele
	 * @param arg0
	 * @param arg1
	 */
	public static void waitForElementNotCssStyle(QAFWebElement ele, String arg0, String arg1) {
		ele.waitForNotCssStyle(arg0, arg1);
	}

	/**
	 * waitForNotPresent
	 * 
	 * @param ele
	 */
	public static void waitForElementNotPresent(QAFWebElement ele) {
		ele.waitForNotPresent();
	}

	/**
	 * waitForNotSelected
	 * 
	 * @param ele
	 */
	public static void waitForElementNotSelected(QAFWebElement ele) {
		ele.waitForNotSelected();
	}

	/**
	 * waitForNotText
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void waitForElementNotText(QAFWebElement ele, String arg0) {
		ele.waitForNotText(arg0);
	}

	/**
	 * waitForNotValue
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void waitForElementNotValue(QAFWebElement ele, Object arg0) {
		ele.waitForNotValue(arg0);
	}

	/**
	 * waitForNotVisible
	 * 
	 * @param ele
	 */
	public static void waitForElementNotVisible(QAFWebElement ele) {
		ele.waitForNotVisible();
	}

	/**
	 * waitForPresent
	 * 
	 * @param ele
	 */
	public static void waitForElementPresent(QAFWebElement ele) {
		ele.waitForPresent();
	}

	/**
	 * waitForSelected
	 * 
	 * @param ele
	 */
	public static void waitForElementSelected(QAFWebElement ele) {
		ele.waitForSelected();
	}

	/**
	 * waitForText
	 * 
	 * @param ele
	 * @param arg0
	 */
	public static void waitForElementText(QAFWebElement ele, String arg0) {
		ele.waitForText(arg0);
	}

	/**
	 * waitForValue
	 * 
	 * @param ele
	 * @param obj
	 */
	public static void waitForElementValue(QAFWebElement ele, Object obj) {
		ele.waitForValue(obj);
	}

	/**
	 * waitForVisible
	 * 
	 * @param ele
	 */
	public static void waitForElementVisible(QAFWebElement ele) {
		ele.waitForVisible();
	}

	/**
	 * @param loc
	 * @param text
	 */
	public void type(String loc, String text) {
		new QAFExtendedWebElement(loc).sendKeys(text);
	}

	public void altKeyDown() {
		// TODO Auto-generated method stub

	}

	public void altKeyUp() {
		// TODO Auto-generated method stub

	}

	public void answerOnNextPrompt(String arg0) {
		// TODO Auto-generated method stub

	}

	public void check(String loc) {
		QAFExtendedWebElement ele = new QAFExtendedWebElement(loc);
		if (!ele.isSelected()) {
			ele.click();
		}

	}

	public void chooseCancelOnNextConfirmation() {
		// TODO Auto-generated method stub

	}

	public void chooseOkOnNextConfirmation() {
		// TODO Auto-generated method stub

	}

	public void click(String arg0) {
		// TODO Auto-generated method stub

	}

	public void clickAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void contextMenu(String arg0) {
		// TODO Auto-generated method stub

	}

	public void contextMenuAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void controlKeyDown() {
		// TODO Auto-generated method stub

	}

	public void controlKeyUp() {
		// TODO Auto-generated method stub

	}

	public void createCookie(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void deleteAllVisibleCookies() {
		// TODO Auto-generated method stub

	}

	public void deleteCookie(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void deselectPopUp() {
		// TODO Auto-generated method stub

	}

	public void doubleClick(String arg0) {
		// TODO Auto-generated method stub

	}

	public void doubleClickAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void dragAndDrop(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void dragAndDropToObject(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void dragdrop(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void fireEvent(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void focus(String arg0) {
		// TODO Auto-generated method stub

	}

	public String getAlert() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllWindowIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllWindowNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllWindowTitles() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAttributeFromAllWindows(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBodyText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConfirmation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCookie() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCookieByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getCssCount(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getCursorPosition(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getElementHeight(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getElementIndex(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getElementPositionLeft(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getElementPositionTop(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getElementWidth(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEval(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getExpression(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHtmlSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Number getMouseSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPrompt() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSelectOptions(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectedId(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSelectedIds(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectedIndex(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSelectedIndexes(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectedLabel(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSelectedLabels(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectedValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSelectedValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTable(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getWhetherThisFrameMatchFrameExpression(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getWhetherThisWindowMatchWindowExpression(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public Number getXpathCount(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void goBack() {
		// TODO Auto-generated method stub

	}

	public void highlight(String arg0) {
		// TODO Auto-generated method stub

	}

	public void ignoreAttributesWithoutValue(String arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isAlertPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChecked(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isConfirmationPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCookiePresent(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEditable(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isElementPresent(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOrdered(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPromptPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSomethingSelected(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTextPresent(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVisible(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void keyDown(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void keyDownNative(String arg0) {
		// TODO Auto-generated method stub

	}

	public void keyPress(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void keyPressNative(String arg0) {
		// TODO Auto-generated method stub

	}

	public void keyUp(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void keyUpNative(String arg0) {
		// TODO Auto-generated method stub

	}

	public void metaKeyDown() {
		// TODO Auto-generated method stub

	}

	public void metaKeyUp() {
		// TODO Auto-generated method stub

	}

	public void mouseDown(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseDownAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void mouseDownRight(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseDownRightAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void mouseMove(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseMoveAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void mouseOut(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseOver(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseUp(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseUpAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void mouseUpRight(String arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseUpRightAt(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void open(String arg0) {
		// TODO Auto-generated method stub

	}

	public void open(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void openWindow(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void removeAllSelections(String arg0) {
		// TODO Auto-generated method stub

	}

	public void removeScript(String arg0) {
		// TODO Auto-generated method stub

	}

	public void removeSelection(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void rollup(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void runScript(String arg0) {
		// TODO Auto-generated method stub

	}

	public void select(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void selectFrame(String arg0) {
		// TODO Auto-generated method stub

	}

	public void selectPopUp(String arg0) {
		// TODO Auto-generated method stub

	}

	public void selectWindow(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setBrowserLogLevel(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setContext(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setCursorPosition(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void setExtensionJs(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setMouseSpeed(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setSpeed(String arg0) {
		// TODO Auto-generated method stub

	}

	public void setTimeout(String arg0) {
		// TODO Auto-generated method stub

	}

	public void shiftKeyDown() {
		// TODO Auto-generated method stub

	}

	public void shiftKeyUp() {
		// TODO Auto-generated method stub

	}

	public void showContextualBanner() {
		// TODO Auto-generated method stub

	}

	public void showContextualBanner(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void shutDownSeleniumServer() {
		// TODO Auto-generated method stub

	}

	public void submit(String loc) {
		new QAFExtendedWebElement(loc).submit();
	}

	public void typeKeys(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void uncheck(String loc) {
		QAFExtendedWebElement ele = new QAFExtendedWebElement(loc);
		if (ele.isSelected()) {
			ele.click();
		}

	}

	public void waitForCondition(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void waitForFrameToLoad(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void waitForPageToLoad(String arg0) {
		// TODO Auto-generated method stub

	}

	public void waitForPopUp(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void windowFocus() {
		// TODO Auto-generated method stub

	}

	public void windowMaximize() {
		// TODO Auto-generated method stub

	}
}
