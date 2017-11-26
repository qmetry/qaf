/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/
package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.text.StrSubstitutor;

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
			stepCall = BDDDefinitionHelper.quoteParams(stepCall);
			return BDDDefinitionHelper.matches(stepDescription, stepCall);
		}

		@Override
		public List<String[]> getArgsFromCall(String stepDescription, String stepCall, Map<String, Object> context) {
			stepCall = BDDDefinitionHelper.quoteParams(stepCall);
			return BDDDefinitionHelper.getArgsFromCall(stepDescription, stepCall);
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
			Matcher matcher = getMatcher(stepDescription, stepCall);
			while (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					String arg = matcher.group(i);
					args.add(new String[] { arg, ParamType.getType(arg).name() });
				}
			}
			return args;
		}

		private String replaceParams(String stepCall, Map<String, Object> context) {
			stepCall = StrSubstitutor.replace(stepCall, context);
			stepCall = getBundle().getSubstitutor().replace(stepCall);
			return stepCall;
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
