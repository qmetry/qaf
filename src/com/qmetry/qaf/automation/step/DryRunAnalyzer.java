/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/
package com.qmetry.qaf.automation.step;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.CustomStep;
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
				Reporter.log(name, MessageTypes.TestStepFail);
				// new StepNotFoundException(this);//just generate snippet....
				String codeSnippet = step.getCodeSnippet();
				getInstance().addStep(step.getDescription(), name, step.getSignature(),
						codeSnippet);
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
