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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class BehaviorTests {

	@Test
	public void parseBDDWithBackground() {
		BDDFileParser parser = new BDDFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/scenarios/scenariosWithBackground.bdd", scenarios);
		Validator.assertThat("Scenarios in bdd", scenarios, Matchers.hasSize(3));
		for (Scenario scenario : scenarios) {
			Validator.assertThat(scenario.getSteps().iterator().next().getDescription(),
					Matchers.equalTo("Given I am on Google Search Page"));
		}
	}

	@Test
	public void parseBDDWithSingleScenario() {
		BDDFileParser parser = new BDDFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		parser.parse("resources/scenarios/singlescenario.bdd", scenarios);
		Validator.assertThat("Scenarios in bdd", scenarios, Matchers.hasSize(1));
		//multi-line comment is considered as one log step
		Validator.assertThat("steps in scenario", scenarios.get(0).getSteps(), Matchers.hasSize(4));
		
	}
	

	@Test
	public void testGetKeyword() {
		String call = "GiVen I am testing.";
		Validator.assertThat(BDDKeyword.getKeywordFrom(call),
				Matchers.equalToIgnoringCase("Given"));
	}

	@Test
	public void testBehaviorCallWithKeyword() {
		String call = "GiVen I am Testing.";
		String def = "I am testing.";
		Validator.assertThat(BDDDefinitionHelper.matches(def, call), Matchers.is(true));
	}

	@Test
	public void BDDParamTypeTest() {
		String call = "Given I have 14 tabs";
		ParamType type = ParamType.LONG;
		String expValue = "14";

		Pattern p = Pattern.compile(type.getRegx());
		Matcher matcher = p.matcher(call);
		String value = "";
		if (matcher.find())
			value = matcher.group();

		Validator.assertThat(value, Matchers.equalToIgnoringCase(expValue));
	}

	@Test(dataProvider = "bddParamDP")
	public void BDDParamTypeTest(String call, ParamType type, String expValue) {

		Pattern p = Pattern.compile(type.getRegx());
		Matcher matcher = p.matcher(call);
		String value = "";
		if (matcher.find())
			value = matcher.group();

		Validator.assertThat(value, Matchers.equalToIgnoringCase(expValue));
	}

	@DataProvider(name = "bddParamDP")
	public Iterator<Object[]> getPramData() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{"Given I have 14 tabs", ParamType.LONG, "14"});
		data.add(new Object[]{"Given I have {'a':'b';'n':1;'f':1.5;'b':true} tabs",
				ParamType.MAP, "{'a':'b';'n':1;'f':1.5;'b':true}"});
		data.add(new Object[]{"Given I have 'user' tabs", ParamType.STRING, "'user'"});
		data.add(new Object[]{"Given I have ['user','admin',true, 1, 1.5] tabs",
				ParamType.LIST, "['user','admin',true, 1, 1.5]"});
		data.add(new Object[]{"Given I have 14.5 tabs", ParamType.DOUBLE, "14.5"});

		data.add(new Object[]{
				"Given I have ['user','admin',true, 1, 1.5, {'a':'b';'n':1;'f':1.5;'b':true},[1,'a',true]] tabs",
				ParamType.LIST,
				"['user','admin',true, 1, 1.5, {'a':'b';'n':1;'f':1.5;'b':true},[1,'a',true]]"});
		data.add(new Object[]{
				"Given I have {'a':'b';'n':1;'f':1.5;'b':true;'map':{'a':'b';'n':1;'f':1.5;'b':true};'list':[1,2,3]} tabs",
				ParamType.MAP,
				"{'a':'b';'n':1;'f':1.5;'b':true;'map':{'a':'b';'n':1;'f':1.5;'b':true};'list':[1,2,3]}"});

		return data.iterator();
	}
}
