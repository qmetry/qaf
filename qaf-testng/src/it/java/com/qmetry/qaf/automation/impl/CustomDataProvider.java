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
package com.qmetry.qaf.automation.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.collections.Maps;

/**
 * @author chirag.jayswal
 *
 */
public class CustomDataProvider {
	@DataProvider(name="dp-without-injection")
	public static Object[][] dataProviderForBDD(){
		Map<Object, Object> m = Maps.newHashMap();
		m.put("value", "OK");
		return new Object[][]{{m}};
	}
	
	@DataProvider(name="dp-with-testngmethod")
	public Object[][] dataProviderForBDD(ITestNGMethod method){
		Map<Object, Object> m = Maps.newHashMap();
		m.put("method", method.getMethodName());
		return new Object[][]{{m}};
	}
	
	@DataProvider(name="dp-with-method")
	public Object[][] dataProviderForBDD(Method method){
		Map<Object, Object> m = Maps.newHashMap();
		m.put("method", method.getName());
		return new Object[][]{{m}};
	}
	
	@DataProvider(name="dp-with-testngmethod-contex")
	public Object[][] dataProviderForBDD(ITestNGMethod method, ITestContext contex){
		Map<Object, Object> m = Maps.newHashMap();
		m.put("method", method.getMethodName());
		m.put("contex", contex.getName());
		return new Object[][]{{m}};
	}
	
	@DataProvider(name="dp-for-filter")
	public static Object[][] dataProviderToTestFilter(){
		Map<Object, Object> m = Maps.newHashMap();
		m.put("name", "a");
		m.put("uname", "a");

		m.put("id", 1);
		
		Map<Object, Object> m2 = Maps.newHashMap();
		m2.put("name", "b");
		m2.put("uname", "b");

		m2.put("id", 2);

		Map<Object, Object> m3 = Maps.newHashMap();
		m3.put("name", "c");
		m3.put("uname", "c");

		m3.put("id", 3);
		
		Map<Object, Object> m4 = Maps.newHashMap();
		m4.put("name", "d");
		m4.put("uname", "d");

		m4.put("id", 4);

		Map<Object, Object> m5 = Maps.newHashMap();
		m5.put("name", "e");
		m5.put("uname", "e");

		m5.put("id", 5);
		return new Object[][]{{m},{m2},{m3},{m4},{m5}};
	}
}
