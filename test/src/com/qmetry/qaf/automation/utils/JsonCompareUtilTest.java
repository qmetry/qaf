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

import static com.qmetry.qaf.automation.util.Validator.assertThat;
import static com.qmetry.qaf.automation.util.Validator.verifyJsonContains;
import static com.qmetry.qaf.automation.util.Validator.verifyJsonMatches;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.TestBaseProvider;

/**
 * @author chirag.jayswal
 *
 */
public class JsonCompareUtilTest {

	@Test(dataProvider = "jsonContainsTestData", description = "Verify actual json contains expected json")
	public void testJsonContains(String actual, String expected, boolean outcome) {
		boolean result = verifyJsonContains(actual, expected);
		String message = TestBaseProvider.instance().get().getCheckPointResults().get(0).getMessage();
		TestBaseProvider.instance().get().claerAssertionsLog();
		assertThat(message, result, is(outcome));
	}

	@Test(dataProvider = "jsonMatchesTestData", description = "Verify actual json matches expected json")
	public void testJsonEq(String actual, String expected, boolean outcome) {
		boolean result = verifyJsonMatches(actual, expected);
		String message = TestBaseProvider.instance().get().getCheckPointResults().get(0).getMessage();
		TestBaseProvider.instance().get().claerAssertionsLog();
		assertThat(message, result, is(outcome));
	}
	
	@DataProvider(name = "jsonContainsTestData")
	Object[][] jsonContainsTestData() {
		List<Object[]> data = new ArrayList<Object[]>();
		//String actual, String expected, boolean expected-outcome
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':['a',1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':['a']}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'b':['a',1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a'}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1,2], 'c':true}", "{'a':'a','b':['a',1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1.0,2], 'c':true}", "{'a':'a','b':['a','eq:1','eq:2.0']}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1.0,2], 'c':true}", "{'a':'a','b':['a','exact:1']}", false });
		data.add(new Object[] { "{'a':'a','b':['a',1,2], 'c':true}", "{'a':'a','b':['a','exact:1']}", true });

		data.add(new Object[] { "{'a':'a','b':['a',1.0,2], 'c':true}", "{'a':'a','b':['a','exact:1.0','GT:1']}", true });

		data.add(new Object[] { "{'a':'a','b':['a',2], 'c':true}", "{'a':'a','b':['a',1]}", false });
		data.add(new Object[] { "{'a':'a','c':true}", "{'a':'a','b':['a',1]}", false });
		data.add(new Object[] { "{'a':'a','b':['a',2], 'c':true}", "{'a':'a','b':'*'}", true });
		data.add(new Object[] { "{'a':'a','c':true}", "{'a':'a','b':'*'}", false });
		
		//list
		data.add(new Object[] { "[{'a':'a','b':['a',1.0,2], 'c':true},{'a':'a1','b':['a1',1.0,2], 'c':true}]", "[{'a':'a','b':['a','exact:1.0','GT:1']}]", true });
		data.add(new Object[] { "[{'a':'a1','b':['a1',1.0,2], 'c':true},{'b':[1.0,'a',2],'a':'a', 'c':true}]", "[{'a':'a','b':['a','exact:1.0','GT:1']}]", true });

		//if expected duplicate in list, actual must have duplicate entry!... 
		data.add(new Object[] { "{'a':'a','b':['a',1,1,2]}", "{'a':'a','b':['a',1,1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1,2]}", "{'a':'a','b':['a',1,1]}", false });
		data.add(new Object[] { "[{'a':'a','b':['a',1.0,2]}, {'a':'a1','b':['a1',1.0,2], 'c':true},{'b':[1.0,'a',2],'a':'a', 'c':true}]", "[{'a':'a','b':['a','exact:1.0',2]}, {'a':'a','b':['a','exact:1.0',2]}]", true });
		data.add(new Object[] { "[{'a':'a1','b':['a1',1.0,2], 'c':true},{'b':[1.0,'a',2],'a':'a', 'c':true}]", "[{'a':'a','b':['a','exact:1.0',2]},{'a':'a','b':['a','exact:1.0',2]}]", false });

		data.add(new Object[] { "{'a':null,'b':['a',null,1,2]}", "{'a':null,'b':['a',1, null]}", true });
		data.add(new Object[] { "{'b':['a',null,1,2]}", "{'a':'*','b':['a',1, 2, '*']}", false });
		
		//you can't consider null as * (any value)
		data.add(new Object[] { "{'a':null,'b':['a',null,1,2]}", "{'a':'*','b':['a',1, 2, null]}", false });
		data.add(new Object[] { "{'a':null,'b':['a',null,1,2]}", "{'a':null,'b':['a',1, 2, '*']}", false });

		return data.toArray(new Object[][] {});
	}
	
	
	@DataProvider(name = "jsonMatchesTestData")
	Object[][] jsonMatchesTestData() {
		List<Object[]> data = new ArrayList<Object[]>();
		//String actual, String expected, boolean expected-outcome
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':['a',1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':[1,'a']}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'b':[1,'a'], 'a':'a'}", true });
		data.add(new Object[] { "{'a':'a','b':['a',2]}", "{'a':'a','b':'*'}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1.0,2]}", "{'a':'a','b':['a','eq:1','eq:2.0']}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':['a','exact:1']}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1.0,2]}", "{'a':'a','b':['a','exact:1.0','GT:1']}", true });

		data.add(new Object[] { "{'a':{'a':1,'b':2,'c':3},'b':['a',1.0,2], 'c':true}", "{'a':'*','b':'*', 'c':*}", true });

		data.add(new Object[] { "{'a':{'a':1,'b':2,'c':3},'b':['a',1.0,2], 'c':true}", "{'a':'*','b':'*', 'c':true}", true });

		//list
		data.add(new Object[] { "[{'a':'a','b':['a',1.0,2], 'c':true},{'b':['a1',1.0,2], 'a':'a1','c':true}]", "[{'a':'a1','b':['a1',1.0,2], 'c':true},{'a':'a','b':['a',1.0,2]}]", true });
		data.add(new Object[] { "[{'a':'a','b':['a',1.0,2], 'c':true},{'a':'a1','b':['a1',1.0,2], 'c':true}]", "[{'a':'a','b':['a','exact:1.0','GT:1']}]", false });

		//if expected duplicate in list, actual must have duplicate entry!... 
		data.add(new Object[] { "{'a':'a','b':[1,'a',1]}", "{'a':'a','b':['a',1,1]}", true });
		data.add(new Object[] { "{'a':'a','b':['a',1]}", "{'a':'a','b':['a',1,1]}", false });
		data.add(new Object[] { "{'a':'a','b':['a',1,2]}", "{'a':'a','b':['a',1,1]}", false });
	
		data.add(new Object[] { "{'a':null,'b':['a',null,1]}", "{'a':null,'b':['a',1, null]}", true });
		data.add(new Object[] { "{'b':['a',true,1,2]}", "{'a':'*','b':['a',1, 2, '*']}", false });
		
		//you can't consider null as * (any value)
		data.add(new Object[] { "{'a':null,'b':['a',null,1,2]}", "{'a':'*','b':['a',1, 2, null]}", false });
		data.add(new Object[] { "{'a':null,'b':['a',null,1,2]}", "{'a':null,'b':['a',1, 2, '*']}", false });

		return data.toArray(new Object[][] {});
	}
}
