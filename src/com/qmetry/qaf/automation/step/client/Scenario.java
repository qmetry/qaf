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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.data.MetaDataScanner;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.StepExecutionTracker;
import com.qmetry.qaf.automation.step.StepNotFoundException;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.TestStepCompositer;
import com.qmetry.qaf.automation.testng.QAFTestNGTest;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.client.Scenario.java
 * 
 * @author chirag.jayswal
 */
public class Scenario extends WebDriverTestCase
		implements
			QAFTestNGTest,
			TestStepCompositer {

	protected String scenarioName;
	protected String description = "";
	protected Collection<TestStep> steps;
	private static final int SCANARIOBASEINDEX = 1000;
	private static int scanariocount = 0;
	private int priority;
	protected String[] m_groups = {};
	protected String[] m_groupsDependedUpon = {};
	protected String[] m_methodsDependedUpon = {};
	protected String[] m_beforeGroups = {};
	protected String[] m_afterGroups = {};
	protected long timeOut;
	private String signature;
	protected String status = "";
	private Map<String, Object> metadata =
			new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

	public Scenario(String testName, Collection<TestStep> steps) {
		this(testName, steps, null);
	}

	public Scenario(String testName, Collection<TestStep> steps,
			Map<String, Object> metadata) {
		priority = SCANARIOBASEINDEX + scanariocount++;
		scenarioName = testName.trim();
		this.steps = steps;
		if (null != metadata)
			this.metadata.putAll(metadata);
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		signature = comuteSign();

		if (metadata.containsKey(ScenarioFactory.GROUPS)) {
			m_groups = ((List<String>) metadata.get(ScenarioFactory.GROUPS))
					.toArray(new String[]{});
		}
		if (metadata.containsKey("dependsOnGroups")) {
			m_groupsDependedUpon = ((List<String>) metadata.get("dependsOnGroups"))
					.toArray(new String[]{});
		}

		if (metadata.containsKey("dependsOnMethods")) {
			m_methodsDependedUpon = ((List<String>) metadata.get("dependsOnMethods"))
					.toArray(new String[]{});
		}
		if (metadata.containsKey("desc"))
			description = (String) metadata.get("desc");
		if (metadata.containsKey("description"))
			description = (String) metadata.get("description");

		if (metadata.containsKey("priority")) {
			priority = ((Number) metadata.get("priority")).intValue();
		}
		if (metadata.containsKey("timeOut")) {
			timeOut = ((Number) metadata.get("timeOut")).longValue();
		}

	}

	protected String comuteSign() {
		return getPackage() + "." + scenarioName + "()";
	}

	@Override
	public String getTestName() {
		return scenarioName;
	}

	protected void execute(TestStep[] stepsToExecute, Map<String, Object> context) {
		int executionIndx = 0;
		try {
			for (executionIndx = 0; executionIndx < stepsToExecute.length;) {
				TestStep currTestStep = stepsToExecute[executionIndx];
				if(StringUtil.isBlank(currTestStep.getName()) && null!=currTestStep.getActualArgs() && currTestStep.getActualArgs().length>0){
					++executionIndx;
					Reporter.log(currTestStep.getActualArgs()[0].toString());
					continue;
				}
				((StringTestStep) currTestStep).initStep();
				
				StepExecutionTracker stepExecutionTracker =
						currTestStep.getStepExecutionTracker();
				if (null != stepExecutionTracker) {
					stepExecutionTracker.setContext(context);
					stepExecutionTracker.setStepCompositer(this);
					stepExecutionTracker.getContext().put("testStepCompositer", this);
					stepExecutionTracker.setStepIndex(executionIndx);
					stepExecutionTracker.setNextStepIndex(++executionIndx);
				} else {
					++executionIndx;
					QAFTestBase stb = TestBaseProvider.instance().get();

					CheckpointResultBean stepResultBean = new CheckpointResultBean();
					stepResultBean
							.setMessage(currTestStep.getDescription() + " :: Not Found.");
					stepResultBean.setType(MessageTypes.Warn);
					stb.getCheckPointResults().add(stepResultBean);

					LoggingBean comLoggingBean =
							new LoggingBean(currTestStep.getName(),
									new String[]{Arrays
											.toString(currTestStep.getActualArgs())},
									"Error: Step Not Found");
					stb.getLog().add(comLoggingBean);
					if (!ApplicationProperties.DRY_RUN_MODE.getBoolenVal(false))
						throw new StepNotFoundException((StringTestStep) currTestStep);
				}

				currTestStep.execute();

				// next index can be modified form tracker in before or after
				// step
				if ((null != stepExecutionTracker))
					executionIndx = stepExecutionTracker.getNextStepIndex();

			}
			if (ApplicationProperties.DRY_RUN_MODE.getBoolenVal(false)){
				Map<String, Object> metadataToValidate =
						new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
				metadataToValidate.putAll(metadata);
				metadataToValidate.putAll(context);
				String messages = MetaDataScanner.applyMetaRule(metadataToValidate);
				if(StringUtil.isNotBlank(messages)){
					Reporter.log("Metadata rule failure:" + messages, MessageTypes.TestStepFail);
					throw new AutomationError("Metadata rule failure:" + messages);
				}
			}
			status = "SUCCESS";

		} catch (RuntimeException t) {
			status = "FAILURE";
			throw t;
		} catch (Error e) {
			status = "FAILURE";
			throw e;
		} finally {
			// report remaining steps, if any, which are not executed.....
			for (; executionIndx < steps.size(); executionIndx++) {
				TestStep notRunTestStep = stepsToExecute[executionIndx];
				((StringTestStep) notRunTestStep).initStep();

				QAFTestBase stb = TestBaseProvider.instance().get();

				CheckpointResultBean stepResultBean = new CheckpointResultBean();
				stepResultBean.setMessage(notRunTestStep.getDescription());
				stepResultBean.setType(MessageTypes.TestStep);
				stb.getCheckPointResults().add(stepResultBean);

				LoggingBean comLoggingBean =
						new LoggingBean(notRunTestStep.getName(),
								new String[]{
										Arrays.toString(notRunTestStep.getActualArgs())},
								"Not Run");
				stb.getLog().add(comLoggingBean);

			}

			logger.info("Competed scenario: " + scenarioName + " with status " + status);
			getBundle().subset("ctx").clear();
		}
	}

	@Test(groups = "scenario")
	public void scenario() {
		beforeScanario();
		if (steps.size() > 0) {
			TestStep[] stepsToExecute = steps.toArray(new TestStep[steps.size()]);// new
																					// TestStep[steps.size()];
			for (int i = 0; i < stepsToExecute.length; i++) {
				stepsToExecute[i] = stepsToExecute[i].clone(); // fix for retry
			}
			execute(stepsToExecute, new HashMap<String, Object>());
		}

	}

	public String getDescription() {
		return description;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	@Override
	public int hashCode() {
		return ((Integer) getPriority()).hashCode();
	}

	public int getPriority() {
		return priority;
	}

	public String[] getM_groups() {
		return m_groups;
	}

	public String[] getM_groupsDependedUpon() {
		return m_groupsDependedUpon;
	}

	public String[] getM_methodsDependedUpon() {
		return m_methodsDependedUpon;
	}

	public long getTimeOut() {
		return timeOut;
	}
	public boolean isM_isAlwaysRun() {
		return !metadata.containsKey("alwaysRun") || (Boolean) metadata.get("alwaysRun");// m_isAlwaysRun;
	}

	public boolean isM_enabled() {
		return !metadata.containsKey("enabled") || (Boolean) metadata.get("enabled");// m_enabled;
	}

	protected void beforeScanario() {
		status = "NOTRUN";
		logger.info("\n\nExecuting scenario: " + scenarioName + " - " + description);
		getBundle().setProperty(ApplicationProperties.CURRENT_TEST_NAME.key,
				scenarioName);
		getBundle().setProperty(ApplicationProperties.CURRENT_TEST_DESCRIPTION.key,
				description);
	}

	public String getSignature() {
		return signature;
	}

	@Override
	public String getFileName() {
		if (metadata.containsKey("fileName"))
			return (String) metadata.get("fileName");
		return "";
	}

	@Override
	public int getLineNumber() {
		if (metadata.containsKey("lineNumber"))
			return (Integer) metadata.get("lineNumber");
		return 0;
	}

	/**
	 * default is false - don't ignore missing dependencies
	 * 
	 * @return
	 */
	public boolean getIgnoreMissingDependencies() {
		return metadata.containsKey("ignoreMissingDependencies")
				&& (Boolean) metadata.get("ignoreMissingDependencies");
	}

	@Override
	public Collection<TestStep> getSteps() {
		return steps;
	}

	protected String getPackage() {
		if (null == metadata || !metadata.containsKey("referece")) {
			return "";
		}
		String filePath = (String) metadata.get("referece");

		return filePath.replaceAll("/", ".");
	}

}
