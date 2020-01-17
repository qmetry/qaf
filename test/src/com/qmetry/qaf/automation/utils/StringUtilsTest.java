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

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.util.Validator;

public class StringUtilsTest {

	@Test(dataProvider = "seTestData")
	void seleniumEqualsTest(String expectedPattern, String actulaString, boolean expectedResult) {
		boolean actualResult = StringUtil.seleniumEquals(expectedPattern, actulaString);
		Validator.assertThat(actualResult, Matchers.equalTo(expectedResult));

	}

	@Test(dataProvider = "bvTestData")
	public void testBooleanValueOf(String val, Boolean expected) {
		boolean actual = StringUtil.booleanValueOf(val);
		Validator.assertThat(actual, Matchers.equalTo(expected));
	}

	@Test(dataProvider="csvTestDp")
	public void testToCSV(String csvStr, char seperator, Object[] expectedResult) {
		Object[] csv = StringUtil.parseCSV(csvStr,seperator);
		System.out.println(csvStr);
		System.out.println(new JSONArray(csv).toString());

		Validator.assertThat("Unable to parse: " + csvStr, csv, Matchers.arrayContaining(expectedResult));
		
	}
	
	@DataProvider(name = "csvTestDp")
	Object[][] csvTestDp() {
		return new Object[][] {
				new Object[] { "\"a\",1,true,1.5", ',', new Object[] { "a",1, true, 1.5}},
				new Object[] { "\"a\",1,true,1.5, a bc  ", ',', new Object[] { "a",1, true, 1.5, "a bc"}},
				new Object[] { "\"a\",1,true,1.5, a bc  ,null", ',', new Object[] { "a",1, true, 1.5, "a bc",null}},

				new Object[] { "\"a,b\",1,true,1.5", ',', new Object[] { "a,b",1, true, 1.5}},
				new Object[] { "\" a \",1,true,1.5", ',', new Object[] { " a ",1, true, 1.5}},
				new Object[] { " \" a \" , 1 , true , 1.5 ", ',', new Object[] { " a ",1, true, 1.5}},

				new Object[] { "a,1,true,1.5,a\\, bc", ',', new Object[] { "a",1, true, 1.5, "a, bc"}},
				new Object[] { "a,1,true,1.5,a\\, bc,", ',', new Object[] { "a",1, true, 1.5, "a, bc",null}},

				new Object[] { " a | 1 | true | 1.5 ", '|', new Object[] { "a",1, true, 1.5}},
				new Object[] { "\" a \"|	1 |true|	1.5	", '|', new Object[] { " a ",1, true, 1.5}},
				new Object[] { "\"a, b\"|	1 |true|	1.5	", '|', new Object[] { "a, b",1, true, 1.5}},
				new Object[] { "a b |	1 |true|	1.5	", '|', new Object[] { "a b",1, true, 1.5}},
				new Object[] { "a b |	abcd efgh. | 1 |true|	1.5	", '|', new Object[] { "a b","abcd efgh.",1, true, 1.5}},
				new Object[] { "a b |	abcd, efgh. | 1 |true|	1.5	", '|', new Object[] { "a b","abcd, efgh.",1, true, 1.5}},
				new Object[] { "\"a\"\" b\" |	1 |true|	1.5	", '|', new Object[] { "a\" b",1, true, 1.5}},
				new Object[] { "a b |	abc'd, efgh. | 1 |true|	1.5	", '|', new Object[] { "a b","abc'd, efgh.",1, true, 1.5}},
				
				//below case of 'single quoted text' without double quote will not work with tabs addition spaces to exclude
				new Object[] { "a b|\"'abc' d efgh.\"|1|true|1.5", '|', new Object[] { "a b","'abc' d efgh.",1, true, 1.5}},
				
				//ensure first and last value empty
				new Object[] { "| QMetry Automation Framework |", '|', new Object[] { null,"QMetry Automation Framework",null}},
				new Object[] { "||", '|', new Object[] { null,null,null}},
				//ensure all null
				new Object[] { "		,		,		", ',', new Object[] { null,null,null}},
				new Object[] { ",,", ',', new Object[] { null,null,null}},
				
				new Object[] { "| Selenium ISFW | Infostretch, Test Automation Framework | 10 |", '|', new Object[] { null,"Selenium ISFW", "Infostretch, Test Automation Framework", 10,null}},
				new Object[] { "| Selenium ISFW | \"Infostretch, Test Automation Framework\" | 10 |", '|', new Object[] { null,"Selenium ISFW", "Infostretch, Test Automation Framework", 10,null}},
				new Object[] { " | \"Selenium ISFW\" | Chirag's Test Automation Framework | 10 |", '|', new Object[] { null,"Selenium ISFW", "Chirag's Test Automation Framework", 10,null}},
				new Object[] { " | \"Selenium ISFW\" | \"Chirag's Test Automation Framework\" | 10 |", '|', new Object[] { null,"Selenium ISFW", "Chirag's Test Automation Framework", 10,null}},
				new Object[] { " | \"Selenium ISFW\" | 'My \"Test Automation Framework\"' | 10 |", '|', new Object[] { null,"Selenium ISFW", "My \"Test Automation Framework\"", 10,null}}
		};
	}

	@DataProvider(name = "seTestData")
	Object[][] seDp() {
		return new Object[][] {
				// String expectedPattern,String actulaString, boolean
				// expectedResult
				new Object[] { "exact:ab*cd", "ab*cd", true }, new Object[] { "ab*cd", "ab*cd", true },
				new Object[] { "***", "***", true }, new Object[] { "*abc*", "anythig abcshould fine", true },
				new Object[] { "*abc", "anythig abcshould fine", false }, new Object[] { "*abc", "abc", true },
				new Object[] { "abc", "a*c", false }, new Object[] { "exact:*abc", "abc", false },

				new Object[] { "*ab*cd", "*ab*cd", true }, new Object[] { "start:aB", "Ab*cd", false },
				new Object[] { "start:ab", "ab*cd", true }

		};
	}

	@Test(dataProvider = "bvTestData2")
	public void testBooleanValueOfWithDefault(String val, Boolean defaultValue, Boolean expected, String desc) {
		boolean actual = StringUtil.booleanValueOf(val, defaultValue);
		Validator.verifyThat(desc, actual, Matchers.equalTo(expected));
	}

	@DataProvider(name = "bvTestData")
	Object[][] dp() {
		return new Object[][] {
				// String value, expected boolean
				new Object[] { "T", true }, new Object[] { "t", true }, new Object[] { "Y", true },
				new Object[] { "y", true }, new Object[] { "Yes", true }, new Object[] { "YES", true },
				new Object[] { "yes", true }, new Object[] { "yEs", true }, new Object[] { "yeS", true },
				new Object[] { "0", false }, new Object[] { "0.9", false }, new Object[] { "1", true },
				new Object[] { "-5", false }, new Object[] { "5", true }, new Object[] { "on", true },
				new Object[] { "On", true }, new Object[] { "ON", true }, new Object[] { "abcd", false },
				new Object[] { " True  ", true }, new Object[] { "true", true }, new Object[] { "   ", false },
				new Object[] { "	", false }, new Object[] { "", false }

		};

	}

	@DataProvider(name = "bvTestData2")
	Object[][] dp2() {
		return new Object[][] {
				// val, defaultValue,expected, desc
				new Object[] { "", false, false, "blank stiring with default false" },
				new Object[] { "	", true, true, "blank stiring with default true" },
				new Object[] { "T", false, true, "T with default false" },
				new Object[] { "F", true, false, "F stiring with default true" },
				new Object[] { null, true, true, "null with default true" },
				new Object[] { null, false, false, "null with default false" }, };
	}
	
	public static void main(String[] args) {
		
	}
}
