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

import static com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.quoteParams;
import static com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.replaceParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper;
import com.qmetry.qaf.automation.step.client.text.BDDDefinitionHelper.ParamType;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class BDDStepMatcherFactory {
	public static BDDStepMatcher getStepMatcher(TestStep s) {
		if (!(s instanceof JavaStep) || ((JavaStep) s).isQafStepImpl()) {
			return new DefaultBDDStepMatcher();
		} else {
			return new GherkinStepMatcher();
		}
	}

	public static class DefaultBDDStepMatcher implements BDDStepMatcher {

		@Override
		public boolean matches(String stepDescription, String stepCall, Map<String, Object> context) {
			stepDescription=fixEsc(stepDescription);
			stepCall = replaceParams(stepCall, context);

			stepCall = quoteParams(stepCall);
			return BDDDefinitionHelper.matches(stepDescription, stepCall);
		}

		@Override
		public List<String[]> getArgsFromCall(String stepDescription, String stepCall, Map<String, Object> context) {
			stepDescription=fixEsc(stepDescription);
			stepCall = replaceParams(stepCall, context);
			stepCall = quoteParams(stepCall);
			return BDDDefinitionHelper.getArgsFromCall(stepDescription, stepCall);
		}
		private static String fixEsc(String s) {
			Pattern p1 = Pattern.compile("\\\\\\((.*?)\\)");
			Pattern p2 = Pattern.compile("\\\\\\{(.*?)\\}");
			s = p1.matcher(s).replaceAll("\\\\\\($1\\\\\\)");
			s = p2.matcher(s).replaceAll("\\\\\\{$1\\\\\\}");
			return s;
		}

	}

	public static class GherkinStepMatcher implements BDDStepMatcher {
		@Override
		public boolean matches(String stepDescription, String stepCall, Map<String, Object> context) {
			stepCall = replaceParams(stepCall, context);
			Pattern p = getPattern(stepDescription);
			return p.matcher(stepCall).matches();
		}

		@Override
		public List<String[]> getArgsFromCall(String stepDescription, String stepCall, Map<String, Object> context) {
			List<String[]> args = new ArrayList<String[]>();
			stepCall = replaceParams(stepCall, context);
			//stepCall = quoteParams(stepCall);
			Matcher matcher = getMatcher(stepDescription, stepCall);
			while (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String arg = matcher.group(i);
					args.add(new String[] { arg, ParamType.getType(arg).name() });
				}
			}
			return args;
		}
		private Matcher getMatcher(String stepDescription, String stepName) {
			Pattern pattern = getPattern(opArgs(stepDescription));

			if (pattern.matcher(stepName).lookingAt()) {
				return pattern.matcher(stepName);
			}
			pattern = getPattern(stepDescription);
			return pattern.matcher(stepName);
		}

		private Pattern getPattern(String stepDescription) {
			if (stepDescription.endsWith(":$")) {
				String exp = "(.+)";
				stepDescription = StringUtil.replace(stepDescription, ":$", ":" + exp + "$", 1);
			}
			return Pattern.compile(stepDescription, Pattern.CASE_INSENSITIVE);
		}

		private static String opArgs(String stepDescription) {
			String st = Pattern.quote("(?:");
			String end = Pattern.quote(")?");
			Pattern pattern = Pattern.compile(st + ".*" + end);
			Matcher matcher = pattern.matcher(stepDescription);
			while (matcher.find()) {
				for (int i = 0; i <= matcher.groupCount(); i++) {
					String match = matcher.group(i);
					match = match.substring(3, match.length() - 2);
					stepDescription = stepDescription.replaceFirst(st + ".*" + end, match);
				}
			}
			return stepDescription;
		}

	}
	
}
