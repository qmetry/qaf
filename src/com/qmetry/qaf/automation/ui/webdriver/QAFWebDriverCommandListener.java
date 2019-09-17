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

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.Response;

import com.qmetry.qaf.automation.core.QAFListener;

/**
com.qmetry.qaf.automation.ui.webdriver.QAFWebElementCommandListener.java
 * 
 * @author chirag.jayswal
 */
public interface QAFWebDriverCommandListener extends QAFListener {
	/**
	 * This can be used as intercepter. If you want to skip execution of actual
	 * command then set response in {@link CommandTracker#setResponce(Response)}
	 * 
	 * @param driver
	 * @param commandHandler
	 */
	public void beforeCommand(QAFExtendedWebDriver driver, CommandTracker commandHandler);

	public void afterCommand(QAFExtendedWebDriver driver, CommandTracker commandHandler);

	/**
	 * This can be used to propagate exception. You can get information about
	 * from where exception thrown by inspecting
	 * {@link CommandTracker#getStage()}
	 * 
	 * @param driver
	 * @param commandHandler
	 */
	public void onFailure(QAFExtendedWebDriver driver, CommandTracker commandHandler);

	/**
	 * Here you can specify additional desired capabilities for the driver.
	 * 
	 * @param desiredCapabilities
	 */
	public void beforeInitialize(Capabilities desiredCapabilities);

	/**
	 * this method will be called when new driver instance is created
	 * 
	 * @param driver
	 */
	public void onInitialize(QAFExtendedWebDriver driver);
	
	public void onInitializationFailure(Capabilities desiredCapabilities, Throwable t);

}
