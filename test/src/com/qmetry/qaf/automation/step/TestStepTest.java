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
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.BDDStepMatcherFactory.GherkinStepMatcher;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.Validator;

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
	@SuppressWarnings("unchecked")
	@Test(description = "cucumber step with object arg from test data", enabled=true)
	public void bug321() {
		
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step");
		String json =" {\r\n" + 
				"    \"nestedObject\": {\r\n" + 
				"      \"keyName\": \"SOME TEXT WITH BLANK SPACES\"	  \r\n" + 
				"    }\r\n" + 
				"  }";
		
		GherkinStepMatcher m = new GherkinStepMatcher();
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("nestedObject", JSONUtil.toObject(json));
		boolean found = m.matches("^I parse nested object ((?:\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\")|(?:'([^'\\\\]*(\\\\.[^'\\\\]*)*)'))$", "I parse nested object \"${nestedObject}\"", context);
		System.out.println(found);
		List<String[]> args = m.getArgsFromCall("^I parse nested object \"([^\"]*)\"$", "I parse nested object \"${nestedObject}\"", context);
		System.out.println(JSONUtil.toString(args));
		//^I have ((?:-?\d+)|(?:\d+)) cukes in my ((?:"([^"\\]*(\\.[^"\\]*)*)")|(?:'([^'\\]*(\\.[^'\\]*)*)'))s
		//args = m.getArgsFromCall("^I have ((?:-?\\d+)|(?:\\d+)) cukes in my ((?:\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\")|(?:'([^'\\\\]*(\\\\.[^'\\\\]*)*)'))s", "I have 10 cukes in my \"bag\"s", context);
		//System.out.println(JSONUtil.toString(args));
		ConfigurationManager.getBundle().setProperty("nestedObject",  new Gson().fromJson(json,Object.class));
		Object res = StringTestStep.execute("I parse nested object \"${nestedObject}\"");
		Validator.assertThat(res, Matchers.instanceOf(Map.class));
		Validator.assertThat(((Map<String, Object>)res).get("nestedObject"), Matchers.instanceOf(Map.class));
		
		System.out.println(JSONUtil.toString(res));

	}
	
	@Test(description = "Step with partial escap")
	public void issue320() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step");

		Object res = StringTestStep.execute("test value(s) 'somevalue' with {esc}", new Object[]{});

		Validator.verifyThat("result " + res,
				res, Matchers.is("somevalue"));
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
	
	@Test(description = "Auto parse List of complex objects in step argument")
	public void feature303() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step");

		StringTestStep.execute("And I have set of:[{\"name\":\"item-1\"},{\"name\":\"item-2\"}]", new Object[]{});

		StringTestStep.execute("And I have set of:[\"name\",\"item,-1\",\"item-2\"]", new Object[]{});
		
		StringTestStep.execute("And I have set of:{\"name\":\"<item,-1>\"}", new Object[]{});
		StringTestStep.execute("I see following colors:\"RED\"", new Object[]{});
		StringTestStep.execute("I see following colors:[\"GREEN\"]", new Object[]{});
		StringTestStep.execute("I see following colors:[\"R,ED\",\"GREEN\"]", new Object[]{});
	}
	
	@SuppressWarnings({"unchecked"})
	@Test(description = "Bdd Step call with Map argument removes entry with null value from map argument")
	public void bug315() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step");

		String json = "{'l':1,'a':null}";

		Map<String, Object> o = JSONUtil.toObject(json, Map.class);
		Validator.assertThat("map should have key 'a'" + o, o, Matchers.hasEntry("a",null));
		System.out.println(o);

		o = (Map<String, Object>) StringTestStep.execute("testArguments", new Object[] {o});
		Validator.assertThat("map should have key 'a'" + o, o, Matchers.hasKey("a"));
	}

	@Test(description = "")
	public void testStepFromClassExtendingAnotherClass() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "com.qmetry.qaf.automation.impl.step.common");

		StringTestStep.execute("step from class extending another class", new Object[]{});

	}}
