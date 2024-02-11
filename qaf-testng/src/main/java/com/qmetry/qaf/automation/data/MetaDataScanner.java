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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Internal class for test case and test step meta data scanning.
 * 
 * @author Chirag.jayswal
 */
public class MetaDataScanner extends MetaDataHelper{
	private static final Log logger = LogFactoryImpl.getLog(MetaDataScanner.class);

	

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
		} else if (context.containsKey(parameter)) {
			paramValue = context.get(parameter);
		} else if (getBundle().containsKey(parameter)) {
			try {
				// unresolved value
				paramValue = (String) getBundle().configurationAt(parameter).getRoot().getValue();
			} catch (Exception e) {
				paramValue = getBundle().getString(parameter, "");
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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSubset(ITestNGMethod method, String parameter) {
		if(null==method) {
			return ConfigurationConverter.getMap(getBundle().subset(parameter));
		}
		return getSubset(method.getXmlTest(), parameter);
	}
	public static Map<String, Object> getSubset(ITestContext context, String parameter) {
		return getSubset(context.getCurrentXmlTest(), parameter);
	}
	public static Map<String, Object> getSubset(XmlTest xmlTest, String parameter) {
			@SuppressWarnings("unchecked")
			Map<String, Object> subset = ConfigurationConverter.getMap(getBundle().subset(parameter));

			if (null != xmlTest) {
				Map<String, String> ctx = xmlTest.getAllParameters();
				ctx.keySet().removeAll(System.getProperties().keySet());

				String prefix = parameter + ".";
				// ctx.keySet().removeIf(e->!e.startsWith(prefix));
				for (Entry<String, String> e : ctx.entrySet()) {
					if (e.getKey().startsWith(prefix)) {
						Object val = e.getValue();
						if (e.getValue().indexOf(';') > 0) {
							val = Arrays.asList(e.getValue().split(";"));
						}
						subset.put(e.getKey().replace(prefix, ""), val);
					}
				}
			}
			return subset;
	}




	/**
	 * Method for none testNG implementation.
	 * 
	 * @param metadata
	 * @return true if test should included false otherwise
	 * @see MetaDataScanner#applyMetafilter(ITestNGMethod) for TestNG
	 */
	public static boolean applyMetafilter(Map<String, Object> metadata) {
		Object enabled = metadata.get("enabled");
		if (null != enabled && !("true".equalsIgnoreCase(enabled.toString())))
			return false;
		return applyMetafilter(null, metadata);
	}

	/**
	 * Method for TestNG implementation.
	 * 
	 * @param imethod
	 * @return true if it should included false otherwise
	 * @see MetaDataScanner#applyMetafilter(Map) for none TestNG implementation
	 */
	public static boolean applyMetafilter(ITestNGMethod imethod) {
		try {
			Map<String, Object> scenarioMetadata = new HashMap<String, Object>();

			// ideally ITestNGMethod should converted to TestNGScenario but if
			// framework class not loaded then need to process separately
			if (imethod instanceof TestNGScenario) {
				TestNGScenario method = (TestNGScenario) imethod;
				scenarioMetadata = method.getMetaData();

			} else if (Scenario.class.isAssignableFrom(imethod.getRealClass())) {
				Scenario method = (Scenario) imethod.getInstance();
				scenarioMetadata = method.getMetadata();
			} else {
				scenarioMetadata = getMetadata(imethod.getConstructorOrMethod().getMethod(), false);
			}
			return applyMetafilter(imethod, scenarioMetadata);
		} catch (Exception e) {
			logger.debug("Unable to apply meta-data filter ",e);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean applyMetafilter(ITestNGMethod imethod, Map<String, Object> scenarioMetadata) {
		String includeStr = getParameter(imethod, "include");
		String excludeStr = getParameter(imethod, "exclude");
		Map<String, Object> includeSubset = getSubset(imethod, "include");
		Map<String, Object> excludeSubset = getSubset(imethod, "exclude");

		if (StringUtil.isBlank(includeStr) && StringUtil.isBlank(excludeStr) && includeSubset.isEmpty() && excludeSubset.isEmpty()) {
			// no need to process as no include/exclude filter provided
			return true;
		}

		Gson gson = new GsonBuilder().create();
		

		Map<String, Object> includeMeta = gson.fromJson(includeStr, Map.class);
		Map<String, Object> excludeMeta = gson.fromJson(excludeStr, Map.class);
		
		//higher priority on individual include entry
		if (includeMeta != null) {
			includeMeta.putAll(includeSubset);
		} else {
			includeMeta = includeSubset;
		}
		if (excludeMeta != null) {
			excludeMeta.putAll(excludeSubset);
		} else {
			excludeMeta = excludeSubset;
		}

		return MetaDataScanner.includeMethod(scenarioMetadata, includeMeta, excludeMeta);
	}

}
