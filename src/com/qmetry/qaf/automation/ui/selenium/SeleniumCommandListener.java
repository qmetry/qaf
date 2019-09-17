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
package com.qmetry.qaf.automation.ui.selenium;

import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener.java To create
 * new selenium command listener implement this interface and register it use
 * proerty {@link ApplicationProperties#SELENIUM_CMD_LISTENERS}
 * (selenium.command.listeners)
 * 
 * @author chirag
 */

public interface SeleniumCommandListener {
	/**
	 * This method is invoked before doCommand by command processor
	 * 
	 * @param commandName
	 *            - the remote command verb
	 * @param args
	 *            - the arguments to the remote command (depends on the verb)
	 */
	public void beforeCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker);

	/**
	 * This method is invoked after doCommand by command processor
	 * 
	 * @param commandName
	 *            - the remote command verb
	 * @param args
	 *            - the arguments to the remote command (depends on the verb)
	 * @param result
	 *            - the command result, defined by the remote JavaScript. "getX"
	 *            style commands may return data from the browser; other "doX"
	 *            style commands may just return "OK" or an error message.
	 */
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker);
}
