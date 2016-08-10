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


package com.infostretch.automation.ui.selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infostretch.automation.ui.selenium.customcommands.GetCssPropertyCommand;
import com.infostretch.automation.ui.selenium.customcommands.SetAttributeCommand;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;

/**
 * com.infostretch.automation.ui.selenium.SeleniumCommandProcessor.java
 * chirag
 */
/**
 * @author chirag
 */
public class SeleniumCommandProcessor extends HttpCommandProcessor implements QAFCommandProcessor {
	transient private List<SeleniumCommandListener> commandListeners = new ArrayList<SeleniumCommandListener>();
	private boolean invokingListener;
	private final Map<String, QAFCustomCommand> QAFCustomCommands = new HashMap<String, QAFCustomCommand>();

	public SeleniumCommandProcessor(String serverHost, int serverPort, String browserStartCommand, String browserURL) {

		this("http://" + serverHost + ":" + Integer.toString(serverPort) + "/selenium-server/driver/",
				browserStartCommand, browserURL);
	}

	public SeleniumCommandProcessor(String serverHost, String browserStartCommand, String browserURL) {
		super(serverHost, browserStartCommand, browserURL);
		initCustomCommands();
	}

	private void initCustomCommands() {
		QAFCustomCommands.put("getCssProperty", new GetCssPropertyCommand());
		QAFCustomCommands.put("setAttribute", new SetAttributeCommand());
	}

	public String doDefaultDoCommand(String commandName, String[] args) {
		return super.doCommand(commandName, args);

	}

	@Override
	public String doCommand(String commandName, String[] args) {

		SeleniumCommandTracker commandTracker = new SeleniumCommandTracker(commandName, args);
		invokeBeforeCommand(commandTracker);

		if (null == commandTracker.getResult()) {
			String res = null;
			try {
				if (QAFCustomCommands.containsKey(commandName)) {
					QAFCustomCommand cmd = QAFCustomCommands.get(commandName);
					res = cmd.doCommand(this, commandTracker.getArgs());
				} else {
					res = super.doCommand(commandName, commandTracker.getArgs());
				}
				commandTracker.setResult(res);
			} catch (SeleniumException e) {
				System.out.println("Error caught in doCommand!....." + commandName);
				commandTracker.setException(e);
			}
		}
		invokeAfterCommand(commandTracker);
		if (commandTracker.hasException()) {
			throw commandTracker.getException();
		}
		return commandTracker.getResult();
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
				} catch (SeleniumException ex) {
					ex.printStackTrace();
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

	public QAFCustomCommand getMethod(String methodName) {
		return QAFCustomCommands.get(methodName);
	}
}
