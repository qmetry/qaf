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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.selenium.QAFCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandTracker;
import com.qmetry.qaf.automation.util.StackTraceUtils;

/**
 * com.qmetry.qaf.automation.ui.selenium.SeleniumCommandLogger.java
 * <p>
 * This is internal listener used by the framework to prepare selenium command
 * log that is available in html report. By default it will exclude
 * ["getHtmlSource", "captureEntirePageScreenshotToString", "executeScript",
 * "screenshot"] commands. To exclude command in reporter log you can provide
 * "reporter.log.exclude.commands" property.
 * </p>
 * 
 * @see ApplicationProperties#REPORTER_LOG_EXCLUDE_CMD
 * @author chirag
 */
public class SeleniumCommandLogger implements SeleniumCommandListener {
	protected ArrayList<LoggingBean> commandLog;
	protected final Log logger = LogFactory.getLog(getClass());
	protected Set<String> excludeCommandsFromLogging;

	public SeleniumCommandLogger(ArrayList<LoggingBean> commandLog) {
		this.commandLog = commandLog;
		excludeCommandsFromLogging = new HashSet<String>(Arrays.asList(new String[] { "getHtmlSource",
				"captureEntirePageScreenshotToString", "executeScript", "screenshot" }));
		excludeCommandsFromLogging
				.addAll(Arrays.asList(ApplicationProperties.REPORTER_LOG_EXCLUDE_CMD.getStringVal("").split(",")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * afterCommand
	 * (com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		String[] args = commandTracker.getArgs();
		LoggingBean bean = new LoggingBean(commandTracker.getCommand(), args, commandTracker.getResult());

		if ((args != null) && (args.length > 0) && !StackTraceUtils.isWaitInvolved()
				&& !isCommandExcludedFromLogging(commandTracker.getCommand())) {
			commandLog.add(bean);

		}
		logger.info(bean.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.SeleniumCommandListener#
	 * beforeCommand
	 * (com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public void beforeCommand(QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		logger.info(new LoggingBean(commandTracker.getCommand(), commandTracker.getArgs(), "").toString());
	}

	public void clear() {
		commandLog.clear();
	}

	public List<LoggingBean> getLog() {
		return commandLog;
	}

	protected boolean isCommandExcludedFromLogging(final String commandName) {
		return excludeCommandsFromLogging.contains(commandName);
	}
}
