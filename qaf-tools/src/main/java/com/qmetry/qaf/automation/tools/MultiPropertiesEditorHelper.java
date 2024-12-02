package com.qmetry.qaf.automation.tools;

import static com.qmetry.qaf.automation.util.FileUtil.checkCreateDir;
import static com.qmetry.qaf.automation.util.FileUtil.getExtention;
import static com.qmetry.qaf.automation.util.FileUtil.isLocale;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringEscapeUtils;

import com.google.common.io.Files;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.StringUtil;

public class MultiPropertiesEditorHelper {
	
	/**
	 * Example return value:
	 * <pre>
	 * <code>
	 * [
	 * {"key":"some.prop","en_US":"valForUS","fr_FR":"valInFR"},
	 * {"key":"another.prop","en_US":"val2ForUS","fr_FR":"val2InFR"},
	 * ...
	 * ]
	 * </code></pre>
	 * 
	 * @param filePath
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static Collection<Map<String, Object>> getContent(String filePath) {
		File[] files = getFiles(filePath);
		Map<String, Map<String, Object>> entries = new LinkedHashMap<String, Map<String,Object>>();
		
		for(File file: files) {
			try {
				PropertiesConfiguration p = new PropertiesConfiguration();
				p.setEncoding(
						ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal(StandardCharsets.UTF_8.toString()));
				p.load(file);
				
				String colName = getExtention(file.getName());
				p.getKeys().forEachRemaining((k)->{
					Map<String, Object> entry = entries.get(k);
					if(null == entry) {
						entry = new LinkedHashMap<String, Object>();
						entry.put("key", (String)k);
						entries.put((String)k, entry);
					}
					String val = StringEscapeUtils.escapeHtml(p.getString((String)k));
					entry.put(colName, val);
				});
				//add blank row
				Map<String, Object> entry = entries.get("");
				if(null == entry) {
					entry = new LinkedHashMap<String, Object>();
					entry.put("key", "");
					entries.put("", entry);
				}
				entry.put(colName, "");
			} catch (Exception e) {
				System.err.println("Unable to read " + file + e.getMessage());
			} 
		}
		
		return entries.values();
	}
	
	public static void saveContent(Collection<Map<String, Object>> data, String path) {
		File file = new File(path);
		String name = Files.getNameWithoutExtension(file.getName());
		String parent = file.getParent();
		saveContent(data, name, parent);
	}

	private static void saveContent(Collection<Map<String, Object>> data, String name, String parent) {
		Map<String, PropertiesConfiguration> properties = new HashMap<String, PropertiesConfiguration>();		
		data.iterator().forEachRemaining(entry -> {
			String key = (String) entry.remove("key");
			if (StringUtil.isNotBlank(key)) {
				entry.entrySet().forEach(colEntry -> {
					String colName = colEntry.getKey();
					PropertiesConfiguration p = properties.get(colName);
					if (null == p) {
						try {
							checkCreateDir(parent);
							File target = new File(parent, String.join(".", name, colName));
							target.createNewFile();
							p = new PropertiesConfiguration();
							p.setEncoding(
									ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal(StandardCharsets.UTF_8.toString()));				
							p.load(target);
							p.setFile(target);
							properties.put(colName, p);
						} catch (IOException | ConfigurationException e) {
							throw new RuntimeException(e);
						}

					}
					p.setProperty(key, colEntry.getValue());
				});
			}
		});
		
		properties.values().forEach(p->{
			try {
				String encoding = ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal();
				if(StringUtil.isNotBlank(encoding)) {
					StringWriter sw = new StringWriter();
					p.save(sw);
					try(Writer out = new FileWriter(p.getFile(), false)){
					String tranStr = StringEscapeUtils.unescapeJava( sw.toString());
					//StringEscapeUtils.unescapeJava(out , sw.toString());
					out.write(tranStr);
					out.flush();
					}
				}else {
					//save unicode;
					p.save();
				}
			} catch (ConfigurationException | IOException e2) {
				throw new RuntimeException(e2);
			}
		});

	}
	
	public static File[] getFiles(String name, boolean locale) {
		if(!locale || !isLocale(name) ) return new File[] {};
		
		File f = new File(name);
		return f.getParentFile().listFiles((dir,fname)->{
			return (!locale || isLocale(fname)) && Files.getNameWithoutExtension(fname).equalsIgnoreCase(Files.getNameWithoutExtension(name));
		});
	}
	
	public static File[] getFiles(String name) {
		return getFiles(name, isLocale(name));
	}
}
