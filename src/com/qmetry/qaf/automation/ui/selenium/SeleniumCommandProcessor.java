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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qmetry.qaf.automation.util.StringUtil;

import com.qmetry.qaf.automation.ui.selenium.customcommands.GetCssPropertyCommand;
import com.qmetry.qaf.automation.ui.selenium.customcommands.SetAttributeCommand;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;

/**
 * com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor.java
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
					if (StringUtil.isEmpty(listenerClass)) {
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
