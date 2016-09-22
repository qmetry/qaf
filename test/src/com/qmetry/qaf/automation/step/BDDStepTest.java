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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.util.Reporter;

/**
 * 
 * @author chirag.jayswal
 */
public class BDDStepTest {
	@BeforeClass
	public void setUp() {
		getBundle().setProperty("step.provider.pkg", "test.step");
		StringTestStep.addSteps(JavaStepFinder.getAllJavaSteps());
	}

	@Test
	public void testBDD() {
		String stepCall = "Bdd with String arg 'aaa' and Array arg [\"val1\",\"val2\"]";

		StringTestStep step = new StringTestStep(stepCall);
		step.execute();
	}

	@Test
	public void testBDDwithaAostrophe() {
		String stepCall = "Bdd with arg 'aaa\\'bbbb' and second arg 'data\\'with'";

		StringTestStep step = new StringTestStep(stepCall);
		step.execute();
	}

	@Test
	public void testBDDWithDate() {
		String stepCall = "Bdd with date '12-05-2012'";

		StringTestStep step = new StringTestStep(stepCall);
		step.execute();

	}

	@Test
	public void testBDDArgOrderInDesc() {
		String stepCall = "type 'aa a' into 'bbb'";

		StringTestStep step = new StringTestStep(stepCall);
		step.execute();

	}

	@Test
	public void testBDDMapArg() {
		String stepCall = "Bdd with map {'aaa':111}";

		StringTestStep step = new StringTestStep(stepCall);
		step.execute();

		stepCall = "step for int 5";
		step = new StringTestStep(stepCall);
		step.execute();
		stepCall = "step for int list '[5,6]'";
		step = new StringTestStep(stepCall);
		step.execute();
	}
	@Test
	public void testArgAsParam() {
		getBundle().setProperty("err_msg",
				"message Wrong user name or password");

		String stepCall = "Then It should show error message '${err_msg}' on screen";
		StringTestStep step = new StringTestStep(stepCall);
		step.execute();

	}
	@Test
	public void test1Param() {
		String stepCall =
				"Then It should show error message 'message Wrong user name or password' on screen";
		StringTestStep step = new StringTestStep(stepCall);
		step.execute();

	}
	
	@QAFTestStep(description = "Bdd with arg {0} and second arg {1}")
	public static void bddApostrophe(String arg1, String arg2) {
		System.out.println("arguments are::" + arg1 + "second" + arg2);
	}

	@QAFTestStep(description = "Bdd with String arg {0} and Array arg {1}")
	public static void bdd(String resultLinkText, String[] s) {
		System.out.println(
				"bdd resultLinkText: " + resultLinkText + " - " + Arrays.toString(s));
		Reporter.log("bdd with array argument: " + resultLinkText + " - "
				+ Arrays.toString(s));
	}

	@QAFTestStep(stepName = "Bdd 1 argument {0}")
	public static void bdd1arg(String resultLinkText) {
		System.out.println("bdd: " + resultLinkText);
		Reporter.log("bdd with array argument: " + resultLinkText);
	}

	@QAFTestStep(stepName = "Bdd with array {0}")
	public static void bdd1arg_array(String[] s) {
		System.out.println("bdd: " + Arrays.toString(s));
		Reporter.log("bdd with array argument: " + Arrays.toString(s));
	}

	@QAFTestStep(stepName = "Bdd with date {0}")
	public static void bdd1arg_date(Date birthday) {
		System.out.println("bdd: " + birthday);
		Reporter.log("bdd with array argument: " + birthday);
	}

	@QAFTestStep(stepName = "Bdd with map {0}")
	public static void bdd1arg_array(Map<String, Object> s) {
		System.out.println("bdd: " + s);
		Reporter.log("bdd with array argument: " + s);
	}
	@QAFTestStep(stepName = "Normal step")
	public static void normal(String resultLinkText, String[] s) {

		System.out.println("Testing normal " + resultLinkText + " - " + s[0]);
	}

	@QAFTestStep(description = "It should show error message {0} on screen")
	public static void verifyAlert(String error) {
		System.out.println("Error: " + error);
	}

	@QAFTestStep(description = "step for int {0,number,integer}")
	public static void intArg(int error) {
		System.out.println("int: " + error);
	}

	@QAFTestStep(description = "step for int list {0}")
	public static void intListArg(List<Integer> error) {
		System.out.println("Error: " + error);
	}

	@QAFTestStep(description = "type {1} into {0}")
	public static void type(String loc, String val) {
		System.out.println(loc + " -> " + val);
	}

}
