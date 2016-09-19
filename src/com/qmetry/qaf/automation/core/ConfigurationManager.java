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


package com.qmetry.qaf.automation.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
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
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringComparator;
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

	/**
	 * Private constructor, prevents instantiation from other classes
	 */
	private ConfigurationManager() {
		AbstractConfiguration.setDefaultListDelimiter(';');
	}

	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	private static InheritableThreadLocal<PropertyUtil> LocalProps = new InheritableThreadLocal<PropertyUtil>() {
		@Override
		protected PropertyUtil initialValue() {
			PropertyUtil p = new PropertyUtil(
					System.getProperty("application.properties.file", "resources/application.properties"));
			p.setProperty("isfw.build.info", getBuildInfo());

			log.info("ISFW build info: " + p.getProperty("isfw.build.info"));
			String[] resources = p.getStringArray("env.resources", "resources");
			for (String resource : resources) {
				addBundle(p, resource);

			}
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
		ConfigurationManager.addBundle(getBundle(), fileOrDir);
	}

	/**
	 * @param p
	 * @param fileOrDir
	 */
	private static void addBundle(PropertyUtil p, String fileOrDir) {
		String localResources = p.getString("local.reasources", p.getString("env.local.resources", "resources"));
		fileOrDir = p.getSubstitutor().replace(fileOrDir);
		File resourceFile = new File(fileOrDir);
		String[] locals = p.getStringArray(ApplicationProperties.LOAD_LOCALES.key);
		/**
		 * will reload existing properties value(if any) if the last loaded
		 * dir/file is not the current one. case: suit-1 default, suit-2 :
		 * s2-local, suit-3: default Here after suit-2 you need to reload
		 * default.
		 */
		if (!localResources.equalsIgnoreCase(resourceFile.getAbsolutePath())) {
			p.addProperty("local.reasources", resourceFile.getAbsolutePath());
			if (resourceFile.exists()) {
				if (resourceFile.isDirectory()) {
					boolean loadSubDirs = p.getBoolean("resources.load.subdirs", true);
					File[] propFiles = FileUtil.listFilesAsArray(resourceFile, ".properties", StringComparator.Suffix,
							loadSubDirs);
					log.info("Resource dir: " + resourceFile.getAbsolutePath() + ". Found property files to load: "
							+ propFiles.length);
					File[] locFiles = FileUtil.listFilesAsArray(resourceFile, ".loc", StringComparator.Suffix,
							loadSubDirs);
					PropertyUtil p1 = new PropertyUtil();
					p1.load(propFiles);
					p1.load(locFiles);
					p.copy(p1);

					propFiles = FileUtil.listFilesAsArray(resourceFile, ".xml", StringComparator.Suffix, loadSubDirs);
					log.info("Resource dir: " + resourceFile.getAbsolutePath() + ". Found property files to load: "
							+ propFiles.length);

					p1 = new PropertyUtil();
					p1.load(propFiles);
					p.copy(p1);

				} else {
					try {
						if (fileOrDir.endsWith(".properties")) {
							p.load(new File[] { resourceFile });
						}
					} catch (Exception e) {
						log.error("Unable to load " + resourceFile.getAbsolutePath() + "!", e);
					}
				}
				// add locals if any
				if (null != locals && locals.length > 0 && (locals.length == 1
						|| StringUtil.isBlank(p.getString(ApplicationProperties.DEFAULT_LOCALE.key, "")))) {
					p.setProperty(ApplicationProperties.DEFAULT_LOCALE.key, locals[0]);
				}
				for (String local : locals) {
					log.info("loading local: " + local);
					addLocal(p, local, fileOrDir);
				}

			} else {
				log.error(resourceFile.getAbsolutePath() + " not exist!");
			}
		}
	}

	private static void addLocal(PropertyUtil p, String local, String fileOrDir) {
		String defaultLocal = p.getString(ApplicationProperties.DEFAULT_LOCALE.key, "");//
		File resourceFile = new File(fileOrDir);
		/**
		 * will reload existing properties value(if any) if the last loaded
		 * dir/file is not the current one. case: suit-1 default, suit-2 :
		 * s2-local, suit-3: default Here after suit-2 you need to reload
		 * default.
		 */
		boolean loadSubDirs = p.getBoolean("resources.load.subdirs", true);

		if (resourceFile.exists()) {
			PropertyUtil p1 = new PropertyUtil();
			p1.setEncoding(p.getString(ApplicationProperties.LOCALE_CHAR_ENCODING.key, "UTF-8"));
			if (resourceFile.isDirectory()) {
				File[] propFiles = FileUtil.listFilesAsArray(resourceFile, "." + local, StringComparator.Suffix,
						loadSubDirs);
				p1.load(propFiles);

			} else {
				try {
					if (fileOrDir.endsWith(local)) {
						p1.load(fileOrDir);
					}
				} catch (Exception e) {
					log.error("Unable to load " + resourceFile.getAbsolutePath() + "!", e);
				}
			}
			if (local.equalsIgnoreCase(defaultLocal)) {
				p.copy(p1);
			} else {
				Iterator<?> keyIter = p1.getKeys();
				Configuration localSet = p.subset(local);
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					localSet.addProperty(key, p1.getObject(key));
				}
			}

		} else {
			log.error(resourceFile.getAbsolutePath() + " not exist!");
		}
	}

	public static void addAll(Map<String, String> props) {
		ConfigurationManager.getBundle().addAll(props);
	}

	public static PropertyUtil getBundle() {
		return ConfigurationManager.LocalProps.get();
	}

	private static Map<String, String> getBuildInfo() {
		Manifest manifest = null;
		Map<String, String> buildInfo = new HashMap<String, String>();
		JarFile jar = null;
		try {
			URL url = ConfigurationManager.class.getProtectionDomain().getCodeSource().getLocation();
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
			ConfigurationManager.getBundle().addProperty("teststep.mapping", JavaStepFinder.getAllJavaSteps());
			if (ConfigurationManager.getBundle().containsKey(ApplicationProperties.STEP_PROVIDER_PKG.key)) {
				for (String pkg : ConfigurationManager.getBundle()
						.getStringArray(ApplicationProperties.STEP_PROVIDER_PKG.key)) {
					for (ScenarioFactory factory : getStepFactories()) {
						factory.process(pkg.replaceAll("\\.", "/"));
					}
				}
			}
		}
		return (Map<String, TestStep>) ConfigurationManager.getBundle().getObject("teststep.mapping");
	}

	private static ScenarioFactory[] getStepFactories() {
		return new ScenarioFactory[] { new BDDTestFactory(Arrays.asList("bdl")),
				new KwdTestFactory(Arrays.asList("kwl")), new ExcelTestFactory() };
	}

	private static class PropertyConfigurationListener implements ConfigurationListener {
		String oldValue;

		@SuppressWarnings("unchecked")
		@Override
		public void configurationChanged(ConfigurationEvent event) {

			if ((event.getType() == AbstractConfiguration.EVENT_CLEAR_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY) && event.isBeforeUpdate()) {
				oldValue = String.format("%s", getBundle().getObject(event.getPropertyName()));
			}

			if ((event.getType() == AbstractConfiguration.EVENT_ADD_PROPERTY
					|| event.getType() == AbstractConfiguration.EVENT_SET_PROPERTY) && !event.isBeforeUpdate()) {
				String key = event.getPropertyName();
				Object value = event.getPropertyValue();
				if (null != oldValue && Matchers.equalTo(oldValue).matches(value)) {
					// do nothing
					return;
				}

				// driver reset
				if (key.equalsIgnoreCase(ApplicationProperties.DRIVER_NAME.key)
						|| StringMatcher.containsIgnoringCase("additional.capabilities").match(key)
						|| StringMatcher.containsIgnoringCase("driver.").match(key)
						|| key.equalsIgnoreCase(ApplicationProperties.REMOTE_SERVER.key)
						|| key.equalsIgnoreCase(ApplicationProperties.REMOTE_PORT.key)) {
					TestBaseProvider.instance().get().tearDown();
				}
				String[] bundles = null;

				// Resource loading
				if (key.equalsIgnoreCase("env.resources")) {

					if (event.getPropertyValue() instanceof ArrayList<?>) {
						ArrayList<String> bundlesArray = ((ArrayList<String>) event.getPropertyValue());
						bundles = bundlesArray.toArray(new String[bundlesArray.size()]);
					} else {
						String resourcesBundle = (String) value;
						if (StringUtil.isNotBlank(resourcesBundle))
							bundles = resourcesBundle.split(String.valueOf(PropertyUtil.getDefaultListDelimiter()));
					}
					if (null != bundles && bundles.length > 0) {
						for (String res : bundles) {
							log.info("Adding resources from: " + res);
							ConfigurationManager.addBundle(res);
						}
					}
				}
				// Locale loading
				if (key.equalsIgnoreCase(ApplicationProperties.DEFAULT_LOCALE.key)) {
					String[] resources = getBundle().getStringArray("env.resources", "resources");
					for (String resource : resources) {
						String fileOrDir = getBundle().getSubstitutor().replace(resource);
						addLocal(getBundle(), (String) event.getPropertyValue(), fileOrDir);
					}
				}
				// step provider package re-load
				if (key.equalsIgnoreCase(ApplicationProperties.STEP_PROVIDER_PKG.key)) {

					// has loaded steps and adding more or override java
					// steps....
					// for example suite level parameter has common steps and
					// test level parameter has test specific steps
					if (ConfigurationManager.getBundle().containsKey("teststep.mapping")) {
						ConfigurationManager.getStepMapping().putAll(JavaStepFinder.getAllJavaSteps());

						for (ScenarioFactory factory : getStepFactories()) {

							if (event.getPropertyValue() instanceof ArrayList<?>) {
								ArrayList<String> bundlesArray = ((ArrayList<String>) event.getPropertyValue());
								bundles = bundlesArray.toArray(new String[bundlesArray.size()]);
								for (String pkg : bundlesArray) {
									factory.process(pkg.replaceAll("\\.", "/"));
								}
							} else {
								String resourcesBundle = (String) value;
								if (StringUtil.isNotBlank(resourcesBundle)) {
									factory.process(resourcesBundle.replaceAll("\\.", "/"));
								}
							}
						}
					}
				}
			}

		}
	}

}
