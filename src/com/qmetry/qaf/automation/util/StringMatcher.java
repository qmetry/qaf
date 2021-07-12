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

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;

public abstract class StringMatcher {
	protected String stringToMatch;

	public StringMatcher(String stringToMatch) {
		this.stringToMatch = stringToMatch;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + stringToMatch;
	}

	/**
	 * Match expected string to actual string
	 * 
	 * @param actual - actual string
	 * @return
	 */
	abstract public boolean match(String actual);

	public static boolean match(String val1, String val2) {
		if (null == val1 || null == val2) {
			return val1 == val2;
		}
		StringMatcher matcher = getMatcherOrNull(val1);
		if (matcher != null) {
			return matcher.match(val2);
		}
		matcher = getMatcherOrNull(val2);
		if (matcher != null) {
			return matcher.match(val1);
		}

		String defaultMatcherType = ConfigurationManager.getBundle().getString("stringmatcher.default.matcher",
				"exact");
		matcher = get(defaultMatcherType, val1);
		if (matcher != null) {
			return matcher.match(val2);
		}
		throw new AutomationError(String.format("Wrong matcher type [%s]", defaultMatcherType));
	}

	/**
	 * provision for define matcher in external data file, that can be converted to
	 * actual one in code!
	 * 
	 * @param type
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher get(String type, String stringToMatch) {
		try {
			type = type.toLowerCase().replace("nocase", "ignoringcase").replace("anycase", "ignoringcase")
					.replace("ignorecase", "ignoringcase");
			Method m = ClassUtil.getMethod(StringMatcher.class, type);
			return (StringMatcher) m.invoke(null, stringToMatch);
		} catch (Exception e) {
		}
		return null;
	}

	private static StringMatcher getMatcherOrNull(String stringWithMatcher) {
		if (stringWithMatcher.indexOf(":") > 0) {

			try {
				String[] parts = stringWithMatcher.split(":", 2);
				return get(parts[0], parts[1]);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static StringMatcher expected(String stringWithMatcher) {
		if (StringUtil.isNullOrEmpty(stringWithMatcher)) {
			return new NullOrEmpty();
		}
		String[] parts = stringWithMatcher.split(":", 2);
		if (StringUtil.isNotBlank(parts[0])) {
			try {
				return get(parts[0], parts[1]);
			} catch (Exception e) {

			}
		}
		return getDefultMatcher(stringWithMatcher);
	}

	private static StringMatcher getDefultMatcher(String expectedString) {
		String defaultMatcherType = ConfigurationManager.getBundle().getString("stringmatcher.default.matcher",
				"exact");
		StringMatcher matcher = get(defaultMatcherType, expectedString);
		if (matcher != null) {
			return matcher;
		}
		throw new AutomationError(String.format("Wrong matcher type [%s]", defaultMatcherType));
	}

	public static StringMatcher blank(String...nothig) {
		return new Blank();
	}

	public static StringMatcher in(String stringToMatch) {
		return new Contains(stringToMatch);
	}

	public static StringMatcher end(String stringToMatch) {
		return new EndsWith(stringToMatch);
	}

	public static StringMatcher endIgnoringCase(String stringToMatch) {
		return new EndsWith(stringToMatch);
	}

	public static StringMatcher start(String stringToMatch) {
		return new StartsWith(stringToMatch);
	}

	public static StringMatcher startIgnoringCase(String stringToMatch) {
		return new StartsWithIgnoringCase(stringToMatch);
	}

	public static StringMatcher regex(String stringToMatch) {
		return new Like(stringToMatch);
	}

	public static StringMatcher regexp(String stringToMatch) {
		return new Like(stringToMatch);
	}

	public static StringMatcher regexi(String stringToMatch) {
		return new LikeIgnoringCase(stringToMatch);
	}

	public static StringMatcher regexpi(String stringToMatch) {
		return new LikeIgnoringCase(stringToMatch);
	}

	public static StringMatcher glob(String stringToMatch) {
		return new Global(stringToMatch);
	}

	public static StringMatcher exact(String stringToMatch) {
		return new Exact(stringToMatch);
	}

	public static StringMatcher exactIgnoringCase(String stringToMatch) {
		return new ExactIgnoringCase(stringToMatch);
	}

	public static StringMatcher startsWith(String stringToMatch) {
		return new StartsWith(stringToMatch);
	}

	public static StringMatcher startsWithIgnoringCase(String stringToMatch) {
		return new StartsWithIgnoringCase(stringToMatch);
	}

	public static StringMatcher endsWith(String stringToMatch) {
		return new EndsWith(stringToMatch);
	}

	public static StringMatcher endsWithIgnoringCase(String stringToMatch) {
		return new EndsWithIgnoringCase(stringToMatch);
	}

	public static StringMatcher contains(String stringToMatch) {
		return new Contains(stringToMatch);
	}

	public static StringMatcher containsIgnoringCase(String stringToMatch) {
		return new ContainsIgnoringCase(stringToMatch);
	}

	/**
	 * @param stringToMatch : valid regular expression
	 * @return
	 */
	public static StringMatcher like(String stringToMatch) {
		return new Like(stringToMatch);
	}

	/**
	 * @param stringToMatch : valid regular expression
	 * @return
	 */
	public static StringMatcher likeIgnoringCase(String stringToMatch) {
		return new LikeIgnoringCase(stringToMatch);
	}

	/**
	 * Numeric greater then
	 * 
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher gt(String stringToMatch) {
		return new GT(stringToMatch);
	}

	/**
	 * Numeric greater then equal
	 * 
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher gte(String stringToMatch) {
		return new GTE(stringToMatch);
	}

	/**
	 * Numeric less then
	 * 
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher lt(String stringToMatch) {
		return new LT(stringToMatch);
	}

	/**
	 * Numeric less then equal
	 * 
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher lte(String stringToMatch) {
		return new LTE(stringToMatch);
	}

	/**
	 * Numeric equal, for string equality use either {@link #exact(String)} or
	 * {@link #exactIgnoringCase(String)}
	 * 
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher eq(String stringToMatch) {
		return new EQ(stringToMatch);
	}

	private static class Exact extends StringMatcher {

		Exact(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return stringToMatch.equals(target);
		}

	}

	private static class ExactIgnoringCase extends StringMatcher {
		ExactIgnoringCase(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return stringToMatch.equalsIgnoreCase(target);
		}

	}

	private static class StartsWithIgnoringCase extends StringMatcher {
		StartsWithIgnoringCase(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.toUpperCase().startsWith(stringToMatch.toUpperCase());
		}
	}

	private static class StartsWith extends StringMatcher {
		StartsWith(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.startsWith(stringToMatch);
		}
	}

	private static class EndsWithIgnoringCase extends StringMatcher {
		EndsWithIgnoringCase(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.toUpperCase().endsWith(stringToMatch.toUpperCase());
		}
	}

	private static class EndsWith extends StringMatcher {
		EndsWith(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.endsWith(stringToMatch);
		}
	}
	
	private static class NullOrEmpty extends StringMatcher {
		NullOrEmpty() {
			super("");
		}

		@Override
		public boolean match(String target) {
			return StringUtil.isNullOrEmpty(stringToMatch);
		}
	}
	
	private static class Blank extends StringMatcher {
		Blank() {
			super("");
		}

		@Override
		public boolean match(String target) {
			return StringUtil.isBlank(stringToMatch);
		}
	}
	private static class ContainsIgnoringCase extends StringMatcher {
		ContainsIgnoringCase(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.toUpperCase().contains(stringToMatch.toUpperCase());
		}
	}

	private static class Contains extends StringMatcher {
		Contains(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return target.contains(stringToMatch);
		}
	}

	private static class LikeIgnoringCase extends StringMatcher {
		LikeIgnoringCase(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			Pattern p = Pattern.compile(stringToMatch, Pattern.CASE_INSENSITIVE);
			return p.matcher(target).matches();
		}
	}

	private static class Like extends StringMatcher {
		Like(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			return Pattern.matches(stringToMatch, target);
		}
	}

	private static class Global extends StringMatcher {
		Global(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			String expectedGlob = stringToMatch.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1");
			expectedGlob = expectedGlob.replaceAll("\\*", ".*");
			expectedGlob = expectedGlob.replaceAll("\\?", ".");
			return Pattern.compile(expectedGlob, Pattern.DOTALL).matcher(target).matches();
		}
	}

	private static class EQ extends StringMatcher {
		EQ(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			try {
				double expected = Double.parseDouble(stringToMatch);
				double actual = Double.parseDouble(target);

				return actual == expected;
			} catch (NumberFormatException e) {
				return target == null ? target == stringToMatch : target.equals(stringToMatch);
			} catch (Exception e) {
				return false;
			}
		}
	}
	
	private static class LT extends StringMatcher {
		LT(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			try {
				double expected = Double.parseDouble(stringToMatch);
				double actual = Double.parseDouble(target);

				return actual < expected;
			} catch (Exception e) {
				return false;
			}
		}
	}

	private static class LTE extends StringMatcher {
		LTE(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			try {
				double expected = Double.parseDouble(stringToMatch);
				double actual = Double.parseDouble(target);

				return actual <= expected;
			} catch (Exception e) {
				return false;
			}
		}
	}

	private static class GT extends StringMatcher {
		GT(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			try {
				double expected = Double.parseDouble(stringToMatch);
				double actual = Double.parseDouble(target);

				return actual > expected;
			} catch (Exception e) {
				return false;
			}
		}
	}

	private static class GTE extends StringMatcher {
		GTE(String stringToMatch) {
			super(stringToMatch);
		}

		@Override
		public boolean match(String target) {
			try {
				double expected = Double.parseDouble(stringToMatch);
				double actual = Double.parseDouble(target);

				return actual >= expected;
			} catch (Exception e) {
				return false;
			}
		}
	}

}
