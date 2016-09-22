/*******************************************************************************
 * Copyright 2016 Infostretch Corporation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
	}

	@Test
	public void exactIgnoringCaseTest() {
		Assert.assertEquals(exactIgnoringCase("a1a").match("a1a"), true);
		Assert.assertEquals(exactIgnoringCase("AAa").match("aaa"), true);
		Assert.assertEquals(exactIgnoringCase("aaa").match("aaaa"), false);
		Assert.assertEquals(exactIgnoringCase("a?a").match("a1a"), false);
		Assert.assertEquals(exactIgnoringCase("a*a").match("a1a"), false);
		Assert.assertEquals(exactIgnoringCase("a*a").match("aaaa"), false);
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
