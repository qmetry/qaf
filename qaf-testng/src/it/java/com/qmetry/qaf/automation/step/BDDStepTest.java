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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.testng.dataprovider.QAFInetrceptableDataProvider;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;

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
		Validator.assertThat(step.getActualArgs()[0].toString(), Matchers.equalToIgnoringCase("aaa'bbbb"));

	}

	@Test
	public void testArgRef() {
		StringTestStep step = new StringTestStep("step with '${testdata.ref}' arg" );
		Object res = step.execute();
		Validator.assertThat(res.toString(), Matchers.equalToIgnoringCase("actual value"));
		
		step = new StringTestStep("step with '${testdata.subref}' arg" );
		res = step.execute();
		Validator.assertThat(res.toString(), Matchers.equalToIgnoringCase("actual value 1"));

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
		Validator.assertThat(step.getActualArgs()[1].toString(), Matchers.equalToIgnoringCase("aa a"));


	}

	@Test
	public void testArgOrderAsParam() {
	
		String stepCall = "scheduled payment of type 'savings' from 'account-2' to 'account-1'";
		StringTestStep step = new StringTestStep(stepCall);
		step.execute();
		Validator.assertThat(step.getActualArgs()[1].toString(), Matchers.equalToIgnoringCase("account-2"));

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
	public void testArgWithQuotes() {
		String err_msg=
				"message Wrong \\\"user name or password\\\"";
		System.out.println(err_msg);

		String stepCall = "Then It should show error message \""+err_msg +"\" on screen";
		StringTestStep step = new StringTestStep(stepCall);
		step.execute();
		
		err_msg=
				"message Wrong \"user name or password\"";

		System.out.println(err_msg);
		 stepCall = "Then It should show error message '"+err_msg +"' on screen";
		 step = new StringTestStep(stepCall);
		step.execute();

	}
	
	@QAFDataProvider(dataFile="resources/testdata.txt")
	@Test(dataProvider=QAFDataProvider.NAME, dataProviderClass=QAFInetrceptableDataProvider.class)
	public void testArgWithQuotesFromFile(Map<String, Object> data) {
		String err_msg= (String) data.get("searchResult");
		getBundle().setProperty("err_msg_from_file", err_msg);
		String stepCall = "step with \"${err_msg_from_file}\" arg";
		boolean res = BDDDefinitionHelper.matches("step with {0} arg", stepCall);
		Validator.assertTrue(res, "Step call didn't match", "Step call matched");
		
		StringTestStep step = new StringTestStep("step with \"${err_msg_from_file}\" arg" );
		Object sres = step.execute();
		System.out.println(sres);
		Validator.assertThat(sres.toString(), Matchers.equalToIgnoringCase(err_msg));
		
		getBundle().clearProperty("err_msg_from_file");

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
		Validator.assertThat(arg1, Matchers.notNullValue());
		Validator.assertThat(arg2, Matchers.notNullValue());
	}

	@QAFTestStep(description = "Bdd with String arg {0} and Array arg {1}")
	public static void bdd(String resultLinkText, String[] s) {
		Validator.assertThat(resultLinkText, Matchers.notNullValue());
		Validator.assertThat(s, Matchers.notNullValue());
		Reporter.log("bdd with array argument: " + resultLinkText + " - "
				+ Arrays.toString(s));
	}

	@QAFTestStep(stepName = "Bdd 1 argument {0}")
	public static void bdd1arg(String resultLinkText) {
		Validator.assertThat(resultLinkText, Matchers.notNullValue());
		Reporter.log("bdd with 1 argument: " + resultLinkText);
	}
	
	@QAFTestStep(stepName = "step with {0} arg")
	public static Object stepRetVal(Object arg) {
		return arg;
	}

	@QAFTestStep(stepName = "Bdd with array {0}")
	public static void bdd1arg_array(String[] s) {
		Validator.assertThat(s, Matchers.notNullValue());
		Reporter.log("bdd with array argument: " + Arrays.toString(s));
	}

	@QAFTestStep(stepName = "Bdd with date {0}")
	public static void bdd1arg_date(Date birthday) {
		Validator.assertThat(birthday, Matchers.notNullValue());
		Reporter.log("bdd with array argument: " + birthday);
	}

	@QAFTestStep(stepName = "Bdd with map {0}")
	public static void bdd1arg_array(Map<String, Object> s) {
		Validator.assertThat(s, Matchers.notNullValue());
		Reporter.log("bdd with array argument: " + s);
	}
	@QAFTestStep(stepName = "Normal step")
	public static void normal(String resultLinkText, String[] s) {
		Validator.assertThat(resultLinkText, Matchers.notNullValue());
		Validator.assertThat(s, Matchers.notNullValue());	
	}

	@QAFTestStep(description = "It should show error message {0} on screen")
	public static void verifyAlert(String error) {
		Validator.assertThat(error, Matchers.notNullValue());
	}

	@QAFTestStep(description = "step for int {0,number,integer}")
	public static void intArg(int error) {
		Validator.assertThat(error, Matchers.notNullValue());
	}

	@QAFTestStep(description = "step for int list {0}")
	public static void intListArg(List<Integer> error) {
		Validator.assertThat(error, Matchers.notNullValue());
	}

	@QAFTestStep(description = "type {1} into {0}")
	public static void type(String loc, String val) {
		Validator.assertThat(loc, Matchers.notNullValue());
		Validator.assertThat(val, Matchers.notNullValue());
	}
	
    @QAFTestStep(description = "scheduled payment of type {ScheduleType} from {acct2} to {acct1}")
    public void scheduledPaymentType(String scheduleType, String acct2, String acct1) {
		Validator.assertThat(scheduleType, Matchers.notNullValue());
		Validator.assertThat(acct2, Matchers.notNullValue());
		Validator.assertThat(acct1, Matchers.notNullValue());
    }

	@Test
	public void stepFormatterTest() {
		StringTestStep.execute("Just for test formatter 'test'");
	}
}
