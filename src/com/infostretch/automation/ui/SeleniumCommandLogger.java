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


package com.infostretch.automation.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostretch.automation.core.LoggingBean;
import com.infostretch.automation.keys.ApplicationProperties;
import com.infostretch.automation.ui.selenium.QAFCommandProcessor;
import com.infostretch.automation.ui.selenium.SeleniumCommandListener;
import com.infostretch.automation.ui.selenium.SeleniumCommandTracker;
import com.infostretch.automation.util.StackTraceUtils;

/**
 * com.infostretch.automation.ui.selenium.SeleniumCommandLogger.java
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
	 * @see com.infostretch.automation.ui.selenium.SeleniumCommandListener#
	 * afterCommand
	 * (com.infostretch.automation.ui.selenium.SeleniumCommandProcessor,
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
	 * @see com.infostretch.automation.ui.selenium.SeleniumCommandListener#
	 * beforeCommand
	 * (com.infostretch.automation.ui.selenium.SeleniumCommandProcessor,
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
