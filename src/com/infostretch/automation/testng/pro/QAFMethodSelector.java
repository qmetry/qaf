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

package com.infostretch.automation.testng.pro;

import static com.infostretch.automation.data.MetaDataScanner.getMetadata;
import static com.infostretch.automation.data.MetaDataScanner.getParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infostretch.automation.step.client.Scenario;
import com.infostretch.automation.step.client.TestNGScenario;
import com.infostretch.automation.util.StringUtil;

/**
 * This is a method selector class that will selects the method with custom
 * meta-data filter.
 * 
 * @author chirag jayswal
 */
public class QAFMethodSelector implements IMethodSelector {

	/**
	 * 
	 */
	private static final long serialVersionUID = -681517529788899839L;

	/**
	 * 
	 */
	public QAFMethodSelector() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IMethodSelector#includeMethod(org.testng.
	 * IMethodSelectorContext , org.testng.ITestNGMethod, boolean)
	 */
	@Override
	public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {

		boolean include = applyMetafilter(method);
		if (!include) {
			context.setStopped(true);
		}
		return include;
	}

	@SuppressWarnings("unchecked")
	private boolean applyMetafilter(ITestNGMethod imethod) {
		String includeStr = getParameter(imethod, "include");
		String excludeStr = getParameter(imethod, "exclude");
		if (StringUtil.isBlank(includeStr) && StringUtil.isBlank(excludeStr)) {
			// no need to process as no include/exclude filter provided
			return true;
		}

		Gson gson = new GsonBuilder().create();

		Map<String, Object> includeMeta = gson.fromJson(includeStr, Map.class);
		Map<String, Object> excludeMeta = gson.fromJson(excludeStr, Map.class);

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

		return includeMethod(scenarioMetadata, includeMeta, excludeMeta);

	}

	public static boolean includeMethod(Map<String, Object> scenarioMetadata, Map<String, Object> includeMeta,
			Map<String, Object> excludeMeta) {

		boolean binclude = includeMeta == null || includeMeta.isEmpty()
				|| hasMetaValue(includeMeta, scenarioMetadata, true);
		boolean bexclude = excludeMeta != null && !excludeMeta.isEmpty()
				&& hasMetaValue(excludeMeta, scenarioMetadata, false);

		return binclude && !bexclude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IMethodSelector#setTestMethods(java.util.List)
	 */
	@Override
	public void setTestMethods(List<ITestNGMethod> testMethods) {
		// do nothing...

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

}
