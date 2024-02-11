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
