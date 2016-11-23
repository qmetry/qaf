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


package com.qmetry.qaf.automation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

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
	 * @param formStr
	 * @return
	 */
	public static String toCamelCaseIdentifier(String formStr) {
		StringBuffer res = new StringBuffer();

		formStr = formStr.replaceAll("\\{(\\d)*(\\s)*\\}", "");
		String[] strArr = formStr.split("\\W");
		int i = 0;
		for (String str : strArr) {
			if (str.trim().length() > 0) {
				char[] stringArray = str.trim().toCharArray();
				if (i == 0)
					stringArray[0] = Character.toLowerCase(stringArray[0]);
				else stringArray[0] = Character.toUpperCase(stringArray[0]);
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
	 * @param str
	 *            : string to check
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
	 * @param dateStr
	 *            : date string to be formated
	 * @param formatFrom
	 *            : format of the given date string
	 * @param formatTo
	 *            : String expected format
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

	public static boolean seleniumEquals(String expectedPattern, String actual) {

		if ((expectedPattern == null) || (actual == null)) {
			return expectedPattern == actual;
		}
		if (actual.startsWith("regexp:") || actual.startsWith("regex:") || actual.startsWith("regexpi:")
				|| actual.startsWith("regexi:") || actual.startsWith("start:") || actual.startsWith("end:")
				|| actual.startsWith("in:")) {
			// swap 'em
			String tmp = actual;
			actual = expectedPattern;
			expectedPattern = tmp;
		}
		if (expectedPattern.startsWith("start:")) {
			return actual.startsWith(expectedPattern.replaceFirst("start:", ""));
		}
		if (expectedPattern.startsWith("end:")) {
			return actual.endsWith(expectedPattern.replaceFirst("end:", ""));
		}
		if (expectedPattern.startsWith("in:")) {
			return actual.contains(expectedPattern.replaceFirst("in:", ""));
		}
		Boolean b;
		b = handleRegex("regexp:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regex:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexpi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}

		if (expectedPattern.startsWith("exact:")) {
			String expectedExact = expectedPattern.replaceFirst("exact:", "");
			if (!expectedExact.equals(actual)) {
				System.out.println("expected " + actual + " to match " + expectedPattern);
				return false;
			}
			return true;
		}

		String expectedGlob = expectedPattern.replaceFirst("glob:", "");
		expectedGlob = expectedGlob.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1");

		expectedGlob = expectedGlob.replaceAll("\\*", ".*");
		expectedGlob = expectedGlob.replaceAll("\\?", ".");
		if (!Pattern.compile(expectedGlob, Pattern.DOTALL).matcher(actual).matches()) {
			System.out.println("expected \"" + actual + "\" to match glob \"" + expectedPattern
					+ "\" (had transformed the glob into regexp \"" + expectedGlob + "\"");
			return false;
		}
		return true;
	}

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
	 * @param csvKeyVal
	 *            or other char separated key=value pair. For example:
	 *            "foo=bar,xxx=yyy"
	 * @param ensureKeyUppercase
	 *            : if true then it will set upper-case key for value
	 * @param ch
	 *            (optional) char used to separate key=value pair. default
	 *            separator char is ","
	 * @return
	 */
	public static Map<String, String> toMap(String csvKeyVal, boolean ensureKeyUppercase, char... ch) {
		String[] params = StringUtil.parseCSV(csvKeyVal, ch);

		return toMap(params, ensureKeyUppercase);
	}

	/**
	 * @param csvKeyVal
	 *            array of key=value pair.
	 * @param ensureKeyUppercase
	 *            : if true then it will set upper-case key for value
	 * @return map
	 */
	public static Map<String, String> toMap(String[] csvKeyVal, boolean ensureKeyUppercase) {
		WeakHashMap<String, String> map = new WeakHashMap<String, String>();
		if (null == csvKeyVal) {
			return map;
		}
		for (String param : csvKeyVal) {
			if (isNotBlank(param)) {
				String[] kv = param.split("=");
				map.put(ensureKeyUppercase ? kv[0].toUpperCase() : kv[0], kv.length > 1 ? (kv[1]) : "");
			}
		}
		return map;
	}

	/**
	 * Method to parse character separated values, generic version of comma
	 * separated values Supports escape Character
	 * 
	 * @param data
	 * @param char[]
	 *            optional char args<br>
	 *            char[0] : Separator - default value ','<br>
	 *            char[1] : escape charter - default value '\'
	 * @return
	 */
	public static String[] parseCSV(String data, char... ch) {
		List<String> values = new ArrayList<String>();
		char seperator = ((null == ch) || (ch.length < 1) || (ch[0] == NULL)) ? ',' : ch[0];
		char escapeChar = ((null == ch) || (ch.length < 2) || (ch[1] == NULL)) ? '\\' : ch[1];
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length(); ++i) {
			char c = data.charAt(i);
			if (c == seperator) {
				values.add(sb.toString());
				sb = new StringBuilder();
				continue;
			} else if (c == escapeChar) {
				++i;
				c = data.charAt(i);
			}
			sb.append(c);
		}
		values.add(sb.toString());

		return (values.toArray(new String[values.size()]));
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
	 * @param strings
	 *            The Strings to combine.
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
	 * <li>"true", "True", "T", "t", "Y", "Yes", "YES", "On", "ON", "oN" as
	 * true.
	 * <li>"0" is false, "1" as true ("0" or negative as false and non-zero
	 * positive integer as true.)
	 * </ul>
	 * 
	 * @param sVal
	 *            string value
	 * @param defaultValue
	 *            value to be return if provided string is blank or null
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
	 * <li>"true", "True", "T", "t", "Y", "Yes", "YES", "On", "ON", "oN" as
	 * true.
	 * <li>"0" or negative as false and non-zero positive positive integer as
	 * true.
	 * </ul>
	 * 
	 * @param sVal
	 * @return boolean value for the string.
	 */
	public static boolean booleanValueOf(String sVal) {
		return booleanValueOf(sVal, false);

	}

	/**
	 * Convert number to string with suffix. For example: 1 -> 1st, 2 -> 2nd and
	 * so on.
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
}
