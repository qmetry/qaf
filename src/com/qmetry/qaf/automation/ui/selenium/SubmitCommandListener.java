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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * com.qmetry.qaf.automation.ui.selenium.SubmitCommandListener.java
 * chirag
 */
/**
 * @author chirag
 */
public class SubmitCommandListener implements SeleniumCommandListener {
	Log log = LogFactoryImpl.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * afterCommand (com.thoughtworks.selenium.HttpCommandProcessor,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		if (commandTracker.getCommand().equalsIgnoreCase("submit") && (null != commandTracker.getArgs())
				&& (commandTracker.getArgs().length > 0)) {
			log.info("Unable to submit field! trying by key press");

			try {
				if (commandTracker.hasException()) {
					proc.doCommand("keyPress", new String[] { commandTracker.getArgs()[0], "\\13" });
					commandTracker.setException(null);
				}
			} catch (Throwable t) {
				log.error(t.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * beforeCommand (com.thoughtworks.selenium.HttpCommandProcessor,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public void beforeCommand(QAFCommandProcessor proc, SeleniumCommandTracker sc) {
	}
}
