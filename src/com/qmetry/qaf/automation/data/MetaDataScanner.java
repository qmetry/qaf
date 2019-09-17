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
package com.qmetry.qaf.automation.data;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.text.StrSubstitutor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.google.gson.Gson;

/**
 * Internal class for test case and test step meta data scanning.
 * 
 * @author Chirag.jayswal
 */
public class MetaDataScanner {

	/**
	 * Scans all annotation except @Test, and generates map.
	 * 
	 * @param MethodOrFileld
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getMetadata(AccessibleObject methodOrFileld, boolean excludeTest) {
		Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

		try {
			Annotation[] allAnnotations = methodOrFileld.getAnnotations();
			for (Annotation annotation : allAnnotations) {
				if (excludeTest && annotation instanceof Test)
					continue;

				Method[] annotationMethods = annotation.annotationType().getDeclaredMethods();
				for (Method annotationMethod : annotationMethods) {
					Object objVal = annotationMethod.invoke(annotation);
					if (annotation instanceof MetaData) {
						@SuppressWarnings("unchecked")
						Map<String, Object> map = new Gson().fromJson((String) objVal, Map.class);
						metadata.putAll(map);
					} else {
						metadata.put(annotationMethod.getName(), objVal);
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return metadata;
	}

	/**
	 * Scans all the annotation on the method and prepares a map.
	 * 
	 * @param method
	 * @return map containing all annotation parameter as key.
	 */
	public static Map<String, Object> getMetadata(AccessibleObject methodOrFileld) {
		return getMetadata(methodOrFileld, false);
	}

	/**
	 * 
	 * @param xmlTest
	 * @param parameter
	 * @return
	 */
	public static String getParameter(XmlTest xmlTest, String parameter) {
		String paramValue = "";
		
		boolean overrideUsingSystemProp = System.getProperties().containsKey(parameter);
		
		Map<String, String> context = xmlTest.getAllParameters();
		context.keySet().removeAll(System.getProperties().keySet());
		
		if (overrideUsingSystemProp) {
			paramValue = System.getProperty(parameter);
		}else if(context.containsKey(parameter)){
			paramValue = context.get(parameter);
		}else if(getBundle().containsKey(parameter)){
			try {
				//unresolved value
				paramValue = (String) getBundle().configurationAt(parameter).getRoot().getValue();
			} catch (Exception e) {
				paramValue=getBundle().getString(parameter, "");
			}
		}
		paramValue = StrSubstitutor.replace(paramValue, context);
		paramValue = getBundle().getSubstitutor().replace(paramValue);
		return paramValue;
	}

	/**
	 * Get parameter value from the system property, context or configuration.
	 * 
	 * @param context
	 * @param parameter
	 * @return parameter value, first preference is system property, second is
	 *         context and last is configuration/properties.
	 */
	public static String getParameter(ITestContext context, String parameter) {
		return getParameter(context.getCurrentXmlTest(), parameter);
	}

	/**
	 * Get parameter value from the system property, context or configuration.
	 * 
	 * @param method
	 * @param parameter
	 * @return parameter value, first preference is system property, second is
	 *         context and last is configuration/properties.
	 */
	public static String getParameter(ITestNGMethod method, String parameter) {
		if (null != method && null != method.getXmlTest()) {
			return getParameter(method.getXmlTest(), parameter);
		}		
		return getBundle().getString(parameter);
	}
}
