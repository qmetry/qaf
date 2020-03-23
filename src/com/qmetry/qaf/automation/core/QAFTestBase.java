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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.google.common.base.Supplier;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.UiDriver;
import com.qmetry.qaf.automation.ui.UiDriverFactory;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.util.DynamicWait;
import com.qmetry.qaf.automation.ui.util.ExpectedCondition;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Provides uiDriver, assertion and verification service at test level To add
 * command custom listeners To run web browser with suffix "driver" for example,
 * firefoxDriver, firefoxDriver<br>
 * To run tests which developed using web-driver api on RC provide rc supported
 * browser string with suffix "_driver"<br>
 * for example, *firefox_driver<br>
 * 
 * @author chirag
 */
public class QAFTestBase {
	private static final String COMMAND_LOG = "commandLog";
	private static final String CHECKPOINTS = "checkPointResults";
	private static final String CONTEXT = "qafcontext";
	private static final String VERIFICATION_ERRORS = "verificationErrors";
	public static final String SELENIUM_DEFAULT_TIMEOUT = "selenium.wait.timeout";

	private Map<String, UiDriver> driverContext;

	private final Log logger = LogFactoryImpl.getLog(QAFTestBase.class);
	private boolean prepareForShutdown;
	/** Use this object to run all of your uiDriver tests */
	private String[] stb;
	private boolean alwaysCaptureScreenShot = false;
	private boolean captureScreenShotOnFailure = false;
	private String lastCapturedScreenShot;
	private String screenShotDir;
	private String reportDir;
	private PropertyUtil context;

	/**
	 * QAFTestBase setup arguments
	 * 
	 * @author chirag.jayswal
	 */
	public enum STBArgs {
		browser_str("firefoxDriver"), base_url("http://localhost"), sel_server("localhost"), port("4444");
		public String defaultVal;

		private STBArgs(String def) {
			defaultVal = def;
		}

		public String getFrom(String... args) {
			if ((args != null) && (args.length > ordinal())) {
				return ConfigurationManager.getBundle().getSubstitutor().replace(args[ordinal()]);
			}
			return "";
		}

		public static String allToString(String... args) {
			StringBuilder sb = new StringBuilder();
			for (STBArgs arg : values()) {
				sb.append("," + arg.name() + ":" + arg.getFrom(args));
			}
			return sb.substring(1);

		}

		public String[] set(String val, String... args) {
			if ((args != null) && (args.length > ordinal())) {
				args[ordinal()] = val;
				return args;
			}
			String[] extended = new String[STBArgs.values().length];
			extended[ordinal()] = val;
			if (args != null) {
				System.arraycopy(args, 0, extended, 0, args.length);
			}
			return extended;
		}

		public String[] setIfEmpty(String val, String... args) {
			if (StringUtils.isBlank(getFrom(args))) {

				return set(val, args);
			}
			return args;

		}

		public String getDefaultVal() {
			return defaultVal;
		}
	}

	protected QAFTestBase() {
		context = new PropertyUtil();
		context.setDelimiterParsingDisabled(true);
		context.setProperty(COMMAND_LOG, new ArrayList<LoggingBean>());
		context.setProperty(CHECKPOINTS, new ArrayList<CheckpointResultBean>());
		context.setProperty(VERIFICATION_ERRORS, 0);
		driverContext = new HashMap<String, UiDriver>();

		setAlwaysCaptureScreenShot(ApplicationProperties.SUCEESS_SCREENSHOT.getBoolenVal());

		setScreenShotDir(ApplicationProperties.SCREENSHOT_DIR.getStringVal("./img"));
		setReportDir(ApplicationProperties.REPORT_DIR.getStringVal("./"));
	}

	public String getHTMLFormattedLog() {
		return new HtmlCommandLogFormatter().getLog(getLog());
	}

	@SuppressWarnings("unchecked")
	public List<LoggingBean> getLog() {
		return (List<LoggingBean>) getContext().getObject(COMMAND_LOG);
	}

	/**
	 * @deprecated use {@link #getDriverName()} instead
	 * @return
	 */
	public String getBrowser() {
		return STBArgs.browser_str.getFrom(stb);
	}

	/**
	 * @since 2.1.13
	 * @return current driver name with active session or session will be
	 *         created on next {@link #getUiDriver()} call.
	 */
	public String getDriverName() {
		return STBArgs.browser_str.getFrom(stb);
	}

	/** checks for verification errors and stops the browser */
	public void tearDown() {
		Map<String, UiDriver> drivercontext = getDriverContext();
		String[] drivers = drivercontext.keySet().toArray(new String[] {});
		for (String driver : drivers) {
			UiDriver uiDriver = (UiDriver) drivercontext.get(driver);
			if (null != uiDriver) {
				new UiDriverFactory().tearDown(uiDriver);
				if (getDriverName().equalsIgnoreCase(driver)) {
					setDriver("");
				}
			}
			drivercontext.remove(driver);
		}
	}

	/** checks for verification errors and stops the browser */
	public void tearDown(String driverName) {
		Map<String, UiDriver> drivercontext = getDriverContext();
		UiDriver uiDriver = (UiDriver) drivercontext.get(driverName);
		if (null != uiDriver) {
			new UiDriverFactory().tearDown(uiDriver);
		}
		drivercontext.remove(driverName);
		if (getDriverName().equalsIgnoreCase(driverName)) {
			setDriver("");
		}
	}

	public void setDriver(String driverName) {
		stb = STBArgs.browser_str.set(driverName);
		// make sure driver specific resource are loaded when switch between
		// driver
		if (StringUtil.isNotBlank(driverName) && hasDriver()) {
			UiDriverFactory.loadDriverResouces(driverName);
			 UiDriver driver = getDriverContext().get(driverName);
			if (null != driver && driver instanceof QAFExtendedWebDriver) {
				ConfigurationManager.getBundle().setProperty("driver.actualCapabilities",
						((QAFExtendedWebDriver) driver).getCapabilities().asMap());
			}
		}
	}

	public void setDriver(String driverName, UiDriver driver) {
		stb = STBArgs.browser_str.set(driverName);
		setUiDriver(driver);
	}

	public String getBaseUrl() {
		return STBArgs.base_url.getFrom(stb);
	}

	/**
	 * Method to check driver session exist or not.
	 * 
	 * @param driverName
	 * @return whether given driver session is running or not.
	 */
	public boolean hasDriver(String driverName) {
		return getDriverContext().containsKey(driverName);
	}

	/**
	 * Method to check driver session exist or not. If you want to query current
	 * driver name use {@link #getDriverName()}
	 * 
	 * @return whether configured driver session is running or not.
	 * @see #hasDriver(String)
	 */
	public boolean hasDriver() {
		String driverName = getDriverName();
		return StringUtil.isNotBlank(driverName) && hasDriver(driverName);
	}

	public UiDriver getUiDriver() {
		if (!hasUiDriver()) {
			init();
		}
		return getDriverContext().get(getDriverName());
	}

	/** Sleeps for the specified number of milliseconds */
	public static void pause(long millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
		}
	}

	public boolean isPrepareForShutdown() {
		return prepareForShutdown;
	}

	public void setMethod(Method method) {
		ConfigurationManager.getBundle().addProperty(ApplicationProperties.CURRENT_TEST_NAME.key, method.getName());
	}

	public void setPrepareForShutdown(boolean prepareForShutdown) {
		this.prepareForShutdown = prepareForShutdown;
	}

	public String getLastCapturedScreenShot() {
		if (!hasDriver() || StringUtil.isBlank(lastCapturedScreenShot)) {
			return "";
		}
		String dir = ApplicationProperties.SCREENSHOT_RELATIVE_PATH
				.getStringVal(FileUtil.getReletivePath(ApplicationProperties.REPORT_DIR.getStringVal("./"),
						ApplicationProperties.SCREENSHOT_DIR.getStringVal("./img/")));
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		return dir + lastCapturedScreenShot;
	}

	public void setLastCapturedScreenShot(String lastCapturedScreenShot) {
		this.lastCapturedScreenShot = lastCapturedScreenShot;
	}

	public boolean isVerificationFailed() {
		return getVerificationErrors() > 0;
	}

	/**
	 * Asserts that there were no verification errors during the current test,
	 * failing immediately if any are found
	 */
	public void checkForVerificationErrors() {
		if (isVerificationFailed()) {
			fail(getVerificationErrors() + " Verification Errors");
		}
	}

	public static void fail(String message) {
		throw new AssertionError(message);
	}

	public void claerAssertionsLog() {
		clearVerificationErrors();
		getCheckPointResults().clear();
		getLog().clear();
		lastCapturedScreenShot = "";
	}

	/** Clears out the list of verification errors */
	public void clearVerificationErrors() {
		getContext().setProperty(VERIFICATION_ERRORS, 0);
	}

	public int getVerificationErrors() {
		return getContext().getInt(VERIFICATION_ERRORS);
	}

	public String getAssertionsLog() {
		return new HtmlCheckpointResultFormatter().getResults(getCheckPointResults());
	}

	/**
	 * @return the checkPointResults
	 */
	@SuppressWarnings("unchecked")
	public List<CheckpointResultBean> getCheckPointResults() {
		return (List<CheckpointResultBean>) getContext().getObject(CHECKPOINTS);

	}

	public String getLastCapturedScreenShotFile() {
		return lastCapturedScreenShot;
	}

	public void setAlwaysCaptureScreenShot(boolean alwaysCaptureScreenShot) {
		this.alwaysCaptureScreenShot = alwaysCaptureScreenShot;
	}

	public String takeScreenShot() {
		if (!hasUiDriver()) {
			return "";
		}
		try {
			lastCapturedScreenShot = base64ImageToFile(getUiDriver().takeScreenShot());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return lastCapturedScreenShot;

	}

	public void addVerificationError(Throwable e) {
		addAssertionLog(e.getMessage(), MessageTypes.Fail);
		if (!logger.isDebugEnabled())
			logger.error(MessageTypes.Fail.formatText(e.getMessage()));
		else
			logger.debug(e.getMessage(), e);

	}

	public void addVerificationError(String message) {
		addAssertionLog(message, MessageTypes.Fail);
		logger.debug(message);

	}

	public void addAssertionLogWithScreenShot(String msg, MessageTypes type) {
		takeScreenShot();
		addAssertionLog(msg, type);
	}

	public void addAssertionLog(String msg, MessageTypes type) {
		logger.debug(type.formatText(msg));
		if (type.shouldReport()) {

			CheckpointResultBean bean = new CheckpointResultBean();
			bean.setMessage(msg);
			bean.setType(type);
			boolean added = addCheckpoint(bean);

			if (added && StringUtil.isBlank(getLastCapturedScreenShot())
					&& ((ApplicationProperties.FAILURE_SCREENSHOT.getBoolenVal(true) && (type.isFailure()))
							|| ((type != MessageTypes.Info)
									&& ApplicationProperties.SUCEESS_SCREENSHOT.getBoolenVal(false)))) {

				takeScreenShot();
			}
			bean.setScreenshot(getLastCapturedScreenShot());
			setLastCapturedScreenShot("");
		}
		if (type == MessageTypes.Fail) {
			int verificationErrors = getVerificationErrors() + 1;
			getContext().setProperty(VERIFICATION_ERRORS, verificationErrors);
		}

	}

	public PropertyUtil getContext() {
		ITestResult tr = Reporter.getCurrentTestResult();
		if (null != tr) {
			PropertyUtil contextFromTr = (PropertyUtil) Reporter.getCurrentTestResult().getAttribute(CONTEXT);
			if (null == contextFromTr) {
				Reporter.getCurrentTestResult().setAttribute(CONTEXT, context);
				return context;
			}
			return contextFromTr;
		}
		return context;// (PropertyUtil)
						// Reporter.getCurrentTestResult().getAttribute(CONTEXT);
	}

	// base logging and checkpoint
	protected String getReportDir() {
		return reportDir;
	}

	protected boolean isCaptureScreenShotOnFailure() {
		return captureScreenShotOnFailure;
	}

	protected void setCaptureScreenShotOnFailure(boolean captureScreetShotOnFailure) {
		captureScreenShotOnFailure = captureScreetShotOnFailure;
	}

	protected boolean isAlwaysCaptureScreenShot() {
		return alwaysCaptureScreenShot;
	}

	protected String getTestCaseName() {
		return ApplicationProperties.CURRENT_TEST_NAME.getStringVal("QAFTest");
	}

	protected void setScreenShotDir(String screenShotDir) {
		this.screenShotDir = screenShotDir;
		FileUtil.checkCreateDir(screenShotDir);
	}

	protected void setReportDir(String reportDir) {
		this.reportDir = reportDir;
	}

	@Override
	protected void finalize() throws Throwable {
		logger.debug("Unloading TestBase, cleaning up...");
		tearDown();
		super.finalize();
	}

	private void init() {
		if (ApplicationProperties.DRIVER_NAME.getStringVal("").equalsIgnoreCase("")) {
			System.err.println("Driver not configured!... \nUsing " + STBArgs.browser_str.getDefaultVal()
					+ " as default value. Please configure driver to be used using '"
					+ ApplicationProperties.DRIVER_NAME.key + "' property");
		}
		stb = initStbArgs();
		logger.info("Initializing Driver..." + STBArgs.allToString(stb));
		// uiDriver = new UiDriverFactory().get(commandLog, stb);
		DriverInitExpectedCondition driverInitExpectedCondition = new DriverInitExpectedCondition(
				(ArrayList<LoggingBean>) getLog(), stb);
		UiDriver uiDriver = new UiDriverInitializer()
				.withTimeout(ApplicationProperties.DRIVER_INIT_TIMEOUT.getIntVal(0), TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS).withMessage(driverInitExpectedCondition)
				.ignoring(WebDriverException.class).until(driverInitExpectedCondition);

		setUiDriver(uiDriver);
		logger.info("driver init done");
	}

	private boolean hasFailure(List<CheckpointResultBean> subSteps) {
		for (CheckpointResultBean subStep : subSteps) {
			if (StringMatcher.containsIgnoringCase("fail").match(subStep.getType())) {
				return true;
			}
		}
		return false;
	}

	private boolean addCheckpoint(CheckpointResultBean bean) {
		int checkPoints = getCheckPointResults().size();
		CheckpointResultBean lastCheckpoint = checkPoints > 1 ? getCheckPointResults().get(checkPoints - 1) : null;

		List<CheckpointResultBean> parent = MessageTypes.TestStep.name().equalsIgnoreCase(bean.getType())
				|| (lastCheckpoint == null) || !MessageTypes.TestStep.name().equalsIgnoreCase(lastCheckpoint.getType())
						? getCheckPointResults() : lastCheckpoint.getSubCheckPoints();

		CheckpointResultBean prevCheckpointResultBean = !parent.isEmpty() ? parent.get(parent.size() - 1) : null;

		if ((prevCheckpointResultBean == null) || !prevCheckpointResultBean.equals(bean)) {
			parent.add(bean);
			if ((lastCheckpoint != null) && MessageTypes.TestStep.name().equalsIgnoreCase(lastCheckpoint.getType())) {
				lastCheckpoint.setType(hasFailure(lastCheckpoint.getSubCheckPoints()) ? MessageTypes.TestStepFail
						: MessageTypes.TestStepPass);
			}
			return true;
		}

		return false;
	}

	private String getScreenShotDir() {

		return screenShotDir;
	}

	private String base64ImageToFile(String base64Image) {
		String filename = "";
		try {
			String tcname = StringUtil.toTitleCaseIdentifier(getTestCaseName());
			// too long file name may not supported in some os
			if (tcname.length() > 25) {
				tcname.substring(0, 25);
			}
			filename = FileUtil.saveImageFile(base64Image, StringUtil.createRandomString(tcname), getScreenShotDir());
			lastCapturedScreenShot = filename;
			logger.debug("Capturing screen shot" + lastCapturedScreenShot);

		} catch (Exception e) {
			logger.error("Error in capturing screenshot\n" + e.getMessage());
		}
		return filename;

	}

	private String[] initStbArgs(String... args) {
		args = STBArgs.browser_str.setIfEmpty(getBrowser(), args);
		return STBArgs.browser_str.setIfEmpty(
				ApplicationProperties.DRIVER_NAME.getStringVal(STBArgs.browser_str.defaultVal),
				STBArgs.base_url.setIfEmpty(
						ApplicationProperties.SELENIUM_BASE_URL.getStringVal(STBArgs.base_url.defaultVal),
						STBArgs.port.setIfEmpty(

								ApplicationProperties.REMOTE_PORT.getStringVal(STBArgs.port.defaultVal),
								STBArgs.sel_server.setIfEmpty(
										ApplicationProperties.REMOTE_SERVER.getStringVal(STBArgs.sel_server.defaultVal),
										args))));

	}

	private class DriverInitExpectedCondition
			implements ExpectedCondition<UiDriverFactory, UiDriver>, Supplier<String> {
		int count = 0;
		private ArrayList<LoggingBean> commandLog;
		private String[] stb;

		public DriverInitExpectedCondition(ArrayList<LoggingBean> commandLog, String[] stb) {
			this.commandLog = commandLog;
			this.stb = stb;
		}

		public UiDriver apply(UiDriverFactory driverFectory) {
			try {
				count++;
				return driverFectory.get(commandLog, stb);
			} catch (Throwable e) {
				String msg = get();
				System.err.println(msg + e.getMessage());
				throw new WebDriverException(msg, e.getCause());
			}
		}

		@Override
		public String get() {
			return "Unable to create driver instance in " + StringUtil.toStringWithSufix(count)
					+ " attempt with retry timeout of " + ApplicationProperties.DRIVER_INIT_TIMEOUT.getIntVal(0)
					+ " seconds. You can check/set value of '" + ApplicationProperties.DRIVER_INIT_TIMEOUT.key
					+ "' appropriately to set retry timeout on driver initialization failure.";
		}

	}

	private class UiDriverInitializer extends DynamicWait<UiDriverFactory> {

		public UiDriverInitializer() {
			super(new UiDriverFactory());
		}

//		@Override
//		protected RuntimeException timeoutException(String message, Throwable lastException) {
//			AutomationError ae = new AutomationError(message + "\n" + lastException.getCause().getMessage());
//			ae.setStackTrace(lastException.getCause().getStackTrace());
//			return ae;
//		}

	}

	private boolean hasUiDriver() {

		return null != driverContext.get(getDriverName());
	}

	private void setUiDriver(UiDriver uiDriver) {
		driverContext.put(getDriverName(), uiDriver);
	}

	private Map<String, UiDriver> getDriverContext() {
		return driverContext;
	}

	public static void main(String[] args) {
		System.setProperty("driver.name", "chromeDriver");
		System.setProperty("webdriver.chrome.driver", "/Users/chiragjayswal/Downloads/chromedriver");
		System.setProperty("webdriver.gecko.driver", "/Users/chiragjayswal/Downloads/geckodriver");

		new WebDriverTestBase().getDriver().get("http://www.google.com");
		pause(5000);

		System.out.println(TestBaseProvider.instance().get().getDriverName());

		TestBaseProvider.instance().get().setDriver("chrome2Driver");
		System.out.println(TestBaseProvider.instance().get().getDriverName());
		new WebDriverTestBase().getDriver().get("http://www.google.com");
		new WebDriverTestBase().getDriver().findElement("name=q").sendKeys("firefoxDriver");

		TestBaseProvider.instance().get().setDriver("chromeDriver");
		System.out.println(TestBaseProvider.instance().get().getDriverName());
		new WebDriverTestBase().getDriver().findElement("name=q").sendKeys("chromeDriver");

		pause(50000);

		System.exit(0);

	}
}
