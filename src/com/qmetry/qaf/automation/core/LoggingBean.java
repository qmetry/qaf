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
