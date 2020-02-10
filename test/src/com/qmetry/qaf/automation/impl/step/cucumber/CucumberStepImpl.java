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
package com.qmetry.qaf.automation.impl.step.cucumber;

import java.util.List;
import java.util.Map;

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

	@When("^I parse nested object \"([^\"]*)\"$")
	public Map<String, Object> IParseNestedObject(Map<String, Object> nestedObject) {
		System.out.println(nestedObject.entrySet());
		return nestedObject;
	}

}
