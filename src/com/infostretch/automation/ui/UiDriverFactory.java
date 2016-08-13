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


package com.infostretch.automation.ui;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infostretch.automation.core.AutomationError;
import com.infostretch.automation.core.ConfigurationManager;
import com.infostretch.automation.core.DriverFactory;
import com.infostretch.automation.core.LoggingBean;
import com.infostretch.automation.core.QAFTestBase.STBArgs;
import com.infostretch.automation.keys.ApplicationProperties;
import com.infostretch.automation.ui.selenium.AutoWaitInjector;
import com.infostretch.automation.ui.selenium.IEScreenCaptureListener;
import com.infostretch.automation.ui.selenium.QAFCommandProcessor;
import com.infostretch.automation.ui.selenium.SeleniumCommandProcessor;
import com.infostretch.automation.ui.selenium.SubmitCommandListener;
import com.infostretch.automation.ui.selenium.webdriver.QAFWebDriverBackedSelenium;
import com.infostretch.automation.ui.webdriver.ChromeDriverHelper;
import com.infostretch.automation.ui.webdriver.QAFExtendedWebDriver;
import com.infostretch.automation.ui.webdriver.QAFWebDriverCommandListener;
import com.infostretch.automation.util.StringUtil;

/**
 * com.infostretch.automation.ui.UiDriverFactory.java
 * 
 * @author chirag
 */
public class UiDriverFactory implements DriverFactory<UiDriver> {
	private static final Log logger = LogFactoryImpl.getLog(UiDriverFactory.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostretch.automation.core.DriverFactory#get(java.lang.String[])
	 */
	@SuppressWarnings("deprecation")
	@Override
	public UiDriver get(ArrayList<LoggingBean> commandLog, String[] stb) {
		WebDriverCommandLogger cmdLogger = new WebDriverCommandLogger(commandLog);
		String browser = STBArgs.browser_str.getFrom(stb);
		logger.info("browser: " + browser);

		String baseUrl = STBArgs.base_url.getFrom(stb);
		if (browser.toLowerCase().contains("driver") && !browser.startsWith("*")) {
			return getDriver(cmdLogger, stb);
		}

		QAFCommandProcessor commandProcessor = new SeleniumCommandProcessor(STBArgs.sel_server.getFrom(stb),
				Integer.parseInt(STBArgs.port.getFrom(stb)), browser.split("_")[0], baseUrl);
		CommandExecutor executor = getObject(commandProcessor);
		QAFExtendedWebDriver driver = new QAFExtendedWebDriver(executor, new DesiredCapabilities(), cmdLogger);
		QAFWebDriverBackedSelenium selenium = new QAFWebDriverBackedSelenium(commandProcessor, driver);

		commandProcessor.addListener(new SubmitCommandListener());

		commandProcessor.addListener(cmdLogger);
		commandProcessor.addListener(new AutoWaitInjector());
		if (browser.contains("iexproper") || browser.contains("iehta")) {
			commandProcessor.addListener(new IEScreenCaptureListener());
		}
		String listners = ApplicationProperties.SELENIUM_CMD_LISTENERS.getStringVal("");

		if (!listners.equalsIgnoreCase("")) {
			commandProcessor.addListener(listners.split(","));
		}

		selenium.setTimeout(ApplicationProperties.SELENIUM_WAIT_TIMEOUT.getStringVal("5000"));

		return selenium;
	}

	@Override
	public void tearDown(UiDriver driver) {
		try {
			driver.stop();
			ChromeDriverHelper.teardownService();
			logger.info("UI-driver tear down complete...");
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}
	}

	public static String[] checkAndStartServer(String... args) {
		if (!isServerRequired(args)) {
			return args;
		}
		if (isSeverRunning(STBArgs.sel_server.getFrom(args), Integer.parseInt(STBArgs.port.getFrom(args)))) {
			return args;
		}

		// override args values to default
		args = STBArgs.sel_server.set(STBArgs.sel_server.getDefaultVal(), args);
		if (isSeverRunning(STBArgs.sel_server.getFrom(args), Integer.parseInt(STBArgs.port.getFrom(args)))) {
			logger.info("Assigning server running on localhost");

			return args;
		}
		return args;
	}

	private CommandExecutor getObject(Object commandProcessor) {

		try {
			Class<?> clazz = Class.forName("org.openqa.selenium.SeleneseCommandExecutor");
			Class<?> commandProcessorclazz = Class.forName("com.thoughtworks.selenium.CommandProcessor");
			Constructor<?> ctor = clazz.getConstructor(commandProcessorclazz);
			return (CommandExecutor) ctor.newInstance(new Object[] { commandProcessor });
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage()
					+ "SeleneseCommandExecutor is not available. Please try with selenium 2.32 or older.");
		}
	}

	private static boolean isServerRequired(String... args) {
		String browser = STBArgs.browser_str.getFrom(args).toLowerCase();
		return browser.contains("*") || browser.contains("remote");
	}

	private static boolean isSeverRunning(String host, int port) {
		boolean isRunning = false;

		Socket socket = null;
		try {
			socket = new Socket(host, (port));
			isRunning = socket.isConnected();
		} catch (Exception exp) {
			logger.error("Error occured while checking Selenium : " + exp.getMessage());
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {

			}
		}
		return isRunning;
	}

	private static void beforeInitialize(Capabilities desiredCapabilities) {
		LinkedHashSet<QAFWebDriverCommandListener> listners = new LinkedHashSet<QAFWebDriverCommandListener>();
		String[] clistners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.WEBDRIVER_COMMAND_LISTENERS.key);
		for (String listenr : clistners) {
			try {
				QAFWebDriverCommandListener cls = (QAFWebDriverCommandListener) Class.forName(listenr).newInstance();
				listners.add(cls);
			} catch (Exception e) {
				logger.error("Unable to register listener class " + listenr, e);
			}
		}
		if ((listners != null) && !listners.isEmpty()) {
			for (QAFWebDriverCommandListener listener : listners) {
				listener.beforeInitialize(desiredCapabilities);
			}
		}
	}

	private static QAFExtendedWebDriver getDriver(WebDriverCommandLogger reporter, String... args) {
		String b = STBArgs.browser_str.getFrom(args).toLowerCase();
		String urlStr = STBArgs.sel_server.getFrom(args).startsWith("http") ? STBArgs.sel_server.getFrom(args)
				: String.format("http://%s:%s/wd/hub", STBArgs.sel_server.getFrom(args), STBArgs.port.getFrom(args));

		for (Browsers browser : Browsers.values()) {
			if (b.contains(browser.name())) {

				ConfigurationManager.getBundle().setProperty("driver.desiredCapabilities",
						browser.getDesiredCapabilities().asMap());
				QAFExtendedWebDriver driver = b.contains("remote") ? browser.getDriver(urlStr, reporter)
						: browser.getDriver(reporter, urlStr);
				ConfigurationManager.getBundle().setProperty("driver.actualCapabilities",
						driver.getCapabilities().asMap());
				return driver;
			}
		}
		return null;
	}

	private static WebDriver getDriverObj(Class<? extends WebDriver> of, Capabilities capabilities, String urlStr) {
		try {
			Constructor<? extends WebDriver> constructor = of.getConstructor(Capabilities.class);
			return constructor.newInstance(capabilities);
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof WebDriverException) {
				throw (WebDriverException) e.getCause();
			}
			try {
				return of.newInstance();
			} catch (Exception e1) {
				try {

					Constructor<? extends WebDriver> constructor = of.getConstructor(URL.class, Capabilities.class);

					return constructor.newInstance(new URL(urlStr), capabilities);
				} catch (InvocationTargetException e2) {
					throw new WebDriverException(e2);
				} catch (InstantiationException e2) {
					throw new WebDriverException(e2);
				} catch (IllegalAccessException e2) {
					throw new WebDriverException(e2);
				} catch (IllegalArgumentException e2) {
					throw new WebDriverException(e2);
				} catch (MalformedURLException e2) {
					throw new WebDriverException(e2);
				} catch (NoSuchMethodException e2) {
					throw new WebDriverException(e2);
				} catch (SecurityException e2) {
					throw new WebDriverException(e2);
				}
			}
		}
	}

	private enum Browsers {
		firefox(DesiredCapabilities.firefox(), FirefoxDriver.class), iexplorer(DesiredCapabilities.internetExplorer(),
				InternetExplorerDriver.class), chrome(DesiredCapabilities.chrome(), ChromeDriver.class), opera(
						new DesiredCapabilities("opera", "", Platform.ANY),
						"com.opera.core.systems.OperaDriver"), android(DesiredCapabilities.android(),
								"org.openqa.selenium.android.AndroidDriver"), iphone(
										new DesiredCapabilities("iPhone", "", Platform.MAC),
										"org.openqa.selenium.iphone.IPhoneDriver"), ipad(
												new DesiredCapabilities("iPad", "", Platform.MAC),
												"org.openqa.selenium.iphone.IPhoneDriver"), safari(
														new DesiredCapabilities("safari", "", Platform.ANY),
														"org.openqa.selenium.safari.SafariDriver"), appium(
																new DesiredCapabilities(),
																"io.appium.java_client.AppiumDriver"),
		/**
		 * can with assumption that you have set desired capabilities using
		 * property. This is to provide support for future drivers or custom
		 * drivers if any. You can provide driver class as capability :
		 * driver.class, for example :<br>
		 * driver.class=org.openqa.selenium.safari.SafariDriver
		 */
		other(new DesiredCapabilities());

		private DesiredCapabilities desiredCapabilities;

		private Class<? extends WebDriver> driverCls = null;

		private Browsers(DesiredCapabilities desiredCapabilities) {
			this.desiredCapabilities = desiredCapabilities;
			this.desiredCapabilities.setJavascriptEnabled(true);
			this.desiredCapabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			this.desiredCapabilities.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);

		}

		@SuppressWarnings("unchecked")
		private Browsers(DesiredCapabilities desiredCapabilities, String drivercls) {
			this(desiredCapabilities);
			if (null == driverCls) {
				// not overridden by extra capability
				try {
					driverCls = (Class<? extends WebDriver>) Class.forName(drivercls);
				} catch (Exception e) {
					// throw new AutomationError(e);
				}
			}

		}

		private Browsers(DesiredCapabilities desiredCapabilities, Class<? extends WebDriver> driver) {
			this(desiredCapabilities);
			if (null == driverCls) {
				// not overridden by extra capability
				driverCls = driver;
			}
		}

		@SuppressWarnings("unchecked")
		private DesiredCapabilities getDesiredCapabilities() {
			Map<String, Object> capabilities = new HashMap<String, Object>(desiredCapabilities.asMap());
			Gson gson = new GsonBuilder().create();

			// capabilities provided for all driver
			Map<String, Object> extraCapabilities = gson
					.fromJson(ApplicationProperties.DRIVER_ADDITIONAL_CAPABILITIES.getStringVal("{}"), Map.class);
			capabilities.putAll(extraCapabilities);

			// individual capability property for all driver
			Configuration config = ConfigurationManager.getBundle()
					.subset(ApplicationProperties.DRIVER_CAPABILITY_PREFIX.key);
			capabilities.putAll(new ConfigurationMap(config));
			// capabilities specific to this driver
			String driverCapsKey = String.format(ApplicationProperties.DRIVER_ADDITIONAL_CAPABILITIES_FORMAT.key,
					name());
			extraCapabilities = gson.fromJson(ConfigurationManager.getBundle().getString(driverCapsKey, "{}"),
					Map.class);
			capabilities.putAll(extraCapabilities);

			// individual capability property with driver name prefix
			String driverCapKey = String.format(ApplicationProperties.DRIVER_CAPABILITY_PREFIX_FORMAT.key, name());
			config = ConfigurationManager.getBundle().subset(driverCapKey);
			capabilities.putAll(new ConfigurationMap(config));

			Object driverclass = capabilities.get(ApplicationProperties.CAPABILITY_NAME_DRIVER_CLASS.key);
			if (null == driverclass) {// backward compatibility only
				driverclass = capabilities.get("driver.class");
			}
			if (null != driverclass) {
				try {
					driverCls = (Class<? extends WebDriver>) Class.forName(String.valueOf(driverclass));
				} catch (Exception e) {
					// throw new AutomationError(e);
				}
			}

			System.out.println("desiredCapabilities: " + capabilities);
			return new DesiredCapabilities(capabilities);
		}

		private QAFExtendedWebDriver getDriver(WebDriverCommandLogger reporter, String urlstr) {
			DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

			beforeInitialize(desiredCapabilities);

			try {
				if (this.name().equalsIgnoreCase("chrome")) {
					return new QAFExtendedWebDriver(ChromeDriverHelper.getService().getUrl(), desiredCapabilities,
							reporter);
				}
				WebDriver driver = getDriverObj(driverCls, desiredCapabilities, urlstr);// driverCls.newInstance();
				return new QAFExtendedWebDriver(driver, reporter);
			} catch (Exception e) {
				throw new AutomationError("Unable to Create Driver Instance for " + name() + ": " + e.getMessage(), e);
			}
		}

		private QAFExtendedWebDriver getDriver(String url, WebDriverCommandLogger reporter) {
			try {
				DesiredCapabilities desiredCapabilities = getDesiredCapabilities();
				beforeInitialize(desiredCapabilities);
				if (StringUtil.isNotBlank(ApplicationProperties.WEBDRIVER_REMOTE_SESSION.getStringVal())
						|| desiredCapabilities.asMap()
								.containsKey(ApplicationProperties.WEBDRIVER_REMOTE_SESSION.key)) {
					try {
						Constructor<?> constructor = Class
								.forName("com.infostretch.automation.ui.webdriver.LiveIsExtendedWebDriver")
								.getDeclaredConstructor(URL.class, Capabilities.class, WebDriverCommandLogger.class);
						return (QAFExtendedWebDriver) constructor.newInstance(new URL(url), desiredCapabilities,
								reporter);
					} catch (ClassNotFoundException e) {
						throw new AutomationError("Pro version Feature", e);
					}
				}
				return new QAFExtendedWebDriver(new URL(url), desiredCapabilities, reporter);
			} catch (Exception e) {
				throw new AutomationError("Unable to Create Driver Instance " + e.getMessage(), e.getCause());
			}
		}
	}

}
