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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.text.StrSubstitutor;

import com.qmetry.qaf.automation.util.DatabaseUtil;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 *
 */
public class JsonDataBean {
	private int samples = 10;
	private Map<String, Master> master;
	private List<Field> fields;
	private List<RawCollectors> collectors;

	private transient List<Colloector> _collectors;
	private transient Map<String, Class<? extends Colloector>> colloectorByName;
	private transient PropertyUtil params;

	private JsonDataBean() {
		params = new PropertyUtil();
	}

	private void init() {
		colloectorByName = new TreeMap<String, Class<? extends Colloector>>(String.CASE_INSENSITIVE_ORDER);

		colloectorByName.put("file", FileCollector.class);
		colloectorByName.put("db", DBCollector.class);
		colloectorByName.put("database", DBCollector.class);
		colloectorByName.put("json", JsonCollector.class);
		colloectorByName.put("expr", ExprCollector.class);
		if (null != collectors) {
			collectors.stream().forEach(c -> addCollector(getColloector(c)));
		}
		setDataGenerator();
	}

	@SuppressWarnings("unchecked")
	private Colloector getColloector(RawCollectors rc) {
		Configuration collectorParams = params.subset("collectors." + rc.type);
		boolean disabled = collectorParams.getBoolean("disabled", rc.disabled);
		if (disabled) {
			return null;
		}
		Class<? extends Colloector> className = rc.className;
		if (null == className) {
			className = colloectorByName.get(rc.type);
		}
		if (null == className) {
			throw new RuntimeException("Collector class not found for type:" + rc.type
					+ ". You can provide class for custom type using className");
		}
		if (null == rc.options) {
			rc.options = new HashMap<String, Object>();
		}
		collectorParams.subset("options").getKeys().forEachRemaining(e -> {
			rc.options.put(e.toString(), collectorParams.getProperty("options." + e));
		});
		String options = JSONUtil.toString(rc.options);
		return JSONUtil.toObject(options, className);
	}

	private void setDataGenerator() {
		Map<String, Class<?>> classByName = new TreeMap<String, Class<?>>(String.CASE_INSENSITIVE_ORDER);
		;
		classByName.put("int", Integer.class);
		classByName.put("integer", Integer.class);
		classByName.put("short", Short.class);
		classByName.put("long", Long.class);
		classByName.put("float", Float.class);
		classByName.put("double", Double.class);

		for (Field field : fields) {
			Configuration fieldParams = params.subset(field.name);
			if (field.type != null) {
				field._type = classByName.get(field.type);
			}
			if (StringUtil.isNotBlank(field.reference)) {
				String[] kv = field.reference.split("\\.", 2);
				field._dataGenerator = new RefernceSelector(kv[0], kv[1]);
			} else if (null == field.dataset || field.dataset.length == 0) {
				int min = fieldParams.getInt("min", field.min);
				int max = fieldParams.getInt("max", field.max);

				if ((min == max) && (min == 0)) {

					if (StringUtil.isBlank(field.format)) {
						if (field.type != null && field.type.toUpperCase().startsWith("CAL")) {
							// calculated field
							field._dataGenerator = (m) -> {
								return "";
							};
						} else {
							field._dataGenerator = new StringGenerator(field.type,
									fieldParams.getInt("length", field.length),
									fieldParams.getString("charSet", field.charSet));
						}

					} else {
						field._dataGenerator = new FormatGenerator(field.format);
					}
				} else {
					if (field.type != null && field.type.equalsIgnoreCase("date")) {
						field._dataGenerator = new DateGenerator(min, max);
					} else {
						field._dataGenerator = new DoubleGenerator(min, max);
					}
				}
			} else {
				Object[] dataset = fieldParams.containsKey("dataset") ? fieldParams.getStringArray("dataset")
						: field.dataset;
				field._dataGenerator = new ListDataSelector(master, dataset);
			}
			if(StringUtil.isNotBlank(field.prefix) ||StringUtil.isNotBlank(field.suffix)) {
				if(StringUtil.isBlank(field.formatVal)) {
					field.formatVal="_value";
				}
				if(StringUtil.isNotBlank(field.prefix)){
					field.formatVal = "'"+field.prefix+"'+"+field.formatVal;
				}
				if (StringUtil.isNotBlank(field.suffix)){
					field.formatVal =field.formatVal + "+'"+field.suffix+"'";
				}
			}
		}

	}

	public static JsonDataBean get(String file) {
		return get(file, null);
	}

	public static JsonDataBean get(String file, Map<String, Object> params) {

		String source;
		try {
			source = FileUtil.readFileToString(new File(file), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Unable to read file.", e);
		}

		JsonDataBean bean = JSONUtil.toObject(source, JsonDataBean.class);

		if (null != params) {
			bean.params.addAll(params);
		}
		bean.init();
		return bean;
	}

	public void addCollector(Colloector colloector) {
		if (null == _collectors) {
			_collectors = new ArrayList<Colloector>();
		}
		if (null != colloector)
			_collectors.add(colloector);
	}

	public void removeAllCollectors() {
		if (null != _collectors)
			_collectors.clear();
	}

	public void setSamples(int samples) {
		this.samples = samples;
	}

	public void generateData() throws ScriptException {

		for (Colloector colloector : _collectors) {
			String[] fidNames = fields.stream().map(f -> (String) f.name).toArray(String[]::new);
			colloector.init(fidNames);
		}
		for (int i = 0; i < samples; i++) {
			Map<String, Object> record = populate();
			for (Colloector colloector : _collectors) {
				colloector.collect(record);
			}
		}
		for (Colloector colloector : _collectors) {
			colloector.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> populate() throws ScriptException {
		Map<String, Object> record = new LinkedHashMap<String, Object>();

		Map<String, Map<String, Object>> _references = new HashMap<String, Map<String, Object>>();
		record.put("_references", _references);

		for (Field field : fields) {
			Object value = field._dataGenerator.generate(record);
			if (null != field.type && field.type.charAt(0) == 'r') {// reference
				_references.put(field.name, (Map<String, Object>) value);

			} else {
				if (null != field._type) {
					value = JSONUtil.toObject(String.valueOf(value), field._type);
				}
				if (StringUtil.isNotBlank(field.formatVal)) {
					Map<String, Object> param = new LinkedHashMap<String, Object>(record);
					param.put("_value", value);

					value = StringUtil.eval(StrSubstitutor.replace(field.formatVal,param), param);
				}
				record.put(field.name, value);
			}
		}
		record.remove("_references");
		return record;
	}

	private class RefernceSelector implements RandomDataGenerator {
		String refkey;
		String prop;

		RefernceSelector(String refKey, String prop) {
			this.refkey = refKey;
			this.prop = prop;
		}

		@SuppressWarnings("unchecked")
		public Object generate(Map<String, Object> record) {
			Map<String, Map<String, Object>> references = ((Map<String, Map<String, Object>>) record
					.get("_references"));
			Object val = references.get(refkey).get(prop);
			if (val instanceof List) {
				return new ListDataSelector(master, ((List<Object>) val).toArray(new Object[] {})).generate(record);
			} else if (val instanceof String) {
				return new ListDataSelector(master, val).generate(record);
			}
			return val;
		}
	}

	private interface RandomDataGenerator {
		public Object generate(Map<String, Object> record);
	}

	private class FormatGenerator implements RandomDataGenerator {
		String format;

		FormatGenerator(String format) {
			this.format = format;
		}

		public Object generate(Map<String, Object> record) {
			return StringUtil.getRandomString(format);
		}
	}

	private class StringGenerator implements RandomDataGenerator {
		private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private static final String NUMS = "0123456789";
		int length;
		char[] charSet;

		StringGenerator(String type, int length, String charSet) {
			this.length = length > 0 ? length : 10;
			if (null == charSet || charSet.length() == 0) {
				StringBuilder set = new StringBuilder();
				if (null == type) {
					set.append(CHARS);
					set.append(CHARS.toLowerCase());
					set.append(NUMS);
				} else if (type.toUpperCase().startsWith("CHAR") || type.toUpperCase().startsWith("STR")) {
					set.append(CHARS);
					set.append(CHARS.toLowerCase());
				} else {
					set.append(NUMS);
				}
				this.charSet = set.toString().toCharArray();
			} else {
				this.charSet = charSet.toCharArray();
			}
		}

		public Object generate(Map<String, Object> record) {
			StringBuilder sb = new StringBuilder(length);
			for (int i = 0; i < length; i++) {
				int ci = ((int) (Math.random() * charSet.length));
				sb.append(charSet[ci]);
			}
			return sb.toString();
		}
	}

	private class DateGenerator extends NumberGenerator {
		DateGenerator(int min, int max) {
			super(min, max);
		}

		public Date generate(Map<String, Object> record) {
			return DateUtil.getDate(getNum().intValue());
		}
	}

	private class DoubleGenerator extends NumberGenerator {
		DoubleGenerator(int min, int max) {
			super(min, max);
		}

		public Double generate(Map<String, Object> record) {
			return getNum();
		}
	}

	private abstract class NumberGenerator implements RandomDataGenerator {
		int min;
		int max;

		NumberGenerator(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public Double getNum() {
			return ((Math.random() * ((max - min) + 1)) + min);
		}
	}

	private class ListDataSelector implements RandomDataGenerator {
		private Object[] values;
		private Map<String, Master> master;
		private boolean needToResolve;

		ListDataSelector(Map<String, Master> master, Object... values) {
			this.values = values;
			this.master=master;
			needToResolve = false;
			if (values != null && values.length == 1) {
				if (values[0].toString().indexOf("${") < 0) {

					String key = values[0].toString().replaceFirst("master.", "");
					if (null != master && master.containsKey(key)) {
						Master masterRec = master.get(key);
						checkSetMasterData(masterRec);

						this.values = masterRec.data.toArray(new Object[] {});
					} else if (getBundle().containsKey(values[0].toString())) {
						this.values = getBundle().getStringArray(values[0].toString(), values[0].toString());
					}
				} else {
					needToResolve = true;
				}
			}
		}

		public Object generate(Map<String, Object> record) {
			Object[] values = this.values.clone();
			if (needToResolve) {
				String key = StrSubstitutor.replace(values[0], record);

				key = key.replaceFirst("master.", "");
				if (null != master && master.containsKey(key)) {
					Master masterRec = master.get(key);
					checkSetMasterData(masterRec);
					values = masterRec.data.toArray(new Object[] {});
				} else if (getBundle().containsKey(key)) {
					values = getBundle().getStringArray(key, key);
				}
			}
			if ((values == null) || (values.length == 0)) {
				return "";
			}

			Random rand = new Random();
			int r = rand.nextInt(values.length);

			return values[r];
		}
	}

	@SuppressWarnings("unchecked")
	private void checkSetMasterData(Master masterRec) {
		if (masterRec.data == null && null != masterRec.metadata) {
			Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
			metadata.putAll(masterRec.metadata);
			Object[][] data = DataProviderUtil.getData(metadata);
			masterRec.data = Stream.of(data).flatMap(Arrays::stream).map(c -> (Map<String, Object>) c)
					.collect(Collectors.toList());
		}
	}

	private class Master {
		private TreeMap<String, Object> metadata;
		private List<Map<String, Object>> data;
	}

	private class Field {
		private String name;
		private String type = "char";
		private String format;
		private String prefix;
		private String suffix;
		private int min;
		private int max;
		private int length = 10;
		private Object[] dataset;
		private String formatVal;
		private String reference;
		private transient RandomDataGenerator _dataGenerator;
		private String charSet;
		private transient Class<?> _type;

	}

	private class RawCollectors {

		private String type = "file";
		private boolean disabled;
		private Class<? extends Colloector> className;
		private Map<String, Object> options;
	}

	public static interface Colloector {
		public void collect(Map<String, Object> record);

		public void init(String[] fidNames);

		public void close();

		public default List<String> getErrorMessage() {
			return Collections.emptyList();
		};
	}

	private static class ExprCollector implements Colloector {
		private String call;

		@Override
		public void collect(Map<String, Object> record) {
			Map<String, Object> param = new LinkedHashMap<String, Object>(record);
			param.put("_record", record);
			try {
				StringUtil.eval(call, param);
			} catch (ScriptException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void init(String[] fidNames) {
		}

		@Override
		public void close() {
		}

	}

	private static class FileCollector implements Colloector {

		private String file;
		private boolean append;
		private boolean headers;
		private String outputFormat;
		private String seperator = "";
		private PrintWriter out;
		private Formatter formatter;

		@Override
		public void collect(Map<String, Object> record) {
			if (null != out)
				out.println(formatter.format(record));
		}

		@Override
		public void init(String[] fieldNames) {
			if (StringUtil.isNotBlank(file)) {
				try {
					out = new PrintWriter(new FileWriter(file, append));
					if (headers) {
						out.println(StringUtil.join(fieldNames, seperator));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (StringUtil.isNotBlank(outputFormat)) {
				formatter = (data) -> {
					return new StrSubstitutor(data).replace(outputFormat);
				};
			} else {
				formatter = (data) -> {
					if (StringUtil.isBlank(seperator)) {
						return StringUtil.join(data.values(), "");
					}
					return data.values().stream().map(v -> {
						if (null == v) {
							return "";
						}
						if (v.toString().indexOf(seperator) >= 0)
							return "\"" + v + "\"";
						else
							return v.toString();
					}).collect(Collectors.joining(seperator));
				};
			}

		}

		@Override
		public void close() {
			if (null != out) {
				out.flush();
				out.close();
			}

		}
	}

	private static class DBCollector implements Colloector {
		private String sql;
		private String prefix = "";
		private Connection con;

		@Override
		public void collect(Map<String, Object> record) {
			String queryStr = new StrSubstitutor(record).replace(sql);
			if (null != con) {
				try (Statement st = con.createStatement()) {
					st.executeUpdate(queryStr);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void init(String[] fidNames) {
			try {
				con = DatabaseUtil.getConnection(prefix);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void close() {
			DatabaseUtil.close(con);
		}

	}

	public static class JsonCollector implements Colloector {
		private String file;
		private List<Map<String, Object>> dataset;

		@Override
		public void collect(Map<String, Object> record) {
			dataset.add(record);
		}

		@Override
		public void init(String[] fidNames) {
			dataset = new ArrayList<Map<String, Object>>();
		}

		@Override
		public void close() {
			if (null != file) {
				JSONUtil.writeJsonObjectToFile(file, dataset);
			}
		}

		public List<Map<String, Object>> getDataset() {
			return dataset;
		}
	}

	private interface Formatter {
		String format(Map<String, Object> data);
	}
	
	public static void main(String[] args) throws IOException, ScriptException {
		String file = getBundle().getString("schema", getBundle().getString("datagen.schema", ""));
		int samples = getBundle().getInt("samples", getBundle().getInt("datagen.samples", 10));

		if (StringUtil.isBlank(file)) {
			throw new IOException(
					"required datagen schema file. You can provide \"-Dschema=<filepath>\" or \"-Ddatagen.schema=<filepath>\"");
		}
		JsonDataBean bean = JsonDataBean.get(file);
		bean.setSamples(samples);
		bean.generateData();
	}
}