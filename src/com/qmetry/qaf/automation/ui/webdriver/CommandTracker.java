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


package com.qmetry.qaf.automation.ui.webdriver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.Response;

/**
 * Track the selenium-2 api command with all information including exception and
 * execution stage. com.qmetry.qaf.automation.ui.webdriver.CommandTracker.java
 * 
 * @author chirag
 */
public class CommandTracker {
	/**
	 * indicates what is the stage. specially helpful while you are handling
	 * exception in OnFailure method and want to propagate.
	 * com.qmetry.qaf.automation.ui.webdriver.CommandTracker.java
	 * 
	 * @author chirag
	 */
	public enum Stage {
		executingBeforeMethod, executingMethod, executingAfterMethod, executingOnFailure;
	}

	RuntimeException exception;
	String command;
	Map<String, Object> parameters;
	Stage stage;
	boolean retry;
	private long startTime;
	private long endTime;

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	/**
	 * Inspect current state of command execution.
	 * 
	 * @return the {@link Stage current stage}
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public CommandTracker(String command, Map<String, ?> parameters) {
		this.command = command;
		setParameters(parameters);
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

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map<String, ?> parameters) {
		this.parameters = new HashMap<String, Object>();
		this.parameters.putAll(parameters);
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	Response responce;

	/**
	 * @return the responce
	 */
	public Response getResponce() {
		return responce;
	}

	/**
	 * @param responce
	 *            the responce to set
	 */
	public void setResponce(Response responce) {
		this.responce = responce;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
