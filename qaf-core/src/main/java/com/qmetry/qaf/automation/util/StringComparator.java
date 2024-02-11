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

/**
 * com.qmetry.qaf.automation.util.StringComparator.java
 * <dl>
 * Usage:
 * <dt>
 * </dl>
 * 
 * @author chirag
 */
public enum StringComparator {
	/**
	 * to compare s1 with s2.
	 */
	Exact("", ""),
	/**
	 * to compare s1 for s2 as prefix.
	 */
	Prefix("", ".*"),
	/**
	 * to compare s1 for s2 as suffix.
	 */
	Suffix(".*", ""),
	/**
	 * to check whether s1 contains s2?
	 */
	In(".*", ".*"),
	/**
	 * compare s1 with regexp, s2 will be treated as regexp
	 */
	RegExp("", "");
	String p, s;

	private StringComparator(String p, String s) {
		this.p = p;
		this.s = s;
	}

	public boolean compare(String s1, String s2) {
		if (RegExp.equals(this)) {
			return s1.matches(s2);
		}
		return s1.matches(p + s2.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1") + s);
	}

	public boolean compareIgnoreCase(String s1, String s2) {
		return compare(s1.toUpperCase(), s2.toUpperCase());
	}

}
