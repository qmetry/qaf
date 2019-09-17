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
package com.qmetry.qaf.automation.ui.webdriver;

/**
 * Adapter class for {@link QAFWebElementCommandListener}.All the methods takes
 * care to prevents iterative call.
 * 
 * @author chirag
 */
public class QAFWebElementCommandAdapter implements QAFWebElementCommandListener {
	@Override
	public void afterCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
	}

	@Override
	public void beforeCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
	}

	@Override
	public void onFailure(QAFExtendedWebElement element, CommandTracker commandTracker) {
	}

	/**
	 * enables to execute command on element.
	 * 
	 * @param element
	 * @param commandTracker
	 * @return
	 */
	protected void execute(QAFExtendedWebElement element, CommandTracker commandTracker) {
		if (null != commandTracker.getResponce()) {
			commandTracker.setResponce(element.execute(commandTracker.getCommand(), commandTracker.getParameters()));
		}
	}
	
	/**
	 * enables to execute command on element.
	 * 
	 * @param element
	 * @param commandTracker
	 * @return
	 */
	protected void executeWithoutLog(QAFExtendedWebElement element, CommandTracker commandTracker) {
		if (null != commandTracker.getResponce()) {
			commandTracker.setResponce(element.executeWitoutLog(commandTracker.getCommand(), commandTracker.getParameters()));
		}
	}

}
