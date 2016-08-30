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


package com.qmetry.qaf.automation.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.qmetry.qaf.automation.core.AutomationError;

/**
 * com.qmetry.qaf.automation.util.JSONUtil.java
 * 
 * @author chirag.jayswal
 */
public class JSONUtil {
	private static final Log logger = LogFactoryImpl.getLog(JSONUtil.class);

	public static Map<String, Object> toMap(String json) throws JSONException {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = new Gson().fromJson(json, Map.class);
		return map;
	}

	public static boolean isValidJsonString(String str) {
		try {
			new JSONObject(str);
			return true;
		} catch (JSONException e) {
			return false;
		}

	}

	/**
	 * This is specifically to work with GSON. for example even "some string" is
	 * not valid json but gson can considers as valid json
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidGsonString(String str) {
		try {
			new JsonParser().parse(str);
			return true;
		} catch (JsonParseException e) {
			return false;
		}

	}

	/**
	 * @param obj
	 * @return
	 */
	static Map<String, Object> toMap(JSONObject obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj != null) {
			Iterator<?> iter = obj.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				try {
					map.put(key, obj.get(key));
				} catch (JSONException e) {

				}
			}
		}
		return map;
	}

	/**
	 * @param str
	 * @return JsonElement or null if not a valid json
	 */
	public static JsonElement getGsonElement(String str) {
		try {
			return new JsonParser().parse(str);
		} catch (JsonParseException e) {
			return null;
		}
	}

	public static <T> void writeJsonObjectToFile(final String file, final T obj) {

		File f = new File(file);
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			String jsonStr = gson.toJson(obj, obj.getClass());

			FileUtil.writeStringToFile(f, jsonStr, "UTF-8");
		} catch (Throwable e) {
			System.err.println("Unable to write : " + obj.getClass().getCanonicalName() + " in file: " + file + " :"
					+ e.getMessage());
			logger.error("Unable to write : " + obj.getClass().getCanonicalName() + " in file: " + file + " :"
					+ e.getMessage());
		}
	}

	public static Object[][] getJsonArrayOfMaps(String file) {
		try {
			Gson gson = new Gson();
			final Type DATASET_TYPE = new TypeToken<List<Map<String, Object>>>() {
				private static final long serialVersionUID = 4426388930719377223L;
			}.getType();

			String jsonStr = FileUtil.readFileToString(new File(file));
			List<Map<String, Object>[]> mapData = gson.fromJson(jsonStr, DATASET_TYPE);
			Object[][] objecttoreturn = new Object[mapData.size()][1];
			for (int i = 0; i < mapData.size(); i++) {
				objecttoreturn[i][0] = mapData.get(i);
			}
			return objecttoreturn;// mapData.toArray(new Object[][]{});

		} catch (Throwable e) {
			throw new AutomationError(e);
		}
	}

	public static <T> T getJsonObjectFromFile(final String file, final Class<T> cls) {

		File f = new File(file);
		String json;
		String defVal = List.class.isAssignableFrom(cls) || cls.isArray() ? "[]"
				: ClassUtil.isPrimitiveOrWrapperType(cls) ? "" : "{}";
		try {
			json = FileUtil.readFileToString(f, "UTF-8");
		} catch (IOException e) {
			json = defVal;
		} catch (Throwable e) {
			logger.error("unable to load [" + cls.getName() + "] from file[ " + file + "] - " + e.getMessage());
			json = defVal;
		}
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, cls);

	}

}
