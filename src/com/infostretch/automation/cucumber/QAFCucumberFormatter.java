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

package com.infostretch.automation.cucumber;

import static com.infostretch.automation.core.ConfigurationManager.getBundle;
import static com.infostretch.automation.util.ClassUtil.setField;
import static com.infostretch.automation.util.JSONUtil.getJsonObjectFromFile;
import static com.infostretch.automation.util.JSONUtil.writeJsonObjectToFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationConverter;

import com.infostretch.automation.core.CheckpointResultBean;
import com.infostretch.automation.core.LoggingBean;
import com.infostretch.automation.core.MessageTypes;
import com.infostretch.automation.core.QAFTestBase;
import com.infostretch.automation.core.TestBaseProvider;
import com.infostretch.automation.keys.ApplicationProperties;
import com.infostretch.automation.testng.report.ClassInfo;
import com.infostretch.automation.testng.report.MetaInfo;
import com.infostretch.automation.testng.report.MethodInfo;
import com.infostretch.automation.testng.report.MethodResult;
import com.infostretch.automation.testng.report.Report;
import com.infostretch.automation.testng.report.ReportEntry;
import com.infostretch.automation.testng.report.TestOverview;
import com.infostretch.automation.util.DateUtil;
import com.infostretch.automation.util.FileUtil;
import com.infostretch.automation.util.StringMatcher;
import com.infostretch.automation.util.StringUtil;

import gherkin.formatter.Argument;
import gherkin.formatter.Formatter;
import gherkin.formatter.NiceAppendable;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.ExamplesTableRow;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

public class QAFCucumberFormatter implements Formatter, Reporter {
	public static final String SUITE_NAME = "cucumber.suite.name";
	public static final String TEST_NAME = "cucumber.test.name";
	public static final String DESIREDCAPABILITIES = "driver.desiredCapabilities";
	public static final String ACTUALCAPABILITIES = "driver.actualCapabilities";

	public static final String JSON_REPORT_DIR = ApplicationProperties.JSON_REPORT_DIR
			.getStringVal(ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results") + "/"
					+ DateUtil.getDate(0, "dd-MMM-yy_hh_mm_ssa"));
	private final Long START_TIME = System.currentTimeMillis();
	private final NiceAppendable out;
	private boolean outline;

	private static final String CUCUMBER_REPORT = "cucumber.reporting";

	private static final String URI = "cucumber.current.url";
	private static final String METHOD_INFO = CUCUMBER_REPORT + ".method.info";
	private static final String METHOD_RESULT = CUCUMBER_REPORT + ".method.result";
	private static final String STEPS = CUCUMBER_REPORT + ".method.steps";
	private static final String FEATURE = "cucumber.current.feature";
	private static final String EXAMPLES = "scenario.outline.examples"; // don't
																		// clear
																		// after
																		// scenario....
	private static final String STEP_MATCH = CUCUMBER_REPORT + "step.match";

	public QAFCucumberFormatter(Appendable appendable) {
		out = new NiceAppendable(appendable);
		init();
	}

	@Override
	public void uri(String uri) {
		String url = uri.replace('/', '.');
		getTestBase().getContext().setProperty(URI, url);
		updateOverview();
	}

	@Override
	public void feature(Feature feature) {
		getTestBase().getContext().setProperty(FEATURE, feature);
	}

	@Override
	public void background(Background background) {
		Feature feature = (Feature) getTestBase().getContext().getObject(FEATURE);
		outline = false;

		MethodResult methodResult = getMethodResult();
		methodResult = new MethodResult();
		methodResult.setCheckPoints(new ArrayList<CheckpointResultBean>());
		methodResult.setSeleniumLog(new ArrayList<LoggingBean>());

		MethodInfo methodInfo = getMethodInfo();
		methodInfo.setStartTime(System.currentTimeMillis());

		methodInfo.setType("config");
		Map<String, Object> metaData = new HashMap<String, Object>();

		metaData.put("groups", getGroups(feature, null));
		metaData.put("name", background.getKeyword() + ": " + feature.getName() + "-" + background.getName());
		metaData.put("resultFileName",
				(background.getKeyword() + feature.getName() + background.getName()).replaceAll("[:\\\\/*?|<>]", "_"));

		metaData.put("description", feature.getDescription() + "\n" + background.getDescription());

		methodInfo.setMetaData(metaData);
		update(methodInfo, methodResult);

	}

	@Override
	public void scenario(Scenario scenario) {
		Feature feature = (Feature) getTestBase().getContext().getObject(FEATURE);

		outline = false;
		MethodInfo methodInfo = getMethodInfo();
		MethodResult methodResult = getMethodResult();
		methodResult = new MethodResult();
		methodResult.setCheckPoints(new ArrayList<CheckpointResultBean>());
		methodResult.setSeleniumLog(new ArrayList<LoggingBean>());

		methodInfo = new MethodInfo();
		methodInfo.setType("test");
		int index = getIndex((String) scenario.toMap().get("id"));
		methodInfo.setIndex(index);
		methodInfo.setArgs(getTestData(index));
		methodInfo.setStartTime(System.currentTimeMillis());
		Map<String, Object> metaData = new HashMap<String, Object>();

		metaData.put("groups", getGroups(feature, scenario));
		metaData.put("name", feature.getName() + "-" + scenario.getName());
		metaData.put("resultFileName",
				scenario.toMap().get("id").toString().replace(";", "_").replaceAll("[:\\\\/*?|<>]", "_"));

		metaData.put("description", feature.getDescription() + "\n" + scenario.getDescription());
		methodInfo.setMetaData(metaData);
		update(methodInfo, methodResult);
	}

	private static Set<String> getGroups(Feature feature, Scenario scenario) {
		Set<String> groups = new HashSet<String>();
		if (null != feature && feature.getTags() != null) {
			for (Tag tag : feature.getTags()) {
				groups.add(tag.getName());
			}
		}
		if (null != scenario && scenario.getTags() != null) {
			for (Tag tag : scenario.getTags()) {
				groups.add(tag.getName());
			}
		}
		return groups;
	}

	@Override
	public void scenarioOutline(ScenarioOutline scenarioOutline) {
		outline = true;
	}

	@Override
	public void examples(Examples examples) {
		getTestBase().getContext().setProperty(EXAMPLES, examples);
	}

	@Override
	public void step(Step step) {
		if (!outline) {
			String name = step.getName();
			String newName = getBundle().getSubstitutor().replace(name);
			if (!newName.equalsIgnoreCase(name)) {
				setField("name", step, newName);
			}
			addStep(step);
		}
	}

	@Override
	public void eof() {
	}

	@Override
	public void syntaxError(String s, String s2, List<String> strings, String s3, Integer integer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void done() {
		getSteps().clear();
		updateMetaInfo(); // re-stamp
	}

	@Override
	public void close() {
		out.close();
	}

	@Override
	public void before(Match match, Result result) {
	}

	@Override
	public void result(Result result) {
		QAFTestBase testBase = TestBaseProvider.instance().get();
		MethodResult methodResult = getMethodResult();
		if (result.getStatus() == Result.FAILED) {
			String e = methodResult.getErrorTrace();
			methodResult.setErrorTrace((StringUtil.isBlank(e) ? "" : e + "\n") + result.getErrorMessage());
		} else if (testBase.getVerificationErrors() > 0) {
			modifyResult(result, testBase.getVerificationErrors());
		}

		logStepResult(result, methodResult);

	}

	private static void log(String message, MessageTypes type) {
		com.infostretch.automation.util.Reporter.log(message, type);
	}

	private void logStepResult(Result result, MethodResult methodResult) {
		Match match = (Match) getTestBase().getContext().getObject(STEP_MATCH);
		QAFTestBase testBase = getTestBase();
		MethodInfo methodInfo = getMethodInfo();
		Step step;
		if (getSteps().isEmpty()) {
			step = new Step(null, "MISMATCH BETWEEN STEPS AND RESULTS", "", 0, null, null);
		} else {
			step = getStep();
		}

		Status mstatus = Status.combine(result.getStatus(), methodInfo.getResult());
		methodInfo.setResult(mstatus.value);

		Status status = Status.from(result.getStatus());
		String format = "%s %s";
		String message = String.format(format, step.getKeyword(), step.getName());
		int duration = computeTiming(result);
		CheckpointResultBean stepCheckPoint = new CheckpointResultBean();
		stepCheckPoint.setMessage(message);
		if (null != result.getDuration())
			stepCheckPoint.setDuration(duration);

		stepCheckPoint.setSubCheckPoints(new ArrayList<CheckpointResultBean>(testBase.getCheckPointResults()));
		stepCheckPoint.setType(status == Status.passed ? MessageTypes.TestStepPass
				: status == Status.failed ? MessageTypes.TestStepFail : MessageTypes.TestStep);
		methodResult.getCheckPoints().add(stepCheckPoint);

		LoggingBean stepCommandLog = new LoggingBean();
		String commandName = match.getLocation();
		if (StringUtil.isBlank(commandName)) {
			commandName = "Missing step";
			methodResult.setErrorTrace(
					"Missing Step: " + step.getStackTraceElement(getTestBase().getContext().getString(URI)));
		}
		stepCommandLog.setCommandName(commandName);
		if ((match.getArguments() != null) && (match.getArguments().size() > 0)) {
			String[] stepArgs = new String[match.getArguments().size()];
			int i = 0;
			for (Argument argument : match.getArguments()) {
				stepArgs[i++] = argument.getVal();
			}

			stepCommandLog.setArgs(stepArgs);
		}
		stepCommandLog.setSubLogs(new ArrayList<LoggingBean>(testBase.getLog()));
		stepCommandLog.setResult(result.getStatus());
		stepCommandLog.setDuration(duration);

		methodResult.getSeleniumLog().add(stepCommandLog);

		if (getSteps().isEmpty()) {
			methodInfo.setDuration(System.currentTimeMillis() - methodInfo.getStartTime());
			if (!methodInfo.getType().equalsIgnoreCase("test")) {
				// background .....
				writeMethodResult(methodInfo, methodResult);
				updateOverview();
				updateMetaInfo();// re-stamp suite status
				// clean up
				getTestBase().getContext().configurationAt(CUCUMBER_REPORT).clear();
				testBase.claerAssertionsLog();
			}

		} else {
			update(methodInfo, methodResult);
		}
	}

	private int computeTiming(Result result) {
		return result.getDuration() == null ? 0
				: ((Long) TimeUnit.NANOSECONDS.toMillis(result.getDuration())).intValue();
	}

	@Override
	public void after(Match match, Result result) {

	}

	@Override
	public void write(String msg) {
		log(msg, MessageTypes.Info);
	}

	@Override
	public void match(Match match) {
		getTestBase().getContext().setProperty(STEP_MATCH, match);
	}

	@Override
	public void embedding(String mimeType, byte[] bytes) {
		if (bytes != null && bytes.length > 0)
			write("<img src=\"data:image/png;base64," + new String(Base64.encodeBase64(bytes))
					+ "\" alt=\"Screenshot\" width=\"300px;\"/>");
	}

	private class ExamplesTableRowItem {
		String id;

		ExamplesTableRowItem(String id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ExamplesTableRow) {
				return id.equalsIgnoreCase(((ExamplesTableRow) o).getId());
			}
			return super.equals(o);
		}
	}

	private String getTestResultDir() {
		return JSON_REPORT_DIR + "/" + getTestName();
	}

	private String getSuiteName() {
		return getBundle().getString(SUITE_NAME, "Default Suite");
	}

	private String getTestName() {
		return getBundle().getString(TEST_NAME, "Cucumber");
	}

	private void init() {
		getTestBase().getContext().setProperty(ApplicationProperties.JSON_REPORT_DIR.key, JSON_REPORT_DIR);
		FileUtil.checkCreateDir(ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results"));
		FileUtil.checkCreateDir(JSON_REPORT_DIR);

		ReportEntry reportEntry = new ReportEntry();
		reportEntry.setName(getSuiteName());
		reportEntry.setStartTime(System.currentTimeMillis());
		reportEntry.setDir(JSON_REPORT_DIR);

		String file = ApplicationProperties.JSON_REPORT_ROOT_DIR.getStringVal("test-results") + "/meta-info.json";
		MetaInfo metaInfo = getJsonObjectFromFile(file, MetaInfo.class);
		metaInfo.getReports().remove(reportEntry);
		metaInfo.getReports().add(reportEntry);
		writeJsonObjectToFile(file, metaInfo); // 1. main meta-info

	}

	private void updateMetaInfo() {
		String file = JSON_REPORT_DIR + "/meta-info.json";

		Report report = new Report();

		report.setStartTime(START_TIME);
		report.setEndTime(System.currentTimeMillis());

		report.setName(getSuiteName());

		report.setTests(getTestNames());
		report.setDir(JSON_REPORT_DIR);

		report.setPass(Status.passed.getTotalCount());
		report.setFail(Status.failed.getTotalCount());
		report.setSkip(Status.skipped.getTotalCount());
		report.setTotal(Status.passed.getTotalCount() + Status.failed.getTotalCount() + Status.skipped.getTotalCount());
		report.setStatus(
				Status.failed.getTotalCount() > 0 ? "fail" : Status.passed.getTotalCount() > 0 ? "pass" : "unstable");

		try {
			writeJsonObjectToFile(file, report); // 2. report meta-info
		} catch (Exception e) {
			System.err.println("createMetaInfo: " + e.getMessage());
		}
	}

	private List<String> getTestNames() {
		ArrayList<String> testNames = new ArrayList<String>();
		File[] files = new File(JSON_REPORT_DIR).listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				testNames.add(file.getName());
			}
		}
		return testNames;
	}

	private void updateOverview() {
		try {
			String file = getTestResultDir() + "/overview.json";
			TestOverview overview = getJsonObjectFromFile(file, TestOverview.class);

			try {

				Map<String, Object> envInfo = new HashMap<String, Object>();
				envInfo.put("isfw-build-info", getBundle().getObject("isfw.build.info"));
				Map<?, ?> env = ConfigurationConverter.getMap(getBundle().subset("env"));
				envInfo.put("run-parameters", env);

				Object desiredCap = getBundle().getObject(DESIREDCAPABILITIES);
				Object actualCapabilities = getBundle().getObject(ACTUALCAPABILITIES);
				if (actualCapabilities != null) {
					envInfo.put("browser-desired-capabilities", desiredCap);
					envInfo.put("browser-actual-capabilities", actualCapabilities);
				}

				overview.setEnvInfo(envInfo);
				Map<String, Object> executionEnvInfo = new HashMap<String, Object>();
				executionEnvInfo.put("os.name", System.getProperty("os.name"));
				executionEnvInfo.put("os.version", System.getProperty("os.version"));

				executionEnvInfo.put("os.arch", System.getProperty("os.arch"));
				executionEnvInfo.put("java.version", System.getProperty("java.version"));
				executionEnvInfo.put("java.vendor", System.getProperty("java.vendor"));

				executionEnvInfo.put("user.name", System.getProperty("user.name"));

				envInfo.put("execution-env-info", executionEnvInfo);
			} catch (Exception e) {
				System.err.println("Setting env info: " + e.getMessage());
			}

			overview.setTotal(Status.passed.getCount() + Status.skipped.getCount() + Status.failed.getCount());
			overview.setPass(Status.passed.getCount());
			overview.setSkip(Status.skipped.getCount());
			overview.setFail(Status.failed.getCount());
			overview.getClasses().add(getTestBase().getContext().getString(URI));

			overview.setEndTime(System.currentTimeMillis());

			overview.setStartTime(START_TIME);
			writeJsonObjectToFile(file, overview); // 3. overview test-Dir
		} catch (Exception e) {
			System.err.println("updateOverview: " + e.getMessage());
		}
	}

	private void writeMethodResult(MethodInfo methodInfo, MethodResult methodResult) {

		String dir = getTestResultDir() + "/" + getTestBase().getContext().getString(URI) + "/";

		String file = dir + methodInfo.getMetaData().get("resultFileName") + ".json";
		String metafile = dir + "meta-info.json";
		FileUtil.checkCreateDir(dir);

		ClassInfo classInfo = getJsonObjectFromFile(metafile, ClassInfo.class);
		classInfo.getMethods().add(methodInfo);
		writeJsonObjectToFile(metafile, classInfo); // 4. class meta-info
		writeJsonObjectToFile(file, methodResult);// 5. method result

	}

	private enum Status {
		undefined("skip"), passed("pass"), skipped("skip"), failed("fail");
		private AtomicInteger count;
		private String value;

		private Status(String value) {
			this.value = value;
			count = new AtomicInteger();
		}

		void add() {
			count.incrementAndGet();
			getTestBase().getContext().setProperty(CUCUMBER_REPORT + name(), getCount() + 1);
		}

		public int getTotalCount() {
			return count.get();
		}

		public int getCount() {
			return getTestBase().getContext().getInt(CUCUMBER_REPORT + name(), 0);
		}

		static Status from(String value) {
			if (StringUtil.isBlank(value)) {
				return undefined;
			}
			for (Status status : Status.values()) {
				if (StringMatcher.containsIgnoringCase(value).match(status.name())) {
					return status;
				}
			}
			return undefined;
		}

		static Status combine(String s1, String s2) {
			Status status1 = from(s1);
			Status status2 = from(s2);
			return status1.ordinal() > status2.ordinal() ? status1 : status2;
		}
	}

	private void modifyResult(Result result, int verificationErrors) {
		Match match = (Match) getTestBase().getContext().getObject(STEP_MATCH);

		setField("status", result, Result.FAILED);
		String message = String.format("%d Verification Error%s @%s", verificationErrors,
				(verificationErrors > 1 ? "s" : ""), match.getLocation());
		setField("error", result, new Error(message));
		setField("error_message", result, message);
	}

	private int getIndex(String s) {
		Examples examples = (Examples) getTestBase().getContext().getObject(EXAMPLES);
		if (examples == null) {
			return 0;
		}
		ExamplesTableRowItem item = new ExamplesTableRowItem(s);
		int index = 0;
		index = examples.getRows().indexOf(item);
		return Math.max(0, index);
	}

	private Object[] getTestData(int index) {
		Examples examples = (Examples) getTestBase().getContext().getObject(EXAMPLES);

		if ((index > 0) && (examples != null)) {
			List<String> keys = examples.getRows().get(0).getCells();
			List<String> values = examples.getRows().get(index).getCells();
			Map<String, String> data = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.size(); i++) {
				data.put(keys.get(i), values.get(i));
			}
			if (index == examples.getRows().size()) {
				examples = null;
			}
			return new Object[] { data };
		}
		return new Object[] {};
	}

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {

		Feature feature = (Feature) getTestBase().getContext().getObject(FEATURE);
		outline = false;
		MethodInfo methodInfo = getMethodInfo();
		MethodResult methodResult = getMethodResult();
		methodResult = new MethodResult();
		methodResult.setCheckPoints(new ArrayList<CheckpointResultBean>());
		methodResult.setSeleniumLog(new ArrayList<LoggingBean>());

		methodInfo = new MethodInfo();
		methodInfo.setType("test");
		int index = getIndex((String) scenario.toMap().get("id"));
		methodInfo.setIndex(index);
		methodInfo.setArgs(getTestData(index));
		methodInfo.setStartTime(System.currentTimeMillis());
		Map<String, Object> metaData = new HashMap<String, Object>();

		metaData.put("groups", getGroups(feature, scenario));
		metaData.put("name", scenario.toMap().get("id").toString().replace(";", "_"));
		metaData.put("description", feature.getDescription() + "\n" + scenario.getDescription());
		methodInfo.setMetaData(metaData);
		update(methodInfo, methodResult);
	}

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		QAFTestBase testBase = TestBaseProvider.instance().get();
		MethodResult methodResult = getMethodResult();
		methodResult.getCheckPoints().addAll(getTestBase().getCheckPointResults());
		MethodInfo methodInfo = getMethodInfo();
		if (methodInfo.getType().equalsIgnoreCase("test")) {
			Status.from(methodInfo.getResult()).add();
		}

		writeMethodResult(methodInfo, methodResult);
		updateOverview();
		updateMetaInfo();// re-stamp suite status
		// clean up

		getTestBase().getContext().configurationAt(CUCUMBER_REPORT).clear();
		testBase.claerAssertionsLog();
	}

	private static MethodInfo getMethodInfo() {
		MethodInfo methodInfo = (MethodInfo) getTestBase().getContext().getObject(METHOD_INFO);
		if (null == methodInfo) {
			methodInfo = new MethodInfo();
			getTestBase().getContext().setProperty(METHOD_INFO, methodInfo);
		}
		return methodInfo;
	}

	private static void update(MethodInfo methodInfo, MethodResult methodResult) {
		if (null != methodInfo) {
			getTestBase().getContext().setProperty(METHOD_INFO, methodInfo);
		}
		if (null != methodResult) {
			getTestBase().getContext().setProperty(METHOD_RESULT, methodResult);
		}
	}

	private static MethodResult getMethodResult() {
		MethodResult methodResult = (MethodResult) getTestBase().getContext().getObject(METHOD_RESULT);
		if (null == methodResult) {
			methodResult = new MethodResult();
			methodResult.setCheckPoints(new ArrayList<CheckpointResultBean>());
			methodResult.setSeleniumLog(new ArrayList<LoggingBean>());

			getTestBase().getContext().setProperty(METHOD_RESULT, methodResult);
		}
		return methodResult;
	}

	@SuppressWarnings("unchecked")
	private static LinkedList<Step> getSteps() {
		LinkedList<Step> steps = new LinkedList<Step>();
		try {
			steps.addAll(getTestBase().getContext().getList(STEPS));
		} catch (Exception e) {
			Step step = (Step) getTestBase().getContext().getObject(STEPS);
			steps.add(step);
		}

		return steps;
	}

	private static void addStep(Step step) {
		List<Step> steps = getSteps();
		steps.add(step);
		getTestBase().getContext().setProperty(STEPS, steps);
	}

	private static Step getStep() {
		LinkedList<Step> steps = getSteps();
		Step step = steps.pop();
		getTestBase().getContext().setProperty(STEPS, steps);
		return step;
	}

	private static QAFTestBase getTestBase() {
		return TestBaseProvider.instance().get();
	}

}
