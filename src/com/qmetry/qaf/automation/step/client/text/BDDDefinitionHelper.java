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

package com.qmetry.qaf.automation.step.client.text;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class BDDDefinitionHelper {

	/**
	 * This enumeration specifies BDD keywords used while BDD step mapping. You
	 * can specify synonyms for keyword in properties file by using keyword name
	 * as key.
	 * <p>
	 * For example to specify Synonyms for "Given" keyword provide property:<br>
	 * <code>
	 * Given=Provided;GivenThat;Assume</code>
	 * </p>
	 * Above property enable "Provided", "GivenThat" and "Assume" as synonym of
	 * "Given"
	 * 
	 * @author chirag.jayswal
	 */
	public enum BDDKeyword {
		Given, When, Then, And, Using, Having, With;

		/**
		 * all keywords including synonyms
		 * 
		 * @return
		 */
		public static List<String> getAllKeyWords() {
			List<String> keywords = new ArrayList<String>();
			for (BDDKeyword keyword : BDDKeyword.values()) {
				keywords.add(keyword.name());
				// add all synonyms for this keyword
				for (String synonym : keyword.getSynonyms()) {
					keywords.add(synonym);
				}
			}
			return keywords;
		}

		public static String getKeyWordRegEx() {
			StringBuilder sb = new StringBuilder("^(");
			for (String keyword : getAllKeyWords()) {
				sb.append(keyword);
				sb.append("|");
			}
			sb.deleteCharAt(sb.length() - 1); // remove last |
			sb.append(")");
			return sb.toString();
		}

		/**
		 * @return Synonyms for keyword defined in properties using
		 *         {@link BDDKeyword} name as key.
		 */
		public List<String> getSynonyms() {
			List<String> synonyms = new ArrayList<String>();
			for (Object object : getBundle().getList(name())) {
				if (null != object && StringUtil.isNotBlank(object.toString()))
					synonyms.add(object.toString());
			}
			return synonyms;
		}

		public static String getKeywordFrom(String behavior) {
			String regx = getKeyWordRegEx();
			Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(behavior);
			if (matcher.find())
				return matcher.group();
			return "";
		}
	}

	public enum ParamType {

		STRING(
				// "('[^(\\\\')*]*((\\\\')*(\\*|\\!|\\(|\\))*[^(\\\\')*]*)*')|(\"[^(\\\\\")*]*((\\\\\")*(\\*|\\!|\\(|\\))*[^(\\\\\")*]*)*\")"),
				"('([^\\\\']|\\\\\\\\|\\\\')*')|(\"([^\\\\\"]|\\\\\\\\|\\\\\")*\")"), MAP("(\\{.*})"), LIST(
						"(\\[.*])"), LONG("([-+]?\\d+)"), DOUBLE("([-+]?\\d+(\\.\\d+)?)"), ANY("(.*)");

		private String regx;

		ParamType(String regx) {
			this.regx = regx;
		}

		public static ParamType getType(String value) {
			for (ParamType type : ParamType.values()) {
				Pattern p = Pattern.compile(type.getRegx());
				Matcher matcher = p.matcher(value);
				if (matcher.matches())
					return type;
			}
			return null;
		}

		public String getRegx() {
			return regx;
		}

		public static String getParamValueRegx() {
			StringBuilder sb = new StringBuilder("(");

			// sequence is important
			sb.append(MAP.getRegx());
			sb.append("|");
			sb.append(LIST.getRegx());
			sb.append("|");
			sb.append(STRING.getRegx());
			// long it self is double and will reads 2 numbers for floating
			// point value (for eg: reads 12 and 5 for 12.5), so don't consider
			// only double which is valid for both case
			sb.append("|");
			sb.append(DOUBLE.getRegx());

			sb.append(")");
			return sb.toString();
		}

		public static String getParamDefRegx() {
			return "\\{([^\\}]).*?}";
		}
	}

	public static String quoteParams(String call) {
		String exp = "(\\s|^)\\$\\{[\\w\\.]*}(\\s|$)";
		Pattern p = Pattern.compile(exp);
		Matcher matcher = p.matcher(call);
		String resultString = new String(call);
		while (matcher.find()) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String unQuatedparam = (matcher.group(i));
				if (StringUtil.isNotBlank(unQuatedparam)) {
					String quatedparam =
							unQuatedparam.replace("${", "'${").replace("}", "}'");
					resultString = new String(StringUtil.replace(new String(resultString),
							unQuatedparam, quatedparam));
				}
			}
		}
		return resultString;
	}

	public static List<String[]> getArgs(String call, String def, List<String> argsInDef) {

		List<String[]> rlst = new ArrayList<String[]>();
		String wcopy = def;
		for (int i = 0; i < argsInDef.size(); i++) {
			String curArg = argsInDef.get(i);
			int argPos = wcopy.indexOf(curArg);
			String part = wcopy.substring(0, argPos);

			String result = getFirstMatch(part, call);

			// remove string till current argument including current argument
			// string
			wcopy = wcopy.substring(argPos + curArg.length());
			call = call.substring(result.length());

			String nextGroup = i == argsInDef.size() - 1 ? wcopy
					: wcopy.substring(0, wcopy.indexOf(argsInDef.get(i + 1)));
			nextGroup = getFirstMatch(nextGroup, call);
			String temp = getFirstMatch(ParamType.getParamValueRegx() + nextGroup, call);
			temp = temp.replaceAll(nextGroup, "");

			String[] arg = new String[] { temp, ParamType.getType(temp).name() };
			rlst.add(arg);

			// remove arg from call
			call = call.substring(temp.length());

		}

		return rlst;
	}

	/**
	 * @param def
	 * @param call
	 * @return
	 */
	public static List<String[]> getArgsFromCall(String def, String call) {
		return getArgsFromCall(def,call,getArgNames(def));
	}

	public static List<String[]> getArgsFromCall(String def, String call,List<String> defArgPos) {
		List<String[]> argsToreturn = new ArrayList<String[]>();
		List<String[]> args = getArgs(call, def, defArgPos);

		Pattern num = Pattern.compile(ParamType.LONG.getRegx());

		argsToreturn.addAll(args);
		// check possible combinations
		for (int i = 0; i < defArgPos.size(); i++) {
			String posInDef = defArgPos.get(i);
			def = def.replace(posInDef, Pattern.quote(args.get(i)[0]));

			Matcher numMathcher = num.matcher(posInDef);
			int argPos = numMathcher.find() ? Integer.parseInt(numMathcher.group()) : i;
			args.get(i)[0] = processArg(args.get(i)[0]);
			argsToreturn.set(argPos, args.get(i));
		}

		return argsToreturn;
	}
	
	public static boolean matches(String def, String call) {
		String origDef = def;
		def = def.replaceAll(ParamType.getParamDefRegx(), ParamType.getParamValueRegx().replaceAll("\\\\", "\\\\\\\\"));
		// System.out.println(def);
		if (!StringMatcher.likeIgnoringCase("(((" + BDDKeyword.getKeyWordRegEx() + ")\\s)?" + def + ")").match(call)) {
			return false;
		} else {
			List<String[]> argsa = getArgsFromCall(origDef, call);
			if (getArgNames(def).size() != argsa.size())
				return false;
		}
		return true;
	}

	private static String processArg(String s) {
		if (s.startsWith("'") && s.endsWith("'"))
			s = s.substring(1, s.length() - 1).replaceAll("(\\\\')", "'");
		if (s.startsWith("\"") && s.endsWith("\""))
			s = s.substring(1, s.length() - 1).replaceAll("(\\\\\")", "\"");
		return s;
	}

	public static List<String> getArgNames(String def) {
		Pattern p = Pattern.compile(ParamType.getParamDefRegx());
		Matcher matcher = p.matcher(def);
		List<String> args = new ArrayList<String>();
		while (matcher.find()) {
			args.add(matcher.group());
		}
		return args;
	}

	private static String getFirstMatch(String exp, String s) {
		Matcher m = Pattern.compile(exp).matcher(s);
		if (m.find()) {
			return m.group();
		}
		return "";
	}

	public static String format(String def, Object... objects) {
		if (null == objects || objects.length <= 0)
			return def;
		List<String> args = getArgNames(def);
		if (args.isEmpty() || args.size() != objects.length)
			return def;

		for (int i = 0; i < args.size(); i++) {
			def = StringUtil.replace(def, args.get(i), "'" + String.valueOf(objects[i]) + "'", 1);
		}

		return def;
	}
}
