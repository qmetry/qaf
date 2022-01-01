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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getStepMapping;
import static com.qmetry.qaf.automation.util.StringUtil.toCamelCaseIdentifier;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.BDDKeyword;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;

/**
 * A proxy step class facilitate to create non java custom steps. It is also a
 * step executor for any java or custom step.
 * com.qmetry.qaf.automation.step.StringTestStep.java
 * 
 * @author chirag.jayswal
 */
public class StringTestStep extends BaseTestStep {

	private String resultParameterName;
	private Map<String, Object> context;
	private String codeSnippet;
	private TestStep step;

	public StringTestStep(String name, Object... actualArgs) {
		this.name = name;
		description = name;
		setActualArgs(actualArgs);
		context = new HashMap<String, Object>();
	}

	public StringTestStep(String name, Map<String, Object> context,
			Object... actualArgs) {
		this(name, actualArgs);
		this.context = context;
	}

	public void initStep() {
		if (step == null) {
			step = getTestStep();
			if (null != step) {
				step.setActualArgs(actualArgs);
				step.setDescription(description);
				step.getStepExecutionTracker()
						.setContext(getStepExecutionTracker().getContext());
				setStepMatcher(step.getStepMatcher());
			}
		}
	}

	public TestStep deepClone() {
		initStep();
		TestStep s = step.clone();
		s.setActualArgs(actualArgs);
		s.getStepExecutionTracker().setContext(getStepExecutionTracker().getContext());
		return s.clone();
	}

	@Override
	public Object[] getActualArgs() {
		return step == null ? super.getActualArgs() : step.getActualArgs();
	}

	@Override
	public void setActualArgs(Object... args) {
		super.setActualArgs(args);
		if (step != null) {
			step.setActualArgs(args);
		}
	}

	public StringTestStep() {
	}

	public static void addSteps(Map<String, TestStep> steps) {
		getStepMapping().putAll(steps);
	}

	public static void addStep(String name, TestStep step) {
		getStepMapping().put(name.toUpperCase(), step);
	}

	public void setResultParamName(String resultParamName) {
		resultParameterName = resultParamName;
	}

	@Override
	public Object execute() {
		if (DryRunAnalyzer.isDryRun(this)) {
			return null;
		}
		initStep();

		if (null != step) {
			Object retVal = null;
			try {
				retVal = step.execute();
				if (isNotBlank(resultParameterName)) {
					if (resultParameterName.indexOf("${") == 0) {
						getBundle().setProperty(resultParameterName, retVal);

					} else {
						getBundle().setProperty("${" + resultParameterName + "}", retVal);

					}

				}
			} catch (Error ae) {
				StepInvocationException se = new StepInvocationException(this, ae);
				ae.setStackTrace(se.getStackTrace());
				throw ae;
			} catch (Throwable e) {
				StepInvocationException se = new StepInvocationException(this, e);
				RuntimeException re =
						(RuntimeException.class.isAssignableFrom(e.getClass())
								? (RuntimeException) e : new RuntimeException(e));
				re.setStackTrace(se.getStackTrace());
				throw re;
			}
			return retVal;
		}

		throw new StepNotFoundException(this);
	}

	@Override
	public String getSignature() {
		return name + "@" + getFileName() + "#" + getLineNumber();
	}

	public static Object execute(String stepName, Object... args) {
		StringTestStep proxy = new StringTestStep(stepName, args);
		proxy.initStep();
		return proxy.execute();
	}

	@Override
	public TestStep clone() {
		StringTestStep clone = new StringTestStep(name,
				(null != actualArgs ? actualArgs.clone() : null));
		clone.setFileName(fileName);
		clone.setLineNumber(lineNumber);
		clone.resultParameterName = resultParameterName;
		clone.context = context;
		clone.setStepMatcher(getStepMatcher());
		return clone;
	}

	public TestStep getTestStep() {
		TestStep step = null;
		String prefix = BDDKeyword.getKeywordFrom(name);
		String nameWithoutPrefix = removePrefix(prefix, name);
		if (getStepMapping().containsKey(nameWithoutPrefix.toUpperCase())) {

			step = getStepMapping().get(nameWithoutPrefix.toUpperCase()).clone();
		} else if (getBundle().getBoolean("step.natural.lang.support", true)) {
			Collection<TestStep> set = getStepMapping().values();
			for (TestStep stepName : set) {
				BDDStepMatcher matcher = stepName.getStepMatcher();//BDDStepMatcherFactory.getStepMatcher(stepName);
				if (matcher.matches(stepName.getDescription(), nameWithoutPrefix,
						context)) {
					List<String[]> parameters = matcher.getArgsFromCall(
							stepName.getDescription(), nameWithoutPrefix, context);
					Object[] params = new Object[parameters.size()];
					for (int i = 0; i < parameters.size(); i++) {
						params[i] = parameters.get(i)[0];
					}
					setActualArgs(params);
					step = getStepMapping().get(stepName.getName().toUpperCase()).clone();
					step.getStepExecutionTracker().setType(prefix);
					break;
				}
			}
		}

		return step;
	}

	private static String removePrefix(String prefix, String s) {
		if (isBlank(prefix))
			return s;
		return s.substring(prefix.length()).trim();
	}

	@Override
	protected Object doExecute() {
		// this is proxy!...
		return null;
	}

	@Override
	public StepExecutionTracker getStepExecutionTracker() {
		return step != null ? step.getStepExecutionTracker() : null;
	}

	public String getCodeSnippet() {
		if (isBlank(codeSnippet)) {
			generateCodeSnippet();
		}
		return codeSnippet;
	}

	private void generateCodeSnippet() {
		absractArgsAandSetDesciption();

		StringBuilder snippet = new StringBuilder(
				"\n/**\n* Auto-generated code snippet by QMetry Automation Framework.\n*/");
		snippet.append("\n@QAFTestStep(description=\"" + description + "\")");

		snippet.append("\npublic void " + name + "(");

		for (int i = 0; i < actualArgs.length; i++) {
			String arg = ((String) actualArgs[i]).trim();
			if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
				snippet.append("Boolean b" + i);
			} else {
				ParamType type = BDDDefinitionHelper.ParamType.getType(arg);
				snippet.append(type.getArgString() + i);
			}
			if (i < actualArgs.length - 1)
				snippet.append(',');
		}
		snippet.append("){");
		snippet.append(
				"\n\t//TODO: remove NotYetImplementedException and call test steps");
		snippet.append("\n\tthrow new NotYetImplementedException();\n}");

		codeSnippet = snippet.toString();
	}
	private void absractArgsAandSetDesciption() {
		String prefix = BDDKeyword.getKeywordFrom(name);
		if (actualArgs == null || actualArgs.length == 0) {
			final String REGEX = ParamType.getParamValueRegx();
			List<String> arguments = new ArrayList<String>();

			description = removePrefix(prefix, name);

			Pattern p = Pattern.compile(REGEX);
			Matcher m = p.matcher(description); // get a matcher object
			int count = 0;

			while (m.find()) {
				String arg = description.substring(m.start(), m.end());
				arguments.add(arg);
				count++;
			}
			for (int i = 0; i < count; i++) {
				description = description.replaceFirst(Pattern.quote(arguments.get(i)),
						Matcher.quoteReplacement("{" + i + "}"));
			}
			actualArgs = arguments.toArray(new String[]{});
		}
		name = toCamelCaseIdentifier(description.length() > 0 ? description : name);
	}
}
