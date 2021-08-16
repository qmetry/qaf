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
package com.qmetry.qaf.automation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class StringUtil extends StringUtils {
	/**
	 * @param str
	 * @return string with first char in upper-case
	 */
	public static String getTitleCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Utility method to create variable or method name from string.
	 * 
	 * @param formStr
	 * @return
	 */
	public static String toCamelCaseIdentifier(String formStr) {
		StringBuffer res = new StringBuffer();
		if (isEmpty(formStr)) {
			return "";
		}
		formStr = formStr.replaceAll("\\{(\\d)*(\\s)*\\}", "");
		String[] strArr = formStr.split("\\W");
		int i = 0;
		for (String str : strArr) {
			if (str.trim().length() > 0) {
				char[] stringArray = str.trim().toCharArray();
				if (i == 0)
					stringArray[0] = Character.toLowerCase(stringArray[0]);
				else
					stringArray[0] = Character.toUpperCase(stringArray[0]);
				str = new String(stringArray);

				res.append(str);
			}
			i++;
		}
		return res.toString().trim();
	}

	public static String toTitleCaseIdentifier(String formStr) {
		return getTitleCase(toCamelCaseIdentifier(formStr));
	}

	public static String getRandomString(String format) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			char a = Character.isDigit(c) ? RandomStringUtils.randomNumeric(1).charAt(0)
					: Character.isLetter(c) ? RandomStringUtils.randomAlphabetic(c).charAt(0) : c;
			sb.append(a);
		}
		return sb.toString();
	}

	/**
	 * @param str : string to check
	 * @return true if string contains any number, false otherwise
	 */
	public boolean containsNumbers(String str) {
		return str.matches(".*\\d.*");
	}

	/**
	 * Convert date string from one format to another format.
	 * <p>
	 * <b>Example:</b>
	 * <ul>
	 * <li><code>
	 * formatDate("2012-01-11",
				"yyy-MM-dd", "MMM d, yyyy"))
	 * </code> will retrun "Jan 11, 2012"</li>
	 * <li>formatDate("2012-01-11T05:38:00+0530", {@link #BPM_DATETIME_FORMAT},
	 * {@link #GI_DATETIME_FORMAT})) will retrun "Jan 11, 2012 05:38 AM"</li>
	 * </ul>
	 * </p>
	 * 
	 * @param dateStr    : date string to be formated
	 * @param formatFrom : format of the given date string
	 * @param formatTo   : String expected format
	 * @return date string in expected format
	 */
	public static String getFormatedDate(String dateString, String formatFrom, String formatTo) {
		SimpleDateFormat aformat = new SimpleDateFormat(formatFrom);
		SimpleDateFormat eformat = new SimpleDateFormat(formatTo);
		Date d;
		try {
			d = aformat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
		return eformat.format(d);
	}

	public static String createRandomString(String prefix) {
		Random r = new Random();

		String token = Long.toString(Math.abs(r.nextLong()), 36);

		return (prefix + "_" + token);
	}

	public static String createRandomString() {
		Random r = new Random();

		String token = Long.toString(Math.abs(r.nextLong()), 36);

		return (token);
	}

	public static boolean isNullOrEmpty(String s) {
		return (null == s) || s.isEmpty();
	}

	public static boolean isXpath(String val) {
		return !isNullOrEmpty(val) && (val.startsWith("//") || val.toLowerCase().startsWith("xpath"));
	}

	public static String getWellFormedXPATH(String val) {
		String fstr = val;
		if (!val.toLowerCase().startsWith("xpath")) {
			fstr = "xpath=(" + val + ")[1]";
		}
		if (!val.endsWith("]")) {
			fstr = val + "[1]";
		}
		return fstr;
	}

	public static String extractParamValueFromUrl(String urlString, String paramName) {
		String retVal = "";
		String[] params = urlString.split("\\?|&");
		for (String param : params) {
			if (param.startsWith(paramName.trim() + "=")) {
				retVal = param.substring(paramName.trim().length() + 1);
				break;
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @param expectedPattern
	 * @param actual
	 * @return
	 */
	public static boolean seleniumEquals(String expectedPattern, String actual) {
		return StringMatcher.match(expectedPattern, actual);
	}

	/**
	 * @deprecated - unused method will be removed in future.
	 * @param prefix
	 * @param expectedPattern
	 * @param actual
	 * @param flags
	 * @return
	 */
	public static Boolean handleRegex(String prefix, String expectedPattern, String actual, int flags) {
		if (expectedPattern.startsWith(prefix)) {
			String expectedRegEx = expectedPattern.replaceFirst(prefix, "");// +
			// ".*";
			Pattern p = Pattern.compile(expectedRegEx, flags);
			if (!p.matcher(actual).matches()) {
				System.out.println("expected " + actual + " to match regexp " + expectedPattern);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return null;
	}

	public static final char NULL = Character.MIN_VALUE;

	/**
	 * get map form key value pair separated by char (default char is ",")
	 * 
	 * @param csvKeyVal          or other char separated key=value pair. For
	 *                           example: "foo=bar,xxx=yyy"
	 * @param ensureKeyUppercase : if true then it will set upper-case key for value
	 * @param ch                 (optional) char used to separate key=value pair.
	 *                           default separator char is ","
	 * @return
	 */
	public static Map<String, String> toMap(String csvKeyVal, boolean ensureKeyUppercase, char... ch) {
		Object[] params = StringUtil.parseCSV(csvKeyVal, ch);

		return toMap((String[]) params, ensureKeyUppercase);
	}

	/**
	 * @param csvKeyVal          array of key=value pair.
	 * @param ensureKeyUppercase : if true then it will set upper-case key for value
	 * @return map
	 */
	public static Map<String, String> toMap(String[] csvKeyVal, boolean ensureKeyUppercase) {
		WeakHashMap<String, String> map = new WeakHashMap<String, String>();
		if (null == csvKeyVal) {
			return map;
		}
		for (String param : csvKeyVal) {
			if (isNotBlank(param)) {
				String[] kv = param.split("=", 2);
				map.put(ensureKeyUppercase ? kv[0].toUpperCase() : kv[0], kv.length > 1 ? (kv[1]) : "");
			}
		}
		return map;
	}

	/**
	 * Method to parse character separated values, generic version of comma
	 * separated values Supports escape Character. It also supports quoted
	 * string.Examples:
	 * <ul>
	 * <li>"a",1,true,1.5 -> ["a",1,true,1.5]
	 * <li>"a,b",1,true,1.5 -> ["a,b",1,true,1.5]
	 * <li>" a ",1,true,1.5 -> [" a ",1,true,1.5]
	 * <li>,,, -> [null,null,null,null]
	 * <li>" a " , 1 , true , 1.5 ->[" a ",1,true,1.5]
	 * <li>a | 1 | true | 1.5 Separator |->["a",1,true,1.5]
	 * <li>" a "| 1 |true| 1.5 ->Separator |[" a ",1,true,1.5]
	 * <li>"a, b"| 1 |true| 1.5 ->Separator |["a, b",1,true,1.5]
	 * <li>a b | 1 |true| 1.5 ->Separator |["a b",1,true,1.5]
	 * <li>"a\" b" | 1 |true| 1.5 ->Separator |["a\" b",1,true,1.5]
	 * <li>| | | ->Separator |[null,null,null,null]
	 * <li>"a"" b" | 1 |true| 1.5 ->Separator |["a\" b",1,true,1.5]
	 * 
	 * 
	 * @param data
	 * @param char[] optional char args<br>
	 *               char[0] : Separator - default value ','<br>
	 *               char[1] : escape charter - default value '\'
	 * @return
	 */
	public static Object[] parseCSV(String data, char... ch) {
		List<Object> values = new ArrayList<Object>();
		boolean hasSeperator = null != ch && ch.length > 0 && ch[0] != NULL && ch[0] != ',';
		char seperator = hasSeperator ? ch[0] : ',';
		char escapeChar = ((null == ch) || (ch.length < 2) || (ch[1] == NULL)) ? '\\' : ch[1];

		if (data.indexOf(escapeChar + "" + seperator) < 0) {
			// without escape char for separator
			if (hasSeperator && data.contains(",")) {
				data = ensuerStringQuated(data, seperator);
			}

			String commaSperatoredData = data.replace(seperator, ',');
			// fix ignored end column if it is null
			if (commaSperatoredData.trim().endsWith(",")) {
				commaSperatoredData = commaSperatoredData + "\"\"";
			}

			return getArrayFromCsv(commaSperatoredData);
		}
		// to continue support old way with use of escape char without quoted string
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length(); ++i) {
			char c = data.charAt(i);
			if (c == seperator) {
				values.add(toObject(sb.toString()));
				sb = new StringBuilder();
				continue;
			} else if (c == escapeChar) {
				++i;
				c = data.charAt(i);
			}
			sb.append(c);
		}
		values.add(toObject(sb.toString()));

		return (values.toArray(new Object[values.size()]));
	}

	/**
	 * Extract all numbers from given string. For example: extractNums(
	 * "test123456.0012300 another number -201") will return array of Double
	 * {123456.00123, -201.0}
	 * 
	 * @param s
	 * @return array of numbers
	 */
	public static Double[] extractNums(String s) {
		ArrayList<Double> lst = new ArrayList<Double>();
		Pattern p = Pattern.compile("-?\\d+.?\\d+");
		Matcher m = p.matcher(s);
		while (m.find()) {
			lst.add(Double.parseDouble(m.group()));
		}
		return lst.toArray(new Double[lst.size()]);
	}

	/**
	 * Takes a list of Strings and combines them into a single comma-separated
	 * String.
	 * 
	 * @param strings The Strings to combine.
	 * @return The combined, comma-separated, String.
	 */
	public static String commaSeparate(Collection<String> strings) {
		StringBuilder buffer = new StringBuilder();
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			buffer.append(string);
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	/**
	 * This method will will consider:
	 * <ul>
	 * <li>"true", "True", "T", "t", "Y", "Yes", "YES", "On", "ON", "oN" as true.
	 * <li>"0" is false, "1" as true ("0" or negative as false and non-zero positive
	 * integer as true.)
	 * </ul>
	 * 
	 * @param sVal         string value
	 * @param defaultValue value to be return if provided string is blank or null
	 * @return boolean value for the string.
	 */
	public static boolean booleanValueOf(String sVal, Boolean defaultValue) {
		if (isBlank(sVal)) {
			return null == defaultValue ? false : defaultValue.booleanValue();
		}
		sVal = sVal.trim();
		boolean val = isNumeric(sVal) ? (Integer.parseInt(sVal) != 0)
				: Boolean.parseBoolean(sVal) || sVal.equalsIgnoreCase("T") || sVal.equalsIgnoreCase("Y")
						|| sVal.equalsIgnoreCase("YES") || sVal.equalsIgnoreCase("ON");

		return val;

	}

	/**
	 * This method will will consider:
	 * <ul>
	 * <li>Blank or null as false
	 * <li>"true", "True", "T", "t", "Y", "Yes", "YES", "On", "ON", "oN" as true.
	 * <li>"0" or negative as false and non-zero positive positive integer as true.
	 * </ul>
	 * 
	 * @param sVal
	 * @return boolean value for the string.
	 */
	public static boolean booleanValueOf(String sVal) {
		return booleanValueOf(sVal, false);

	}

	/**
	 * Convert number to string with suffix. For example: 1 -> 1st, 2 -> 2nd and so
	 * on.
	 * 
	 * @param number
	 * @return string with suffix
	 */
	public static String toStringWithSufix(int number) {
		if ((number % 100 > 10 && number % 100 < 20))
			return number + "th";
		switch (number % 10) {
		case 1:
			return number + "st";

		case 2:
			return number + "nd";

		case 3:
			return number + "rd";

		default:
			return number + "th";
		}
	}

	/**
	 * @see #eval(String, Map)
	 * 
	 * @param expression
	 * @return
	 * @throws ScriptException
	 */
	public static <T> T eval(String expression) throws ScriptException {
		return eval(expression, Collections.emptyMap());
	}

	/**
	 * 
	 * <ul>
	 * <li>In addition to context provided in method call, any property and static
	 * methods/variables with fully qualified class name are supported as variable
	 * in expression.
	 * <li>Variable names are case-sensitive, e.g. var1 and Var1 are different
	 * variables. However those provided in context are case insensitive.
	 * <li>If variable names are not following jexl standards for variable names, it
	 * can be referenced with <code>_ctx</code>. For example: 
	 * <br />commons-logging // invalid variable name (hyphenated) can be used in expression as <code>_ctx['commons-logging']</code>
	 * <li>static methods and variables are allowed in expressions. For example,
	 * <br />eval("java.lang.Math.min(23,a)", context)
	 * </ul>
	 * Refer <a href="https://commons.apache.org/proper/commons-jexl/reference/syntax.html">documentation</a> for expression syntax.
	 * 
	 * @param expression
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T eval(String expression, Map<? extends String, ? extends Object> context)
			throws ScriptException {
		try {
			JexlEngine jexlEngine = new JexlBuilder().create();
			JexlExpression expr = jexlEngine.createExpression(expression);
			JexlContext jc = new QAFJexlContext(context);

			return (T) expr.evaluate(jc);
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * Try to convert a string into java primitive type, java object or null. If the
	 * string can't be converted, return the string. From 3.0.0 It will return ""
	 * for empty string.
	 * 
	 * @param string
	 * @return Object
	 */
	public static Object toObject(String string) {
		if (null == string || JSONObject.NULL == string) {
			return null;
		}
		Object val = JSONObject.stringToValue(string);
		if (null == val || JSONObject.NULL == val) {
			return null;
		}
		return val;
	}

	private static String ensuerStringQuated(String data, char seperator) {
		if (isBlank(data) || data.indexOf(seperator) < 0) {
			return data;
		}
		StringBuilder sb = new StringBuilder();
		String[] parts = data.split(Pattern.quote(String.valueOf(seperator)), -1);
		for (String part : parts) {
			part = part.trim();
			if (part.indexOf(',') >= 0 && !part.startsWith("\"") && !part.endsWith("\"")) {
				sb.append(JSONObject.quote(part));
			} else {
				sb.append(part);
			}
			sb.append(seperator);
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * 
	 * @param csv
	 * @return
	 */
	private static Object[] getArrayFromCsv(String csv) {
		JSONArray obj = CDL.rowToJSONArray(new JSONTokener(csv));

		List<Object> strings = obj.toList();
		Object[] array = new Object[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			array[i] = toObject((String) strings.get(i));
		}
		return array;
	}
}
