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

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.client.DataDrivenScenario;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.step.client.text.BDDFileParser2;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

public class RuntimeScenarioGenerator {

	public static void main(String[] args) {
		File file = new File( ConfigurationManager.getBundle().getString("scenario.file.loc","scenarios"));
		if(file.isDirectory()) {
			FileUtil.getFiles(file, "feature", true).forEach((f)->createTestClass(f));
		}else {
			createTestClass(file);
		}
	}

	public static void createTestClass(File bddFile) {

		BDDFileParser2 bddParser = new BDDFileParser2();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		bddParser.parse(bddFile.getPath(), scenarios);
		String className= StringUtil.toTitleCaseIdentifier(bddFile.getName().replace(".feature", ""));
		String dest = "auto_generated/"+bddFile.getParent();

		Map<String, Object> classMetaData = new HashMap<String, Object>();

		if (scenarios.size() > 1) {
			Iterator<Scenario> iter = scenarios.iterator();
			classMetaData.putAll(iter.next().getMetadata());
			while (iter.hasNext()) {
				classMetaData.entrySet().retainAll(iter.next().getMetadata().entrySet());
			}
		}
		classMetaData.remove("reference");

		List<String> statements = new ArrayList<String>();

		statements.add("package "+ bddFile.getParent().replace('/', '.') + ";");
		statements.add("import static com.qmetry.qaf.automation.step.client.RuntimeScenarioFactory.scenario;");
		statements.add("import java.util.Map;");
		statements.add("import org.testng.annotations.Test;");
		statements.add("import com.qmetry.qaf.automation.step.NotYetImplementedException;");
		statements.add("import com.qmetry.qaf.automation.data.MetaData;");
		statements.add("import com.qmetry.qaf.automation.ui.WebDriverTestCase");

		statements.add("");
		statements.add("");

		if(!classMetaData.isEmpty())
		statements.add(String.format("@MetaData(\"%s\")", JSONUtil.toString(classMetaData).replace('"', '\'')));
		// start class
		statements.add(String.format("public class %s extends WebDriverTestCase {", className));

		for(Scenario scenario : scenarios) {
			if(null!=scenario.getMetadata() && !scenario.getMetadata().isEmpty()) {
				
				scenario.getMetadata().entrySet().removeAll(classMetaData.entrySet());
				scenario.getMetadata().remove("lineno");
				scenario.getMetadata().remove("reference");
				Object JSON_DATA_TABLE = scenario.getMetadata().remove("JSON_DATA_TABLE");
				if(null!=JSON_DATA_TABLE) {
					scenario.getMetadata().put("dataFile", generateDataFile(dest, StringUtil.toTitleCaseIdentifier(scenario.getTestName()), (String)JSON_DATA_TABLE));
				}
				statements.add(String.format("@MetaData(\"%s\")", JSONUtil.toString(scenario.getMetadata()).replace('"', '\'')));
			}

			statements.add("@Test");
			//start method
			statements.add(String.format("public void %s(%s){", scenario.getTestName().replace(' ','_'),(scenario instanceof DataDrivenScenario)?"Map<String, Object> data":""));
			statements.add(String.format("\tscenario().", JSONUtil.toString(classMetaData)));

			for(TestStep step: scenario.getSteps()) {
				String desc = step.getDescription().replace('"', '\'');
				String keyword = BDDKeyword.getKeywordFrom(step.getDescription());
				if (isBlank(keyword)) {
					keyword="step";
				}else {
					desc = desc.substring(keyword.length()).trim();
				}
				statements.add(String.format("\t\t%s (\"%s\",()->{\n\t\t\tthrow new NotYetImplementedException();\n\t\t}).",keyword.toLowerCase(), desc));
			}
			statements.add("\t\texecute();");

			// end method
			statements.add("\t}");
		}
		// end class
		statements.add("}");
		
		try {
			FileUtil.checkCreateDir(dest );
			FileUtil.writeLines(new File(dest,className+".java"), statements);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	private static Collection<String> jsonArrayToCSV(String json){
		Collection<String> csvdata = new ArrayList<String>();
		Object[][] dataset = JSONUtil.getJsonArrayOfMaps(json);
		for(Object[] record: dataset) {
			Map<String, Object> datamap = (Map<String, Object>)record[0];
			if(csvdata.isEmpty()) {
				csvdata.add(String.join(",", datamap.keySet()));
			}
			
			csvdata.add(datamap.values().stream().map(Object::toString).collect(Collectors.joining(",")));
		}
		return csvdata;
	}
	
	private static String generateDataFile(String dest, String name, String JSON_DATA_TABLE) {
			String type = ConfigurationManager.getBundle().getString("runtimescenario.datafile.type","json");
			switch (type.toLowerCase()) {
			case "txt":
			case "csv":
				File dataFile = new File(dest,name+"."+type);
				try {
					FileUtil.writeLines(dataFile, jsonArrayToCSV((String)JSON_DATA_TABLE));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return dataFile.getPath();
			
			default:
				if(!type.equalsIgnoreCase("json")) {
					System.err.println("Only json or csv or txt supported for exmples to data file conversion.");
				}
				File jsonFile = new File(dest,name+".json");
				try {
					FileUtil.write(jsonFile, (String)JSON_DATA_TABLE, StandardCharsets.UTF_8 );
				} catch (IOException e) {
					e.printStackTrace();
				}
				return jsonFile.getPath();
			}
	}
}
