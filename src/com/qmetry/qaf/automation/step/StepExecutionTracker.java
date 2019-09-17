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
package com.qmetry.qaf.automation.step;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITestResult;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.TestNGScenario;

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

	/**
	 * This is utility method and will return {@link TestNGScenario} for which
	 * current step invoked or null if not found reference.
	 * 
	 * @return
	 */
	public TestNGScenario getScenario() {
		ITestResult result =
				(ITestResult) ApplicationProperties.CURRENT_TEST_RESULT.getObject();
		if (result != null) {
			return ((TestNGScenario) result.getMethod());
		}
		return null;
	}

}
