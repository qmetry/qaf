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
package com.qmetry.qaf.automation.ws;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.annotations.SerializedName;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class WsRequestBean extends BaseDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 423394605353099602L;

	private String method = "GET";

	private String baseUrl = "";

	private String endPoint = "";

	private Map<String, Object> headers = new HashMap<String, Object>();

	private String[] accept = {};

	private String schema = "";

	private String body = "";

	@SerializedName(value = WSCRepositoryConstants.QUERY_PARAMETERS)
	private Map<String, Object> queryParameters = new HashMap<String, Object>();

	@SerializedName(value = WSCRepositoryConstants.FORM_PARAMETERS)
	private Map<String, Object> formParameters = new HashMap<String, Object>();

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private String reference = "";

	// private final transient Gson gson = getGson();

	public String getBaseUrl() {
		return StringUtil.isNotBlank(baseUrl) ? baseUrl : ApplicationProperties.SELENIUM_BASE_URL.getStringVal("");
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, Object> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(Map<String, Object> parameters) {
		this.queryParameters = parameters;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endpoint) {
		this.endPoint = endpoint;
	}

	public String[] getAccept() {
		return accept;
	}

	public void setAccept(String[] accept) {
		this.accept = accept;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Map<String, Object> getFormParameters() {
		return formParameters;
	}

	public void setFormParameters(Map<String, Object> formParameters) {
		this.formParameters = formParameters;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Priority for resolver is:
	 * <ol>
	 * <li>data provided in argument
	 * <li>parameter in request call
	 * <li>parameter in request reference
	 * <li>configuration property
	 * </ol>
	 * To ignore specific query or form parameter provide blank ('') value for that key.
	 * @param data
	 */
	public void resolveParameters(Map<String, Object> data) {

		JSONObject j = new JSONObject(this);
		j.remove("reference");
		String source = j.toString();

		// highest: data provided in argument while doing request
		if (null != data && !data.isEmpty()) {
			source = StrSubstitutor.replace(source, data);
		}
		//second: configuration manager
		source = getBundle().getSubstitutor().replace(source);

		fillFromJsonString(source);

		// lowest: default value in request call
		if (null != getParameters() && !getParameters().isEmpty()) {
			source = StrSubstitutor.replace(source, getParameters());
			fillFromJsonString(source);
		}
		// body from file?
		if (StringUtil.isNotBlank(body) && StringMatcher.startsWithIgnoringCase("file:").match(body)) {
			String file = body.split(":", 2)[1].trim();
			try {
				body = FileUtil.readFileToString(new File(file), StandardCharsets.UTF_8);
				resolveParameters(data);
			} catch (IOException e) {
				throw new AutomationError("Unable to read file: " + file, e);
			}
		}else {
			removeNulls(formParameters);
			removeNulls(queryParameters);
			removeNulls(headers);
		}
	}
	


	@SuppressWarnings("unchecked")
	@Override
	public void fillData(Object obj) {
		try {
			boolean isString = (obj instanceof String);
			if(obj instanceof Map) {
				fillData((Map<String, Object>)obj);
			}else if (isString && (getBundle().containsKey((String) obj) || !getBundle().subset((String) obj).isEmpty())) {
				fillFromConfig((String) obj);

			} else {
				String jsonStr = (isString ? (String) obj : JSONUtil.toString(obj));
				fillFromJsonString(jsonStr);
			}
		} catch (Exception e) {
			throw new AutomationError("Unable to populate request from" + obj, e);
		}
	}

	@Override
	public void fillFromConfig(String reqkey) {
		ConfigurationNode node = getBundle().configurationAt(reqkey).getRootNode();
		if (node.getValue()!=null) {
			fillFromJsonString((String)node.getValue());
		} else {
			Map<String, Object> map = nodeToMap(node);
			fillData(map);
		}
	}
	
	private Map<String, Object> nodeToMap(ConfigurationNode node){
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<?> nodes = node.getChildren().iterator();
		while (nodes.hasNext()) {
			Node cNode = (Node) nodes.next();
			if (!cNode.hasChildren()) {
				map.put(cNode.getName(), cNode.getValue());
			}else {
				map.put(cNode.getName(), nodeToMap(cNode));
			}
		}
		return map;
	}
	

	@Override
	public void fillData(Map<String, Object> map) {
		if (map.containsKey(WSCRepositoryConstants.REFERENCE)) {
			fillFromConfig((String)map.get(WSCRepositoryConstants.REFERENCE));
		}

		updateKey(map, WSCRepositoryConstants.FORM_PARAMETERS, "formParameters");
		updateKey(map, WSCRepositoryConstants.QUERY_PARAMETERS, "queryParameters");

		super.fillData(map);
	}

	/**
	 * fill bean from json data.
	 * 
	 * @param jsonstr
	 */
	public void fillFromJsonString(String jsonstr) {
		try {
			JSONObject jsonObject = new JSONObject(jsonstr);
			String[] keys = JSONObject.getNames(jsonObject);
			if (null != keys) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String key : keys) {
					try {
						map.put(key, jsonObject.getJSONObject(key).toString());
					} catch (Exception e) {
						map.put(key, jsonObject.get(key).toString());
					}
				}
				fillData(map);
			}
		} catch (JSONException e) {
			throw new AutomationError(jsonstr + " is not valid Json", e);
		}

	}

	public void setFormParameters(String val) {
		setMap(val, formParameters);
	}

	public void setQueryParameters(String val) {
		setMap(val, queryParameters);
	}

	public void setHeaders(String val) {
		setMap(val, headers);
	}

	public void setParameters(String val) {
		setMap(val, parameters);
	}

	private void setMap(String val, Map<String, Object> map) {
		if (StringUtil.isNotBlank(val)) {
			map.putAll(JSONUtil.toMap(val));
		}
	}

	private void updateKey(Map<String, Object> map, String oldKey, String newKey) {
		if (map.containsKey(oldKey)) {
			Object value = map.remove(oldKey);
			map.put(newKey, value);
		}
	}

	private void removeNulls(Map<String, Object> map) {
		// clear null values
		Iterator<Entry<String, Object>> itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Object> entry = itr.next();
			if (entry.getValue() == null ){
				//keep blank values as that may be intention to test
				itr.remove();
			}
		}
	}

	public static void main(String[] args) {
		getBundle().setProperty("env.baseurl", "http://httpbin.org");
		getBundle().setProperty("get.sample.ref",
				"{'headers':{},'endPoint':'/post','baseUrl':'${env.baseurl}','method':'POST','query-parameters':{'param1':'${val1}','param2':'${val2}'},'form-parameters':{'a':'b','i':'${i}','j':'${j}','k':10.01},'body':'','parameters':{'val1':'abc','val2':'xyz','i':10,'j':20}}");

		getBundle().setProperty("get.sample.call",
				"{'reference':'get.sample.ref','parameters':{'val1':'','val3':'xyz123','i':20,'j':''},'body':'fileabcd123'}");
		WsRequestBean r = new WsRequestBean();
		r.fillData("get.sample.call");

		System.out.println(r);

		r.resolveParameters(null);
		System.out.println(r);
		
		//System.out.println(WsStep.userRequests(r).getEntity(String.class));
	}
	
	public String toString() {
		return JSONUtil.toString(this);
	}
}
