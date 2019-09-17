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
package com.qmetry.qaf.automation.utils;

import static com.qmetry.qaf.automation.util.StringMatcher.contains;
import static com.qmetry.qaf.automation.util.StringMatcher.containsIgnoringCase;
import static com.qmetry.qaf.automation.util.StringMatcher.exact;
import static com.qmetry.qaf.automation.util.StringMatcher.exactIgnoringCase;
import static com.qmetry.qaf.automation.util.StringMatcher.startsWith;
import static com.qmetry.qaf.automation.util.StringMatcher.startsWithIgnoringCase;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringMatcherSuite {
	@Test
	public void exactTest() {
		Assert.assertEquals(exact("a1a").match("a1a"), true);
		Assert.assertEquals(exact("AAa").match("aaa"), false);
		Assert.assertEquals(exact("aaa").match("aaaa"), false);
		Assert.assertEquals(exact("a?a").match("a1a"), false);
		Assert.assertEquals(exact("a*a").match("a1a"), false);
		Assert.assertEquals(exact("a*a").match("aaaa"), false);
		Assert.assertEquals(exact("a*a").match("a*a"), true);

	}

	@Test
	public void exactIgnoringCaseTest() {
		Assert.assertEquals(exactIgnoringCase("a1a").match("a1a"), true);
		Assert.assertEquals(exactIgnoringCase("AAa").match("aaa"), true);
		Assert.assertEquals(exactIgnoringCase("aaa").match("aaaa"), false);
		Assert.assertEquals(exactIgnoringCase("a?a").match("a1a"), false);
		Assert.assertEquals(exactIgnoringCase("a*a").match("a1a"), false);
		Assert.assertEquals(exactIgnoringCase("a*a").match("aaaa"), false);
		Assert.assertEquals(exactIgnoringCase("a*a").match("A*a"), true);

	}

	@Test
	public void containsTest() {
		Assert.assertEquals(contains("a1a").match("a1a"), true);
		Assert.assertEquals(contains("AAa").match("aaa"), false);
		Assert.assertEquals(contains("aaa").match("xaaax sad"), true);
		Assert.assertEquals(contains("a1a").match(".*xa1abb"), true);
		Assert.assertEquals(contains("a1a").match(".*xA1abb"), false);

		Assert.assertEquals(contains("a?a").match("a1a"), false);
		Assert.assertEquals(contains("a*a").match("a1a"), false);
		Assert.assertEquals(contains("a*a").match("aaaa"), false);
		Assert.assertEquals(contains("a*a").match("aa*a"), true);

	}

	@Test
	public void containsIgnorecaseTest() {
		Assert.assertEquals(containsIgnoringCase("a1a").match("a1a"), true);
		Assert.assertEquals(containsIgnoringCase("AAa").match("aaa"), true);
		Assert.assertEquals(containsIgnoringCase("aaa").match("aaaa"), true);
		Assert.assertEquals(containsIgnoringCase("a1a").match(".*xa1abb"), true);
		Assert.assertEquals(containsIgnoringCase("a1a").match(".*xA1abb"), true);

		Assert.assertEquals(containsIgnoringCase("a?a").match("a1a"), false);
		Assert.assertEquals(containsIgnoringCase("a*a").match("a1a"), false);
		Assert.assertEquals(containsIgnoringCase("a*a").match("aaaa"), false);
		Assert.assertEquals(containsIgnoringCase("a*a").match("aA*a"), true);

	}

	@Test
	public void startsTest() {
		Assert.assertEquals(startsWith("a1a").match("a1a"), true);
		Assert.assertEquals(startsWith("AAa").match("aaa"), false);
		Assert.assertEquals(startsWith("aaa").match("aaaa"), true);
		Assert.assertEquals(startsWith("a1a").match(".*xa1abb"), false);
		Assert.assertEquals(startsWith("a1a").match("A1abb"), false);

		Assert.assertEquals(startsWith("a?a").match("a1adfs"), false);
		Assert.assertEquals(startsWith("a*a").match("a1aaaa"), false);
		Assert.assertEquals(startsWith("a*a").match("aaaa"), false);
	}

	@Test
	public void startsIgnorecaseTest() {
		Assert.assertEquals(startsWithIgnoringCase("a1a").match("a1a"), true);
		Assert.assertEquals(startsWithIgnoringCase("AAa").match("aaa"), true);
		Assert.assertEquals(startsWithIgnoringCase("aaa").match("aaaa"), true);
		Assert.assertEquals(startsWithIgnoringCase("a1a").match(".*xa1abb"), false);
		Assert.assertEquals(startsWithIgnoringCase("a1a").match("A1abb"), true);
		Assert.assertEquals(startsWithIgnoringCase("a*a").match("a*Abb"), true);

		Assert.assertEquals(startsWithIgnoringCase("a?a").match("a1a"), false);
		Assert.assertEquals(startsWithIgnoringCase("a*a").match("a1a"), false);
		Assert.assertEquals(startsWithIgnoringCase("a*a").match("aaaa"), false);
	}
}
