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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.SkipException;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.testng.RetryAnalyzer;
import com.qmetry.qaf.automation.testng.dataprovider.DataProviderUtil;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;
import com.qmetry.qaf.automation.util.Randomizer;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.data.BaseDataBean.java
 * 
 * @author chirag
 */
public abstract class BaseDataBean implements DataBean {
	transient protected final Log logger = LogFactory.getLog(getClass());

	public String toCSV() {
		StringBuffer sb = new StringBuffer();
		Field[] flds = this.getClass().getDeclaredFields();
		for (Field fld : flds) {
			try {
				fld.setAccessible(true);
				if (fld.getDeclaringClass().equals(this.getClass()) && (fld.get(this) != null)) {
					sb.append(fld.get(this).toString());
				}
				sb.append(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public void fillData(Object obj) {
		if (obj instanceof Map) {
			fillData((Map<String, Object>) obj);
			return;
		}
		if (obj instanceof String) {
			String str = String.valueOf(obj);
			if (JSONUtil.isValidJsonString(str)) {
				fillFromJsonString(str);
				return;
			}
			if (str.startsWith("select")) {
				fillDataFromDB(str);
				return;
			}
			if (!getBundle().subset(str).isEmpty()) {
				fillFromConfig(str);
				return;
			}

		}
		throw new SkipException(
				"Unable to fill data with unknown object. It must be either Map or String: valid json / property key / sql statement."
						+ obj);

	}

	/**
	 * returns all fields including from super class
	 */
	protected Field[] getFields() {
		return ClassUtil.getAllFields(this.getClass(), BaseDataBean.class);
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
			for (String key : keys) {
				fillData(key, jsonObject.optString(key));
			}
		} catch (JSONException e) {
			logger.error(e);
		}

	}

	/**
	 * Can be used with xml configuration file or properties file
	 * 
	 * @param datakey
	 */
	public void fillFromConfig(String datakey) {
		List<Object[]> set = DataProviderUtil.getDataSetAsMap(datakey,"");
		if (set.isEmpty()) {
			return;
		}
		int index = 0;
		if (set.size() > 1) {
			if (ApplicationProperties.BEAN_POPULATE_RANDOM.getBoolenVal(false)) {
				// get random index from 0 to size-1.
				index = RandomUtils.nextInt(set.size());
			} else {
				// get next index, if index exceeds size then start with 0.
				int cindex = getBundle().getInt(RetryAnalyzer.RETRY_INVOCATION_COUNT, 0);
				index = cindex % set.size();
			}
		}
		fillData(set.get(index)[0]);

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Field[] flds = getFields();
		for (Field fld : flds) {
			try {
				fld.setAccessible(true);
				if ((fld.get(this) != null)) {
					sb.append(fld.getName() + " : " + fld.get(this).toString());
					sb.append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		if (sb.length() > 0) {
			sb.append("]");
		}

		return sb.toString();
	}

	public String toCSV(String csvNames) {
		if ((csvNames == null) || csvNames.equalsIgnoreCase("all") || csvNames.equalsIgnoreCase("*")) {
			return toCSV();
		}
		StringBuffer sb = new StringBuffer();
		StringTokenizer fldNames = new StringTokenizer(csvNames, ",");

		while (fldNames.hasMoreTokens()) {
			try {
				Field fld = this.getClass().getDeclaredField(fldNames.nextToken());
				fld.setAccessible(true);
				if (fld.get(this) != null) {
					sb.append(fld.get(this).toString());
				}
				sb.append(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public String getCSVLabel() {
		StringBuffer sb = new StringBuffer();
		Field[] flds = this.getClass().getDeclaredFields();
		for (Field fld : flds) {
			try {
				if (fld.getDeclaringClass().equals(this.getClass())) {
					sb.append(fld.getName());
					sb.append(",");
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return sb.toString();
	}

	/**
	 * @param map
	 *            bean property name as key and value to set as value. For
	 *            example if bean property is "firstName" map should contain
	 *            entry with key "FIRSTNAME" (case insensitive)
	 */
	public void fillData(Map<String, Object> map) {
		for (String key : map.keySet()) {
			fillData(key, String.valueOf(map.get(key)));
		}
	}

	/**
	 * Column names in record returned by query must match bean property names.
	 * If query returns multiple records then bean will filled with first
	 * record.
	 * 
	 * @param query
	 *            for example
	 *            <code>select col1 as beanprop1, col2 as beanprop2 ...
	 *            from table</code>
	 */
	@SuppressWarnings("unchecked")
	public void fillDataFromDB(String query) {
		fillData(((Map<String, Object>) DatabaseUtil.getRecordDataAsMap(query)[0][0]));
	}

	/**
	 * @param fieldName
	 *            case insensitive field name
	 * @param value
	 */
	public void fillData(String fieldName, String value) {
		Field[] fields = getFields();// this.getClass().getDeclaredFields();
		for (Field field2 : fields) {
			Field field = field2;
			if (field.getName().equalsIgnoreCase(fieldName)) {
				if (!(Modifier.isFinal(field.getModifiers()))) {
					setField(field, value);
				}
				return;
			}
		}
	}

	/**
	 * This will fill random data except those properties which has skip=true in
	 * {@link Randomizer} annotation. Use {@link Randomizer} annotation to
	 * specify data value to be generated for specific property.
	 * 
	 * @see Randomizer
	 */
	public void fillRandomData() {
		Field[] fields = getFields();
		for (Field field : fields) {
			logger.debug("NAME :: " + field.getName());
			if (!(Modifier.isFinal(field.getModifiers()))) {
				RandomizerTypes type = RandomizerTypes.MIXED;
				int len = 10;
				long min = 0, max = 0;
				String prefix = "", suffix = "";
				String format = "";
				String[] list = {};

				Randomizer randomizer = field.getAnnotation(Randomizer.class);

				if (randomizer != null) {
					if (randomizer.skip()) {
						continue;
					}
					type = field.getType() == Date.class ? RandomizerTypes.DIGITS_ONLY : randomizer.type();
					len = randomizer.length();
					prefix = randomizer.prefix();
					suffix = randomizer.suffix();
					min = randomizer.minval();
					max = min > randomizer.maxval() ? min : randomizer.maxval();
					format = randomizer.format();
					list = randomizer.dataset();
				} else {
					// @Since 2.1.2 randomizer annotation is must for random
					// value
					// generation
					continue;
				}

				String str = "";
				if ((list == null) || (list.length == 0)) {
					str = StringUtil.isBlank(format) ? RandomStringUtils.random(len,
							!type.equals(RandomizerTypes.DIGITS_ONLY), !type.equals(RandomizerTypes.LETTERS_ONLY))
							: StringUtil.getRandomString(format);
				} else {
					str = getRandomValue(list);
				}

				try {
					// deal with IllegalAccessException
					field.setAccessible(true);
					Method setter = null;
					try {
						setter = this.getClass().getMethod("set" + StringUtil.getTitleCase(field.getName()),
								String.class);
					} catch (Exception e) {

					}

					if ((field.getType() == String.class) || (null != setter)) {
						if ((list == null) || (list.length == 0)) {
							if ((min == max) && (min == 0)) {
								str = StringUtil.isBlank(format)
										? RandomStringUtils.random(len, !type.equals(RandomizerTypes.DIGITS_ONLY),
												!type.equals(RandomizerTypes.LETTERS_ONLY))
										: StringUtil.getRandomString(format);

							} else {
								str = String.valueOf((int) (Math.random() * ((max - min) + 1)) + min);

							}
						}
						String rStr = prefix + str + suffix;
						if (null != setter) {
							setter.setAccessible(true);
							setter.invoke(this, rStr);
						} else {
							field.set(this, rStr);
						}
					} else {
						String rStr = "";
						if ((min == max) && (min == 0)) {
							rStr = RandomStringUtils.random(len, false, true);
						} else {
							rStr = String.valueOf((int) (Math.random() * ((max - min) + 1)) + min);

						}

						if (field.getType() == Integer.TYPE) {
							field.setInt(this, Integer.parseInt(rStr));
						} else if (field.getType() == Float.TYPE) {
							field.setFloat(this, Float.parseFloat(rStr));

						} else if (field.getType() == Double.TYPE) {
							field.setDouble(this, Double.parseDouble(rStr));

						} else if (field.getType() == Long.TYPE) {
							field.setLong(this, Long.parseLong(rStr));

						} else if (field.getType() == Short.TYPE) {
							field.setShort(this, Short.parseShort(rStr));
						} else if (field.getType() == Date.class) {
							logger.info("filling date " + rStr);
							int days = Integer.parseInt(rStr);
							field.set(this, DateUtil.getDate(days));
						} else if (field.getType() == Boolean.TYPE) {
							field.setBoolean(this, RandomUtils.nextBoolean());

						}
					}
				} catch (IllegalArgumentException e) {

					logger.error("Unable to fill random data in field " + field.getName(), e);
				} catch (IllegalAccessException e) {
					logger.error("Unable to Access " + field.getName(), e);
				} catch (InvocationTargetException e) {
					logger.error("Unable to Access setter for " + field.getName(), e);

				}
			}

		}

	}

	protected void setField(Field field, String val) {
		try {
			// deal with IllegalAccessException
			field.setAccessible(true);
			if (field.getType() == String.class) {
				field.set(this, val);
			} else {
				Method setter = null;
				try {
					setter = this.getClass().getMethod("set" + StringUtil.getTitleCase(field.getName()), String.class);
				} catch (Exception e) {

				}
				if (null != setter) {
					setter.setAccessible(true);
					setter.invoke(this, val);
				} else if (field.getType() == Integer.TYPE) {
					field.setInt(this, Integer.parseInt(val));
				} else if (field.getType() == Float.TYPE) {
					field.setFloat(this, Float.parseFloat(val));

				} else if (field.getType() == Double.TYPE) {
					field.setDouble(this, Double.parseDouble(val));

				} else if (field.getType() == Long.TYPE) {
					field.setLong(this, Long.parseLong(val));

				} else if (field.getType() == Boolean.TYPE) {
					Boolean bval = StringUtils.isBlank(val) ? null
							: NumberUtils.isNumber(val) ? (Integer.parseInt(val) != 0)
									: Boolean.parseBoolean(val) || val.equalsIgnoreCase("T")
											|| val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("YES");

					field.setBoolean(this, bval);

				} else if (field.getType() == Short.TYPE) {
					field.setShort(this, Short.parseShort(val));
				} else if (field.getType() == Date.class) {
					Date dVal = null;
					try {
						dVal =

								StringUtils.isBlank(val) ? null
										: NumberUtils.isNumber(val) ? DateUtil.getDate(Integer.parseInt(val))
												: DateUtil.parseDate(val, "MM/dd/yyyy");
					} catch (ParseException e) {
						logger.error("Expected date in MM/dd/yyyy format.", e);
					}
					field.set(this, dVal);
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error("Unable to fill random data in field " + field.getName(), e);
		} catch (IllegalAccessException e) {
			logger.error("Unable to Access " + field.getName(), e);
		} catch (InvocationTargetException e) {
			logger.error("Unable to invoke setter for " + field.getName(), e);
		}

	}

	public static String getRandomValue(String... values) {
		if ((values == null) || (values.length == 0)) {
			return "";
		}
		if ((values.length == 1) && getBundle().containsKey(values[0])) {
			values = getBundle().getStringArray(values[0], values[0]);
		}
		Random rand = new Random();
		int r = rand.nextInt(values.length);

		return values[r];
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseDataBean> T deepClone() {
		try {
			// Serialization of object
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this);

			// De-serialization of object
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			T copied = (T) in.readObject();

			return copied;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
