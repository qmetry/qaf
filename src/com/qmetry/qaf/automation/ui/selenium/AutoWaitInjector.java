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

import com.qmetry.qaf.automation.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.StackTraceUtils;
import com.thoughtworks.selenium.Wait;

/**
 * com.qmetry.qaf.automation.ui.selenium.AutoWaitInjector.java <br>
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
				&& (commandTracker.getArgs().length > 0) && StringUtil.isNotBlank(commandTracker.getArgs()[0])
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
							return StringUtil.isNotBlank(proc.getString("getSelectedIndex", new String[] { loc, }));
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
