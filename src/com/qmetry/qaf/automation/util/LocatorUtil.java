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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.openqa.selenium.By;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.webdriver.ByAny;
import com.qmetry.qaf.automation.ui.webdriver.ByCustom;
import com.qmetry.qaf.automation.ui.webdriver.ByExtCompQuery;
import com.qmetry.qaf.automation.ui.webdriver.ByExtDomQuery;
import com.qmetry.qaf.automation.ui.webdriver.ByJQuery;

public class LocatorUtil {

	public static String getXPathLoc(String id) {
		return "//*[@id='" + id + "']";
	}

	public static String getCssLoc(String id) {
		return "css=*#" + id;
	}

	public static String getDescription(String locator) {
		if (JSONUtil.isValidJsonString(locator)) {
			try {
				Map<String, Object> map = JSONUtil.toMap(locator);
				return map.containsKey("desc") ? (String) map.get("desc")
						: map.containsKey("description") ? (String) map.get("description")
								: (String) map.get("locator");
			} catch (JSONException e) {
			}
		}
		return locator;
	}

	public static By getBy(String loc) {
		return getBy(loc, ConfigurationManager.getBundle());
	}

	public static By getBy(String loc, PropertyUtil props) {
		Gson gson = new Gson();
		loc = props.getSubstitutor().replace(loc);
		loc = props.getString(loc, loc);
		JsonElement element = JSONUtil.getGsonElement(loc);
		if ((null != element) && element.isJsonObject()) {
			Object obj = gson.fromJson(element, Map.class).get("locator");

			loc = obj instanceof String ? (String) obj :

					gson.toJson(obj);
		}
		element = JSONUtil.getGsonElement(loc);
		if ((null != element) && element.isJsonArray()) {
			String[] locs = new Gson().fromJson(element, String[].class);
			return new ByAny(locs);
		}
		if (loc.startsWith("//")) {
			return By.xpath(loc);
		} else if (loc.indexOf("=") > 0) {
			String parts[] = loc.split("=", 2);
			if (parts[0].equalsIgnoreCase("key") || parts[0].equalsIgnoreCase("property")) {
				String val = props.getSubstitutor().replace(parts[1]);
				return getBy(props.getString(val, val), props);
			}
			if (parts[0].equalsIgnoreCase("jquery")) {
				return new ByJQuery(parts[1]);
			}
			if (parts[0].equalsIgnoreCase("extDom")) {
				return new ByExtDomQuery(parts[1]);
			}
			if (parts[0].equalsIgnoreCase("extComp")) {
				return new ByExtCompQuery(parts[1]);
			}
			if (parts[0].equalsIgnoreCase("name")) {
				return By.name(parts[1]);
			} else if (parts[0].equalsIgnoreCase("id")) {
				return By.id(parts[1]);
			} else if (parts[0].equalsIgnoreCase("xpath")) {
				return By.xpath(parts[1]);
			} else if (parts[0].equalsIgnoreCase("css")) {
				return By.cssSelector(parts[1]);
			} else if (parts[0].equalsIgnoreCase("link") || parts[0].equalsIgnoreCase("linkText")) {
				return By.linkText(parts[1]);
			} else if (parts[0].equalsIgnoreCase("partialLink") || parts[0].equalsIgnoreCase("partialLinkText")) {
				return By.partialLinkText(parts[1]);
			} else if (parts[0].equalsIgnoreCase("className")) {
				return By.className(parts[1]);
			} else if (parts[0].equalsIgnoreCase("tagName")) {
				return By.tagName(parts[1]);
			} else {
				return new ByCustom(parts[0], parts[1]);
			}
		} else {
			return By.xpath(String.format("//*[@name='%s' or @id='%s' or @value='%s']", loc, loc, loc));
		}
	}

	private static String parseParameters(String str) {

		try {
			Pattern p = Pattern.compile("\\$\\{([^}]+)\\}", Pattern.MULTILINE);
			Matcher m = p.matcher(str);

			while (m.find()) {
				String param = m.group(1);
				if (ConfigurationManager.getBundle().containsKey(param)) {
					str = str.replaceAll("\\$\\{" + param + "\\}", ConfigurationManager.getBundle().getString(param));
				}
			}
		} catch (Exception e) {
			System.err.println("Unable to parse: " + str);
			e.printStackTrace();
		}

		return str;

	}

}
