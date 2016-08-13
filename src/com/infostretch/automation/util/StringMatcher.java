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

package com.infostretch.automation.util;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public abstract class StringMatcher {
	protected String stringToMatch;

	public StringMatcher(String stringToMatch) {
		this.stringToMatch = stringToMatch;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + stringToMatch;
	}

	abstract public boolean match(String target);

	/**
	 * provision for define matcher in external data file, that can be converted
	 * to actual one in code!
	 * 
	 * @param type
	 * @param stringToMatch
	 * @return
	 */
	public static StringMatcher get(String type, String stringToMatch) {
		try {
			Method m = ClassUtil.getMethod(StringMatcher.class, type);
			return (StringMatcher) m.invoke(null, stringToMatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	 * @param stringToMatch
	 *            : valid regular expression
	 * @return
	 */
	public static StringMatcher like(String stringToMatch) {
		return new Like(stringToMatch);
	}

	/**
	 * @param stringToMatch
	 *            : valid regular expression
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
	 * Numeric equal
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
