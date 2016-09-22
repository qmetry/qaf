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
