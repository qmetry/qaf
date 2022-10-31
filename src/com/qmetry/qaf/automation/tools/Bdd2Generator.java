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
package com.qmetry.qaf.automation.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.report.BDDGenerator;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.client.AbstractScenarioFileParser;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser2;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class Bdd2Generator {

	public static void main(String[] args) {
		File file = new File(ConfigurationManager.getBundle().getString("scenario.file.loc", "scenarios"));
		if (file.isDirectory()) {
			FileUtil.getFiles(file, "feature", true).forEach((f) -> createBDD2Files(f));
			FileUtil.getFiles(file, "bdd", true).forEach((f) -> createBDD2Files(f));
		} else {
			createBDD2Files(file);
		}
	}

	public static void createBDD2Files(File bddFile) {

		String type = FileUtil.getExtention(bddFile.getName());
		AbstractScenarioFileParser bddParser =type.equalsIgnoreCase("feature")? new BDDFileParser2():new BDDFileParser();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		bddParser.parse(bddFile.getPath(), scenarios);

		Map<String, Object> classMetaData = new HashMap<String, Object>();

		if (scenarios.size() > 1) {
			Iterator<Scenario> iter = scenarios.iterator();
			classMetaData.putAll(iter.next().getMetadata());
			while (iter.hasNext()) {
				classMetaData.entrySet().retainAll(iter.next().getMetadata().entrySet());
			}
		}

		Object featureName = classMetaData.remove("Feature");

		List<String> statements = new ArrayList<String>();
		StringBuffer featureMetaData = new StringBuffer();
		BDDGenerator.addMetadata(featureMetaData , classMetaData, Arrays.asList("reference","package"));
		statements.addAll(Arrays.asList(featureMetaData.toString().split("\n")));
		
		// start Feature
		statements.add("Feature: " + featureName);
		if(StringUtil.isBlank(statements.get(0))) {
			statements.remove(0);
		}
		statements.add("");

		for (Scenario scenario : scenarios) {
			String lastStatement = statements.get(statements.size()-1);
			if(StringUtil.isNotBlank(lastStatement)) {
				statements.add("");
			}
			
			// start scenario
			String JSON_DATA_TABLE = null;
			if (null != scenario.getMetadata() && !scenario.getMetadata().isEmpty()) {

				scenario.getMetadata().entrySet().removeAll(classMetaData.entrySet());
				scenario.getMetadata().remove("lineno");
				scenario.getMetadata().remove("reference");
				scenario.getMetadata().remove("resultFileName");
				JSON_DATA_TABLE = (String) scenario.getMetadata().remove("JSON_DATA_TABLE");
				
				StringBuffer scenarioMetaData = new StringBuffer();
				BDDGenerator.addMetadata(scenarioMetaData , scenario.getMetadata(), Arrays.asList("lineno","resultFileName","Feature"));
				//statements.add(scenarioMetaData.toString());
				statements.addAll(Arrays.asList(scenarioMetaData.toString().split("\n")));

			}
			
			statements.add("Scenario: "+ scenario.getTestName());

			for (TestStep step : scenario.getSteps()) {
				statements.add("\t"+step.getDescription());
			}
			
			if (null != JSON_DATA_TABLE) {
				statements.addAll(generateExamples((String) JSON_DATA_TABLE));
			}
			// end scenario
		}
		// end Feature
		

		try {
			FileUtil.writeLines(new File(bddFile.getParentFile(), bddFile.getName().replace(".bdd", ".feature")), statements);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Collection<String> generateExamples(String JSON_DATA_TABLE) {
		Collection<String> examples = new ArrayList<String>();
		Object[][] dataset = JSONUtil.getJsonArrayOfMaps(JSON_DATA_TABLE);
		
		for (Object[] record : dataset) {
			@SuppressWarnings("unchecked")
			Map<String, Object> datamap = (Map<String, Object>) record[0];
			if (examples.isEmpty()) {
				examples.add("Examples:");
				examples.add("\t| "+String.join(" |", datamap.keySet())+" |");
			}

			examples.add("\t| "+datamap.values().stream().map(Object::toString).collect(Collectors.joining(" |"))+" |");
		}
		return examples;
	}
}
