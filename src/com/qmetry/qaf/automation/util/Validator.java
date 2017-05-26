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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.testng.SkipException;

import com.qmetry.qaf.automation.core.MessageTypes;

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
			throw new AssertionError();
		}
	}

	public static <T> void givenThat(String reason, T actual, Matcher<? super T> matcher) {
		if (!verifyThat(reason, actual, matcher)) {
			throw new SkipException("Precondition not satisfied");
		}
	}

	public static <T> void givenThat(T actual, Matcher<? super T> matcher) {
		givenThat("", actual, matcher);
	}

}
