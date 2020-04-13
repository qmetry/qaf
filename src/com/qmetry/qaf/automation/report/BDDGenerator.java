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
package com.qmetry.qaf.automation.report;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.integration.TestCaseResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseRunResult;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * This class generates feature files from test implementation even if
 * implementation is in Java. Generated feature file will have examples for
 * data-driven test cases, regardless of test data provider. Generated feature
 * files can be used as proof or to share with other team(s) within project and
 * will enable development team to follow behavior driven development with test
 * automation implementation in best efficient way.
 * 
 * @author chirag.jayswal
 *
 */
public class BDDGenerator implements TestCaseResultUpdator {
	public static final String REPORT_DIR = getBundle().getString("bddgenerator.dest", "auto_generated/features");
	private static String LAST_TEST_NAME;
	private static int testCnt = 0, featureCnt = 0, scenarioCnt = 0;

	@Override
	public String getToolName() {
		return "QAF Behavior Generator";
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean updateResult(TestCaseRunResult tr) {
		if (testCnt == 0) {
			FileUtil.deleteQuietly(new File(REPORT_DIR));
		}
		String storyfileName = tr.getClassName();
		testCnt++;
		try {
			File feature = new File(REPORT_DIR + "/" + storyfileName + ".feature");
			String featureName = tr.getMetaData()
					.getOrDefault("story", tr.getMetaData().getOrDefault("feature", storyfileName)).toString();

			if (!feature.exists()) {
				String header = "Feature: " + featureName;
				FileUtil.write(feature, header, StandardCharsets.UTF_8, true);
				featureCnt++;
			}

			StringBuffer scenario = new StringBuffer();
			Map<String, Object> testData = (null == tr.getTestData() || tr.getTestData().isEmpty()) ? null
					: (Map<String, Object>) tr.getTestData().iterator().next();
			if (null != testData) {
				testData.remove("__index");
				String testName = StringUtil.toCamelCaseIdentifier(tr.getName());
				if (!testName.equalsIgnoreCase(LAST_TEST_NAME)) {
					LAST_TEST_NAME = testName;
					addSecanrio(scenario, tr);
					addExampleHeader(scenario, testData);
				}
				// add example to outline
				scenario.append("\n  | ");
				for (Object value : testData.values()) {
					scenario.append(" " + value + " |");
				}

			} else {
				addSecanrio(scenario, tr);
			}
			// System.out.println(data);
			FileUtil.write(feature, scenario, StandardCharsets.UTF_8, true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean allowParallel() {
		return false;
	}

	@Override
	public void beforeShutDown() {
		System.out.printf("\nGenerated %d scenarios and %d feature files from %d TestCases under %s.\n", scenarioCnt, featureCnt,
				testCnt, REPORT_DIR);
	}

	private void addExampleHeader(StringBuffer data, Map<String, Object> testData) {
		data.append("\nExamples:\n  |");

		for (Object value : testData.keySet()) {
			data.append(" " + value + " |");
		}
	}

	private void addMetadata(StringBuffer data, TestCaseRunResult tr) {
		data.append("\n\n");
		List<String> exclude = new ArrayList<String>(Arrays.asList("sign", "Story", "name"));
		for (params param : QAFDataProvider.params.values()) {
			exclude.add(param.name());
		}
		for (Entry<String, Object> kv : tr.getMetaData().entrySet()) {
			if (!exclude.stream().anyMatch(kv.getKey()::equalsIgnoreCase) && kv.getValue() != null
					&& StringUtil.isNotBlank(kv.getValue().toString()))
				data.append("\n@" + kv.getKey() + ": " + JSONUtil.toString(kv.getValue()));
		}
	}

	private void addSecanrio(StringBuffer data, TestCaseRunResult tr) {
		scenarioCnt++;
		addMetadata(data, tr);
		data.append("\nScenario: ").append(tr.getName());
		if (null == tr.getSteps() || tr.getSteps().isEmpty()) {
			for (CheckpointResultBean chkpoint : tr.getCheckPoints()) {
				data.append("\n    ").append(chkpoint.getMessage());
			}
		} else {
			for (String step : tr.getSteps()) {
				data.append("\n    ").append(step);
			}
		}
	}
}
