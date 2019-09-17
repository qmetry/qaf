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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * com.qmetry.qaf.automation.step.BaseTestStep.java
 * 
 * @author chirag.jayswal
 */
public abstract class BaseTestStep implements TestStep {
	protected Log logger = LogFactory.getLog(getClass());
	protected String name;
	protected String description;
	transient protected Object[] actualArgs;
	protected StepExecutionTracker stepExecutionTracker;
	private Set<QAFTestStepListener> stepListeners;
	protected String fileName;
	protected int lineNumber;
	protected int threshold;
	protected Map<String, Object> metaData = new HashMap<String, Object>();

	public BaseTestStep(String name, String description, Object... actualArgs) {
		this.name = name;
		this.description = description;
		setActualArgs(actualArgs);
		stepExecutionTracker = new StepExecutionTracker(this);
		initStepListeners();
	}

	protected BaseTestStep() {
		this("", "");
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setActualArgs(Object... args) {
		if (null != args) {
			actualArgs = Arrays.copyOf(args, args.length);
		}
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Object[] getActualArgs() {
		return actualArgs;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

	@Override
	public abstract TestStep clone();

	protected abstract Object doExecute();

	@Override
	public Object execute() {
		TestStepListener defaultListener = new TestStepListener();
		// make sure default listener before method executes first and
		// after/onFailure method executes last.
		beforExecute(defaultListener);
		try {
			stepExecutionTracker.setStartTime(System.currentTimeMillis());
			Object retval = doExecute();
			stepExecutionTracker.setResult(retval);
			stepExecutionTracker.setEndTime(System.currentTimeMillis());

		} catch (Throwable t) {
			stepExecutionTracker.setEndTime(System.currentTimeMillis());
			stepExecutionTracker.setException(t);
			onFailure(defaultListener);
		}
		afterExecute(defaultListener);
		if (stepExecutionTracker.hasException()) {
			if (stepExecutionTracker.getException() instanceof Error)
				throw (Error) stepExecutionTracker.getException();
			throw (RuntimeException) stepExecutionTracker.getException();
		}
		return stepExecutionTracker.getResult();
	}

	protected void onFailure(TestStepListener defaultListener) {
		for (QAFTestStepListener stepListener : stepListeners) {
			stepListener.onFailure(stepExecutionTracker);
		}
		// failure is taken care by custom listener?
		if (stepExecutionTracker.hasException()) {
			defaultListener.onFailure(stepExecutionTracker);
		}
	}

	protected void beforExecute(TestStepListener defaultListener) {
		defaultListener.beforExecute(stepExecutionTracker);
		for (QAFTestStepListener stepListener : stepListeners) {
			stepListener.beforExecute(stepExecutionTracker);
		}
	}

	protected void afterExecute(TestStepListener defaultListener) {
		for (QAFTestStepListener stepListener : stepListeners) {
			stepListener.afterExecute(stepExecutionTracker);
		}

		defaultListener.afterExecute(stepExecutionTracker);
	}

	private void initStepListeners() {
		stepListeners = new LinkedHashSet<QAFTestStepListener>();
		// add listeners registered through property
		String[] listeners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.TESTSTEP_LISTENERS.key);
		for (String listener : listeners) {
			try {
				QAFTestStepListener cls = (QAFTestStepListener) Class.forName(listener).newInstance();
				stepListeners.add(cls);
			} catch (Exception e) {
				logger.error("Unable to register class as test step listener:  " + listener, e);
			}
		}
		
		listeners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		for (String listener : listeners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).newInstance();
				if(QAFTestStepListener.class.isAssignableFrom(cls.getClass()))
				stepListeners.add((QAFTestStepListener)cls);
			} catch (Exception e) {
				logger.error("Unable to register class as test step listener:  " + listener, e);
			}
		}
	}

	@Override
	public StepExecutionTracker getStepExecutionTracker() {
		return stepExecutionTracker;
	}

	public void setStepExecutionTracker(StepExecutionTracker stepExecutionTracker) {
		this.stepExecutionTracker = stepExecutionTracker;
	}

}
