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
