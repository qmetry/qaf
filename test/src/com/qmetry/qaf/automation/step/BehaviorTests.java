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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class BehaviorTests {

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
	
	@Test(dataProvider="bddParamDP")
	public void BDDParamTypeTest(String call, ParamType type,String expValue) {
		

		Pattern p = Pattern.compile(type.getRegx());
		Matcher matcher = p.matcher(call);
		String value = "";
		if (matcher.find())
			value = matcher.group();

		Validator.assertThat(value, Matchers.equalToIgnoringCase(expValue));
	}
	
	@DataProvider(name="bddParamDP")
	public Iterator<Object[]> getPramData(){
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[]{"Given I have 14 tabs",ParamType.LONG,"14"});
		data.add(new Object[]{"Given I have {'a':'b';'n':1;'f':1.5;'b':true} tabs",ParamType.MAP,"{'a':'b';'n':1;'f':1.5;'b':true}"});
		data.add(new Object[]{"Given I have 'user' tabs",ParamType.STRING,"'user'"});
		data.add(new Object[]{"Given I have ['user','admin',true, 1, 1.5] tabs",ParamType.LIST,"['user','admin',true, 1, 1.5]"});
		data.add(new Object[]{"Given I have 14.5 tabs",ParamType.DOUBLE,"14.5"});

		data.add(new Object[]{"Given I have ['user','admin',true, 1, 1.5, {'a':'b';'n':1;'f':1.5;'b':true},[1,'a',true]] tabs",ParamType.LIST,"['user','admin',true, 1, 1.5, {'a':'b';'n':1;'f':1.5;'b':true},[1,'a',true]]"});
		data.add(new Object[]{"Given I have {'a':'b';'n':1;'f':1.5;'b':true;'map':{'a':'b';'n':1;'f':1.5;'b':true};'list':[1,2,3]} tabs",ParamType.MAP,"{'a':'b';'n':1;'f':1.5;'b':true;'map':{'a':'b';'n':1;'f':1.5;'b':true};'list':[1,2,3]}"});

		return data.iterator();
	}

}
