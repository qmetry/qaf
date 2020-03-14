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
package com.qmetry.qaf.automation.step.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.step.BaseTestStep;
import com.qmetry.qaf.automation.step.StepExecutionTracker;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.TestStepCompositer;

/**
 * This class is to support BDD with test case implementation in Java. You can
 * construct scenario inside your test method and run it. It supports step
 * listeners and also support dry run just like scenario in BDD file.
 * 
 * <h6>Usage:</h6> <code>
 * <pre>@Test(description="")
 *	public void test() {
 *		scenario().
 *		given("a precondition",()->{
 *			//write appropriate code...
 *		}).
 *		when("some action performed",()->{
 *			//write appropriate code...
 *		}).
 *		then("it should have expected outcome",()->{
 *			//write appropriate code...
 *		}).
 *		execute();
 *	}
 * </pre>
  * <pre>@Test(description="")
 *	public void testCustom() {
 *		scenario().
 *		step("परिस्थिति दी गई है",()->{
 *			//write appropriate code...
 *		}).
 *		step("जब कुछ कार्रवाई हुई",()->{
 *			//write appropriate code...
 *		}).
 *		step("तब इसके अपेक्षित परिणाम होने चाहिए",()->{
 *			//write appropriate code...
 *		}).
 *		execute();
 *	}
 *
 * </pre>
 * </code>
 * 
 * @author chirag.jayswal
 *
 */
public class RuntimeScenarioFactory {
	@FunctionalInterface
	public static interface RuntimeTestStepProvider {
		void stepDefinition();
	}

	public static RuntimeScenario scenario() {
		return new RuntimeScenarioImpl();
	}
	
	public static void step(String description, RuntimeTestStepProvider stepDefinition) {
		new RuntimeTestStep(description, stepDefinition, Collections.emptyMap()).execute();
	}
	
	public static void step(String description, RuntimeTestStepProvider stepDefinition,
			Map<String, Object> metaData) {
		new RuntimeTestStep(description, stepDefinition, metaData).execute();
	}
	
	private static class RuntimeTestStep extends BaseTestStep {
		RuntimeTestStepProvider stepProvider;

		private RuntimeTestStep(String description, RuntimeTestStepProvider stepImpl, Map<String, Object> metaData) {
			super(description, description);
			this.stepProvider = stepImpl;
			if (metaData != null) {
				this.metaData = new HashMap<String, Object>(metaData);
			}
		}

		@Override
		public String getSignature() {
			return stepProvider.getClass().toString();
		}

		@Override
		public TestStep clone() {
			return new RuntimeTestStep(getDescription(), stepProvider, new HashMap<String, Object>(metaData));
		}

		@Override
		protected Object doExecute() {
			stepProvider.stepDefinition();
			return null;
		}
	}

	public static interface RuntimeScenario {
		public RuntimeScenario step(String description, RuntimeTestStepProvider step);
		public RuntimeScenario step(String description, RuntimeTestStepProvider step, Map<String, Object> metadata);
		public RuntimeScenario given(String description, RuntimeTestStepProvider step);
		public RuntimeScenario given(String description, RuntimeTestStepProvider step, Map<String, Object> metadata);
		public RuntimeScenario when(String description, RuntimeTestStepProvider step);
		public RuntimeScenario when(String description, RuntimeTestStepProvider step, Map<String, Object> metadata);
		public RuntimeScenario then(String description, RuntimeTestStepProvider step);
		public RuntimeScenario then(String description, RuntimeTestStepProvider step, Map<String, Object> metadata);
		public RuntimeScenario and(String description, RuntimeTestStepProvider step);
		public RuntimeScenario and(String description, RuntimeTestStepProvider step, Map<String, Object> metadata);
		public RuntimeScenario withThreshold(int thresold);
		public void execute();
	}

	private static class RuntimeScenarioImpl implements RuntimeScenario, TestStepCompositer {
		private List<TestStep> steps;

		private RuntimeScenarioImpl() {
			steps = new ArrayList<TestStep>();
		}
		public RuntimeScenario given(String description, RuntimeTestStepProvider step) {
			return given(description, step, Collections.emptyMap());
		}
		public RuntimeScenario when(String description, RuntimeTestStepProvider step) {
			return when(description, step, Collections.emptyMap());
		}
		public RuntimeScenario then(String description, RuntimeTestStepProvider step) {
			return then(description, step, Collections.emptyMap());
		}
		public RuntimeScenario and(String description, RuntimeTestStepProvider step) {
			return and(description, step, Collections.emptyMap());
		}
		public RuntimeScenario step(String description, RuntimeTestStepProvider step) {
			return step(description, step, Collections.emptyMap());
		}
		public RuntimeScenario step(String description, RuntimeTestStepProvider stepDefinition, Map<String, Object> metadata) {
			steps.add(new RuntimeTestStep(description, stepDefinition, metadata));
			return this;
		}
		public RuntimeScenario given(String description, RuntimeTestStepProvider step, Map<String, Object> metadata) {
			return step("Given " + description, step, metadata);
		}
		public RuntimeScenario when(String description, RuntimeTestStepProvider step, Map<String, Object> metadata) {
			return step("When " + description, step, metadata);
		}
		public RuntimeScenario then(String description, RuntimeTestStepProvider step, Map<String, Object> metadata) {
			return step("Then " + description, step, metadata);
		}
		public RuntimeScenario and(String description, RuntimeTestStepProvider step, Map<String, Object> metadata) {
			return step("And " + description, step, metadata);
		}
		public RuntimeScenario withThreshold(int thresold) {
			if (!steps.isEmpty())
				((RuntimeTestStep) steps.get(steps.size() - 1)).setThreshold(thresold);
			return this;
		}

		public void execute() {
			int executionIndx = 0;
			try {
				for (executionIndx = 0; executionIndx < steps.size();) {

					TestStep currTestStep = steps.get(executionIndx);
					StepExecutionTracker stepExecutionTracker = currTestStep.getStepExecutionTracker();
					if (null != stepExecutionTracker) {
						stepExecutionTracker.setContext(Collections.emptyMap());
						stepExecutionTracker.setStepCompositer(this);
						stepExecutionTracker.getContext().put("testStepCompositer", this);
						stepExecutionTracker.setStepIndex(executionIndx);
						stepExecutionTracker.setNextStepIndex(++executionIndx);
					}
					currTestStep.execute();
					// next index can be modified form tracker in before or after
					// step
					if ((null != stepExecutionTracker)) {
						executionIndx = stepExecutionTracker.getNextStepIndex();
					} else {
						executionIndx += 1;
					}
				}
			} finally {
				// report remaining steps, if any, which are not executed.....
				for (; executionIndx < steps.size(); executionIndx++) {
					TestStep notRunTestStep = steps.get(executionIndx);

					QAFTestBase stb = TestBaseProvider.instance().get();

					CheckpointResultBean stepResultBean = new CheckpointResultBean();
					stepResultBean.setMessage(notRunTestStep.getDescription());
					stepResultBean.setType(MessageTypes.TestStep);
					stb.getCheckPointResults().add(stepResultBean);

					LoggingBean comLoggingBean = new LoggingBean(notRunTestStep.getName(),
							new String[] { Arrays.toString(notRunTestStep.getActualArgs()) }, "Not Run");
					stb.getLog().add(comLoggingBean);

				}
			}
		}

		@Override
		public String getFileName() {
			return "";
		}

		@Override
		public int getLineNumber() {
			return 0;
		}

		@Override
		public Collection<TestStep> getSteps() {
			return steps;
		}
	}
}