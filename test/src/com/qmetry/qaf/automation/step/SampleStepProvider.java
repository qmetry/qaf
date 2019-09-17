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

import com.qmetry.qaf.automation.impl.step.formatter.TestArgFormatter;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.util.Validator;

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
	
	@QAFTestStep(description = "Just for test formatter {str}")
	public void stepWithFormatter(@Formatter(TestArgFormatter.class)String s) {
		Validator.assertTrue(s.endsWith("formatted"), "argument didn't formatted", "argument formatted");
	}
}
