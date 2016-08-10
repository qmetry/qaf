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


package com.infostretch.automation.ui.selenium.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.ScreenshotException;

import com.infostretch.automation.ui.selenium.QAFCommandProcessor;
import com.infostretch.automation.ui.selenium.SeleniumCommandListener;
import com.infostretch.automation.ui.selenium.SeleniumCommandTracker;
import com.infostretch.automation.ui.webdriver.QAFExtendedWebDriver;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.WebDriverCommandProcessor;

/**
 * com.infostretch.automation.ui.selenium.webdriver.QAFWebDriverCommandProcessor
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
					if (StringUtils.isEmpty(listenerClass)) {
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
