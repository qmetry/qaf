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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import com.google.gson.Gson;
import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.step.BDDStepMatcherFactory.DefaultBDDStepMatcher;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;

/**
 * com.qmetry.qaf.automation.exceltest.CustomStep.java
 * 
 * @author chirag.jayswal
 */
public class CustomStep extends BaseTestStep implements TestStepCompositer {
	private Collection<TestStep> steps;
	private String def;

	public CustomStep(String name, String description, Collection<TestStep> steps) {
		super(name, description);
		this.steps = steps;
		this.def = description;
		stepMatcher =  new DefaultBDDStepMatcher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.step.TestStep#execute(java.lang.Object[])
	 */
	@Override
	protected Object doExecute() {
		try {
			Object res = null;
			processStepParams();

			int executionIndx = 0;
			TestStep[] stepsToExecute = steps.toArray(new TestStep[steps.size()]);

			for (executionIndx = 0; executionIndx < stepsToExecute.length;) {
				TestStep currTestStep = stepsToExecute[executionIndx];
				((StringTestStep) currTestStep).initStep();

				StepExecutionTracker stepExecutionTracker = currTestStep.getStepExecutionTracker();

				if (null != stepExecutionTracker) {
					// quick-fix by Amit for ISFW-163
					stepExecutionTracker.getContext().putAll(getStepExecutionTracker().getContext());
					stepExecutionTracker.getContext().put("testStepCompositer", this);

					stepExecutionTracker.setStepCompositer(this);
					stepExecutionTracker.setStepIndex(executionIndx);
					stepExecutionTracker.setNextStepIndex(++executionIndx);
				} else {
					++executionIndx;
					QAFTestBase stb = TestBaseProvider.instance().get();

					CheckpointResultBean stepResultBean = new CheckpointResultBean();
					stepResultBean.setMessage(currTestStep.getDescription() + " :: Not Found.");
					stepResultBean.setType(MessageTypes.Warn);
					stb.getCheckPointResults().add(stepResultBean);

					LoggingBean comLoggingBean = new LoggingBean(currTestStep.getName(),
							new String[] { Arrays.toString(currTestStep.getActualArgs()) }, "Error: Step Not Found");
					stb.getLog().add(comLoggingBean);

					throw new StepInvocationException(currTestStep,
							"Test Step (" + currTestStep.getDescription()
									+ ") Not Found.\n Please provide implementation or check 'step.provider.pkg' property value points to appropriate package.",
							true);
				}
				res = currTestStep.execute();

				// next index can be modified form tracker in before or after
				// step
				executionIndx = stepExecutionTracker.getNextStepIndex();

			}
			return res;
		} catch (Throwable e) {
			throw new StepInvocationException(this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.step.TestStep#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSignature() {
		return "Custom-Step[" + name + ":" + getFileName() + "#" + getLineNumber() + "] - " + getDescription();
	}

	@SuppressWarnings({ "unchecked" })
	public void processStepParams() {
		// process parameters in step;
		if ((actualArgs != null) && (actualArgs.length > 0)) {
			Map<String, Object> paramMap = getStepExecutionTracker().getContext();
			List<String> paramNames = BDDDefinitionHelper.getArgNames(def);

			System.out.println(paramNames);
			for (int i = 0; i < actualArgs.length; i++) {
				String paramName = paramNames.get(i).trim();
				// remove starting { and ending } from parameter name
				paramName = paramName.substring(1, paramName.length() - 1).split(":", 2)[0];

				// in case of data driven test args[0] should not be overriden
				// with steps args[0]
				if ((actualArgs[i] instanceof String)) {

					String pstr = (String) actualArgs[i];

					if (pstr.startsWith("${") && pstr.endsWith("}")) {
						String pname = pstr.substring(2, pstr.length() - 1);
						actualArgs[i] = paramMap.containsKey(pstr) ? paramMap.get(pstr)
								: paramMap.containsKey(pname) ? paramMap.get(pname)
										: getBundle().containsKey(pstr) ? getBundle().getObject(pstr)
												: getBundle().getObject(pname);
					} else if (pstr.indexOf("$") >= 0) {
						pstr = getBundle().getSubstitutor().replace(pstr);
						actualArgs[i] = StrSubstitutor.replace(pstr, paramMap);
					}
					// continue;
					ParamType ptype = ParamType.getType(pstr);
					if (ptype.equals(ParamType.MAP)) {
						Map<String, Object> kv = new Gson().fromJson(pstr, Map.class);
						paramMap.put(paramName, kv);
						for (String key : kv.keySet()) {
							paramMap.put(paramName + "." + key, kv.get(key));
						}
					} else if (ptype.equals(ParamType.LIST)) {
						List<Object> lst = new Gson().fromJson(pstr, List.class);
						paramMap.put(paramName, lst);
						for (int li = 0; li < lst.size(); li++) {
							paramMap.put(paramName + "[" + li + "]", lst.get(li));
						}
					}
				}

				paramMap.put("${args[" + i + "]}", actualArgs[i]);
				paramMap.put("args[" + i + "]", actualArgs[i]);
				paramMap.put(paramName, actualArgs[i]);

			}

			description = StrSubstitutor.replace(description, paramMap);

			for (TestStep step : steps) {
				((StringTestStep) step).initStep();
				if ((step.getActualArgs() != null) && (step.getActualArgs().length > 0)) {
					for (int j = 0; j < step.getActualArgs().length; j++) {

						if (paramMap.containsKey(step.getActualArgs()[j])) {
							step.getActualArgs()[j] = paramMap.get(step.getActualArgs()[j]);
						} else {
							step.getActualArgs()[j] = StrSubstitutor.replace(step.getActualArgs()[j], paramMap);
						}

					}
				} else if (step.getName().indexOf("$") >= 0) {
					// bdd?
					String name = StrSubstitutor.replace(step.getName(), paramMap);

					((BaseTestStep) step).setName(name);
				}
			}
		}
	}

	@Override
	public TestStep clone() {
		ArrayList<TestStep> stepsClone = new ArrayList<TestStep>();
		for (TestStep ts : steps) {
			stepsClone.add(ts.clone());
		}
		CustomStep cloneObj = new CustomStep(name, description, stepsClone);
		cloneObj.setFileName(getFileName());
		cloneObj.setLineNumber(getLineNumber());
		cloneObj.setThreshold(getThreshold());
		cloneObj.setMetaData(getMetaData());
		if (null != actualArgs) {
			cloneObj.actualArgs = actualArgs.clone();
		}
		cloneObj.setStepMatcher(getStepMatcher());
		return cloneObj;
	}

	@Override
	public Collection<TestStep> getSteps() {
		return steps;
	}

}
