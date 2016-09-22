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

import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class BDDKeywordTest {

	@Test
	public void keywordTest() {
		for (BDDKeyword keyword : BDDKeyword.values()) {
			String bdd = keyword.name().toUpperCase() + " some step";
			String keywordFromBdd = BDDKeyword.getKeywordFrom(bdd);
			Validator.assertThat(keywordFromBdd,
					Matchers.equalToIgnoringCase(keyword.name()));
		}

	}

	@Test
	public void keywordSynonymTest() {
		ConfigurationManager.getBundle().addProperty("Given",
				"Assume;Provided;धारणा;यदि");

		List<String> sysnonyms = BDDKeyword.Given.getSynonyms();
		Validator.assertThat(sysnonyms.size(), Matchers.is(4));
		Validator.assertThat(BDDKeyword.getAllKeyWords().size(),
				Matchers.is(BDDKeyword.values().length + 4));

		for (String keyword : sysnonyms) {
			String bdd = keyword.toUpperCase() + " some step";
			String keywordFromBdd = BDDKeyword.getKeywordFrom(bdd);
			Validator.assertThat(keywordFromBdd, Matchers.equalToIgnoringCase(keyword));
		}
	}
}
