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
package com.qmetry.qaf.automation.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.script.ScriptException;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.hamcrest.Matchers;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.JavaStepFinder;
import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.step.client.ScenarioFactory;
import com.qmetry.qaf.automation.step.client.csv.KwdTestFactory;
import com.qmetry.qaf.automation.step.client.excel.ExcelTestFactory;
import com.qmetry.qaf.automation.step.client.text.BDDTestFactory;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Configuration manager class. Singleton with early initialization.
 * <p>
 * This class loads file provided by system property
 * <code>application.properties.file</code> (Default value is
 * "resources/application.properties"). Also loads all property files form
 * <code>test.props.dir</code>(default value is "resources") if
 * <code>resources.load.subdirs</code> flag is 1.
 * <p>
 * To access any property value within automation, use following way
 * {@link PropertyUtil} props={@link #ConfigurationManager}.
 * {@link #getInstance()}.{@link#getApplicationProperties()};<br>
 * String sval = props.{@link PropertyUtil#getPropertyValue(String)}
 * 
 * @author chirag
 */
public class ConfigurationManager {
	// early initialization
	static final Log log = LogFactoryImpl.getLog(ConfigurationManager.class);
	private static final ConfigurationManager INSTANCE = new ConfigurationManager();
	private static final ServiceLoader<QAFConfigurationListener> CONFIG_LISTENERS = ServiceLoader
			.load(QAFConfigurationListener.class);

	/**
	 * Private constructor, prevents instantiation from other classes
	 */
	private ConfigurationManager() {
		AbstractConfiguration.setDefaultListDelimiter(';');
		registerLookups();
		setHostName();
	}

	private void setHostName() {
		try {
			System.setProperty("host.name",
					InetAddress.getLocalHost().getHostName());
		} catch (Exception | Error e) {
			// This code added for MAC to fetch hostname
			InputStream stream=null;
			Scanner s = null;
			try {
				Process proc = Runtime.getRuntime().exec("hostname");
				stream = proc.getInputStream();
				if (stream != null) {
					s = new Scanner(stream);
					s.useDelimiter("\\A");
					String val = s.hasNext() ? s.next() : "";
					stream.close();
					s.close();
					System.setProperty("host.name",val);
				}
			} catch (Exception | Error e1) {
				log.trace(e1);
			}finally {
				try {
					if(null!=s)s.close();
					if(null!=stream)stream.close();
				} catch (Exception e2) {
					log.trace(e2);
				}
			}
		}
	}

	private void registerLookups(){
		ConfigurationInterpolator.registerGlobalLookup("rnd", new StrLookup() {
			public String lookup(String var) {
				var = var.replace("<%", "${").replace("%>", "}");
				var = getBundle().getSubstitutor().replace(var);
				return StringUtil.getRandomString(var);
			}
		});
		ConfigurationInterpolator.registerGlobalLookup("expr", new StrLookup() {
			public String lookup(String var) {
				try {
					var = var.replace("<%", "${").replace("%>", "}");
					var = getBundle().getSubstitutor().replace(var);
					Object res = StringUtil.eval(var);
					return String.valueOf(res);
				} catch (ScriptException e) {
					throw new RuntimeException("Unable to evaluate expression: " + var, e);
				}
			}
		});
	}
	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	private static InheritableThreadLocal<PropertyUtil> LocalProps =
			new InheritableThreadLocal<PropertyUtil>() {
				@Override
				protected PropertyUtil initialValue() {
					PropertyUtil p = new PropertyUtil(
							System.getProperty("application.properties.file",
									"resources/application.properties"));
					p.setProperty("isfw.build.info", getBuildInfo());
					p.setEncoding(p.getString(ApplicationProperties.LOCALE_CHAR_ENCODING.key, "UTF-8"));
					p.setProperty("execution.start.ts", System.currentTimeMillis());

					File prjDir = new File(".").getAbsoluteFile().getParentFile();
					p.setProperty("project.path", prjDir.getAbsolutePath());
					if(!p.containsKey("project.name"))
					p.setProperty("project.name", prjDir.getName());

					log.info("ISFW build info: " + p.getProperty("isfw.build.info"));
					String[] resources = p.getStringArray("env.resources", "resources");
					for (String resource : resources) {
						p.addBundle(resource);
					}
					//p.setProperty("execute.initialValuelisteners", true);
					executeOnLoadListeners(p);
					
					ConfigurationListener cl = new PropertyConfigurationListener();
					p.addConfigurationListener(cl);
					
					return p;
				}

				@Override
				protected PropertyUtil childValue(PropertyUtil parentValue) {
					PropertyUtil cp = new PropertyUtil(parentValue);
					ConfigurationListener cl = new PropertyConfigurationListener();
					cp.addConfigurationListener(cl);
					
					return cp;
				}

			};

	/**
	 * To add local resources.
	 * 
	 * @param fileOrDir
	 */
	public static void addBundle(String fileOrDir) {
		getBundle().addBundle(fileOrDir);
	}

	public static void addAll(Map<String, ?> props) {
		ConfigurationManager.getBundle().addAll(props);
	}

	public static PropertyUtil getBundle() {
		return ConfigurationManager.LocalProps.get();
	}

	public static void setBundle(PropertyUtil bundle) {
		LocalProps.set(bundle);
	}

	private static Map<String, String> getBuildInfo() {
		Manifest manifest = null;
		Map<String, String> buildInfo = new HashMap<String, String>();
		JarFile jar = null;
		try {
			URL url = ConfigurationManager.class.getProtectionDomain().getCodeSource()
					.getLocation();
			File file = new File(url.toURI());
			jar = new JarFile(file);
			manifest = jar.getManifest();
		} catch (NullPointerException ignored) {
		} catch (URISyntaxException ignored) {
		} catch (IOException ignored) {
		} catch (IllegalArgumentException ignored) {
		} finally {
			if (null != jar)
				try {
					jar.close();
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
		}

		if (manifest == null) {
			return buildInfo;
		}

		try {
			Attributes attributes = manifest.getAttributes("Build-Info");
			Set<Entry<Object, Object>> entries = attributes.entrySet();
			for (Entry<Object, Object> e : entries) {
				buildInfo.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
			}
		} catch (NullPointerException e) {
			// Fall through
		}

		return buildInfo;
	}

	/**
	 * Get test-step mapping for current configuration
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, TestStep> getStepMapping() {
		if (!ConfigurationManager.getBundle().containsKey("teststep.mapping")) {
			ConfigurationManager.getBundle().setProperty("teststep.mapping",
					JavaStepFinder.getAllJavaSteps());
			if (ConfigurationManager.getBundle()
					.containsKey(ApplicationProperties.STEP_PROVIDER_PKG.key)) {
				for (String pkg : ConfigurationManager.getBundle()
						.getStringArray(ApplicationProperties.STEP_PROVIDER_PKG.key)) {
					for (ScenarioFactory factory : getStepFactories()) {
						factory.process(pkg.replaceAll("\\.", "/"));
					}
				}
			}
		}
		return (Map<String, TestStep>) ConfigurationManager.getBundle()
				.getObject("teststep.mapping");
	}

	private static ScenarioFactory[] getStepFactories() {
		return new ScenarioFactory[]{new BDDTestFactory(Arrays.asList("bdl")),
				new KwdTestFactory(Arrays.asList("kwl")), new ExcelTestFactory()};
	}

	private static void executeOnLoadListeners(PropertyUtil bundle) {
		String[] listners = bundle.getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		Iterator<QAFConfigurationListener> iter = CONFIG_LISTENERS.iterator();
		while (iter.hasNext()) {
			iter.next().onLoad(bundle);
		}
		for (String listener : listners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).newInstance();
				if (QAFConfigurationListener.class.isAssignableFrom(cls.getClass()))
					((QAFConfigurationListener) cls).onLoad(bundle);
			} catch (Exception e) {
				log.error("Unable to invoke onLoad(PropertyUtil) from " + listener, e);
			}
		}
	}
	private static void executeOnChangeListeners() {
		String[] listners = getBundle().getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		Iterator<QAFConfigurationListener> iter = CONFIG_LISTENERS.iterator();
		while (iter.hasNext()) {
			iter.next().onChange();
		}
		for (String listener : listners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).newInstance();
				if (QAFConfigurationListener.class.isAssignableFrom(cls.getClass()))
					((QAFConfigurationListener)cls).onChange();
			} catch (Exception e) {
				log.error("Unable to invoke onChange() from " + listener, e);
			}
		}
	}
	

	private static class PropertyConfigurationListener implements ConfigurationListener {
		String oldValue;

		@SuppressWarnings("unchecked")
		@Override
		public void configurationChanged(ConfigurationEvent event) {

			if ((event.getType() == AbstractConfiguration.EVENT_CLEAR_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY)
					&& event.isBeforeUpdate()) {
				oldValue = String.format("%s",
						getBundle().getObject(event.getPropertyName()));
			}

			if ((event.getType() == AbstractConfiguration.EVENT_ADD_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY)
					&& !event.isBeforeUpdate()) {
				String key = event.getPropertyName();
				Object value = event.getPropertyValue();
				if (null != oldValue && Matchers.equalTo(oldValue).matches(value)) {
					// do nothing
					return;
				}

				// driver reset
				if (key.equalsIgnoreCase(ApplicationProperties.DRIVER_NAME.key)
						// single capability or set of capabilities change
						|| StringMatcher.containsIgnoringCase(".capabilit").match(key)
						|| key.equalsIgnoreCase(ApplicationProperties.REMOTE_SERVER.key)
						|| key.equalsIgnoreCase(ApplicationProperties.REMOTE_PORT.key)) {
					TestBaseProvider.instance().get().tearDown();
					if(key.equalsIgnoreCase(ApplicationProperties.DRIVER_NAME.key)){
						TestBaseProvider.instance().get().setDriver((String)value);
					}
				}
				String[] bundles = null;
				// Resource loading
				if (key.equalsIgnoreCase("env.resources")) {

					if (event.getPropertyValue() instanceof ArrayList<?>) {
						ArrayList<String> bundlesArray =
								((ArrayList<String>) event.getPropertyValue());
						bundles = bundlesArray.toArray(new String[bundlesArray.size()]);
					} else {
						String resourcesBundle = (String) value;
						if (StringUtil.isNotBlank(resourcesBundle))
							bundles = resourcesBundle.split(String
									.valueOf(PropertyUtil.getDefaultListDelimiter()));
					}
					if (null != bundles && bundles.length > 0) {
						for (String res : bundles) {
							log.info("Adding resources from: " + res);
							ConfigurationManager.addBundle(res);
						}
						executeOnChangeListeners();
					}
				}
				// Locale loading
				if (key.equalsIgnoreCase(ApplicationProperties.DEFAULT_LOCALE.key)) {
					String[] resources =
							getBundle().getStringArray("env.resources", "resources");
					for (String resource : resources) {
						String fileOrDir = getBundle().getSubstitutor().replace(resource);
						getBundle().addLocal((String) event.getPropertyValue(),
								fileOrDir);
					}
					executeOnChangeListeners();
				}
				// step provider package re-load
				if (key.equalsIgnoreCase(ApplicationProperties.STEP_PROVIDER_PKG.key)) {

					// has loaded steps and adding more or override java
					// steps....
					// for example suite level parameter has common steps and
					// test level parameter has test specific steps
					if (ConfigurationManager.getBundle()
							.containsKey("teststep.mapping")) {
						ConfigurationManager.getStepMapping()
								.putAll(JavaStepFinder.getAllJavaSteps());

						for (ScenarioFactory factory : getStepFactories()) {

							if (event.getPropertyValue() instanceof ArrayList<?>) {
								ArrayList<String> bundlesArray =
										((ArrayList<String>) event.getPropertyValue());
								bundles = bundlesArray
										.toArray(new String[bundlesArray.size()]);
								for (String pkg : bundlesArray) {
									factory.process(pkg.replaceAll("\\.", "/"));
								}
							} else {
								String resourcesBundle = (String) value;
								if (StringUtil.isNotBlank(resourcesBundle)) {
									factory.process(
											resourcesBundle.replaceAll("\\.", "/"));
								}
							}
						}
					}
				}
			}

		}
	}

}
