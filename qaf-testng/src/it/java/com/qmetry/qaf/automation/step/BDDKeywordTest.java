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
		ConfigurationManager.getBundle().setProperty("Given",
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
