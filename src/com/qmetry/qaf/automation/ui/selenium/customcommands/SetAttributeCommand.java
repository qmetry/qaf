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
package com.qmetry.qaf.automation.ui.selenium.customcommands;

import static org.apache.commons.lang.StringUtils.isNumeric;

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
		if (!(isNumeric(value) || "true".equals(value) || "false".equals(value) || value.startsWith("'")
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
