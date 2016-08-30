/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.step.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public abstract class AbstractScenarioFileParser implements ScenarioFileParser {

	public static final String STEP_DEF = "STEP-DEF";
	public static final String END = "END";
	public static final String TEST_DATA = "TEST-DATA";
	public static final String SCENARIO = "SCENARIO";
	public static final String DESCRIPTION = "desc";

	protected final Log logger;

	private Gson gson;
	private List<String> includeGroups;
	private List<String> excludeGroups;

	public AbstractScenarioFileParser() {
		logger = LogFactoryImpl.getLog(getClass());
		gson = new Gson();
		this.includeGroups = new ArrayList<String>();
		this.excludeGroups = new ArrayList<String>();
	}

	@Override
	public void parse(String scenarioFile, List<Scenario> scenarios) {
		Collection<Object[]> statements = parseFile(scenarioFile);
		String reference = FileUtil.getRelativePath(scenarioFile, "./");
		processStatements(statements.toArray(new Object[0][0]), reference, scenarios);
	}

	@Override
	public void setExcludeGroups(List<String> excludeGroups) {
		this.excludeGroups = excludeGroups;
	}

	@Override
	public void setIncludeGroups(List<String> includeGroups) {
		this.includeGroups = includeGroups;
	}

	/**
	 * This method expects file parser to parse file and return collection of
	 * Object array of size 4. Elements of array should be as follow:
	 * <DL>
	 * <DT>Step call should be array containing:
	 * <DD>
	 * <code>["step-name-or-description-to-call", "inParams", "outParam", lineNo(optional)]</code>
	 * </DD></DT>
	 * <DT>Scenario Definition should be array containing:
	 * <DD>
	 * <code>["SCENARIO", "SCENARIO-name", "meta-data", lineNo(optional)]</code>
	 * </DD></DT>
	 * </DL>
	 * <DT>Step Definition should be array containing:
	 * <DD>
	 * <code>["STEP-DEF", "step-name-or-description", "meta-data", lineNo(optional)]</code>
	 * </DD></DT>
	 * </DL>
	 * </p>
	 * Step-Def
	 * 
	 * @param scenarioFile
	 * @return
	 */
	protected abstract Collection<Object[]> parseFile(String scenarioFile);

	protected void processStatements(Object[][] statements, String referece, List<Scenario> scenarios) {

		for (int statementIndex = 0; statementIndex < statements.length; statementIndex++) {

			String type = ((String) statements[statementIndex][0]).trim();

			// ignore blanks and statements outside scenario or step-def
			if (StringUtil.isBlank(type) || type.equalsIgnoreCase(TEST_DATA) || !(type.equalsIgnoreCase(STEP_DEF)
					|| type.equalsIgnoreCase(SCENARIO) || type.equalsIgnoreCase(END))) {
				String nextSteptype = "";
				do {
					statementIndex++;
					if (statements.length > (statementIndex + 2)) {
						nextSteptype = ((String) statements[statementIndex + 1][0]).trim();
					} else {
						nextSteptype = END; //
					}
				} while (!(nextSteptype.equalsIgnoreCase(STEP_DEF) || nextSteptype.equalsIgnoreCase(SCENARIO)
						|| nextSteptype.equalsIgnoreCase(END)));
			}

			// Custom step definition
			if (type.equalsIgnoreCase(STEP_DEF)) {
				statementIndex = parseStepDef(statements, statementIndex, referece);
			} else if (type.equalsIgnoreCase(SCENARIO)) {
				statementIndex = parseScenario(statements, statementIndex, referece, scenarios);
			}
		}

	}

	protected int parseStepDef(Object[][] statements, int statementIndex, String referece) {

		Object[] stepDef = statements[statementIndex];

		String stepName = stepDef.length > 1 ? ((String) stepDef[1]).trim() : "";
		String description = stepName;

		int lineNo = getLineNum(statements, statementIndex);
		ArrayList<TestStep> steps = new ArrayList<TestStep>();

		CustomStep customStep = new CustomStep(stepName, StringUtil.isBlank(description) ? stepName : description,
				steps);
		customStep.setFileName(referece);
		customStep.setLineNumber(lineNo);

		String nextSteptype = "";
		do {
			statementIndex++;
			lineNo = getLineNum(statements, statementIndex);

			String currStepName = (String) statements[statementIndex][0];

			if (!currStepName.equalsIgnoreCase(END)) {
				StringTestStep step = new StringTestStep((String) statements[statementIndex][0],
						gson.fromJson((String) statements[statementIndex][1], Object[].class));
				step.setResultParamName((String) statements[statementIndex][2]);
				step.setFileName(referece);
				step.setLineNumber(lineNo);
				steps.add(step);
			}
			if (statements.length > (statementIndex + 2)) {
				nextSteptype = ((String) statements[statementIndex + 1][0]).trim();
			} else {
				nextSteptype = END; //
			}
		} while (!(nextSteptype.equalsIgnoreCase(STEP_DEF) || nextSteptype.equalsIgnoreCase(SCENARIO)
				|| nextSteptype.equalsIgnoreCase(END) || nextSteptype.equalsIgnoreCase(TEST_DATA)));

		if (stepDef.length > 2 && null != stepDef[2]) {
			try {
				String metadatastr = (String) stepDef[2];

				Map<String, Object> metadata = StringUtil.isBlank(metadatastr) ? new HashMap<String, Object>()
						: JSONUtil.toMap(metadatastr);
				customStep.setMetaData(metadata);

				if (metadata.containsKey("name")) {
					stepName = (String) metadata.get("name");
					customStep.setName(stepName);

				}
				if (metadata.containsKey("description")) {
					description = (String) metadata.get("description");
					customStep.setDescription(description);
				}
				if (metadata.containsKey("threshold")) {
					int threshold = ((Number) metadata.get("threshold")).intValue();
					customStep.setThreshold(threshold);
				}

			} catch (JSONException e) {

			}
		}
		StringTestStep.addStep(stepName, customStep);

		return statementIndex;
	}

	@SuppressWarnings("unchecked")
	private int parseScenario(Object[][] statements, int statementIndex, String referece, List<Scenario> scenarios) {

		String description = statements[statementIndex].length > 2 ? (String) statements[statementIndex][2] : "";
		String stepName = statements[statementIndex].length > 1 ? ((String) statements[statementIndex][1]).trim() : "";

		int lineNo = getLineNum(statements, statementIndex);

		// collect all steps of scenario
		Collection<TestStep> steps = new ArrayList<TestStep>();

		Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		if (StringUtil.isNotBlank(description)) {
			metadata.putAll(gson.fromJson(description, Map.class));
		}
		metadata.put("referece", referece);
		metadata.put("lineNo", lineNo);

		/**
		 * check enabled flag in meta-data and apply groups filter if configured
		 * in xml configuration file. the custom meta-data filter will covered
		 * in method filter where it will not include groups from xml
		 * configuration file.
		 */
		if (include(includeGroups, excludeGroups, metadata)) {
			String dataProvider = getDP(metadata);
			Scenario scenario = StringUtil.isBlank(dataProvider) ? new Scenario(stepName, steps, metadata)
					: new DataDrivenScenario(stepName, steps, dataProvider, metadata);

			scenarios.add(scenario);
		} else {
			logger.debug("Excluded SCENARIO - " + stepName + ":" + metadata.get(DESCRIPTION));
		}
		String nextSteptype = "";
		do {
			statementIndex++;
			lineNo = getLineNum(statements, statementIndex);

			String currStepName = (String) statements[statementIndex][0];
			if (!currStepName.equalsIgnoreCase(END)) {
				TestStep step = parseStepCall(statements[statementIndex], referece, lineNo);
				steps.add(step);
			}

			if (statements.length > (statementIndex + 2)) {
				nextSteptype = ((String) statements[statementIndex + 1][0]).trim();
			} else {
				nextSteptype = END; // EOF
			}
		} while (!(nextSteptype.equalsIgnoreCase(STEP_DEF) || nextSteptype.equalsIgnoreCase(SCENARIO)
				|| nextSteptype.equalsIgnoreCase(END) || nextSteptype.equalsIgnoreCase(TEST_DATA)));

		return statementIndex;

	}

	protected TestStep parseStepCall(Object[] statement, String referece, int lineNo) {

		String currStepName = (String) statement[0];
		Object[] currStepArgs = null;
		try {
			currStepArgs = (StringUtil.isNotBlank((String) statement[1]))
					? gson.fromJson((String) statement[1], Object[].class) : null;
		} catch (JsonSyntaxException jse) {
			logger.error(jse.getMessage() + statement[1], jse);
		}
		String currStepRes = statement.length > 2 ? (String) statement[2] : "";

		StringTestStep step = new StringTestStep(currStepName, currStepArgs);
		step.setResultParamName(currStepRes);
		step.setFileName(referece);
		step.setLineNumber(lineNo);

		return step;

	}

	private int getLineNum(Object[][] statements, int statementIndex) {
		try {
			return (statements[statementIndex].length > 3 && null != statements[statementIndex][3]
					? (Integer) statements[statementIndex][3] : statementIndex);
		} catch (Exception e) {// not a number???...
			return statementIndex;
		}
	}

	protected String getDP(Map<String, Object> metadata) {
		String dataProvider = "";
		for (params key : params.values()) {
			if (metadata.containsKey(key.name())) {
				dataProvider = String.format("%s%s%s=%s", dataProvider, StringUtil.isNotBlank(dataProvider) ? ";" : "",
						key, metadata.get(key.name()));
			}
		}
		return dataProvider;
	}

	/**
	 * To apply groups and enabled filter
	 * 
	 * @param includeGroups
	 * @param excludeGroups
	 * @param metadata
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean include(List<String> includeGroups, List<String> excludeGroups, Map<String, Object> metadata) {
		// check for enabled
		if (metadata.containsKey("enabled") && !((Boolean) metadata.get("enabled")))
			return false;

		Set<Object> groups = new HashSet<Object>(metadata.containsKey(ScenarioFactory.GROUPS)
				? (List<String>) metadata.get(ScenarioFactory.GROUPS) : new ArrayList<String>());
		if (!includeGroups.isEmpty()) {
			groups.retainAll(includeGroups);
		}
		groups.removeAll(excludeGroups);
		return (!groups.isEmpty() || (includeGroups.isEmpty() && excludeGroups.isEmpty()));
	}
}
