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


package com.infostretch.automation.ui.selenium.customcommands;

import com.infostretch.automation.ui.selenium.JavaScriptHelper;
import com.infostretch.automation.ui.selenium.QAFCustomCommand;
import com.infostretch.automation.ui.selenium.SeleniumCommandProcessor;

/**
 * com.infostretch.automation.ui.selenium.customcommands.GetCssPropertyCommand.
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
