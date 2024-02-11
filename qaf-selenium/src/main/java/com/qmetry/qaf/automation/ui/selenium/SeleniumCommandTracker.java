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

import org.json.JSONException;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.JSONUtil;

/**
 * Track the command of selenium 1 API with all information including exception.
 * <p>
 * com.qmetry.qaf.automation.ui.selenium.CommandTracker.java
 * 
 * @author chirag
 */
public class SeleniumCommandTracker {
	RuntimeException exception;
	String command;
	String[] args;
	String result;

	public SeleniumCommandTracker(String command, String[] args) {
		this.command = command;
		setArgs(args);
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
		if ((args != null) && (args.length > 0)) {
			String loc = ConfigurationManager.getBundle().getSubstitutor().replace(args[0]);
			if (JSONUtil.isValidJsonString(loc)) {
				try {
					loc = (String) JSONUtil.toMap(loc).get("locator");
				} catch (JSONException e) {
					throw new RuntimeException("Unable to get locator from " + loc);
				}
			}
			if (loc.indexOf("=") > 0) {
				String parts[] = loc.split("=", 2);
				if (parts[0].equalsIgnoreCase("key") || parts[0].equalsIgnoreCase("property")) {
					loc = (ConfigurationManager.getBundle().getString(parts[1]));
				}
			}
			this.args[0] = loc;
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	/**
	 * @return the cause
	 */
	public RuntimeException getException() {
		return exception;
	}

	public boolean hasException() {
		return exception != null;
	}

	public Class<? extends RuntimeException> getExceptionType() {
		return exception == null ? null : exception.getClass();
	}

	public String getMessage() {
		return exception == null ? "" : exception.getMessage();
	}

}
