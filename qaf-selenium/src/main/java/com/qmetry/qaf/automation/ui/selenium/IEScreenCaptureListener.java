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

/**
 * com.qmetry.qaf.automation.ui.selenium.IEScreenCuptureListner.java
 * chirag
 */
/**
 * @author chirag
 */
public class IEScreenCaptureListener implements SeleniumCommandListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * afterCommand (com.thoughtworks.selenium.HttpCommandProcessor,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	private String lastLocator;

	@Override
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		if ((commandTracker.getArgs() != null) && (commandTracker.getArgs().length > 0)) {
			lastLocator = commandTracker.getArgs()[0];
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
	public void beforeCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		if (commandTracker.getCommand().toLowerCase().contains("screenshot")) {
			try {
				String snipet = String.format(
						"try{var top=selenium.getElementPositionTop(\"%s\");"
								+ "var left=selenium.getElementPositionLeft(\"%s\");"
								+ "selenium.browserbot.getCurrentWindow().scroll(left,top);}" + "catch(e){}",
						lastLocator, lastLocator);
				proc.doCommand("windowMaximize", new String[] {,});
				proc.getString("getEval", new String[] { snipet });
				proc.doCommand("windowFocus", new String[] {,});

			} catch (Throwable t) {

			}
		}

	}
}
