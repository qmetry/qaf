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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.Reporter;

/**
 * @author chirag.jayswal
 */
public class DryRunAnalyzer {
	private final List<StepCallLog> callLog = new ArrayList<StepCallLog>();

	private DryRunAnalyzer() {
		// ConfigurationManager.getStepMapping().values();
	}

	public synchronized void addStep(String step, String calledAs, String reference,
			String codeSninppet) {
		StepCallLog dstep = getStepCallLog(step);
		dstep.getReferences().add(reference);
		dstep.getCalls().add(calledAs);
		dstep.setCodeSninppet(codeSninppet);
	}

	private StepCallLog getStepCallLog(String step) {
		StepCallLog dstep = new StepCallLog(step);

		int i = callLog.indexOf(dstep);
		if (i >= 0) {
			dstep = callLog.get(i);
		} else {
			callLog.add(dstep);
		}

		return dstep;
	}

	public static boolean isDryRun(StringTestStep step) {
		if (ApplicationProperties.DRY_RUN_MODE.getBoolenVal(false)) {
			TestStep actualStep = step.getTestStep();
			if (null == actualStep) {
				String name = step.getDescription();
				//Reporter.log(name, MessageTypes.TestStepFail);
				String codeSnippet = step.getCodeSnippet();
				getInstance().addStep(step.getDescription(), name, step.getSignature(),
						codeSnippet);
				TestBaseProvider.instance().get().addVerificationError(new StepNotFoundException(step));
				return true;
			} else {
				getInstance().addStep(actualStep.getDescription(), step.getDescription(),
						step.getSignature(), "");
				if (!(actualStep instanceof CustomStep)) {
					Reporter.log(step.getDescription(), MessageTypes.TestStepPass);
					return true;
				}
			}
		}
		return false;
	}

	private static class LazyHolder {
		private static final DryRunAnalyzer INSTANCE = new DryRunAnalyzer();
	}

	public static DryRunAnalyzer getInstance() {
		return LazyHolder.INSTANCE;
	}

	public static class StepCallLog {
		private String name;
		private Set<String> references = new HashSet<String>();
		private Set<String> calls = new HashSet<String>();

		private String codeSninppet = "";

		public StepCallLog(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Set<String> getReferences() {
			return references;
		}
		public void setReferences(Set<String> references) {
			this.references = references;
		}
		public String getCodeSninppet() {
			return codeSninppet;
		}
		public void setCodeSninppet(String codeSninppet) {
			this.codeSninppet = codeSninppet;
		}
		public Set<String> getCalls() {
			return calls;
		}
		public void setCalls(Set<String> calls) {
			this.calls = calls;
		}
		@Override
		public String toString() {
			return name + " | " + (codeSninppet.trim().length() == 0) + " | "
					+ references.size() + " | " + calls.size();
		}

		@Override
		public boolean equals(Object obj) {
			if (null == obj)
				return false;
			if (obj.toString().equalsIgnoreCase(name))
				return true;
			if (obj instanceof StepCallLog) {
				return name.equalsIgnoreCase(((StepCallLog) obj).name);
			}
			return super.equals(obj);
		}
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					List<StepCallLog> steps = getInstance().callLog;
					if (!steps.isEmpty()) {
						System.out.println("Steps executed: " + steps);
						System.out.println("Step Name| Found | References | Calls\n");
						for (StepCallLog log : steps)
							System.out.println(log);
					}
				} catch (Exception exp) {

				}
			}
		});
	}
}
