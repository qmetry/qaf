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
