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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.JavaStepFinder;
import com.qmetry.qaf.automation.step.StringTestStep;

public class TestStepTest {
	@BeforeClass
	public void setUp() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "test.step");
		StringTestStep.addSteps(JavaStepFinder.getAllJavaSteps());
	}

	@Test
	public void stepProviderTest() {
		// StringTestStep.execute("step");
		ConfigurationManager.getBundle().setProperty("aaa", "bbb");

		StringTestStep.execute("step2", "aaa");
		DefaultStepProvider.step("direct call");
		Object o = StringTestStep.execute("setMap",
				(Object) (new String[]{"val1", "aaa", "val2", "bbb"}));
		ConfigurationManager.getBundle().setProperty("aaa", o);
		StringTestStep.execute("printMap", "${aaa}");

	}

	@Test(description = "Method annotted with QAFTestStep in class which is not step provider")
	public void stepExecuterTest() {
		StringTestStep.execute("testStep", "aaa");
		StringTestStep.execute("testStep2");
	}

	@Test()
	public void stepExecuterCommentTest() {
		StringTestStep.execute("comment", "aaahjg kjhkjh");
		// StringTestStep.execute("comment", new Object[]{"aaahjg",
		// " kjhkjh"});

		// StringTestStep.execute("comment");

	}

	@Test(description = "Method not annotted with QAFTestStep in class which is not step provider", expectedExceptions = RuntimeException.class)
	public void stepExecuterTest2() {
		StringTestStep.execute("normalMethod");
	}
}
