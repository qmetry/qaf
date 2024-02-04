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
import java.util.Collection;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class CustomStepTest {

	@Test
	public void CustomTestStepTestMapBDD() {
		String description = "user logins with {asd}";
		Collection<TestStep> steps = new ArrayList<TestStep>();
		steps.add(new StringTestStep("Given COMMENT: '${asd.username}'"));
		steps.add(new StringTestStep("store '${asd.username}' into 'result'"));

		CustomStep cs = new CustomStep("login", description, steps);
		ConfigurationManager.getStepMapping().put("login".toUpperCase(), cs);
		TestStep step = new StringTestStep("user logins with {'username':'bdduser'}");
		step.execute();

		Validator.assertThat(ConfigurationManager.getBundle().getString("result"),
				Matchers.equalToIgnoringCase("bdduser"));
	}

	@Test
	public void CustomTestStepTestMapKWD() {
		String description = "user logins with {asd}";
		Collection<TestStep> steps = new ArrayList<TestStep>();
		steps.add(new StringTestStep("comment", "${asd.username}"));
		steps.add(new StringTestStep("store", "${asd.username}", "result"));

		CustomStep cs = new CustomStep("login", description, steps);
		ConfigurationManager.getStepMapping().put("login".toUpperCase(), cs);
		TestStep step = new StringTestStep("login", "{'username':'kwduser'}");
		step.execute();

		Validator.assertThat(ConfigurationManager.getBundle().getString("result"),
				Matchers.equalToIgnoringCase("kwduser"));
	}
	
	@QAFTestStep(description = "COMMENT: {0}")
	public static void comment(Object args) {
		System.out.printf("COMMENT: %s \n", args);
	}
	
	@QAFTestStep(description = "store {0} into {1}")
	public static void store(Object val, String var) {
		getBundle().addProperty(var, val);
	}
}
