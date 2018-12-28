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
package com.qmetry.qaf.automation.impl.step.cucumber;

import java.util.List;

import com.qmetry.qaf.automation.step.QAFTestStepProvider;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author chiragj.ayswal
 */
@QAFTestStepProvider()
public class CucumberStepImpl {
	@Given("^I am on Google Search Page$")
	public void step1() {
		System.out.println("I am on Google Search Page");

	}

	@When("^I search for \"([^\"]*)\"$")
	public void iSearchFor(String s) {
		System.out.println("I search for " + s);

	}

	@Then("^it should have following search results:$")
	public void itShouldHaveAllSearchResults(List<String> s) {
		System.out.printf("List: %s\n", s);

	}

	@Then("^it should have \"(.*)\" in search results$")
	public void itShouldHave_inSearchResults(String s) {
		System.out.printf("it should have %s in search results\n", s);

	}

	@Then("^I get at least (\\d+) results$")
	public void iGet_inSearchResults(int n) {
		System.out.printf("I get at least %d results\n", n);

	}
	
	@Given("^I have \"(.?)\"(?: and \"(.?)\")?$")
	public void optionalParameter(String param1, String optParam) {

	    System.out.println("text:" + param1 + " forText:" + optParam);
	}

}
