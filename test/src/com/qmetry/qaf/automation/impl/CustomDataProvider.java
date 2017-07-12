/**
 * 
 */
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
}
