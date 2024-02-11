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
package com.qmetry.qaf.automation.ui.selenium.webdriver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.ui.selenium.QAFCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandTracker;

/**
 * This listener for InternetExplorer. Before capturing screen, it will focus
 * window and scroll screen to make last accessed element viewable in
 * screen-shot.
 * com.qmetry.qaf.automation.ui.selenium.IEScreenCuptureListner.java
 */
/**
 * @author chirag
 */
public class GetEvalListener implements SeleniumCommandListener {
	private final Log logger = LogFactoryImpl.getLog(GetEvalListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * afterCommand (com.thoughtworks.selenium.HttpCommandProcessor,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker c) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * beforeCommand (com.thoughtworks.selenium.HttpCommandProcessor,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public void beforeCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		if (commandTracker.getCommand().toLowerCase().contains("getEval")) {
			logger.debug("Executing beforeCommand");

			commandTracker.getArgs()[0] = commandTracker.getArgs()[0]
					.replaceAll("selenium.browserbot.getCurrentWindow()", "window");
		}

	}
}
