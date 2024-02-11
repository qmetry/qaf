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

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.client.DataDrivenScenario;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser2;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class RuntimeScenarioGenerator {
	private final static Log logger = LogFactory.getLog(RuntimeScenarioGenerator.class);

	public static void main(String[] args) {
		String source = ConfigurationManager.getBundle().getString("scenario.file.loc", "scenarios");
		RuntimeScenarioGenerator runtimeScenarioGenerator = new RuntimeScenarioGenerator();
		runtimeScenarioGenerator.generateRuntimeScenarios(source);
	}
	
	public void generateRuntimeScenarios(String source) {
		File file = new File(source);
		if (file.isDirectory()) {
			FileUtil.getFiles(file, "feature", true).forEach((f) -> createTestClass(f));
		} else {
			createTestClass(file);
		}
	}

	protected void createTestClass(File bddFile) {

		BDDFileParser2 bddParser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		bddParser.parse(bddFile.getPath(), scenarios);
		String className = StringUtil.toTitleCaseIdentifier(bddFile.getName().replace(".feature", ""));

		Map<String, Object> classMetaData = new HashMap<String, Object>();

		if (scenarios.size() > 1) {
			Iterator<Scenario> iter = scenarios.iterator();
			classMetaData.putAll(iter.next().getMetadata());
			while (iter.hasNext()) {
				classMetaData.entrySet().retainAll(iter.next().getMetadata().entrySet());
			}
		}
		String pkg = generatePkgName(classMetaData, bddFile);
		String dest = "auto_generated/" + pkg.replace('.', '/');

		classMetaData.remove("reference");
		classMetaData.remove("package");

		List<String> statements = new ArrayList<String>();

		statements.add("package " + pkg + ";");
		statements.add("import static com.qmetry.qaf.automation.step.client.RuntimeScenarioFactory.scenario;");
		statements.add("import java.util.Map;");
		statements.add("import org.testng.annotations.Test;");
		statements.add("import com.qmetry.qaf.automation.step.NotYetImplementedException;");
		statements.add("import com.qmetry.qaf.automation.data.MetaData;");
		statements.add("import com.qmetry.qaf.automation.ui.WebDriverTestCase");

		statements.add("");
		statements.add("");

		if (!classMetaData.isEmpty())
			statements.add(String.format("@MetaData(\"%s\")", JSONUtil.toString(classMetaData).replace('"', '\'')));
		// start class
		addClassName(className, statements);
		for (Scenario scenario : scenarios) {
			addScenario(scenario, statements, classMetaData, dest);
		}
		// end class
		statements.add("}");

		try {
			FileUtil.checkCreateDir(dest);
			File outPutFile = new File(dest, className + ".java");
			FileUtil.writeLines(outPutFile, statements);
			logger.info("Generated file: " + outPutFile + " from " + bddFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	protected String generatePkgName(Map<String, Object> classMetaData, File bddFile) {
		return (String) classMetaData.getOrDefault("package", bddFile.getParent().replace('/', '.'));
	}

	protected void addClassName(String className, List<String> statements) {
		statements.add(String.format("public class %s extends WebDriverTestCase {", className));
	}
	protected void addScenario(Scenario scenario, List<String> statements, Map<String, Object> classMetaData, String dest) {
		if (null != scenario.getMetadata() && !scenario.getMetadata().isEmpty()) {

			scenario.getMetadata().entrySet().removeAll(classMetaData.entrySet());
			scenario.getMetadata().remove("lineno");
			scenario.getMetadata().remove("reference");
			scenario.getMetadata().remove("resultFileName");
			Object JSON_DATA_TABLE = scenario.getMetadata().remove("JSON_DATA_TABLE");
			if (null != JSON_DATA_TABLE) {
				scenario.getMetadata().put("dataFile", generateDataFile(dest,
						StringUtil.toTitleCaseIdentifier(scenario.getTestName()), (String) JSON_DATA_TABLE));
			}
			statements.add(String.format("@MetaData(\"%s\")",
					JSONUtil.toString(scenario.getMetadata()).replace('"', '\'')));
		}

		statements.add("@Test");
		// start method
		statements.add(String.format("public void %s(%s){", scenario.getTestName().replace(' ', '_'),
				(scenario instanceof DataDrivenScenario) ? "Map<String, Object> data" : ""));
		statements.add("\tscenario().");

		for (TestStep step : scenario.getSteps()) {
			String desc = step.getDescription().replace('"', '\'');
			String keyword = BDDKeyword.getKeywordFrom(step.getDescription());
			if (isBlank(keyword)) {
				keyword = "step";
			} else {
				desc = desc.substring(keyword.length()).trim();
			}
			statements.add(
					String.format("\t\t%s (\"%s\",()->{\n\t\t\tthrow new NotYetImplementedException();\n\t\t}).",
							keyword.toLowerCase(), desc));
		}
		statements.add("\t\texecute();");

		// end method
		statements.add("\t}");
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<String> jsonArrayToCSV(String json) {
		Collection<String> csvdata = new ArrayList<String>();
		Object[][] dataset = JSONUtil.getJsonArrayOfMaps(json);
		for (Object[] record : dataset) {
			Map<String, Object> datamap = (Map<String, Object>) record[0];
			if (csvdata.isEmpty()) {
				csvdata.add(String.join(",", datamap.keySet()));
			}

			csvdata.add(datamap.values().stream().map(Object::toString).collect(Collectors.joining(",")));
		}
		return csvdata;
	}

	protected String generateDataFile(String dest, String name, String JSON_DATA_TABLE) {
		String type = ConfigurationManager.getBundle().getString("runtimescenario.datafile.type", "json");
		switch (type.toLowerCase()) {
		case "txt":
		case "csv":
			File dataFile = new File(dest, name + "." + type);
			try {
				FileUtil.writeLines(dataFile, jsonArrayToCSV((String) JSON_DATA_TABLE));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return dataFile.getPath();

		default:
			if (!type.equalsIgnoreCase("json")) {
				System.err.println("Only json or csv or txt supported for exmples to data file conversion.");
			}
			File jsonFile = new File(dest, name + ".json");
			try {
				FileUtil.write(jsonFile, (String) JSON_DATA_TABLE, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jsonFile.getPath();
		}
	}
}
