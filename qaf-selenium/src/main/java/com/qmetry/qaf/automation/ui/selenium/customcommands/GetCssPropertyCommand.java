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

import com.qmetry.qaf.automation.ui.selenium.JavaScriptHelper;
import com.qmetry.qaf.automation.ui.selenium.QAFCustomCommand;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor;

/**
 * com.qmetry.qaf.automation.ui.selenium.customcommands.GetCssPropertyCommand.
 * java <code><pre>
 * function getStyle(el,styleProp){
 * var x = document.getElementById(el);
 * if (x.currentStyle)
 * return x.currentStyle[styleProp];
 * if (window.getComputedStyle)
 * return
 * document.defaultView.getComputedStyle(x,null).getPropertyValue(styleProp);
 * return "";
 * }</pre>
 * </code>
 * 
 * @author chirag
 */
public class GetCssPropertyCommand implements QAFCustomCommand {

	@Override
	public String doCommand(SeleniumCommandProcessor commandProcessor, String... args) {
		int atSign = args[0].lastIndexOf("@");
		String elementLocator = args[0].substring(0, atSign);
		String property = args[0].substring(atSign + 1);

		String snipet = String.format(
				"var ele=selenium.page().findElement(\"%s\");" + "(ele.currentStyle)? ele.currentStyle[\"%s\"]:"
						+ "document.defaultView.getComputedStyle(ele,null).getPropertyValue(\"%s\");" + "",
				elementLocator, property, property);
		return commandProcessor.getString("getEval", new String[] { JavaScriptHelper.getExpression(snipet), "" });
	}

	@Override
	public String getCommandName() {
		return "getCssProperty";
	}

}
