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


package com.qmetry.qaf.automation.step;

import java.util.HashMap;
import java.util.Map;

import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * com.qmetry.qaf.automation.step.StepExecutionContext.java
 * 
 * @author chirag.jayswal
 */
public class StepExecutionTracker {
	private Object result;
	private Throwable exception;
	private TestStep step;
	private Map<String, Object> context;
	private String type = "";
	private int stepIndex;
	private int nextStepIndex;
	private long startTime;
	private long endTime;
	private String verificationError;

	private TestStepCompositer stepCaller;
	/**
	 * indicates step pass/fail status
	 */
	private Boolean success = null;

	// TODO: add checkpoints and command-log over here!

	public StepExecutionTracker(TestStep step) {
		this.step = step;
		context = new HashMap<String, Object>();
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		ConfigurationManager.getBundle().setProperty("last.step.result", result);
		this.result = result;
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

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * @return type of the step i.e "Given", "When", "Then", "And" etc...
	 */
	public String getType() {
		return type;
	}

	/**
	 * set type of the step i.e "Given", "When", "Then", "And" etc...
	 * 
	 * @param type
	 */
	protected void setType(String type) {
		this.type = type;
	}

	public TestStep getStep() {
		return step;
	}

	public boolean hasException() {
		return exception != null;
	}

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		if (context == null) {
			this.context.clear();
		} else {
			this.context.putAll(context);
		}
	}

	public TestStepCompositer getStepCompositer() {
		return stepCaller;
	}

	public void setStepCompositer(TestStepCompositer stepCaller) {
		this.stepCaller = stepCaller;
	}

	/**
	 * @return 0 based step index
	 */
	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public int getNextStepIndex() {
		return nextStepIndex;
	}

	public void setNextStepIndex(int nextStepIndex) {
		this.nextStepIndex = nextStepIndex;
	}

	public String getVerificationError() {
		return verificationError;
	}

	/**
	 * @param verificationError
	 */
	public void setVerificationError(String verificationError) {
		this.verificationError = verificationError;
	}

}
