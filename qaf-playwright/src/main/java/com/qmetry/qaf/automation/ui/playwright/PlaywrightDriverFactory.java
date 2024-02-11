/**
 * 
 */
package com.qmetry.qaf.automation.ui.playwright;

import static com.microsoft.playwright.impl.PlaywrightUtils.createPlaywright;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.DriverFactory;
import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.core.QAFTestBase.STBArgs;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.UiDriver;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 *
 */
public class PlaywrightDriverFactory implements DriverFactory<PlaywrightDriver> {
	private static final Log logger = LogFactoryImpl.getLog(PlaywrightDriverFactory.class);

	/**
	 * 
	 */
	public PlaywrightDriverFactory() {
	}

	@Override
	public PlaywrightDriver get(ArrayList<LoggingBean> logger, String[] args) {
		String browserName = STBArgs.browser_str.getFrom(args);
		if(browserName.contains("remote")) {
			//set env var
		}else {
			//clear env var if exist
		}
		Browsers browser = Browsers.getBrowser(browserName);
		loadDriverResouces(browser);
		PlaywrightCommandLogger cmdLogger = new PlaywrightCommandLogger(logger);


		ConfigurationManager.getBundle().setProperty("driver.desiredCapabilities",
				browser.getLaunchOptions());
		PlaywrightDriver driver =browser.getDriver(cmdLogger);;
		ConfigurationManager.getBundle().setProperty("driver.actualCapabilities", driver.getActualCapabilites());
		return driver;
	}

	@Override
	public void tearDown(UiDriver t) {
		t.stop();
	}
	
	private static void loadDriverResouces(Browsers browser) {
		String driverResourcesKey = String.format(ApplicationProperties.DRIVER_RESOURCES_FORMAT.key,
				browser.browserName);
		String driverResources = ConfigurationManager.getBundle().getString(driverResourcesKey, "");
		if (StringUtil.isNotBlank(driverResources)) {
			ConfigurationManager.addBundle(driverResources);
		}
	}
	
	private enum Browsers {
		
		chromium(),chrome("chromium"),edge("chromium", edgeOptions()),
		firefox(),
		webkit(),
		/**
		 * can with assumption that you have set desired capabilities using
		 * property. This is to provide support for future drivers or custom
		 * drivers if any. You can provide driver class as capability :
		 * driver.class, for example :<br>
		 * driver.class=org.openqa.selenium.safari.SafariDriver
		 */
		other();

		private Map<String, Object> options;
		private String type = name();
		private String browserName = name();

		private Browsers() {
			options = new HashMap<String, Object>();
		}

		private Browsers(String type) {
			options = new HashMap<String, Object>();
			this.type=type;
		}
		private Browsers(String type, Map<String, Object> options) {
			this.options = options;
			this.type=type;
		}

		private static Map<String, Object> edgeOptions() {
			Map<String, Object> options = new HashMap<String, Object>();
			options.put("channel", "msedge");
			return options;
		}
		@SuppressWarnings("unchecked")
		private LaunchOptions getLaunchOptions() {
			Gson gson = new GsonBuilder().create();
			Map<String, Object> capabilities = new HashMap<String, Object>(options);
			// capabilities provided for all driver
			Map<String, Object> extraCapabilities = gson
					.fromJson(ApplicationProperties.DRIVER_ADDITIONAL_CAPABILITIES.getStringVal("{}"), Map.class);
			capabilities.putAll(extraCapabilities);

			// individual capability property for all driver
			Configuration config = ConfigurationManager.getBundle()
					.subset(ApplicationProperties.DRIVER_CAPABILITY_PREFIX.key);
			capabilities.putAll(new ConfigurationMap(config));
			//#332 add default capabilities for standard driver
			if(!name().equalsIgnoreCase(other.name())){
				String driverCapsKey = String.format(ApplicationProperties.DRIVER_ADDITIONAL_CAPABILITIES_FORMAT.key,
						name());
				extraCapabilities = gson.fromJson(ConfigurationManager.getBundle().getString(driverCapsKey, "{}"),
						Map.class);
				capabilities.putAll(extraCapabilities);
			}
			// capabilities specific to this driver
			String driverCapsKey = String.format(ApplicationProperties.DRIVER_ADDITIONAL_CAPABILITIES_FORMAT.key,
					browserName);
			extraCapabilities = gson.fromJson(ConfigurationManager.getBundle().getString(driverCapsKey, "{}"),
					Map.class);
			capabilities.putAll(extraCapabilities);
			// individual capability property with driver name prefix
			String driverCapKey = String.format(ApplicationProperties.DRIVER_CAPABILITY_PREFIX_FORMAT.key, browserName);
			config = ConfigurationManager.getBundle().subset(driverCapKey);
			capabilities.putAll(new ConfigurationMap(config));
			
			for(String key : capabilities.keySet()){
				Object value = capabilities.get(key);
				if(value instanceof String){
					capabilities.put(key, ConfigurationManager.getBundle().getSubstitutor().replace(value));
				}
			}
			LaunchOptions launchOptions = gson.fromJson(gson.toJsonTree(capabilities), LaunchOptions.class);
			return launchOptions;
		}

		private static Browsers getBrowser(String name) {
			for (Browsers browser : Browsers.values()) {
				if (name.contains(browser.name())) {
					browser.setBrowserName(name);
					return browser;
				}
			}
			Browsers b = Browsers.other;
			b.setBrowserName(name);
			return b;
		}

		private void setBrowserName(String name) {
			// remove driver and remote from name
			browserName = name.replaceAll("(?i)remote|driver", "");
		}

		private PlaywrightDriver getDriver(PlaywrightCommandLogger reporter) {
			 LaunchOptions launchOptions = getLaunchOptions();

			Collection<PlaywrightCommandListener> listners = getDriverListeners();
			beforeInitialize(launchOptions, listners);
			try {
				PlaywrightListenerHandler handler = new PlaywrightListenerHandler();

				Playwright playwright = createPlaywright(null, false, handler);//createPlaywright(null, false);//Playwright.create();
				BrowserType browserType = null;
				
				switch(type.toLowerCase()) {
					case "chomium":
						browserType = playwright.chromium();
						break;
					case "firefox":
						browserType = playwright.firefox();
						break;
					case "webkit":
						browserType = playwright.webkit();
						break;
					default:
						try {
							playwright.close();
						} catch (Exception e) {
						}
				        throw new IllegalArgumentException("Unknown browser: " + type);
				}
				Browser browser = browserType.launch(launchOptions);
		        Page page = browser.newPage();
				PlaywrightDriver uiDriver = new PlaywrightDriver(playwright,browser,page);
				onInitialize(uiDriver, listners);
				return uiDriver;
			} catch (Throwable e) {
				onInitializationFailure(launchOptions, e, listners);

				throw new AutomationError("Unable to Create Driver Instance for " + browserName + ": " + e.getMessage(),
						e);
			}
		}
		
		

		void onInitialize(PlaywrightDriver uiDriver,Collection<PlaywrightCommandListener> listeners) {
			for(PlaywrightCommandListener listener : listeners) {
				listener.onInitialize(uiDriver);
			}
			
		}

		void onInitializationFailure(LaunchOptions launchOptions, Throwable e,
				Collection<PlaywrightCommandListener> listeners) {
			for(PlaywrightCommandListener listener : listeners) {
				listener.onInitializationFailure(launchOptions, e);
			}
		}

		void beforeInitialize(LaunchOptions launchOptions, Collection<PlaywrightCommandListener> listeners) {
			for(PlaywrightCommandListener listener : listeners) {
				listener.beforeInitialize(launchOptions);
			}
			
		}
	}
	
	private static Collection<PlaywrightCommandListener> getDriverListeners() {
		LinkedHashSet<PlaywrightCommandListener> listners = new LinkedHashSet<PlaywrightCommandListener>();
		String[] clistners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.WEBDRIVER_COMMAND_LISTENERS.key);
		for (String listenr : clistners) {
			try {
				PlaywrightCommandListener cls = (PlaywrightCommandListener) Class.forName(listenr).getConstructor().newInstance();
				listners.add(cls);
			} catch (Exception e) {
				logger.error("Unable to register listener class " + listenr, e);
			}
		}
		clistners = ConfigurationManager.getBundle().getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		for (String listener : clistners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).getConstructor().newInstance();
				if (PlaywrightCommandListener.class.isAssignableFrom(cls.getClass()))
					listners.add((PlaywrightCommandListener) cls);
			} catch (Exception e) {
				logger.error("Unable to register class as driver listener:  " + listener, e);
			}
		}
		return listners;
	}

	

	@Override
	public void loadDriverResouces(String name) {
		// TODO Auto-generated method stub
		
	}

}
