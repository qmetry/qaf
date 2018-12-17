/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/

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
		String stepCall = "It should show error message \""+err_msg +"\" on screen";
		boolean res = BDDDefinitionHelper.matches("It should show error message {0} on screen", stepCall);
		Validator.assertTrue(res, "Step call didn't match", "Step call matched");
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

	@Test
	public void stepFormatterTest() {
		StringTestStep.execute("Just for test formatter 'test'");
	}
}
