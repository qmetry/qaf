/*******************************************************************************
 * Copyright 2016 Infostretch Corporation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.qmetry.qaf.automation.step;

import java.util.HashMap;
import java.util.Map;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;

/**
 * In this class some methods are test.step.SampleStepProvider.java
 * 
 * @author chirag
 */
public class SampleStepProvider {

	public void normalMethod() {
		System.out.println("step invoked!.....");
	}

	@QAFTestStep(stepName = "testStep", description = "say {0}")
	public static void testStep(String s) {

		System.out.println("testStep invoked!....." + s);
	}

	@QAFTestStep(stepName = "testStep2")
	public void step() {
		System.out.println("testStep without args invoked!.....");
	}

	@QAFTestStep(description = "step that Returns complex object")
	public TestStep stepReturnObj() {
		return new StringTestStep("testStep",
				"step returns step that Returns complex object");
	}

	@QAFTestStep(description = "step  Requires complex object")
	public void stepRequiresObj(TestStep step) {
		step.execute();
	}

	@QAFTestStep(description = "step that Returns map object")
	public Map<String, String> setMap(String[] vals) {
		Map<String, String> retObj = new HashMap<String, String>();
		try {
			for (int i = 0; i < vals.length; i += 2) {
				retObj.put(vals[i], vals[i + 1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retObj;
	}

	@QAFTestStep(description = "step  Requires map object")
	public void printMap(Map<String, String> map) {
		System.out.printf("%s\n", map);
	}

}
