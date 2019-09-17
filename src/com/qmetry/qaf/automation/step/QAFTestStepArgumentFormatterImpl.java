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
			String strVal;
			if (objVal instanceof String) {
				strVal = String.valueOf(objVal);
			}else{
				strVal = gson.toJson(objVal);
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
		if (null!=o && o.toString().indexOf("${")>=0 ){
			String ref = o.toString();
			if(ref.startsWith("${") && ref.toString().endsWith("}")){
				String pname = ref.substring(2, ref.length() - 1);
				return getPropValue(pname, paramType);
			}
			return getBundle().getSubstitutor().replace(ref);
		} 
		if(null==o || o.getClass().isAssignableFrom(paramType)){
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
