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


package com.qmetry.qaf.automation.ui.selenium.customcommands;

import org.apache.commons.lang.StringUtils;

import com.qmetry.qaf.automation.ui.selenium.QAFCustomCommand;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor;

/**
 * com.qmetry.qaf.automation.ui.selenium.webdriver.DriverSkippedCommand.java
 * 
 * @author chirag
 */
public class SetAttributeCommand implements QAFCustomCommand {

	@Override
	public String doCommand(SeleniumCommandProcessor commandProcessor, String... args) {
		int atSign = args[0].lastIndexOf("@");
		String elementLocator = args[0].substring(0, atSign).trim();
		String attributeName = args[0].substring(atSign + 1).trim();
		String value = args[1];
		if (!(StringUtils.isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
				|| value.startsWith("\""))) {
			value = "\"" + value + "\"";
		}
		String snippet = "";
		try {
			snippet = String.format("selenium.page().findElement(\"%s\").setAttribute('%s',%s);", elementLocator,
					attributeName, value);
		} catch (Exception e) {
			if (attributeName.equalsIgnoreCase("style")) {
				snippet = String.format("selenium.page().findElement(\"%s\").style.setAttribute('cssText',%s);",
						elementLocator, value);
			} else if (attributeName.equalsIgnoreCase("class")) {
				snippet = String.format("selenium.page().findElement(\"%s\").className=%s);", elementLocator, value);
			} else {
				snippet = String.format("selenium.page().findElement(\"%s\").%s=%s;", elementLocator, attributeName,
						value);
			}
		}

		return commandProcessor.doDefaultDoCommand("getEval", new String[] { snippet, "" });
	}

	@Override
	public String getCommandName() {
		return "setAttribute";
	}
}
