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
package com.qmetry.qaf.automation.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.openqa.selenium.Capabilities;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.webdriver.CommandTracker;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebDriverCommandListener;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElementCommandListener;
import com.qmetry.qaf.automation.util.StackTraceUtils;

public class WebDriverCommandLogger 
		implements QAFWebDriverCommandListener, QAFWebElementCommandListener {
	private ArrayList<LoggingBean> commandLog;
	private final Log logger = LogFactory.getLog(getClass());
	private Set<String> excludeCommandsFromLogging;
	
	public WebDriverCommandLogger(ArrayList<LoggingBean> commandLog) {
		
		this.commandLog = commandLog;
		excludeCommandsFromLogging = new HashSet<String>(Arrays.asList(new String[] { "getHtmlSource",
				"captureEntirePageScreenshotToString", "executeScript", "screenshot" }));
		excludeCommandsFromLogging
				.addAll(Arrays.asList(ApplicationProperties.REPORTER_LOG_EXCLUDE_CMD.getStringVal("").split(",")));

		excludeCommandsFromLogging = new HashSet<String>(Arrays.asList(new String[] { "getHtmlSource",
				"captureEntirePageScreenshotToString", "executeScript", "screenshot" }));
		excludeCommandsFromLogging
				.addAll(Arrays.asList(ApplicationProperties.REPORTER_LOG_EXCLUDE_CMD.getStringVal("").split(",")));
	}

	public WebDriverCommandLogger() {
		this(new ArrayList<LoggingBean>());
	}

	@Override
	public void afterCommand(QAFExtendedWebDriver driver, CommandTracker commandTracker) {
		if (!StackTraceUtils.isWaitInvolved() && !isCommandExcludedFromLogging(commandTracker.getCommand())) {
			LoggingBean bean;
			try {
				bean = (new LoggingBean(commandTracker.getCommand(),
						new String[] { String.format("%s", new Object[] { commandTracker.getParameters() }) },
						null == commandTracker.getResponce() ? "OK" : "" + commandTracker.getResponce().getValue()));
			} catch (Exception e) {
				bean = (new LoggingBean(commandTracker.getCommand(), new String[] {}, ""));
			}
			logDuration(commandTracker, bean);
			commandLog.add(bean);
			logger.info(bean.toString());
		}

	}

	@Override
	public void beforeCommand(QAFExtendedWebDriver driver, CommandTracker commandTracker) {
		if (commandTracker.getCommand().equalsIgnoreCase("get")) {
			String url = commandTracker.getParameters().get("url").toString();
			if (!url.startsWith("http")) {
				String baseUrl = new WebDriverTestBase().getBaseUrl();
				if (!baseUrl.endsWith("/") && !url.startsWith("/")) {
					baseUrl = baseUrl + "/";
				} else if (url.startsWith("/")) {
					url = url.substring(1, url.length());
				}
				commandTracker.getParameters().put("url", baseUrl + url);
			}
		}
		try {
			logger.info("Executing " + commandTracker.getCommand() + " parameters: "
					+ new JSONObject(commandTracker.getParameters()).toString());
		} catch (Exception e) {
			logger.info("executing " + commandTracker.getCommand());
		}
	}

	@Override
	public void onFailure(QAFExtendedWebDriver driver, CommandTracker commandTracker) {
		LoggingBean bean;
		try {
			bean = (new LoggingBean(commandTracker.getCommand(),
					new String[] { String.format("%s", new Object[] { commandTracker.getParameters() }) },
					commandTracker.getMessage()));
		} catch (Exception e) {
			bean = (new LoggingBean(commandTracker.getCommand(), new String[] {}, commandTracker.getMessage()));

		}
		if (!isCommandExcludedFromLogging(commandTracker.getCommand())) {
			logDuration(commandTracker, bean);
			commandLog.add(bean);
		}
		logger.error(bean.toString());

	}

	@Override
	public void afterCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
		if (!StackTraceUtils.isWaitInvolved() && !isCommandExcludedFromLogging(commandTracker.getCommand())) {
			LoggingBean bean;
			try {
				bean = new LoggingBean(commandTracker.getCommand(),
						new String[] { element.toString(), new JSONObject(commandTracker.getParameters()).toString() },
						null == commandTracker.getResponce() ? "OK"
								: commandTracker.getCommand() + ":" + commandTracker.getResponce().getValue());
			} catch (Exception e) {
				bean = new LoggingBean(commandTracker.getCommand(), new String[] {}, "");

			}
			logDuration(commandTracker, bean);
			commandLog.add(bean);
			logger.info(bean.toString());

		}
	}

	@Override
	public void beforeCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
		try {
			logger.info("Executing " + commandTracker.getCommand() + " element: " + element.toString() + " parameters: "
					+ new JSONObject(commandTracker.getParameters()).toString());
		} catch (Exception e) {
			logger.info("executing " + commandTracker.getCommand());
		}

	}

	@Override
	public void onFailure(QAFExtendedWebElement element, CommandTracker commandTracker) {
		LoggingBean bean;
		try {
			bean = new LoggingBean(commandTracker.getCommand(),
					new String[] { element.toString(), new JSONObject(commandTracker.getParameters()).toString() },
					commandTracker.getMessage());
		} catch (Exception e) {
			bean = new LoggingBean(commandTracker.getCommand(), new String[] {}, commandTracker.getMessage());

		}
		if (!isCommandExcludedFromLogging(commandTracker.getCommand())) {
			logDuration(commandTracker, bean);
			commandLog.add(bean);
		}
		logger.info(bean.toString());
	}

	private static void logDuration(CommandTracker commandTracker, LoggingBean bean) {
		if (commandTracker.getEndTime() > 0) {
			Number duration = commandTracker.getEndTime() - commandTracker.getStartTime();
			bean.setDuration(duration.intValue());
		}
	}

	public void addMessage(String msg, MessageTypes type, Object... objects) {
		QAFTestBase stb = TestBaseProvider.instance().get();
		stb.addAssertionLog(msg, type);

	}

	private static String notOpPassFormat = "Expected {0} not {op} : Actual {0} not {op}";
	private static String notOpFailFormat = "Expected {0} not {op} : Actual {0} {op}";
	private static String opPassFormat = "Expected {0} {op} : Actual {0} {op}";
	private static String opFailFormat = "Expected {0} {op} : Actual {0} not {op}";

	private static String notOpValFormat = "Expected {0} {op} should not be {1} : Actual {0} {op} is {2}";
	private static String opValFormat = "Expected {0} {op} should be {1} : Actual {0} {op} is {2}";

	/**
	 * @param operation
	 * @param success
	 * @param args
	 *            to provide in message. Expect arg 1 : as label/message for,
	 *            arg 2 as expected val, arg 3 as actual value
	 * @return
	 */
	public static String getMsgForElementOp(String operation, boolean success, Object... args) {
		String key = "element." + operation + "." + (success ? "pass" : "fail");

		String format = ConfigurationManager.getBundle().getString(key);
		if (format == null) {
			format = (operation.startsWith("not")
					? ((args != null) && (args.length > 2) ? notOpValFormat
							: (success ? notOpPassFormat : notOpFailFormat))
					: ((args != null) && (args.length > 2) ? opValFormat : (success ? opPassFormat : opFailFormat)))
							.replace("{op}", operation.replace("not", ""));
			// store for future reference

			ConfigurationManager.getBundle().setProperty(key, format);

		}

		return MessageFormat.format(format, args);
	}

	@Override
	public void onInitialize(QAFExtendedWebDriver driver) {

	}

	@Override
	public void beforeInitialize(Capabilities desiredCapabilities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationFailure(Capabilities desiredCapabilities, Throwable t) {
		// TODO Auto-generated method stub
		
	}
	

	public List<LoggingBean> getLog() {
		return commandLog;
	}

	protected boolean isCommandExcludedFromLogging(final String commandName) {
		return excludeCommandsFromLogging.contains(commandName);
	}
}
