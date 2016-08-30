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
