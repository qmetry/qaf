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

import static com.qmetry.qaf.automation.util.Reporter.log;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
//import org.testng.SkipException;
import com.google.common.collect.MapDifference;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.SkipTestException;

/**
 * com.qmetry.qaf.automation.util.Validator.java
 * 
 * @author chirag
 */
public class Validator {
	public static <T> boolean verifyThat(String reason, T actual, Matcher<? super T> matcher) {
		boolean result = matcher.matches(actual);
		Description description = new StringDescription();
		description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher)
				.appendText("\n     Actual: ");

		matcher.describeMismatch(actual, description);
		String msg = description.toString();
		if (msg.endsWith("Actual: ")) {
			msg = String.format(msg + "%s", actual);
		}
		msg = msg.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		Reporter.log(msg, result ? MessageTypes.Pass : MessageTypes.Fail);

		return result;
	}

	public static <T> boolean verifyThat(T actual, Matcher<? super T> matcher) {
		return verifyThat("", actual, matcher);
	}

	public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
		assertThat("", actual, matcher);
	}

	public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
		if (!verifyThat(reason, actual, matcher)) {
			throw new AssertionError(reason);
		}
	}

	public static <T> void givenThat(String reason, T actual, Matcher<? super T> matcher) {
		if (!verifyThat(reason, actual, matcher)) {
			throw new SkipTestException("Precondition not satisfied");
		}
	}

	public static <T> void givenThat(T actual, Matcher<? super T> matcher) {
		givenThat("", actual, matcher);
	}

	public static boolean verifyTrue(boolean condition, String failMessage,
			String successMsg) {
		if (condition) {
			log(successMsg, MessageTypes.Pass);
		} else {
			log(failMessage, MessageTypes.Fail);
		}
		return condition;

	}

	public static boolean verifyFalse(boolean condition, String failMessage,
			String successMsg) {
		if (!condition) {
			log(successMsg, MessageTypes.Pass);
		} else {
			log(failMessage, MessageTypes.Fail);
		}
		return !condition;

	}
	public static void assertTrue(boolean condition, String failMessage,
			String successMsg) {
		if (!verifyTrue(condition, failMessage, successMsg)) {
			throw new AssertionError(failMessage);
		}
	}

	public static void assertFalse(boolean condition, String failMessage,
			String successMsg) {
		if (!verifyFalse(condition, failMessage, successMsg)) {
			throw new AssertionError(failMessage);
		}
	}
	
	public static boolean verifyJsonContains(String actualJsonStr, String expectedJsonStr) {
		
		MapDifference<String, Object> res = JsonCompareUtil.jsonCompare(actualJsonStr, expectedJsonStr, false);
	
		String failureMsg = String.format("Diff:%s, Expected but Not Found: %s", res.entriesDiffering(), res.entriesOnlyOnLeft());
		String successMsg = String.format("Found Expected: %s", res.entriesInCommon());
	
		return Validator.verifyTrue(res.entriesOnlyOnLeft().isEmpty() && res.entriesDiffering().isEmpty(),failureMsg,
				successMsg);
	}

	public static void assertJsonContains(String actualJsonStr, String expectedJsonStr) {
		if (!verifyJsonContains(actualJsonStr, expectedJsonStr)) {
			throw new AssertionError("Actual json doesn't contains expected json");
		}
	}

	public static boolean verifyJsonMatches(String actualJsonStr, String expectedJsonStr) {
		
		MapDifference<String, Object> res = JsonCompareUtil.jsonCompare(actualJsonStr, expectedJsonStr, true);
	
		String failureMsg = String.format("Diff:%s, Expected but Not Found: %s", res.entriesDiffering(), res.entriesOnlyOnLeft());
		String successMsg = String.format("Found Expected: %s", res.entriesInCommon());
	
		return Validator.verifyTrue(res.entriesOnlyOnLeft().isEmpty() && res.entriesDiffering().isEmpty(),failureMsg,
				successMsg);
	}

	public static void assertJsonMatches(String actualJsonStr, String expectedJsonStr) {
		if (!verifyJsonMatches(actualJsonStr, expectedJsonStr)) {
			throw new AssertionError("Actual json doesn't matches expected json");
		}
	}
}
