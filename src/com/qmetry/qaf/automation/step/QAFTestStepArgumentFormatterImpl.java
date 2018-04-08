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

package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.text.StrSubstitutor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.qmetry.qaf.automation.gson.GsonDeserializerObjectWrapper;
import com.qmetry.qaf.automation.gson.ObjectWrapper;

/**
 * @author chirag.jayswal
 *
 */
public class QAFTestStepArgumentFormatterImpl implements QAFTestStepArgumentFormatter<Object> {
	private static final Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy")
			.registerTypeAdapter(ObjectWrapper.class, new GsonDeserializerObjectWrapper()).create();
	@Override
	public Object format(Object objVal, Map<String, Object> context) {
		Class<?> paramType = (Class<?>) context.get("__paramType");
		if ((objVal instanceof String)) {
			String pstr = (String) objVal;

			if (pstr.startsWith("${") && pstr.endsWith("}")) {
				String pname = pstr.substring(2, pstr.length() - 1);
				objVal = context.containsKey(pstr) ? context.get(pstr)
						: context.containsKey(pname) ? context.get(pname)
								: getBundle().containsKey(pstr) ? getObject(pstr, paramType ) : getPropValue(pname,paramType);
			} else if (pstr.indexOf("$") >= 0) {
				pstr = StrSubstitutor.replace(pstr, context);
				objVal = getBundle().getSubstitutor().replace(pstr);
			}

		}
		if (String.class.isAssignableFrom(paramType)) {
			return objVal;
		}
		try {
			String strVal = gson.toJson(objVal);
			if (objVal instanceof String) {
				strVal = String.valueOf(objVal);
			}

			strVal = getBundle().getSubstitutor().replace(strVal);
			strVal = StrSubstitutor.replace(strVal, context);

			try {
				// prevent gson from expressing integers as floats
				ObjectWrapper w = gson.fromJson(strVal, ObjectWrapper.class);
				Object obj = w.getObject();
				try {
					objVal = paramType.cast(obj);
				} catch (Exception e) {
					JsonElement j = gson.toJsonTree(obj);
					objVal = gson.fromJson(j, paramType);
				}
			} catch (Exception e) {
				try {
					objVal = gson.fromJson(strVal, paramType);
				} catch (Exception e1) {
					// https://github.com/qmetry/qaf/issues/181
					Object oval = gson.fromJson(strVal, Object.class);
					if (null!=oval && oval instanceof List) {
						objVal=null;
						List<?> lst = ((List<?>)oval);
						if(!lst.isEmpty()){
							objVal=lst.get(0);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return objVal;
	}
	private Object getPropValue(String pname,  Class<?> paramType) {
		Object o = getBundle().subset(pname);
		if (o instanceof HierarchicalConfiguration && ((HierarchicalConfiguration) o).getRoot().getValue() == null
				&& ((HierarchicalConfiguration) o).getRoot().getChildrenCount() > 0) {
			return new ConfigurationMap(getBundle().subset(pname));
		}
		return getObject(pname, paramType);
	}
	
	private Object getObject(String key, Class<?> paramType){
		Object o = getBundle().getProperty(key);
		if(o.getClass().isAssignableFrom(paramType)){
			return o;
		}
		if(paramType.isArray()){
			return getBundle().getList(key).toArray();
		}
		if(o instanceof List){
			return ((List<?>)o).get(0);
		}
		return o;
	}

}
