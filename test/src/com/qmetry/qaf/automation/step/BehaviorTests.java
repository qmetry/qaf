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
