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

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.util.Validator;

public class StringUtilsTest {

	@Test(dataProvider = "seTestData")
	void seleniumEqualsTest(String expectedPattern, String actulaString,
			boolean expectedResult) {
		boolean actualResult = StringUtil.seleniumEquals(expectedPattern, actulaString);
		Validator.assertThat(actualResult, Matchers.equalTo(expectedResult));

	}

	@Test(dataProvider = "bvTestData")
	public void testBooleanValueOf(String val, Boolean expected) {
		boolean actual = StringUtil.booleanValueOf(val);
		Validator.assertThat(actual, Matchers.equalTo(expected));
	}

	@DataProvider(name = "seTestData")
	Object[][] seDp() {
		return new Object[][]{
				// String expectedPattern,String actulaString, boolean
				// expectedResult
				new Object[]{"exact:ab*cd", "ab*cd", true},
				new Object[]{"ab*cd", "ab*cd", true}, new Object[]{"***", "***", true},
				new Object[]{"*abc*", "anythig abcshould fine", true},
				new Object[]{"*abc", "anythig abcshould fine", false},
				new Object[]{"*abc", "abc", true}, new Object[]{"abc", "a*c", false},
				new Object[]{"exact:*abc", "abc", false},

				new Object[]{"*ab*cd", "*ab*cd", true},
				new Object[]{"start:aB", "Ab*cd", false},
				new Object[]{"start:ab", "ab*cd", true}

		};
	}
	@Test(dataProvider = "bvTestData2")
	public void testBooleanValueOfWithDefault(String val, Boolean defaultValue,
			Boolean expected, String desc) {
		boolean actual = StringUtil.booleanValueOf(val, defaultValue);
		Validator.verifyThat(desc, actual, Matchers.equalTo(expected));
	}
	@DataProvider(name = "bvTestData")
	Object[][] dp() {
		return new Object[][]{
				// String value, expected boolean
				new Object[]{"T", true}, new Object[]{"t", true}, new Object[]{"Y", true},
				new Object[]{"y", true}, new Object[]{"Yes", true},
				new Object[]{"YES", true}, new Object[]{"yes", true},
				new Object[]{"yEs", true}, new Object[]{"yeS", true},
				new Object[]{"0", false}, new Object[]{"0.9", false},
				new Object[]{"1", true}, new Object[]{"-5", false},
				new Object[]{"5", true}, new Object[]{"on", true},
				new Object[]{"On", true}, new Object[]{"ON", true},
				new Object[]{"abcd", false}, new Object[]{" True  ", true},
				new Object[]{"true", true}, new Object[]{"   ", false},
				new Object[]{"	", false}, new Object[]{"", false}

		};

	}

	@DataProvider(name = "bvTestData2")
	Object[][] dp2() {
		return new Object[][]{
				// val, defaultValue,expected, desc
				new Object[]{"", false, false, "blank stiring with default false"},
				new Object[]{"	", true, true, "blank stiring with default true"},
				new Object[]{"T", false, true, "T with default false"},
				new Object[]{"F", true, false, "F stiring with default true"},
				new Object[]{null, true, true, "null with default true"},
				new Object[]{null, false, false, "null with default false"},};
	}
}
