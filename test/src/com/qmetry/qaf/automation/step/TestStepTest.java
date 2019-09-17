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

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;

public class TestStepTest {

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
//		StringTestStep.execute("comment", new Object[]{"aaahjg", " kjhkjh"});
//
//		StringTestStep.execute("comment");

	}

	@Test(description = "Method not annotted with QAFTestStep in class which is not step provider", expectedExceptions = RuntimeException.class)
	public void stepExecuterTest2() {
		StringTestStep.execute("normalMethod");
	}

	@Test(description = "Step not found exception", expectedExceptions = StepNotFoundException.class)
	public void stepExecuterTest3() {
		System.out.println(
				new StringTestStep("And i have 10 ruppes for 'true'").getCodeSnippet());
		StringTestStep.execute("And i have 10 ruppes for 'true'", new Object[]{});
		StringTestStep.execute("keyWord", new Object[]{"a"});

	}
	

	@Test(description = "")
	public void testStepFromClassExtendingAnotherClass() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step.common");

		StringTestStep.execute("step from class extending another class", new Object[]{});

	}}
