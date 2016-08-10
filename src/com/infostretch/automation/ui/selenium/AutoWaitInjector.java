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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.infostretch.automation.keys.ApplicationProperties;
import com.infostretch.automation.util.StackTraceUtils;
import com.thoughtworks.selenium.Wait;

/**
 * com.infostretch.automation.ui.selenium.AutoWaitInjector.java <br>
 * {@link SeleniumCommandListener} implementation for automatic wait injection
 * before executing command. To provide exclude command list set property
 * <code>auto.wait.exclude.commands</code>. <<br/>
 * By default this listener is get registered. If you want to skip this
 * listener, provide property {@link ApplicationProperties#SKIP_AUTO_WAIT}
 * (selenium.skip.autowait) value to false. list of commands that will: click,
 * mouse, select, type, getText,
 * check,getElement,removeSelection,addSelection,focus To provide exclude
 * commands set property {@link ApplicationProperties#AUTO_WAIT_EXCLUDE_CMD} To
 * provide include commands set property auto.wait.include.commands
 * 
 * @author chirag
 **/

public class AutoWaitInjector implements SeleniumCommandListener {
	private final Log logger = LogFactoryImpl.getLog(AutoWaitInjector.class);

	private String excommannds = "script,getEval,runScript,setSpeed,open,isTextPresent,isElementPresent,getExpression,getXpathCount,flex, Native,stop,capture";
	private String commands = "click, mouse, select, type, getText, check,getElement,removeSelection,addSelection,focus";

	public AutoWaitInjector() {
		excommannds = excommannds + "," + ApplicationProperties.AUTO_WAIT_EXCLUDE_CMD.getStringVal();
		commands = commands + "," + ApplicationProperties.AUTO_WAIT_INCLUDE_CMD.getStringVal();
	}

	@Override
	public void afterCommand(QAFCommandProcessor proc, SeleniumCommandTracker sc) {

	}

	@Override
	public void beforeCommand(final QAFCommandProcessor proc, SeleniumCommandTracker commandTracker) {
		boolean isWaitInvolved = StackTraceUtils.isWaitInvolved();

		if (!ApplicationProperties.SKIP_AUTO_WAIT.getBoolenVal(false) && (null != commandTracker.getArgs())
				&& (commandTracker.getArgs().length > 0) && StringUtils.isNotBlank(commandTracker.getArgs()[0])
				&& !exclude(commandTracker.getCommand()) && include(commandTracker.getCommand())// !skip(commandName)
				&& !isWaitInvolved) {
			final String loc = commandTracker.getArgs()[0];
			try {
				new Wait() {
					@Override
					public boolean until() {
						return proc.getBoolean("isElementPresent", new String[] { loc, });
					}
				}.wait("Wait time out before command" + commandTracker.getCommand(),
						WaitService.getDefaultPageWaitTimeNum());
			} catch (Throwable e) {
				logger.debug("AutoWait beforeCommand" + e);

			}

			if (commandTracker.getCommand().equalsIgnoreCase("select")) {
				try {
					new Wait() {
						@Override
						public boolean until() {
							return StringUtils.isNotBlank(proc.getString("getSelectedIndex", new String[] { loc, }));
						}
					}.wait("Wait time out before Select" + commandTracker.getCommand(),
							WaitService.getDefaultPageWaitTimeNum());
				} catch (Throwable e) {
					logger.debug(e);
				}
			}

		}
	}

	@SuppressWarnings("unused")
	private boolean skip(String commandName) {
		excommannds = excommannds + "isTextPresent,isElementPresent,getExpression,getXpathCount,"
				+ "getEval,allowNativeXpath,ignoreAttributesWithoutValue,runScript,open";
		return commandName.contains("Cookie") || commandName.contains("Window") || commandName.contains("Prompt")
				|| commandName.contains("Location") || commandName.contains("Browser")
				|| commandName.contains("capture") || commandName.contains("Native")
				|| excommannds.toUpperCase().contains(commandName.toUpperCase()) || commandName.startsWith("wait")
				|| commandName.toLowerCase().contains("flex") || commandName.toLowerCase().startsWith("add")
				|| commandName.toLowerCase().startsWith("set");// setMouseSpeed,setContext,setTimeout,setSpeed
	}

	private boolean include(String command) {
		boolean retVal = false;
		String[] cmds = commands.split(",");
		for (String cmd : cmds) {
			if ((cmd.trim().length() > 0) && command.trim().toUpperCase().contains(cmd.trim().toUpperCase())) {
				return true;
			}
		}
		return retVal;
	}

	private boolean exclude(String command) {
		boolean retVal = false;
		String[] cmds = excommannds.split(",");
		for (String cmd : cmds) {
			if ((cmd.trim().length() > 0) && command.trim().toUpperCase().contains(cmd.trim().toUpperCase())) {
				return true;
			}
		}
		return retVal;
	}

	@SuppressWarnings("unused")
	private boolean excludeInerval(String command) {
		boolean retVal = false;
		String[] cmds = { "window", "wait", "set", "open", "stop", "capture", "focus" };
		for (String cmd : cmds) {
			if ((cmd.trim().length() > 0) && command.trim().toUpperCase().contains(cmd.trim().toUpperCase())) {
				return true;
			}
		}
		return retVal;

	}
}
