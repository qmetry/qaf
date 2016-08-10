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


package com.infostretch.automation.ui.selenium;

import java.awt.Dimension;

import com.infostretch.automation.ui.UiDriver;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * com.infostretch.automation.ui.selenium.IsSelenium.java
 * 
 * @author chirag
 */

public interface IsSelenium extends Selenium, UiDriver {
	// flex commands
	public String getFlexSelectedItemAtIndex(String target, String args) throws SeleniumException;

	public String getFlexSelectedItemAtIndex(String target) throws SeleniumException;

	public String getFlexNumSelectedItems(String target, String args) throws SeleniumException;

	public String getFlexNumSelectedItems(String target) throws SeleniumException;

	public String getFlexVisible(String target, String args) throws SeleniumException;

	public String getFlexVisible(String target) throws SeleniumException;

	public String getFlexTextPresent(String target, String args) throws SeleniumException;

	public String getFlexTextPresent(String target) throws SeleniumException;

	public String getFlexText(String target, String args) throws SeleniumException;

	public String getFlexText(String target) throws SeleniumException;

	public String getFlexStepper(String target, String args) throws SeleniumException;

	public String getFlexStepper(String target) throws SeleniumException;

	public String getFlexSelectionIndex(String target, String args) throws SeleniumException;

	public String getFlexSelectionIndex(String target) throws SeleniumException;

	public String getFlexSelection(String target, String args) throws SeleniumException;

	public String getFlexSelection(String target) throws SeleniumException;

	public String getFlexRadioButton(String target, String args) throws SeleniumException;

	public String getFlexRadioButton(String target) throws SeleniumException;

	public String getFlexProperty(String target, String args) throws SeleniumException;

	public String getFlexProperty(String target) throws SeleniumException;

	public String getFlexParseInt(String target, String args) throws SeleniumException;

	public String getFlexParseInt(String target) throws SeleniumException;

	public String getFlexNumeric(String target, String args) throws SeleniumException;

	public String getFlexNumeric(String target) throws SeleniumException;

	public String getFlexGlobalPosition(String target, String args) throws SeleniumException;

	public String getFlexGlobalPosition(String target) throws SeleniumException;

	public String getFlexExists(String target, String args) throws SeleniumException;

	public String getFlexExists(String target) throws SeleniumException;

	public String getFlexErrorString(String target, String args) throws SeleniumException;

	public String getFlexErrorString(String target) throws SeleniumException;

	public String getFlexEnabled(String target, String args) throws SeleniumException;

	public String getFlexEnabled(String target) throws SeleniumException;

	public String getFlexDate(String target, String args) throws SeleniumException;

	public String getFlexDate(String target) throws SeleniumException;

	public String getFlexDataGridUIComponentLabel(String target, String args) throws SeleniumException;

	public String getFlexDataGridUIComponentLabel(String target) throws SeleniumException;

	public String getFlexDataGridRowIndexForFieldValue(String target, String args) throws SeleniumException;

	public String getFlexDataGridRowIndexForFieldValue(String target) throws SeleniumException;

	public String getFlexDataGridRowCount(String target, String args) throws SeleniumException;

	public String getFlexDataGridRowCount(String target) throws SeleniumException;

	public String getFlexDataGridFieldValueForGridRow(String target, String args) throws SeleniumException;

	public String getFlexDataGridFieldValueForGridRow(String target) throws SeleniumException;

	public String getFlexDataGridCellText(String target, String args) throws SeleniumException;

	public String getFlexDataGridCellText(String target) throws SeleniumException;

	public String getFlexDataGridCell(String target, String args) throws SeleniumException;

	public String getFlexDataGridCell(String target) throws SeleniumException;

	public String getFlexComponentInfo(String target, String args) throws SeleniumException;

	public String getFlexComponentInfo(String target) throws SeleniumException;

	public String getFlexComboContainsLabel(String target, String args) throws SeleniumException;

	public String getFlexComboContainsLabel(String target) throws SeleniumException;

	public String getFlexCheckBoxChecked(String target, String args) throws SeleniumException;

	public String getFlexCheckBoxChecked(String target) throws SeleniumException;

	public String getFlexAlertTextPresent(String target, String args) throws SeleniumException;

	public String getFlexAlertTextPresent(String target) throws SeleniumException;

	public String getFlexAlertText(String target, String args) throws SeleniumException;

	public String getFlexAlertText(String target) throws SeleniumException;

	public String getFlexAlertPresent(String target, String args) throws SeleniumException;

	public String getFlexAlertPresent(String target) throws SeleniumException;

	public String getFlexASProperty(String target, String args) throws SeleniumException;

	public String getFlexASProperty(String target) throws SeleniumException;

	public String getDataGridUIComponentLabel(String target, String args) throws SeleniumException;

	public String getDataGridUIComponentLabel(String target) throws SeleniumException;

	public String getDataGridCellText(String target, String args) throws SeleniumException;

	public String getDataGridCellText(String target) throws SeleniumException;

	public void doRefreshIDToolTips(String target, String args) throws SeleniumException;

	public void doRefreshIDToolTips(String target) throws SeleniumException;

	public void flexWaitForElementVisible(String target, String args) throws SeleniumException;

	public void flexWaitForElementVisible(String target) throws SeleniumException;

	public void flexWaitForElement(String target, String args) throws SeleniumException;

	public void flexWaitForElement(String target) throws SeleniumException;

	public void flexTypeAppend(String target, String args) throws SeleniumException;

	public void flexTypeAppend(String target) throws SeleniumException;

	public void flexType(String target, String args) throws SeleniumException;

	public void flexType(String target) throws SeleniumException;

	public void flexStepper(String target, String args) throws SeleniumException;

	public void flexStepper(String target) throws SeleniumException;

	public void flexSetFocus(String target, String args) throws SeleniumException;

	public void flexSetFocus(String target) throws SeleniumException;

	public void flexSetDataGridCell(String target, String args) throws SeleniumException;

	public void flexSetDataGridCell(String target) throws SeleniumException;

	public void flexSelectMatchingOnField(String target, String args) throws SeleniumException;

	public void flexSelectMatchingOnField(String target) throws SeleniumException;

	public void flexSelectIndex(String target, String args) throws SeleniumException;

	public void flexSelectIndex(String target) throws SeleniumException;

	public void flexSelectComboByLabel(String target, String args) throws SeleniumException;

	public void flexSelectComboByLabel(String target) throws SeleniumException;

	public void flexSelect(String target, String args) throws SeleniumException;

	public void flexSelect(String target) throws SeleniumException;

	public void flexRefreshIDToolTips(String target, String args) throws SeleniumException;

	public void flexRefreshIDToolTips(String target) throws SeleniumException;

	public void flexRadioButton(String target, String args) throws SeleniumException;

	public void flexRadioButton(String target) throws SeleniumException;

	public void flexProperty(String target, String args) throws SeleniumException;

	public void flexProperty(String target) throws SeleniumException;

	public void flexMouseUp(String target, String args) throws SeleniumException;

	public void flexMouseUp(String target) throws SeleniumException;

	public void flexMouseRollOver(String target, String args) throws SeleniumException;

	public void flexMouseRollOver(String target) throws SeleniumException;

	public void flexMouseRollOut(String target, String args) throws SeleniumException;

	public void flexMouseRollOut(String target) throws SeleniumException;

	public void flexMouseOver(String target, String args) throws SeleniumException;

	public void flexMouseOver(String target) throws SeleniumException;

	public void flexMouseMove(String target, String args) throws SeleniumException;

	public void flexMouseMove(String target) throws SeleniumException;

	public void flexMouseDown(String target, String args) throws SeleniumException;

	public void flexMouseDown(String target) throws SeleniumException;

	public void flexDragTo(String target, String args) throws SeleniumException;

	public void flexDragTo(String target) throws SeleniumException;

	public void flexDoubleClick(String target, String args) throws SeleniumException;

	public void flexDoubleClick(String target) throws SeleniumException;

	public void flexDate(String target, String args) throws SeleniumException;

	public void flexDate(String target) throws SeleniumException;

	public void flexClickMenuBarUIComponent(String target, String args) throws SeleniumException;

	public void flexClickMenuBarUIComponent(String target) throws SeleniumException;

	public void flexClickDataGridUIComponent(String target, String args) throws SeleniumException;

	public void flexClickDataGridUIComponent(String target) throws SeleniumException;

	public void flexClickDataGridItem(String target, String args) throws SeleniumException;

	public void flexClickDataGridItem(String target) throws SeleniumException;

	public void flexClick(String target, String args) throws SeleniumException;

	public void flexClick(String target) throws SeleniumException;

	public void flexCheckBox(String target, String args) throws SeleniumException;

	public void flexCheckBox(String target) throws SeleniumException;

	public void flexAlertResponse(String target, String args) throws SeleniumException;

	public void flexAlertResponse(String target) throws SeleniumException;

	public void flexAddSelectMatchingOnField(String target, String args) throws SeleniumException;

	public void flexAddSelectMatchingOnField(String target) throws SeleniumException;

	public void flexAddSelectIndex(String target, String args) throws SeleniumException;

	public void flexAddSelectIndex(String target) throws SeleniumException;

	/**
	 * This method can be used to call flash/flex methods not available in
	 * implementation.
	 * 
	 * @param method
	 * @param args
	 * @return
	 * @throws SeleniumException
	 */
	public String flexCall(String method, String... args) throws SeleniumException;

	public Dimension getScreenSize() throws SeleniumException;

	public Dimension getScreenAvailSize() throws SeleniumException;

	public Dimension getDocumentSize() throws SeleniumException;

	/**
	 * @param attributeLocator
	 *            - an element locator followed by an @ sign and then the name
	 *            of the attribute, e.g. "foo@bar"
	 * @param value
	 *            - value to set
	 */
	public void setAttribute(String attributeLocator, String value);

	public String getCssProperty(String attributeLocator);

	public String exeCustomCommand(String... args);

}
