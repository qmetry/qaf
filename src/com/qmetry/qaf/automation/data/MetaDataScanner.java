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
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.TestNGScenario;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider.params;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Internal class for test case and test step meta data scanning.
 * 
 * @author Chirag.jayswal
 */
public class MetaDataScanner {
	private static final Log logger = LogFactoryImpl.getLog(MetaDataScanner.class);

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

	public static boolean hasDP(Map<String, Object> metadata) {
		if (null == metadata) {
			return false;
		}
		Map<String, Object> kv = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		kv.putAll(metadata);
		for (params key : params.values()) {
			if (kv.containsKey(key.name())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static void formatMetaData(Map<String, Object> metadata) {
		Configuration formats = getBundle().subset(ApplicationProperties.METADATA_FORMTTOR_PREFIX.key);
		for (Entry<String, Object> entry : metadata.entrySet()) {
			String format = formats.getString(entry.getKey(), "");
			Object value = entry.getValue();
			if (value instanceof List) {
				List<Object> lVal = (List<Object>) value;
				for (int i = 0; i < lVal.size(); i++) {
					// if format exist apply only once
					if (StringUtil.isNotBlank(format) && !matches(format, lVal.get(i).toString())) {
						try {
							String formattedVal = MessageFormat.format(format, lVal.get(i));
							lVal.set(i, formattedVal);
						} catch (Exception e) {
							logger.error(
									"Unable to format metadata [" + entry.getKey() + "] using format [" + format + "]",
									e);
						}
					}
				}
			} else
			// if format exist apply only once
			if (StringUtil.isNotBlank(format) && !matches(format, value.toString())) {
				try {
					String formattedVal = MessageFormat.format(format, value);
					entry.setValue(formattedVal);
				} catch (Exception e) {
					logger.error("Unable to format metadata [" + entry.getKey() + "] using format [" + format + "]", e);
				}
			}
		}
	}

	@SuppressWarnings("serial")
	public static String applyMetaRule(Map<String, Object> metadata) {
		StringBuffer result = new StringBuffer();
		List<MetaDataRule> rules = new Gson().fromJson(ApplicationProperties.METADATA_RULES.getStringVal("[]"),
				new TypeToken<ArrayList<MetaDataRule>>() {
				}.getType());
		for (MetaDataRule rule : rules) {
			result.append(rule.apply(metadata));
		}
		return result.toString();
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
	}

	@SuppressWarnings("unchecked")
	private static boolean applyMetafilter(ITestNGMethod imethod, Map<String, Object> scenarioMetadata) {
		String includeStr = getParameter(imethod, "include");
		String excludeStr = getParameter(imethod, "exclude");
		if (StringUtil.isBlank(includeStr) && StringUtil.isBlank(excludeStr)) {
			// no need to process as no include/exclude filter provided
			return true;
		}

		Gson gson = new GsonBuilder().create();

		Map<String, Object> includeMeta = gson.fromJson(includeStr, Map.class);
		Map<String, Object> excludeMeta = gson.fromJson(excludeStr, Map.class);

		return MetaDataScanner.includeMethod(scenarioMetadata, includeMeta, excludeMeta);
	}

	public static boolean includeMethod(Map<String, Object> scenarioMetadata, Map<String, Object> includeMeta,
			Map<String, Object> excludeMeta) {

		boolean binclude = includeMeta == null || includeMeta.isEmpty()
				|| hasMetaValue(includeMeta, scenarioMetadata, true);
		boolean bexclude = excludeMeta != null && !excludeMeta.isEmpty()
				&& hasMetaValue(excludeMeta, scenarioMetadata, false);

		return binclude && !bexclude;
	}

	private static boolean hasMetaValue(Map<String, Object> metaFilter, Map<String, Object> metadata,
			boolean isInclude) {

		// get all the meta data keys used in filter
		Set<String> filterKeys = metaFilter.keySet(); // author, module, brand
		// iterate for each key and match with scenario meta data
		for (String metaKey : filterKeys) {
			// scenario's meta-value for given key
			Object metaVal = metadata.get(metaKey); // M1,M2,M3
			Set<Object> scMetaValues = getMetaValues(metaVal);

			// get meta-data value in filter for given key
			Object metaValuesObjForKey = metaFilter.get(metaKey);
			Set<Object> metaValuesForKey = getMetaValues(metaValuesObjForKey); // M1

			if (!metaValuesForKey.isEmpty()) {

				scMetaValues.retainAll(metaValuesForKey);
				// M1
				// found so just return as AND operation between deferment
				// meta-keys and OR with given meta-key values

				if (isInclude) {
					if (scMetaValues.isEmpty()) {
						return !isInclude;
					}
				} else {
					if (!scMetaValues.isEmpty()) {
						return !isInclude;
					}
				}
			}
		}
		return isInclude;
	}

	@SuppressWarnings("unchecked")
	private static Set<Object> getMetaValues(Object metaVal) {
		if (null == metaVal)
			return new HashSet<Object>();
		if (List.class.isAssignableFrom(metaVal.getClass()))
			return new HashSet<Object>((List<Object>) (metaVal));
		if (metaVal.getClass().isArray()) {
			Object[] vals = (Object[]) metaVal;
			return new HashSet<Object>(Arrays.asList(vals));
		}
		return new HashSet<Object>(Arrays.asList(metaVal));
	}

	private static boolean matches(String formatStr, String s) {
		MessageFormat messageFormat = new MessageFormat(formatStr);
		try {
			return messageFormat.parse(s).length > 0;
		} catch (ParseException e) {
		}
		return false;
	}

	private static class MetaDataRule {
		private String key;
		private MetaDataRule depends;
		private List<Object> values;
		private Boolean required;

		/**
		 * 
		 * @param metadata
		 * @return details of failure if any or empty string
		 */
		public String apply(Map<String, Object> metadata) {
			boolean isApplicable = isApplicable(metadata);

			if (metadata.containsKey(key)) {
				if (!isApplicable) {
					return "\nFound not aplicable Meta data [" + this + "]";
				}
				List<Object> declariedValues = getValues(key, metadata);
				for (Object value : declariedValues) {
					boolean matched = false;
					for (Object allowedValue : values) {
						matched = matched || value.toString().matches(allowedValue.toString());
					}
					if (!matched) {
						return "\nValue mismatch for Meta data [" + this + "]";
					}
				}
			} else if (null != required && required && isApplicable) {
				return "\nMissing required meta data [" + this + "]";
			}
			return "";
		}

		private boolean isApplicable(Map<String, Object> metadata) {
			if (null == depends) {
				return true;
			}
			depends.required = true;
			return StringUtil.isBlank(depends.apply(metadata));
		}

		@SuppressWarnings("unchecked")
		private List<Object> getValues(String key, Map<String, Object> metadata) {
			Object val = metadata.get(key);
			if (val instanceof List) {
				return (List<Object>) val;
			}
			return Arrays.asList(val);
		}

		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}
}
