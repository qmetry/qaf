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


package com.qmetry.qaf.automation.ui.selenium;

import java.awt.Dimension;

import org.apache.commons.lang.StringUtils;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * com.qmetry.qaf.automation.ui.selenium.IsSeleniumImpl.java
 * 
 * @author chirag
 */

public class IsSeleniumImpl extends DefaultSelenium implements IsSelenium {

	/**
	 * @param processor
	 */
	public IsSeleniumImpl(CommandProcessor processor) {
		super(processor);
	}

	/**
	 * @param serverHost
	 * @param serverPort
	 * @param browserStartCommand
	 * @param browserURL
	 */
	public IsSeleniumImpl(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		super(serverHost, serverPort, browserStartCommand, browserURL);
	}

	protected void handleException(SeleniumException e, String command, String target, String args) {
		System.out.println(e.getMessage());
		System.out.println("Failed command : " + command + "(" + target + ", " + args + ")");
	}

	protected String executeCommand(String command, String target, String args) throws SeleniumException {
		String retval = "";
		try {
			retval = commandProcessor.doCommand(command, new String[] { target, args });
		} catch (SeleniumException e) {
			handleException(e, command, target, args);
			throw e;
		}
		return retval;
	}

	public String getFlexSelectedItemAtIndex(String target, String args) throws SeleniumException {
		return executeCommand("getFlexSelectedItemAtIndex", target, args).replace("OK,", "");
	}

	public String getFlexSelectedItemAtIndex(String target) throws SeleniumException {
		return executeCommand("getFlexSelectedItemAtIndex", target, "").replace("OK,", "");
	}

	public String getFlexNumSelectedItems(String target, String args) throws SeleniumException {
		return executeCommand("getFlexNumSelectedItems", target, args).replace("OK,", "");
	}

	public String getFlexNumSelectedItems(String target) throws SeleniumException {
		return executeCommand("getFlexNumSelectedItems", target, "").replace("OK,", "");
	}

	public String getFlexVisible(String target, String args) throws SeleniumException {
		return executeCommand("getFlexVisible", target, args).replace("OK,", "");
	}

	public String getFlexVisible(String target) throws SeleniumException {
		return executeCommand("getFlexVisible", target, "").replace("OK,", "");
	}

	public String getFlexTextPresent(String target, String args) throws SeleniumException {
		return executeCommand("getFlexTextPresent", target, args).replace("OK,", "");
	}

	public String getFlexTextPresent(String target) throws SeleniumException {
		return executeCommand("getFlexTextPresent", target, "").replace("OK,", "");
	}

	public String getFlexText(String target, String args) throws SeleniumException {
		return executeCommand("getFlexText", target, args).replace("OK,", "");
	}

	public String getFlexText(String target) throws SeleniumException {
		return executeCommand("getFlexText", target, "").replace("OK,", "");
	}

	public String getFlexStepper(String target, String args) throws SeleniumException {
		return executeCommand("getFlexStepper", target, args).replace("OK,", "");
	}

	public String getFlexStepper(String target) throws SeleniumException {
		return executeCommand("getFlexStepper", target, "").replace("OK,", "");
	}

	public String getFlexSelectionIndex(String target, String args) throws SeleniumException {
		return executeCommand("getFlexSelectionIndex", target, args).replace("OK,", "");
	}

	public String getFlexSelectionIndex(String target) throws SeleniumException {
		return executeCommand("getFlexSelectionIndex", target, "").replace("OK,", "");
	}

	public String getFlexSelection(String target, String args) throws SeleniumException {
		return executeCommand("getFlexSelection", target, args).replace("OK,", "");
	}

	public String getFlexSelection(String target) throws SeleniumException {
		return executeCommand("getFlexSelection", target, "").replace("OK,", "");
	}

	public String getFlexRadioButton(String target, String args) throws SeleniumException {
		return executeCommand("getFlexRadioButton", target, args).replace("OK,", "");
	}

	public String getFlexRadioButton(String target) throws SeleniumException {
		return executeCommand("getFlexRadioButton", target, "").replace("OK,", "");
	}

	public String getFlexProperty(String target, String args) throws SeleniumException {
		return executeCommand("getFlexProperty", target, args).replace("OK,", "");
	}

	public String getFlexProperty(String target) throws SeleniumException {
		return executeCommand("getFlexProperty", target, "").replace("OK,", "");
	}

	public String getFlexParseInt(String target, String args) throws SeleniumException {
		return executeCommand("getFlexParseInt", target, args).replace("OK,", "");
	}

	public String getFlexParseInt(String target) throws SeleniumException {
		return executeCommand("getFlexParseInt", target, "").replace("OK,", "");
	}

	public String getFlexNumeric(String target, String args) throws SeleniumException {
		return executeCommand("getFlexNumeric", target, args).replace("OK,", "");
	}

	public String getFlexNumeric(String target) throws SeleniumException {
		return executeCommand("getFlexNumeric", target, "").replace("OK,", "");
	}

	public String getFlexGlobalPosition(String target, String args) throws SeleniumException {
		return executeCommand("getFlexGlobalPosition", target, args).replace("OK,", "");
	}

	public String getFlexGlobalPosition(String target) throws SeleniumException {
		return executeCommand("getFlexGlobalPosition", target, "").replace("OK,", "");
	}

	public String getFlexExists(String target, String args) throws SeleniumException {
		return executeCommand("getFlexExists", target, args).replace("OK,", "");
	}

	public String getFlexExists(String target) throws SeleniumException {
		return executeCommand("getFlexExists", target, "").replace("OK,", "");
	}

	public String getFlexErrorString(String target, String args) throws SeleniumException {
		return executeCommand("getFlexErrorString", target, args).replace("OK,", "");
	}

	public String getFlexErrorString(String target) throws SeleniumException {
		return executeCommand("getFlexErrorString", target, "").replace("OK,", "");
	}

	public String getFlexEnabled(String target, String args) throws SeleniumException {
		return executeCommand("getFlexEnabled", target, args).replace("OK,", "");
	}

	public String getFlexEnabled(String target) throws SeleniumException {
		return executeCommand("getFlexEnabled", target, "").replace("OK,", "");
	}

	public String getFlexDate(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDate", target, args).replace("OK,", "");
	}

	public String getFlexDate(String target) throws SeleniumException {
		return executeCommand("getFlexDate", target, "").replace("OK,", "");
	}

	public String getFlexDataGridUIComponentLabel(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridUIComponentLabel", target, args).replace("OK,", "");
	}

	public String getFlexDataGridUIComponentLabel(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridUIComponentLabel", target, "").replace("OK,", "");
	}

	public String getFlexDataGridRowIndexForFieldValue(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridRowIndexForFieldValue", target, args).replace("OK,", "");
	}

	public String getFlexDataGridRowIndexForFieldValue(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridRowIndexForFieldValue", target, "").replace("OK,", "");
	}

	public String getFlexDataGridRowCount(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridRowCount", target, args).replace("OK,", "");
	}

	public String getFlexDataGridRowCount(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridRowCount", target, "").replace("OK,", "");
	}

	public String getFlexDataGridFieldValueForGridRow(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridFieldValueForGridRow", target, args).replace("OK,", "");
	}

	public String getFlexDataGridFieldValueForGridRow(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridFieldValueForGridRow", target, "").replace("OK,", "");
	}

	public String getFlexDataGridCellText(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridCellText", target, args).replace("OK,", "");
	}

	public String getFlexDataGridCellText(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridCellText", target, "").replace("OK,", "");
	}

	public String getFlexDataGridCell(String target, String args) throws SeleniumException {
		return executeCommand("getFlexDataGridCell", target, args).replace("OK,", "");
	}

	public String getFlexDataGridCell(String target) throws SeleniumException {
		return executeCommand("getFlexDataGridCell", target, "").replace("OK,", "");
	}

	public String getFlexComponentInfo(String target, String args) throws SeleniumException {
		return executeCommand("getFlexComponentInfo", target, args).replace("OK,", "");
	}

	public String getFlexComponentInfo(String target) throws SeleniumException {
		return executeCommand("getFlexComponentInfo", target, "").replace("OK,", "");
	}

	public String getFlexComboContainsLabel(String target, String args) throws SeleniumException {
		return executeCommand("getFlexComboContainsLabel", target, args).replace("OK,", "");
	}

	public String getFlexComboContainsLabel(String target) throws SeleniumException {
		return executeCommand("getFlexComboContainsLabel", target, "").replace("OK,", "");
	}

	public String getFlexCheckBoxChecked(String target, String args) throws SeleniumException {
		return executeCommand("getFlexCheckBoxChecked", target, args).replace("OK,", "");
	}

	public String getFlexCheckBoxChecked(String target) throws SeleniumException {
		return executeCommand("getFlexCheckBoxChecked", target, "").replace("OK,", "");
	}

	public String getFlexAlertTextPresent(String target, String args) throws SeleniumException {
		return executeCommand("getFlexAlertTextPresent", target, args).replace("OK,", "");
	}

	public String getFlexAlertTextPresent(String target) throws SeleniumException {
		return executeCommand("getFlexAlertTextPresent", target, "").replace("OK,", "");
	}

	public String getFlexAlertText(String target, String args) throws SeleniumException {
		return executeCommand("getFlexAlertText", target, args).replace("OK,", "");
	}

	public String getFlexAlertText(String target) throws SeleniumException {
		return executeCommand("getFlexAlertText", target, "").replace("OK,", "");
	}

	public String getFlexAlertPresent(String target, String args) throws SeleniumException {
		return executeCommand("getFlexAlertPresent", target, args).replace("OK,", "");
	}

	public String getFlexAlertPresent(String target) throws SeleniumException {
		return executeCommand("getFlexAlertPresent", target, "").replace("OK,", "");
	}

	public String getFlexASProperty(String target, String args) throws SeleniumException {
		return executeCommand("getFlexASProperty", target, args).replace("OK,", "");
	}

	public String getFlexASProperty(String target) throws SeleniumException {
		return executeCommand("getFlexASProperty", target, "").replace("OK,", "");
	}

	public String getDataGridUIComponentLabel(String target, String args) throws SeleniumException {
		return executeCommand("getDataGridUIComponentLabel", target, args).replace("OK,", "");
	}

	public String getDataGridUIComponentLabel(String target) throws SeleniumException {
		return executeCommand("getDataGridUIComponentLabel", target, "").replace("OK,", "");
	}

	public String getDataGridCellText(String target, String args) throws SeleniumException {
		return executeCommand("getDataGridCellText", target, args).replace("OK,", "");
	}

	public String getDataGridCellText(String target) throws SeleniumException {
		return executeCommand("getDataGridCellText", target, "").replace("OK,", "");
	}

	public void doRefreshIDToolTips(String target, String args) throws SeleniumException {
		executeCommand("doRefreshIDToolTips", target, args);
	}

	public void doRefreshIDToolTips(String target) throws SeleniumException {
		executeCommand("doRefreshIDToolTips", target, "");
	}

	public void flexWaitForElementVisible(String target, String args) throws SeleniumException {
		executeCommand("flexWaitForElementVisible", target, args);
	}

	public void flexWaitForElementVisible(String target) throws SeleniumException {
		executeCommand("flexWaitForElementVisible", target, "");
	}

	public void flexWaitForElement(String target, String args) throws SeleniumException {
		executeCommand("flexWaitForElement", target, args);
	}

	public void flexWaitForElement(String target) throws SeleniumException {
		executeCommand("flexWaitForElement", target, "");
	}

	public void flexTypeAppend(String target, String args) throws SeleniumException {
		executeCommand("flexTypeAppend", target, args);
	}

	public void flexTypeAppend(String target) throws SeleniumException {
		executeCommand("flexTypeAppend", target, "");
	}

	public void flexType(String target, String args) throws SeleniumException {
		executeCommand("flexType", target, args);
	}

	public void flexType(String target) throws SeleniumException {
		executeCommand("flexType", target, "");
	}

	public void flexStepper(String target, String args) throws SeleniumException {
		executeCommand("flexStepper", target, args);
	}

	public void flexStepper(String target) throws SeleniumException {
		executeCommand("flexStepper", target, "");
	}

	public void flexSetFocus(String target, String args) throws SeleniumException {
		executeCommand("flexSetFocus", target, args);
	}

	public void flexSetFocus(String target) throws SeleniumException {
		executeCommand("flexSetFocus", target, "");
	}

	public void flexSetDataGridCell(String target, String args) throws SeleniumException {
		executeCommand("flexSetDataGridCell", target, args);
	}

	public void flexSetDataGridCell(String target) throws SeleniumException {
		executeCommand("flexSetDataGridCell", target, "");
	}

	public void flexSelectMatchingOnField(String target, String args) throws SeleniumException {
		executeCommand("flexSelectMatchingOnField", target, args);
	}

	public void flexSelectMatchingOnField(String target) throws SeleniumException {
		executeCommand("flexSelectMatchingOnField", target, "");
	}

	public void flexSelectIndex(String target, String args) throws SeleniumException {
		executeCommand("flexSelectIndex", target, args);
	}

	public void flexSelectIndex(String target) throws SeleniumException {
		executeCommand("flexSelectIndex", target, "");
	}

	public void flexSelectComboByLabel(String target, String args) throws SeleniumException {
		executeCommand("flexSelectComboByLabel", target, args);
	}

	public void flexSelectComboByLabel(String target) throws SeleniumException {
		executeCommand("flexSelectComboByLabel", target, "");
	}

	public void flexSelect(String target, String args) throws SeleniumException {
		executeCommand("flexSelect", target, args);
	}

	public void flexSelect(String target) throws SeleniumException {
		executeCommand("flexSelect", target, "");
	}

	public void flexRefreshIDToolTips(String target, String args) throws SeleniumException {
		executeCommand("flexRefreshIDToolTips", target, args);
	}

	public void flexRefreshIDToolTips(String target) throws SeleniumException {
		executeCommand("flexRefreshIDToolTips", target, "");
	}

	public void flexRadioButton(String target, String args) throws SeleniumException {
		executeCommand("flexRadioButton", target, args);
	}

	public void flexRadioButton(String target) throws SeleniumException {
		executeCommand("flexRadioButton", target, "");
	}

	public void flexProperty(String target, String args) throws SeleniumException {
		executeCommand("flexProperty", target, args);
	}

	public void flexProperty(String target) throws SeleniumException {
		executeCommand("flexProperty", target, "");
	}

	public void flexMouseUp(String target, String args) throws SeleniumException {
		executeCommand("flexMouseUp", target, args);
	}

	public void flexMouseUp(String target) throws SeleniumException {
		executeCommand("flexMouseUp", target, "");
	}

	public void flexMouseRollOver(String target, String args) throws SeleniumException {
		executeCommand("flexMouseRollOver", target, args);
	}

	public void flexMouseRollOver(String target) throws SeleniumException {
		executeCommand("flexMouseRollOver", target, "");
	}

	public void flexMouseRollOut(String target, String args) throws SeleniumException {
		executeCommand("flexMouseRollOut", target, args);
	}

	public void flexMouseRollOut(String target) throws SeleniumException {
		executeCommand("flexMouseRollOut", target, "");
	}

	public void flexMouseOver(String target, String args) throws SeleniumException {
		executeCommand("flexMouseOver", target, args);
	}

	public void flexMouseOver(String target) throws SeleniumException {
		executeCommand("flexMouseOver", target, "");
	}

	public void flexMouseMove(String target, String args) throws SeleniumException {
		executeCommand("flexMouseMove", target, args);
	}

	public void flexMouseMove(String target) throws SeleniumException {
		executeCommand("flexMouseMove", target, "");
	}

	public void flexMouseDown(String target, String args) throws SeleniumException {
		executeCommand("flexMouseDown", target, args);
	}

	public void flexMouseDown(String target) throws SeleniumException {
		executeCommand("flexMouseDown", target, "");
	}

	public void flexDragTo(String target, String args) throws SeleniumException {
		executeCommand("flexDragTo", target, args);
	}

	public void flexDragTo(String target) throws SeleniumException {
		executeCommand("flexDragTo", target, "");
	}

	public void flexDoubleClick(String target, String args) throws SeleniumException {
		executeCommand("flexDoubleClick", target, args);
	}

	public void flexDoubleClick(String target) throws SeleniumException {
		executeCommand("flexDoubleClick", target, "");
	}

	public void flexDate(String target, String args) throws SeleniumException {
		executeCommand("flexDate", target, args);
	}

	public void flexDate(String target) throws SeleniumException {
		executeCommand("flexDate", target, "");
	}

	public void flexClickMenuBarUIComponent(String target, String args) throws SeleniumException {
		executeCommand("flexClickMenuBarUIComponent", target, args);
	}

	public void flexClickMenuBarUIComponent(String target) throws SeleniumException {
		executeCommand("flexClickMenuBarUIComponent", target, "");
	}

	public void flexClickDataGridUIComponent(String target, String args) throws SeleniumException {
		executeCommand("flexClickDataGridUIComponent", target, args);
	}

	public void flexClickDataGridUIComponent(String target) throws SeleniumException {
		executeCommand("flexClickDataGridUIComponent", target, "");
	}

	public void flexClickDataGridItem(String target, String args) throws SeleniumException {
		executeCommand("flexClickDataGridItem", target, args);
	}

	public void flexClickDataGridItem(String target) throws SeleniumException {
		executeCommand("flexClickDataGridItem", target, "");
	}

	public void flexClick(String target, String args) throws SeleniumException {
		executeCommand("flexClick", target, args);
	}

	public void flexClick(String target) throws SeleniumException {
		executeCommand("flexClick", target, "");
	}

	public void flexCheckBox(String target, String args) throws SeleniumException {
		executeCommand("flexCheckBox", target, args);
	}

	public void flexCheckBox(String target) throws SeleniumException {
		executeCommand("flexCheckBox", target, "");
	}

	public void flexAlertResponse(String target, String args) throws SeleniumException {
		executeCommand("flexAlertResponse", target, args);
	}

	public void flexAlertResponse(String target) throws SeleniumException {
		executeCommand("flexAlertResponse", target, "");
	}

	public void flexAddSelectMatchingOnField(String target, String args) throws SeleniumException {
		executeCommand("flexAddSelectMatchingOnField", target, args);
	}

	public void flexAddSelectMatchingOnField(String target) throws SeleniumException {
		executeCommand("flexAddSelectMatchingOnField", target, "");
	}

	public void flexAddSelectIndex(String target, String args) throws SeleniumException {
		executeCommand("flexAddSelectIndex", target, args);
	}

	public void flexAddSelectIndex(String target) throws SeleniumException {
		executeCommand("flexAddSelectIndex", target, "");
	}

	public String flexCall(String method, String... args) throws SeleniumException {
		StringBuilder argStr = new StringBuilder("");
		for (String arg : args) {
			argStr.append(",");
			argStr.append("'" + arg + "'");
		}
		String expr = String.format("selenium.callFlexMethod(%s%s)", method, argStr.toString());
		return this.getEval(expr);
	}

	@Override
	public Dimension getScreenSize() throws SeleniumException {
		int w = Integer.parseInt(getEval(JavaScriptHelper.getExpression("screen.width")));
		int h = Integer.parseInt(getEval(JavaScriptHelper.getExpression("screen.height")));

		return new Dimension(w, h);
	}

	@Override
	public Dimension getScreenAvailSize() throws SeleniumException {
		int w = Integer.parseInt(getEval(JavaScriptHelper.getExpression("screen.availWidth")));
		int h = Integer.parseInt(getEval(JavaScriptHelper.getExpression("screen.availHeight")));

		return new Dimension(w, h);
	}

	@Override
	public Dimension getDocumentSize() throws SeleniumException {
		String js =
				// "new function(){" +
				"var winW = 630, winH = 460;" + "if (document.body && document.body.offsetWidth) {"
						+ " winW = document.body.offsetWidth;" + " winH = document.body.offsetHeight;" + "}"
						+ "if (document.compatMode=='CSS1Compat' && document.documentElement && document.documentElement.offsetWidth ) {"
						+ " winW = document.documentElement.offsetWidth;"
						+ " winH = document.documentElement.offsetHeight;" + "}"
						+ "if (window.innerWidth && window.innerHeight) {" + " winW = window.innerWidth;"
						+ " winH = window.innerHeight;" + "}" + "winW+','+winH;";
		// + "}";
		String[] d = getEval(JavaScriptHelper.getExpression(js)).split(",");

		return new Dimension(Integer.parseInt(d[0]), Integer.parseInt(d[1]));
	}

	@Override
	public void setAttribute(String attributeLocator, String value) {
		int atSign = attributeLocator.lastIndexOf("@");
		String elementLocator = attributeLocator.substring(0, atSign);
		String attributeName = attributeLocator.substring(atSign + 1);

		if (!(StringUtils.isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
				|| value.startsWith("\""))) {
			value = "'" + value + "'";
		}
		String snippet = String.format("selenium.page().findElement('%s').%s=%s;", elementLocator, attributeName,
				value);
		executeCommand("getEval", snippet, "");
	}

	@Override
	public String getCssProperty(String attributeLocator) {
		return executeCommand("getCssProperty", attributeLocator, "");
	}

	@Override
	public String exeCustomCommand(String... args) {
		String command = args[0];
		String target = args.length > 1 ? args[1] : "";
		String value = args.length > 2 ? args[2] : "";
		return executeCommand(command, target, value);
	}

	@Override
	public String takeScreenShot() {
		try {
			return captureEntirePageScreenshotToString(
					ConfigurationManager.getBundle().getString("selenium.screenshots.kwargs", ""));
		} catch (Exception e) {
			return captureScreenshotToString();
		}
	}
}
