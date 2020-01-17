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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.BDDStepMatcherFactory.GherkinStepMatcher;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser2;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class BDD2ParserTest {

	@Test
	public void testGherkinParser() {
		BDDFileParser2 parser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkin.feature", scenarios);
		Validator.assertThat(scenarios, Matchers.hasSize(7));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("Web"));
		}
	}
	
	@Test
	public void testFeatureWithDetails() {
		BDDFileParser2 parser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/featureWithDetails.feature", scenarios);
		Validator.assertThat(scenarios, Matchers.hasSize(1));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("Web"));
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("Smoke"));
			Validator.assertThat("Steps in scenario", scenario.getSteps(), Matchers.hasSize(4));
		}
	}
	
	@Test
	public void testFeatureWithDetails2() {
		BDDFileParser2 parser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/scenarios2/featureWithDetails2.feature", scenarios);
		Validator.assertThat(scenarios, Matchers.hasSize(2));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("Web"));
			Validator.assertThat(scenario.getM_groups(), Matchers.hasItemInArray("Smoke"));
		}
		Validator.assertThat("Steps in scenario", scenarios.get(0).getSteps(), Matchers.hasSize(4));
		//doc string should be added as comment
		Validator.assertThat("Steps in scenario", scenarios.get(1).getSteps(), Matchers.hasSize(5));
		
		Object modules = scenarios.get(0).getMetadata().get("Module");
		System.out.println("Modules :: " +modules);
		
		Validator.assertThat("Modules in scenario metadata", modules, Matchers.instanceOf(Collection.class));
		Validator.assertThat("total Modules in scenario metadata " + modules, (Collection<?>)modules, Matchers.hasSize(3));
		Validator.assertThat("total Type in scenario metadata " + scenarios.get(0).getMetadata().get("type"), scenarios.get(0).getMetadata().get("type"), Matchers.is("b"));


	}

	@Test(description = "make sure it parse scenario with all steps")
	public void testGherkinParserForScenarioSteps() {
		BDDFileParser2 parser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/features/gherkinWithSingleScenario.feature", scenarios);
		Validator.assertThat("Scenarios in feature", scenarios, Matchers.hasSize(1));
		Validator.assertThat("Steps in scenario", scenarios.get(0).getSteps(), Matchers.hasSize(4));
		
	}

	@Test
	public void parseBDDWithMultipleScenario() {
		BDDFileParser2 parser = new BDDFileParser2();
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
		
		scenarios.get(4).scenario();


	}

	@Test
	public void parseGherkinWithBackground() {
		BDDFileParser2 parser = new BDDFileParser2();
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
		 data.add(new Object[]{"^it should have \"(.*)\" in search results$",
				 "it should have \"QMetry \\\"Automation\\\" Framework\" in search results",
				 true,
				 new String[]{"QMetry \\\"Automation\\\" Framework"}});
		
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
