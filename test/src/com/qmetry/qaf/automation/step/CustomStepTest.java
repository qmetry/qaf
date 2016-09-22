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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.client.CustomStep;
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
