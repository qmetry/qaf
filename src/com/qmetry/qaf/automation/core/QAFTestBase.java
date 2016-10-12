/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Supplier;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.UiDriver;
import com.qmetry.qaf.automation.ui.UiDriverFactory;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.util.ExpectedCondition;
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
	private PropertyUtil context;
	private final Log logger = LogFactoryImpl.getLog(QAFTestBase.class);
	public static final String SELENIUM_DEFAULT_TIMEOUT = "selenium.wait.timeout";
	private boolean prepareForShutdown;
	/** Use this object to run all of your uiDriver tests */
	private UiDriver uiDriver;
	private ArrayList<LoggingBean> commandLog;
	private List<CheckpointResultBean> checkPointResults;
	private String[] stb;
	private int verificationErrors = 0;
	private boolean alwaysCaptureScreenShot = false;
	private boolean captureScreenShotOnFailure = false;
	private String lastCapturedScreenShot;
	private String screenShotDir;
	private String reportDir;

	/**
	 * QAFTestBase setup arguments
	 * 
	 * @author chirag.jayswal
	 */
	public enum STBArgs {
		browser_str("firefoxDriver"),
		base_url("http://localhost"),
		sel_server("localhost"),
		port("4444");
		public String defaultVal;

		private STBArgs(String def) {
			defaultVal = def;
		}

		public String getFrom(String... args) {
			if ((args != null) && (args.length > ordinal())) {
				return ConfigurationManager.getBundle().getSubstitutor()
						.replace(args[ordinal()]);
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
		commandLog = new ArrayList<LoggingBean>();
		checkPointResults = new ArrayList<CheckpointResultBean>();
		context = new PropertyUtil();
		setAlwaysCaptureScreenShot(
				ApplicationProperties.SUCEESS_SCREENSHOT.getBoolenVal());

		setScreenShotDir(ApplicationProperties.SCREENSHOT_DIR.getStringVal("./img"));
		setReportDir(ApplicationProperties.REPORT_DIR.getStringVal("./"));
	}

	public String getHTMLFormattedLog() {
		return new HtmlCommandLogFormatter().getLog(commandLog);
	}

	public List<LoggingBean> getLog() {
		return commandLog;
	}

	public String getBrowser() {
		return STBArgs.browser_str.getFrom(stb);
	}

	/** checks for verification errors and stops the browser */
	public void tearDown() {
		if (null != uiDriver) {
			new UiDriverFactory().tearDown(uiDriver);
			uiDriver = null;
		}
	}

	public String getBaseUrl() {
		return STBArgs.base_url.getFrom(stb);
	}

	public UiDriver getUiDriver() {
		if ((uiDriver == null)) {
			init();
		}
		return uiDriver;
	}

	/** Sleeps for the specified number of milliseconds */
	public static void pause(int millisecs) {
		try {
			Thread.sleep(millisecs);
		} catch (InterruptedException e) {
		}
	}

	public boolean isPrepareForShutdown() {
		return prepareForShutdown;
	}

	public void setMethod(Method method) {
		ConfigurationManager.getBundle().addProperty("current.testcase.name",
				method.getName());
	}

	public void setPrepareForShutdown(boolean prepareForShutdown) {
		this.prepareForShutdown = prepareForShutdown;
	}

	public String getLastCapturedScreenShot() {
		if (StringUtil.isBlank(lastCapturedScreenShot)) {
			return "";
		}
		String dir = ApplicationProperties.SCREENSHOT_RELATIVE_PATH.getStringVal(FileUtil
				.getReletivePath(ApplicationProperties.REPORT_DIR.getStringVal("./"),
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
		return verificationErrors > 0;
	}

	/**
	 * Asserts that there were no verification errors during the current test,
	 * failing immediately if any are found
	 */
	public void checkForVerificationErrors() {
		if (verificationErrors > 0) {
			fail(verificationErrors + " Verification Errors");
		}
	}

	public static void fail(String message) {
		throw new AssertionError(message);
	}

	public void claerAssertionsLog() {
		clearVerificationErrors();
		checkPointResults = new ArrayList<CheckpointResultBean>();
		commandLog = new ArrayList<LoggingBean>();
		lastCapturedScreenShot = "";
	}

	/** Clears out the list of verification errors */
	public void clearVerificationErrors() {
		verificationErrors = 0;
	}

	public int getVerificationErrors() {
		return verificationErrors;
	}

	public String getAssertionsLog() {
		return new HtmlCheckpointResultFormatter().getResults(checkPointResults);
	}

	/**
	 * @return the checkPointResults
	 */
	public List<CheckpointResultBean> getCheckPointResults() {
		return checkPointResults;
	}

	public String getLastCapturedScreenShotFile() {
		return lastCapturedScreenShot;
	}

	public void setAlwaysCaptureScreenShot(boolean alwaysCaptureScreenShot) {
		this.alwaysCaptureScreenShot = alwaysCaptureScreenShot;
	}

	public String takeScreenShot() {
		if (null == uiDriver) {
			return "";
		}
		try {
			lastCapturedScreenShot = base64ImageToFile(uiDriver.takeScreenShot());
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
		else logger.debug(e.getMessage(), e);

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
		CheckpointResultBean bean = new CheckpointResultBean();
		bean.setMessage(msg);
		bean.setType(type);
		boolean added = addCheckpoint(bean);

		if (added && StringUtil.isBlank(getLastCapturedScreenShot())
				&& ((ApplicationProperties.FAILURE_SCREENSHOT.getBoolenVal(true)
						&& (type.isFailure()))
						|| ((type != MessageTypes.Info)
								&& ApplicationProperties.SUCEESS_SCREENSHOT
										.getBoolenVal(false)))) {

			takeScreenShot();
		}
		bean.setScreenshot(getLastCapturedScreenShot());
		setLastCapturedScreenShot("");

		if (type == MessageTypes.Fail) {
			verificationErrors++;
		}

	}

	public PropertyUtil getContext() {
		return context;
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
		return ConfigurationManager.getBundle().getString("current.testcase.name",
				"QAFTest");
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
			System.err.println("Driver not configured!... \nUsing "
					+ STBArgs.browser_str.getDefaultVal()
					+ " as default value. Please configure driver to be used using '"
					+ ApplicationProperties.DRIVER_NAME.key + "' property");
		}
		stb = initStbArgs();
		logger.info("Initializing Driver..." + STBArgs.allToString(stb));
		// uiDriver = new UiDriverFactory().get(commandLog, stb);
		DriverInitExpectedCondition driverInitExpectedCondition =
				new DriverInitExpectedCondition(commandLog, stb);
		uiDriver = new UiDriverInitializer()
				.withTimeout(ApplicationProperties.DRIVER_INIT_TIMEOUT.getIntVal(0),
						TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS)
				.withMessage(driverInitExpectedCondition)
				.ignoring(WebDriverException.class).until(driverInitExpectedCondition);
		System.out.println("driver init done");
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
		int checkPoints = checkPointResults.size();
		CheckpointResultBean lastCheckpoint =
				checkPoints > 1 ? checkPointResults.get(checkPoints - 1) : null;

		List<CheckpointResultBean> parent =
				MessageTypes.TestStep.name().equalsIgnoreCase(bean.getType())
						|| (lastCheckpoint == null)
						|| !MessageTypes.TestStep.name()
								.equalsIgnoreCase(lastCheckpoint.getType())
										? checkPointResults
										: lastCheckpoint.getSubCheckPoints();

		CheckpointResultBean prevCheckpointResultBean =
				!parent.isEmpty() ? parent.get(parent.size() - 1) : null;

		if ((prevCheckpointResultBean == null)
				|| !prevCheckpointResultBean.equals(bean)) {
			parent.add(bean);
			if ((lastCheckpoint != null) && MessageTypes.TestStep.name()
					.equalsIgnoreCase(lastCheckpoint.getType())) {
				lastCheckpoint.setType(hasFailure(lastCheckpoint.getSubCheckPoints())
						? MessageTypes.TestStepFail : MessageTypes.TestStepPass);
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
			filename = FileUtil.saveImageFile(base64Image,
					StringUtil.createRandomString(getTestCaseName()), getScreenShotDir());
			lastCapturedScreenShot = filename;
			logger.info("Capturing screen shot" + lastCapturedScreenShot);

		} catch (Exception e) {
			logger.error("Error in capturing screenshot\n" + e.getMessage());
		}
		return filename;

	}

	private String[] initStbArgs(String... args) {

		return STBArgs.browser_str.setIfEmpty(
				ApplicationProperties.DRIVER_NAME
						.getStringVal(STBArgs.browser_str.defaultVal),
				STBArgs.base_url.setIfEmpty(
						ApplicationProperties.SELENIUM_BASE_URL
								.getStringVal(STBArgs.base_url.defaultVal),
						STBArgs.port.setIfEmpty(

								ApplicationProperties.REMOTE_PORT
										.getStringVal(STBArgs.port.defaultVal),
								STBArgs.sel_server.setIfEmpty(
										ApplicationProperties.REMOTE_SERVER.getStringVal(
												STBArgs.sel_server.defaultVal),
										args))));

	}

	private class DriverInitExpectedCondition
			implements
				ExpectedCondition<UiDriverFactory, UiDriver>,
				Supplier<String> {
		int count = 0;
		private ArrayList<LoggingBean> commandLog;
		private String[] stb;

		public DriverInitExpectedCondition(ArrayList<LoggingBean> commandLog,
				String[] stb) {
			this.commandLog = commandLog;
			this.stb = stb;
		}
		public UiDriver apply(UiDriverFactory driverFectory) {
			try {
				count++;
				return driverFectory.get(this.commandLog, this.stb);
			} catch (Throwable e) {
				String msg = get();
				System.err.println(msg + e.getMessage());
				throw new WebDriverException(msg, e.getCause());
			}
		}

		@Override
		public String get() {
			return "Unable to create driver instance in "
					+ StringUtil.toStringWithSufix(count)
					+ " attempt with retry timeout of "
					+ ApplicationProperties.DRIVER_INIT_TIMEOUT.getIntVal(0)
					+ " seconds. You can check/set value of '"
					+ ApplicationProperties.DRIVER_INIT_TIMEOUT.key
					+ "' appropriately to set retry timeout on driver initialization failure.";
		}

	}
	
	private class UiDriverInitializer extends FluentWait<UiDriverFactory>{

		public UiDriverInitializer() {
			super(new UiDriverFactory());
		}
		@Override
		protected RuntimeException timeoutException(String message,
				Throwable lastException) {
			AutomationError ae = new AutomationError(message + "\n"+lastException.getCause().getMessage());
			ae.setStackTrace(lastException.getCause().getStackTrace());
			return ae;
		}
		
	}
}
