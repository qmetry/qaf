package com.qmetry.qaf.automation.tools;

import static com.qmetry.qaf.automation.util.FileUtil.checkCreateDir;
import static com.qmetry.qaf.automation.util.FileUtil.getExtention;
import static com.qmetry.qaf.automation.util.FileUtil.isLocale;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.common.io.Files;
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
				p.setEncoding(StandardCharsets.UTF_8.name());
				p.load(file);
				
				String colName = getExtention(file.getName());
				p.getKeys().forEachRemaining((k)->{
					Map<String, Object> entry = entries.get(k);
					if(null == entry) {
						entry = new HashMap<String, Object>();
						entry.put("key", (String)k);
						entries.put((String)k, entry);
					}
					entry.put(colName, p.getString((String)k));
				});

			} catch (Exception e) {
				System.err.println("Unable to read " + file + e.getMessage());
			} 
		}
		
		return entries.values();
	}
	
	public static void saveContent(Collection<Map<String, Object>> data, String path) {
		Path filePath = Path.of(path);
		String name = filePath.getFileName().toString();
		String parent = filePath.getParent().toString();
		saveContent(data, name, parent);
	}

	public static void saveContent(Collection<Map<String, Object>> data, String name, String parent) {
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
							p.setEncoding(StandardCharsets.UTF_8.toString());
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
				p.save();
				//p.save
			} catch (ConfigurationException e2) {
				throw new RuntimeException(e2);
			}
		});

	}
	
	public static File[] getFiles(String name, boolean locale) {
		if(!locale || !isLocale(name) ) return new File[] {};
		
		File f = new File(name);
		f.getParentFile().listFiles((dir,fname)->{
			return (!locale || isLocale(fname)) && Files.getNameWithoutExtension(fname).equalsIgnoreCase(Files.getNameWithoutExtension(name));
		});
		return null;
	}
	
	public static File[] getFiles(String name) {
		return getFiles(name, isLocale(name));
	}
	
	

	
}
