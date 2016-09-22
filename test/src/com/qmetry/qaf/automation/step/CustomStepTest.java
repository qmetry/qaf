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
