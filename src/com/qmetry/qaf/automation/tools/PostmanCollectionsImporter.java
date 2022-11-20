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
package com.qmetry.qaf.automation.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.text.StrBuilder;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Class to import postman collection and generate wsc file. It requires <code>postman.collection</code> property.
 * 
 * @author chirag.jayswal
 *
 */
@SuppressWarnings("unchecked")
public class PostmanCollectionsImporter {
	public static void main(String[] args) throws IOException {
		String file = ConfigurationManager.getBundle().getString("postman.collection");
		if (StringUtil.isNotBlank(file)) {
			importColletion(file);
		} else {
			System.err.println("provide postman collection file using \"postman.collection\" property");
		}
	}

	public static void importColletion(String file) throws IOException {
		Map<String, Object> collection = JSONUtil.getJsonObjectFromFile(file, Map.class);

		List<Map<String, Object>> items = (List<Map<String, Object>>) collection.get("item");
		String name = (String) ((Map<String, Object>) collection.get("info")).get("name");
		List<String> wsc = new ArrayList<String>();
		for (Map<String, Object> item : items) {
			recordRequests(item, wsc);
		}
		List<Map<String, Object>> variables = (List<Map<String, Object>>) collection.get("variable");
		
		Map<String, List<Map<String, Object>>> variablesByType = variables.stream().collect(Collectors.groupingBy(v->(String)v.get("type")));
		for(String type : variablesByType.keySet()) {
			PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
			//propertiesConfiguration.setEncoding(getString(ApplicationProperties.LOCALE_CHAR_ENCODING.key, "UTF-8"));
			variablesByType.get(type).forEach(m->propertiesConfiguration.setProperty((String)m.get("key"), m.get("value")));
			try {
				propertiesConfiguration.save("resources/auto_generated/"+ StringUtil.toCamelCaseIdentifier(type) + ".properties");
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
		FileUtil.writeLines(new File("resources/auto_generated", StringUtil.toCamelCaseIdentifier(name) + ".wsc"), wsc);
	}

	private static void recordRequests(Map<String, Object> item, List<String> wsc) {
		if (item.containsKey("request")) {
			// record request

			String name = ((String) item.get("name")).replace('/', '.').replace(':', '_').replace(" ", "_");
			if (name.charAt(0) == '.')
				name = name.substring(1);

			Map<String, Object> reqcall = new HashMap<String, Object>();

			Object reqObj = item.get("request");
			if (reqObj instanceof Map) {
				Map<String, Object> req = (Map<String, Object>) reqObj;
				reqcall.put("method", req.get("method"));
				reqcall.put("headers", toMap((List<Map<String, Object>>) req.get("header")));
				parseUrl(reqcall, req.get("url"));
				addAuthHeader(reqcall, (Map<String, Object>) req.get("auth"));
				addBody(reqcall, req.get("body"));
				name = name + "." + req.get("method");
			} else {
				parseUrl(reqcall, reqObj);
			}

			wsc.add(name.toLowerCase() + "=" + JSONUtil.toString(reqcall).replace("\\", "\\\\"));

		} else {
			List<Map<String, Object>> items = (List<Map<String, Object>>) item.get("item");
			for (Map<String, Object> _item : items) {
				recordRequests(_item, wsc);
			}
		}
	}

	private static void addBody(Map<String, Object> reqcall, Object body) {
		if (null != body) {
			if (body instanceof Map) {
				Map<String, Object> map = ((Map<String, Object>) body);
				if (map.containsKey("mode")) {
					switch ((String) map.get("mode")) {
					case "formdata":
						reqcall.put("form-parameters", toMap((List<Map<String, Object>>) map.get("formdata")));
						break;
					case "urlencoded":
						reqcall.put("form-parameters", toMap((List<Map<String, Object>>) map.get("urlencoded")));
						break;
					case "raw":
						reqcall.put("body", map.get("raw"));
						break;
					}
				}else if(!map.isEmpty()) {
					reqcall.put("body", JSONUtil.toString(map));
				}
			} else {
				reqcall.put("body", body);
			}
		}
	}

	private static void addAuthHeader(Map<String, Object> reqcall, Map<String, Object> auth) {
		if (null != auth) {
			// Authorization
			if("bearer".equalsIgnoreCase((String)auth.get("type"))){
				((Map<String, Object>)reqcall.get("headers")).put("Authorization", "Bearer <token>");
			}
		}
	}

	private static void parseUrl(Map<String, Object> reqcall, Object urlObj) {
		if (urlObj instanceof Map) {
			Map<String, Object> url = (Map<String, Object>) urlObj;
			String baseUrl = ((List<String>) url.get("host")).get(0).replace("{{", "${").replace("}}", "}");
			reqcall.put("baseurl", baseUrl);
			reqcall.put("endpoint", getUrl(url.get("path")));
			reqcall.put("query-parameters", toMap((List<Map<String, Object>>) url.get("query")));
		} else {
			reqcall.put("baseurl", getUrl(urlObj));
		}
	}

	private static String getUrl(Object pathObj) {
		List<String> path = (pathObj instanceof List) ? (List<String>) pathObj
				: Arrays.asList(pathObj.toString().split("/"));
		if (null == path || path.size() < 1) {
			return null;
		}
		Iterator<String> iterator = path.iterator();
		if (!iterator.hasNext()) {
			return "";
		}
		String first = iterator.next();
		if (!iterator.hasNext()) {
			return first;
		}

		// two or more elements
		StrBuilder buf = new StrBuilder(first);

		while (iterator.hasNext()) {
			buf.append("/");
			String pathEntry = iterator.next();
			if (pathEntry != null) {
				if (pathEntry.length() > 0 && pathEntry.charAt(0) == ':') {
					buf.append("${");
					buf.append(pathEntry.substring(1));
					buf.append("}");
				} else {
					buf.append(pathEntry.replace("{{", "${").replace("}}", "}"));
				}
			}
		}

		return buf.toString();

	}

	private static Map<String, Object> toMap(List<Map<String, Object>> listOfKvMap) {
		if (null == listOfKvMap) {
			return new HashMap<>();
		}
		return listOfKvMap.stream()
				.collect(Collectors.toMap(s -> (String) s.get("key"), s -> ((String) s.getOrDefault("value", "")).replace("{{", "${").replace("}}", "}")));
	}

}
