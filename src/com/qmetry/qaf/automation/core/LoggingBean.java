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
package com.qmetry.qaf.automation.core;

import java.util.ArrayList;
import java.util.List;

import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.ui.selenium.LoggingBean.java
 * 
 * @author chirag
 */
public class LoggingBean {

	private String commandName;
	private String[] args;
	private String result;
	private List<LoggingBean> subLogs;
	private int duration = -1;// unknown

	public LoggingBean() {
		subLogs = new ArrayList<LoggingBean>();
	}

	public LoggingBean(String command, String[] args, String result) {
		this();
		commandName = command;
		this.args = args;
		this.result = result;

	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the subLogs
	 */
	public List<LoggingBean> getSubLogs() {
		return subLogs;
	}

	/**
	 * @param subLogs
	 *            the subLogs to set
	 */
	public void setSubLogs(List<LoggingBean> subLogs) {
		this.subLogs = subLogs;
	}

	/**
	 * @return command duration, -1 if unknown
	 */
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("command: " + commandName + "[");
		if ((args != null) && (args.length > 0)) {
			for (int i = 0; i < args.length; i++) {
				sb.append(" param-" + (i + 1) + ": " + args[i]);
			}
		}
		sb.append("]");
		if (StringUtil.isNotBlank(result)) {
			sb.append(" Result: " + result);
		}
		return sb.toString();
	}
}
