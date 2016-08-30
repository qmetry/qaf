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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.ConfigurationManager;
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
	private List<QAFTestStepListener> stepListeners;
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
		stepListeners = new ArrayList<QAFTestStepListener>();
		// add listeners registered through property
		String[] listeners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.TESTSTEP_LISTENERS.key);
		for (String listener : listeners) {
			try {
				QAFTestStepListener cls = (QAFTestStepListener) Class.forName(listener).newInstance();
				stepListeners.add(cls);
			} catch (Exception e) {
				logger.error("Unable to register test step listener:  " + listener, e);
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
