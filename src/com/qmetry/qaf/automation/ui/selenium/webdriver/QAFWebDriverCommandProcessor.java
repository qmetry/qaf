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

import java.util.ArrayList;
import java.util.List;

import static com.qmetry.qaf.automation.util.StringUtil.*;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.ScreenshotException;

import com.qmetry.qaf.automation.ui.selenium.QAFCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandTracker;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.WebDriverCommandProcessor;

/**
 * com.qmetry.qaf.automation.ui.selenium.webdriver.QAFWebDriverCommandProcessor
 * .* java
 * 
 * @author chirag
 */
public class QAFWebDriverCommandProcessor extends WebDriverCommandProcessor implements QAFCommandProcessor {
	transient private List<SeleniumCommandListener> commandListeners = new ArrayList<SeleniumCommandListener>();
	private boolean invokingListener;

	/**
	 * @param baseUrl
	 * @param driver
	 */
	public QAFWebDriverCommandProcessor(String baseUrl, QAFExtendedWebDriver driver) {
		super(baseUrl, driver);
		addISExtenedCommands();
	}

	public String extractScreenShot(WebDriverException e) {
		Throwable cause = e.getCause();
		if (cause instanceof ScreenshotException) {
			return ((ScreenshotException) cause).getBase64EncodedScreenshot();
		}
		return null;
	}

	@Override
	public String doCommand(String commandName, String[] args) {

		SeleniumCommandTracker commandTracker = new SeleniumCommandTracker(commandName, args);
		invokeBeforeCommand(commandTracker);
		if (null == commandTracker.getResult()) {
			try {
				String[] argsToPass = commandTracker.getArgs();
				String result = super.doCommand(commandName, argsToPass);

				commandTracker.setResult(result);
			} catch (SeleniumException e) {
				System.out.println("Error in caught doCommand: " + commandName);
				commandTracker.setException(e);
			}
		}
		invokeAfterCommand(commandTracker);
		if (commandTracker.hasException()) {
			throw commandTracker.getException();
		}
		return commandTracker.getResult();
	}

	@Override
	public void start(String strOptions) {
	}

	private void invokeBeforeCommand(SeleniumCommandTracker commandTracker) {
		if (!invokingListener) {
			invokingListener = true;
			for (SeleniumCommandListener listener : commandListeners) {
				try {
					listener.beforeCommand(this, commandTracker);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			invokingListener = false;
		}
	}

	private void invokeAfterCommand(SeleniumCommandTracker commandTracker) {
		if (!invokingListener) {
			invokingListener = true;

			for (SeleniumCommandListener listener : commandListeners) {
				try {
					listener.afterCommand(this, commandTracker);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			invokingListener = false;
		}
	}

	/**
	 * @param listenerClasses
	 */
	@SuppressWarnings("unchecked")
	public void addListener(String... listenerClasses) {
		if (null != listenerClasses) {

			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			for (String listenerClass : listenerClasses) {
				try {
					if (isEmpty(listenerClass)) {
						continue;
					}
					Class<SeleniumCommandListener> cls = (Class<SeleniumCommandListener>) loader
							.loadClass(listenerClass);

					SeleniumCommandListener listener = cls.newInstance();
					commandListeners.add(listener);
				} catch (Throwable e) {
					System.err.println(
							"unable to register" + listenerClass + " as SeleniumCommandListener: " + e.getMessage());
				}
			}
		}
	}

	public void addListener(SeleniumCommandListener listener) {
		if (null != listener) {
			commandListeners.add(listener);
		}
	}

	private void addISExtenedCommands() {
		JavascriptLibrary javascriptLibrary = new JavascriptLibrary();
		ElementFinder elementFinder = new ElementFinder(javascriptLibrary);

		addMethod("shutDownSeleniumServer", new DriverSkippedCommand());
		addMethod("retrieveLastRemoteControlLogs", new DriverSkippedCommand());
		addMethod("addScript", new AddScriptCommand());
		addMethod("setAttribute", new SetAttributeCommand(javascriptLibrary, elementFinder));
		addMethod("getCssCount", new GetCssCountCommand());

		addMethod("getText", new GetText(elementFinder));

	}

	@Override
	public QAFExtendedWebDriver getWrappedDriver() {
		return (QAFExtendedWebDriver) super.getWrappedDriver();
	}
}
