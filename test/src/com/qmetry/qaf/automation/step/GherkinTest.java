/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.BDDStepMatcherFactory.GherkinStepMatcher;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.gherkin.GherkinFileParser;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class GherkinTest {

	@Test
	public void testGherkinParser() {
		GherkinFileParser parser = new GherkinFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkin.feature", scenarios);
		Validator.assertThat(scenarios, Matchers.hasSize(7));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("@Web"));
		}

	}

	@Test(description = "make sure it parse scenario with all steps")
	public void testGherkinParserForScenarioSteps() {
		GherkinFileParser parser = new GherkinFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkinWithSingleScenario.feature", scenarios);
		Validator.assertThat("Scenarios in feature", scenarios, Matchers.hasSize(1));
		Validator.assertThat("Steps in scenario", scenarios.get(0).getSteps(), Matchers.hasSize(4));
	}

	@Test
	public void parseBDDWithMultipleScenario() {
		GherkinFileParser parser = new GherkinFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkin.feature", scenarios);
		Validator.assertThat("Scenarios in feature", scenarios, Matchers.hasSize(7));

		Validator.assertThat("steps in scenario 1", scenarios.get(0).getSteps(), Matchers.hasSize(4));
		Validator.assertThat("steps in scenario 2", scenarios.get(1).getSteps(), Matchers.hasSize(3));
		Validator.assertThat("steps in scenario 3", scenarios.get(2).getSteps(), Matchers.hasSize(4));
		Validator.assertThat("steps in scenario 4", scenarios.get(3).getSteps(), Matchers.hasSize(4));
		Validator.assertThat("steps in scenario 5", scenarios.get(4).getSteps(), Matchers.hasSize(4));
		Validator.assertThat("steps in scenario 6", scenarios.get(5).getSteps(), Matchers.hasSize(4));
		Validator.assertThat("steps in scenario 7", scenarios.get(6).getSteps(), Matchers.hasSize(2));

	}

	@Test
	public void parseGherkinWithBackground() {
		GherkinFileParser parser = new GherkinFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkinWithBackground.feature", scenarios);
		Validator.assertThat(scenarios, Matchers.hasSize(4));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getSteps().iterator().next().getDescription(),
					Matchers.equalTo("Given I am on Google Search Page"));
		}
	}

	@Test(dataProvider = "testMathcerDP")
	public void testMathcer(String stepDescription, String stepCall, boolean expectedMathch, Object[] expectedArgs) {
		GherkinStepMatcher matcher = new GherkinStepMatcher();
		boolean result = matcher.matches(stepDescription, stepCall, new HashMap<String, Object>());
		Validator.assertThat(result, Matchers.equalTo(expectedMathch));

		List<String[]> rargs = matcher.getArgsFromCall(stepDescription, stepCall, new HashMap<String, Object>());
		Validator.assertThat(rargs.size(), Matchers.equalTo(expectedArgs.length));
		int i = 0;
		for (String[] arg : rargs) {
			Validator.assertThat(arg[0], Matchers.equalTo(expectedArgs[i]));
			i++;
		}

	}

	@DataProvider(name = "testMathcerDP")
	public Iterator<Object[]> gettestMathcerData() {
		List<Object[]> data = new ArrayList<Object[]>();
		 data.add(new Object[]{"^I am using QMetry Automation Framework$",
		 "I am using QMetry Automation Framework", true, new String[]{}});
		 data.add(new Object[]{"^I search for \"([^\"]*)\"$",
		 "I search for \"git qmetry\"", true, new String[]{"git qmetry"}});
		 data.add(new Object[]{"^I search for \"(.*?)\"$","I search for \"git qmetry\"",true, new String[]{"git qmetry"}});
		 data.add(new Object[]{"^I search for a \"([^\"]*)\"$",
		 "I search for \"git qmetry\"", false, new String[]{}});
		 data.add(new Object[]{"^it should have \"([^\"]*)\" in search results$",
		 "it should have \"QMetry Automation Framework\" in search results",
		 true,
		 new String[]{"QMetry Automation Framework"}});
		 data.add(new Object[]{"^it should have following search results:$",
		 "it should have following search results:[\"a\"]", true,
		 new String[]{"[\"a\"]"}});
		 data.add(new Object[]{"^it should have following search results:$",
		 "it should have following search results:{'a':1,'b':'a'}", true,
		 new String[]{"{'a':1,'b':'a'}"}});
		 data.add(new Object[]{"^I have (\\d+) cukes in my belly$",
		 "I have 5 cukes in my belly", true, new Object[]{"5"}});
		
		 data.add(new Object[]{
		 ".+book with the title '(.+)', written by '(.+)', published in (.+)",
		 "a book with the title 'One good book', written by 'Anonymous', published in 14 March 2013",
		 true, new Object[]{"One good book", "Anonymous", "14 March 2013"}});
		 data.add(new Object[]{
		 ".+book with the title '(.+)', written by '(.+)', published in (.+)",
		 "another book with the title 'Some other book', written by 'Tim Tomson', published in 23 August 2014",
		 true, new Object[]{"Some other book", "Tim Tomson", "23 August 2014"}});
		 data.add(new Object[]{
		 "(.+)book with the title '(.+)', written by '(.+)', published in (.+)",
		 "a book with the title 'One good book', written by 'Anonymous', published in 14 March 2013",
		 true, new Object[]{"a ", "One good book", "Anonymous", "14 March 2013"}});
		 data.add(new Object[]{
		 "^the customer searches for books published between (\\d+) and (\\d+)$",
		 "the customer searches for books published between 2013 and 2014",
		 true,
		 new Object[]{"2013", "2014"}});
		 data.add(new Object[]{"(\\d+) books should have been found$",
		 "2 books should have been found", true, new Object[]{"2"}});
		 data.add(new Object[]{"Book (\\d+) should have the title '(.+)'$",
		 "Book 1 should have the title 'Some other book'", true,
		 new Object[]{"1", "Some other book"}});

		 //optional parameter test
		data.add(new Object[] { "^I have '(.+)'(?: and '(.+)')?$", "I have 'a book' and 'another book'", true,
				new Object[] { "a book", "another book" } });
		

		data.add(new Object[] { "^I have '(.+)'(?: and '(.+)')?$", "I have 'a book'", true,
				new Object[] { "a book", null } });
		
		data.add(new Object[] { "^I have a book(?: written by '(.+)')?$", "I have a book", true,
				new Object[] { null } });
		data.add(new Object[] { "^I have a book(?: written by '(.+)')?$", "I have a book written by 'Chirag'", true,
				new Object[] { "Chirag" } });
		

		data.add(new Object[] { "^I have '(.+)'(?: and '(.+)'(?: written by '(.+)')?)?$", "I have 'a book' and 'another book'", true,
				new Object[] { "a book", "another book",null } });
		data.add(new Object[] { "^I have '(.+)'(?: and '(.+)'(?: written by '(.+)')?)?$", "I have 'a book'", true,
				new Object[] { "a book", null, null } });

		return data.iterator();
	}

}
