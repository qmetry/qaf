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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.google.gson.Gson;
import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.StepListener.java This is internal class and
 * not to be used f
 * 
 * @author chirag
 */
class TestStepListener implements QAFTestStepListener {

	private static final Log logger = LogFactoryImpl.getLog(TestStepListener.class);
	private ArrayList<CheckpointResultBean> allResults;
	private ArrayList<LoggingBean> allCommands;

	// for transaction steps
	private boolean ignoreDefaultListener = false;

	public TestStepListener() {

	}

	public void beforExecute(StepExecutionTracker stepExecutionTracker) {
		Map<String, Object> metadata = stepExecutionTracker.getStep().getMetaData();
		if (null != metadata && metadata.containsKey("qafstep-transaction")) {
			processTimeTracking(stepExecutionTracker);
		}
		if (!ignoreDefaultListener) {
			logger.info("Invoking " + getStepInfo(stepExecutionTracker.getStep()));

			QAFTestBase stb = TestBaseProvider.instance().get();
			allResults = new ArrayList<CheckpointResultBean>(stb.getCheckPointResults());
			allCommands = new ArrayList<LoggingBean>(stb.getLog());
			stb.getCheckPointResults().clear();
			stb.getLog().clear();
		}
	}

	public void onFailure(StepExecutionTracker stepExecutionTracker) {
		if (!ignoreDefaultListener) {
			logger.error("Failure " + getStepInfo(stepExecutionTracker.getStep()), stepExecutionTracker.getException());
		}

	}

	public void afterExecute(StepExecutionTracker stepExecutionTracker) {
		if (!ignoreDefaultListener) {
			afterStep(stepExecutionTracker);
			logger.info("Completed " + getStepInfo(stepExecutionTracker.getStep()));
		}
	}

	private void afterStep(StepExecutionTracker stepExecutionTracker) {
		Number duration = stepExecutionTracker.getEndTime() - stepExecutionTracker.getStartTime();

		QAFTestBase stb = TestBaseProvider.instance().get();

		if (StringUtil.isNotBlank(stepExecutionTracker.getVerificationError())) {
			stb.addVerificationError(stepExecutionTracker.getVerificationError());
		}

		Boolean success = stepExecutionTracker.isSuccess();
		if (stepExecutionTracker.hasException()) {
			CheckpointResultBean failureCheckpoint = new CheckpointResultBean();
			failureCheckpoint.setMessage(stepExecutionTracker.getException().getMessage());
			failureCheckpoint.setType(MessageTypes.Fail);
			stb.getCheckPointResults().add(failureCheckpoint);
		}
		MessageTypes type = getStepMessageType(stb.getCheckPointResults());
		if (success == null) {
			success = !(stepExecutionTracker.hasException() || type.isFailure());

			stepExecutionTracker.setSuccess(success);
		}

		LoggingBean stepLogBean = new LoggingBean(stepExecutionTracker.getStep().getName(),
				new String[] { Arrays.toString(stepExecutionTracker.getStep().getActualArgs()) },
				success ? "success" : "fail");
		stepLogBean.setSubLogs(new ArrayList<LoggingBean>(stb.getLog()));

		CheckpointResultBean stepResultBean = new CheckpointResultBean();
		stepResultBean.setMessage(processArgs(stepExecutionTracker.getStep().getDescription(),
				stepExecutionTracker.getStep().getActualArgs()));
		stepResultBean.setSubCheckPoints(new ArrayList<CheckpointResultBean>(stb.getCheckPointResults()));
		stepResultBean.setDuration(duration.intValue());
		stepResultBean.setThreshold(stepExecutionTracker.getStep().getThreshold());

		stepResultBean.setType(type);

		allResults.add(stepResultBean);

		stb.getCheckPointResults().clear();
		stb.getCheckPointResults().addAll(allResults);

		allCommands.add(stepLogBean);
		stb.getLog().clear();
		stb.getLog().addAll(allCommands);
	}

	private String getStepInfo(TestStep step) {
		return "Teststep: " + step.getDescription()
				+ ((step.getActualArgs() != null) && (step.getActualArgs().length > 0)
						? " with arguments: " + Arrays.toString(step.getActualArgs()) : "");
	}

	private MessageTypes getStepMessageType(List<CheckpointResultBean> subSteps) {
		MessageTypes type = MessageTypes.TestStepPass;
		for (CheckpointResultBean subStep : subSteps) {
			if (StringMatcher.containsIgnoringCase("fail").match(subStep.getType())) {
				return MessageTypes.TestStepFail;
			}
			if (StringMatcher.containsIgnoringCase("warn").match(subStep.getType())) {
				type = MessageTypes.Warn;
			}
		}
		return type;
	}

	private void processTimeTracking(StepExecutionTracker stepExecutionTracker) {
		QAFTestBase testBase = TestBaseProvider.instance().get();
		PropertyUtil context = testBase.getContext();

		if (context.containsKey("start.transation.tracker")) {

			// process end time tracking step
			StepExecutionTracker starStepExecutionTracker = (StepExecutionTracker) context
					.getObject("start.transation.tracker");
			context.clearProperty("start.transation.tracker");
			ignoreDefaultListener = true;

			int stIndex = getCheckPointIndex(testBase.getCheckPointResults(),
					processArgs(starStepExecutionTracker.getStep().getDescription(),
							starStepExecutionTracker.getStep().getActualArgs()));

			List<CheckpointResultBean> allcheckPoints = new ArrayList<CheckpointResultBean>(
					testBase.getCheckPointResults().subList(0, stIndex));

			List<CheckpointResultBean> subcheckPoints = new ArrayList<CheckpointResultBean>(
					testBase.getCheckPointResults().subList(stIndex + 1, testBase.getCheckPointResults().size()));
			int duration = 0;
			MessageTypes type = getStepMessageType(subcheckPoints);
			for (CheckpointResultBean checkpoint : subcheckPoints) {
				duration += checkpoint.getDuration();
			}

			CheckpointResultBean stepResultBean = new CheckpointResultBean();
			stepResultBean.setMessage((String) starStepExecutionTracker.getStep().getActualArgs()[0]);
			String threshold = starStepExecutionTracker.getStep().getActualArgs()[1].toString();
			stepResultBean
					.setThreshold(StringUtil.isBlank(threshold)?0:Integer.parseInt(threshold));

			// update duration and sub-check-point
			stepResultBean.setSubCheckPoints(subcheckPoints);
			stepResultBean.setDuration(duration);
			stepResultBean.setType(type);
			allcheckPoints.add(stepResultBean);
			testBase.getCheckPointResults().clear();
			testBase.getCheckPointResults().addAll(allcheckPoints);

			// update commands
			int commandIndex = getLogginIndex(testBase.getLog(), starStepExecutionTracker.getStep().getName());

			List<LoggingBean> allCommands = new ArrayList<LoggingBean>(testBase.getLog().subList(0, commandIndex));

			List<LoggingBean> subCommands = new ArrayList<LoggingBean>(
					testBase.getLog().subList(commandIndex + 1, testBase.getLog().size()));

			LoggingBean commandLogBean = new LoggingBean(starStepExecutionTracker.getStep().getName(),
					new String[] { Arrays.toString(starStepExecutionTracker.getStep().getActualArgs()) },
					type.isFailure() ? "fail" : "success");
			commandLogBean.setSubLogs(new ArrayList<LoggingBean>(subCommands));
			duration = 0;
			for (LoggingBean subCommand : subCommands) {
				duration += subCommand.getDuration();
			}
			commandLogBean.setDuration(duration);

			allCommands.add(commandLogBean);
			testBase.getLog().clear();
			testBase.getLog().addAll(allCommands);

		} else {
			// this is the start point
			context.addProperty("start.transation.tracker", stepExecutionTracker);
		}
	}

	private int getLogginIndex(List<LoggingBean> logs, String name) {
		for (int i = logs.size() - 1; i >= 0; i--)
			if (name.equalsIgnoreCase(logs.get(i).getCommandName()))
				return i;
		return -1;
	}

	private int getCheckPointIndex(List<CheckpointResultBean> logs, String name) {
		for (int i = logs.size() - 1; i >= 0; i--)
			if (name.equalsIgnoreCase(logs.get(i).getMessage()))
				return i;
		return -1;
	}

	private String processArgs(String description, Object... actualArgs) {
		if (null == actualArgs || actualArgs.length <= 0)
			return description;
		List<String> args = BDDDefinitionHelper.getArgNames(description);
		if (args.isEmpty() || args.size() != actualArgs.length)
			return description;
		try {
			for (int i = 0; i < actualArgs.length; i++) {
				if ((actualArgs[i] instanceof String)) {
					if (description.indexOf("$") >= 0) {
						// If argument is already replaced
						// like COMMENT: {0} replaced with COMMENT: '${uname}'
						description = StringUtil.replace(description, "$" + args.get(i),
								getParam((String) actualArgs[i]), 1);
					} else {
						// If argument is not processed
						description = StringUtil.replace(description, args.get(i),
								"'" + getParam((String) actualArgs[i]) + "'", 1);
					}
				} else {
					description = StringUtil.replace(description, args.get(i),
							"'" + String.valueOf(actualArgs[i]) + "'", 1);
				}
			}
		} catch (Exception e) {
			// ignore
		}
		return description;
	}

	private String getParam(String text) {
		String result = getBundle().getSubstitutor().replace(text);
		ParamType ptype = ParamType.getType(result);
		if (ptype.equals(ParamType.MAP)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> kv = new Gson().fromJson(result, Map.class);
			if (kv.containsKey("desc")) {
				result = String.valueOf(kv.get("desc"));
			} else if (kv.containsKey("description")) {
				result = String.valueOf(kv.get("description"));
			}
		}
		return result;
	}
	
}
