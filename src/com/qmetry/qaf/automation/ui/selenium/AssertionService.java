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


package com.qmetry.qaf.automation.ui.selenium;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.ScreenshotException;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.HtmlCheckpointResultFormatter;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.selenium.webdriver.QAFWebDriverBackedSelenium;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.PropertyUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * This class provides assertion and verification methods and keeps track of
 * each test-case. To change message format for Assertion/verification update
 * appropriate prop value in assertMessage.properties
 * 
 * @author chirag
 */
public class AssertionService {
	private IsSelenium selenium;
	protected Method method;

	// TODO: remove verificationErrors
	protected StringBuffer verificationErrors = new StringBuffer();
	private boolean alwaysCaptureScreenShot = false;
	private boolean isVerificationFailed = false;
	PropertyUtil seleniumProperties;
	private boolean captureScreenShotOnFailure = false;
	private String screenShotDir;
	private String reportDir;
	protected final Log logger;

	protected List<CheckpointResultBean> checkPointResults;

	public AssertionService() {
		logger = LogFactoryImpl.getLog(this.getClass());
		checkPointResults = new ArrayList<CheckpointResultBean>();
	}

	protected void setUpAssertionService(IsSelenium selenium) {
		setUpAssertionService(selenium, getBundle());
	}

	protected void setUpAssertionService(IsSelenium selenium, PropertyUtil props) {
		this.selenium = selenium;
		seleniumProperties = props;
		setAlwaysCaptureScreenShot(ApplicationProperties.SUCEESS_SCREENSHOT.getBoolenVal());

		setScreenShotDir(ApplicationProperties.SCREENSHOT_DIR.getStringVal("./img"));
		setReportDir(ApplicationProperties.REPORT_DIR.getStringVal("./"));
	}

	public void setMethod(Method method) {
		this.method = method;
		clearVerificationErrors();
		claerAssertionsLog();
	}

	protected String getReportDir() {
		return reportDir;
	}

	protected void setReportDir(String reportDir) {
		this.reportDir = reportDir;
	}

	public String getLastCapturedScreenShot() {
		if (StringUtil.isBlank(lastCapturedScreenShot)) {
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

	public void setVerificationFailed(boolean isVerificationFailed) {
		this.isVerificationFailed = isVerificationFailed;
	}

	protected void setScreenShotDir(String screenShotDir) {
		this.screenShotDir = screenShotDir;
		FileUtil.checkCreateDir(screenShotDir);
	}

	public boolean isVerificationFailed() {
		boolean retVal = isVerificationFailed;
		clearVerificationFailed();
		return retVal;
	}

	protected void setVerificationFailed() {
		isVerificationFailed = true;
	}

	protected void clearVerificationFailed() {
		isVerificationFailed = false;
	}

	/**
	 * Asserts that there were no verification errors during the current test,
	 * failing immediately if any are found
	 */
	public void checkForVerificationErrors() {
		String verificationErrorString = getVerificationErrors();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public static void fail(String message) {
		throw new AssertionError(message);
	}

	/** Clears out the list of verification errors */
	protected void clearVerificationErrors() {
		verificationErrors = new StringBuffer();
	}

	public String getVerificationErrors() {
		String retVal = verificationErrors.toString().trim();
		return retVal;

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

	public void claerAssertionsLog() {
		checkPointResults = new ArrayList<CheckpointResultBean>();
	}

	public void assertElementPresent(String elementLocator, String name) {
		String failMsg = MessageFormat.format(getBundle().getString("element.present.fail"), name);
		String successMsg = MessageFormat.format(getBundle().getString("element.present.pass"), name);
		boolean res = false;
		try {
			new WaitService().waitForElementPresent(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);
	}

	public void assertElementNotPresent(String elementLocator, String name) {
		String failMsg = MessageFormat.format(getBundle().getString("element.notpresent.fail"), name);
		String successMsg = MessageFormat.format(getBundle().getString("element.notpresent.pass"), name);
		boolean res = true;
		try {
			new WaitService().waitForElementNotPresent(elementLocator);
			res = false;
		} catch (Throwable e) {
			// do nothing
		}
		assertFalse(res, failMsg, successMsg);
	}

	public void assertIsVisible(String elementLocator, String elementName) {
		String failMsg = MessageFormat.format(getBundle().getString("element.visible.fail"), elementName);
		String successMsg = MessageFormat.format(getBundle().getString("element.visible.pass"), elementName);
		boolean res = false;
		try {
			new WaitService().waitForElementVisible(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);
	}

	public void assertIsNotVisible(String elementLocator, String elementName) {
		String failMsg = MessageFormat.format(getBundle().getString("element.notvisible.fail"), elementName);
		String successMsg = MessageFormat.format(getBundle().getString("element.notvisible.pass"), elementName);
		boolean res = true;
		try {
			new WaitService().waitForElementInVisible(elementLocator);
			res = false;
		} catch (Throwable e) {
			// do nothing
		}
		assertFalse(res, failMsg, successMsg);
	}

	public void assertIsEditable(String elementLocator, String elementName) {
		String failMsg = MessageFormat
				.format(getBundle().getString("element.editable.fail", "Expected {0} shoud be editable"), elementName);
		String successMsg = MessageFormat
				.format(getBundle().getString("element.editable.pass", "Expected {0} shoud be editable"), elementName);
		boolean res = false;
		try {
			new WaitService().waitForElementEditable(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);
	}

	public void assertIsNotEditable(String elementLocator, String elementName) {
		String failMsg = MessageFormat.format(
				getBundle().getString("element.editable.fail", "Expected {0} shoud not be editable"), elementName);
		String successMsg = MessageFormat.format(
				getBundle().getString("element.editable.pass", "Expected {0} shoud not be editable"), elementName);
		boolean res = false;
		try {
			new WaitService().waitForElementNotEditable(elementLocator);
			res = true;
		} catch (Throwable e) {
			// do nothing
		}
		assertTrue(res, failMsg, successMsg);
	}

	public void assertTrue(boolean b, String message) {
		assertTrue(b, message, message);
	}

	public void assertImageLoaded(String imgLoc, String msg) {
		assertTrue(
				Boolean.valueOf(selenium.getEval("anImgObj=selenium.browserbot.findElement(\"" + imgLoc
						+ "\");(!anImgObj.complete) ? false : !(typeof anImgObj.naturalWidth != \"undefined\" && anImgObj.naturalWidth == 0);")),
				msg + selenium.getEval("selenium.browserbot.findElement(\"" + imgLoc + "\").src"));
	}

	public void assertTrue(boolean b, String failMsg, String successMsg) {
		if (!b) {
			addAssertionLog(failMsg, MessageTypes.Fail);

			throw new AssertionError(failMsg);
		}
		addAssertionLog(successMsg, MessageTypes.Pass);
	}

	public void assertFalse(boolean b, String message) {
		assertFalse(b, message, message);
	}

	public void assertFalse(boolean b, String failMsg, String successMsg) {
		assertTrue(!b, failMsg, successMsg);
	}

	public void assertEquals(Object actual, Object expected, String message) {
		String msg = MessageFormat.format(getBundle().getString("equals.common"), message,
				expected.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"),
				actual.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		assertTrue(seleniumEquals(expected, actual), msg, msg);
	}

	public void assertNotEquals(Object actual, Object expected, String message) {
		String msg = MessageFormat.format(getBundle().getString("not.equals.common"), message,
				expected.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"),
				actual.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		assertFalse(seleniumEquals(expected, actual), msg, msg);
	}

	public void assertIsTextPresent(String text, String message) {
		String failMsg = MessageFormat.format(getBundle().getString("text.present.fail"), message, text);
		String successMsg = MessageFormat.format(getBundle().getString("text.present.pass"), message, text);

		assertTrue(selenium.isTextPresent(text), failMsg, successMsg);
	}

	public void assertIsTextPresent(String text) {
		assertIsTextPresent(text, "");
	}

	/**
	 * to provide register expression use regexp:<exp> eg: "regexp:*test*"
	 */
	public void assertIsTextPresent(String text, String locator, String message) {
		try {
			new WaitService().waitForTextPresent(locator, text);
		} catch (Throwable e) {
		}
		assertEquals(selenium.getText(locator), text, message);
	}

	/**
	 * to provide register expression use regexp:<exp> eg: "regexp:*test*"
	 */
	public void assertNotTextPresent(String text, String locator, String message) {
		try {
			new WaitService().waitForTextNotPresent(locator, text);
		} catch (Throwable e) {
		}
		assertNotEquals(selenium.getText(locator), text, message);
	}

	public void assertIsFiledVlaue(String text, String locator, String name) {
		assertEquals(selenium.getValue(locator), text, name);
	}

	public void assertIsSelectedLabel(String label, String locator, String name) {
		assertEquals(selenium.getSelectedLabel(locator), label, name);
	}

	public String getReqResXml() {
		return selenium.captureNetworkTraffic("xml");
	}

	// ********************************************************//
	// verifications //
	/** Like assertTrue, but fails at the end of the test (during tearDown) */
	public boolean verifyTrue(boolean b, String failMessage, String successMessage) {
		try {
			assertTrue(b, failMessage, successMessage);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyFalse(boolean b, String failMessage, String successMessage) {
		return verifyTrue(!b, failMessage, successMessage);
	}

	/** Like assertEquals, but fails at the end of the test (during tearDown) */
	public boolean verifyEquals(Object actual, Object expected, String message) {
		try {
			assertEquals(actual, expected, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyText(String text, String message) {
		try {
			assertIsTextPresent(text, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/**
	 * method verifies text derived from assertIsTextPresent
	 * 
	 * @see assertIsTextPresent
	 * @param text
	 * @param locator
	 * @param failMessage
	 * @param successMessage
	 */
	public boolean verifyText(String text, String locator, String message) {
		try {
			assertIsTextPresent(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyValue(String text, String locator, String message) {
		try {
			assertIsFiledVlaue(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifySelectedLabel(String text, String locator, String message) {
		try {
			assertIsSelectedLabel(text, locator, message);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyIsVisible(String elementLocator, String elementName) {
		try {
			assertIsVisible(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/** Like assertFalse, but fails at the end of the test (during tearDown) */
	public boolean verifyIsNotVisible(String elementLocator, String elementName) {
		try {
			if (verifyElementPresent(elementLocator, elementName)) {
				assertIsNotVisible(elementLocator, elementName);
			}
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyIsEditable(String elementLocator, String elementName) {
		try {
			assertIsEditable(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyIsNotEditable(String elementLocator, String elementName) {
		try {
			assertIsNotEditable(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyElementPresent(String elementLocator, String elementName) {
		try {
			assertElementPresent(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyElementNotPresent(String elementLocator, String elementName) {
		try {
			assertElementNotPresent(elementLocator, elementName);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	public boolean verifyImageLoaded(String imgLocator, String msg) {
		try {
			assertImageLoaded(imgLocator, msg);
			return true;
		} catch (Error e) {
			addVerificationError(e);
			return false;
		}
	}

	/**
	 * Like JUnit's Assert.assertEquals, but knows how to compare string arrays
	 */
	public static void assertEquals(Object s1, Object s2) {
		if ((s1 instanceof String) && (s2 instanceof String)) {
			assertEquals(s1, s2);
		} else if ((s1 instanceof String) && (s2 instanceof String[])) {
			assertEquals(s1, s2);
		} else if ((s1 instanceof String) && (s2 instanceof Number)) {
			assertEquals(s1, ((Number) s2).toString());
		} else {
			if ((s1 instanceof String[]) && (s2 instanceof String[])) {

				String[] sa1 = (String[]) s1;
				String[] sa2 = (String[]) s2;
				if (sa1.length != sa2.length) {
					throw new Error("Expected " + sa1 + " but saw " + sa2);
				}
				for (int j = 0; j < sa1.length; j++) {
					assertEquals(sa1[j], sa2[j]);
				}
			}
		}
	}

	public static boolean seleniumEquals(String expectedPattern, String actual) {
		if (actual.startsWith("regexp:") || actual.startsWith("regex:") || actual.startsWith("regexpi:")
				|| actual.startsWith("regexi:")) {
			// swap 'em
			String tmp = actual;
			actual = expectedPattern;
			expectedPattern = tmp;
		}
		Boolean b;
		b = handleRegex("regexp:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regex:", expectedPattern, actual, 0);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexpi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}
		b = handleRegex("regexi:", expectedPattern, actual, Pattern.CASE_INSENSITIVE);
		if (b != null) {
			return b.booleanValue();
		}

		if (expectedPattern.startsWith("exact:")) {
			String expectedExact = expectedPattern.replaceFirst("exact:", "");
			if (!expectedExact.equals(actual)) {
				System.out.println("expected " + actual + " to match " + expectedPattern);
				return false;
			}
			return true;
		}

		String expectedGlob = expectedPattern.replaceFirst("glob:", "");
		expectedGlob = expectedGlob.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1");

		expectedGlob = expectedGlob.replaceAll("\\*", ".*");
		expectedGlob = expectedGlob.replaceAll("\\?", ".");
		if (!Pattern.compile(expectedGlob, Pattern.DOTALL).matcher(actual).matches()) {
			System.out.println("expected \"" + actual + "\" to match glob \"" + expectedPattern
					+ "\" (had transformed the glob into regexp \"" + expectedGlob + "\"");
			return false;
		}
		return true;
	}

	private static Boolean handleRegex(String prefix, String expectedPattern, String actual, int flags) {
		if (expectedPattern.startsWith(prefix)) {
			String expectedRegEx = expectedPattern.replaceFirst(prefix, ".*") + ".*";
			Pattern p = Pattern.compile(expectedRegEx, flags);
			if (!p.matcher(actual).matches()) {
				System.out.println("expected " + actual + " to match regexp " + expectedPattern);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return null;
	}

	/**
	 * Compares two objects, but handles "regexp:" strings like HTML Selenese
	 * 
	 * @see #seleniumEquals(String, String)
	 * @return true if actual matches the expectedPattern, or false otherwise
	 */
	public static boolean seleniumEquals(Object expected, Object actual) {
		if ((expected instanceof String) && (actual instanceof String)) {
			return seleniumEquals((String) expected, (String) actual);
		}
		return expected.equals(actual);
	}

	/** Asserts that two string arrays have identical string contents */
	public static void assertEquals(String[] s1, String[] s2) {
		String comparisonDumpIfNotEqual = verifyEqualsAndReturnComparisonDumpIfNot(s1, s2);
		if (comparisonDumpIfNotEqual != null) {
			throw new AssertionError(comparisonDumpIfNotEqual);
		}
	}

	/**
	 * Asserts that two string arrays have identical string contents (fails at
	 * the end of the test, during tearDown)
	 */
	public void verifyEquals(String[] s1, String[] s2) {
		String comparisonDumpIfNotEqual = verifyEqualsAndReturnComparisonDumpIfNot(s1, s2);

		if (comparisonDumpIfNotEqual != null) {
			addAssertionLog(comparisonDumpIfNotEqual, MessageTypes.Fail);
		} else {
			addAssertionLog(String.format("Expected %s : Actual %s", Arrays.asList(s1), Arrays.asList(s2)),
					MessageTypes.Pass);

		}
	}

	private static String verifyEqualsAndReturnComparisonDumpIfNot(String[] s1, String[] s2) {
		boolean misMatch = false;
		if (s1.length != s2.length) {
			misMatch = true;
		}
		for (int j = 0; j < s1.length; j++) {
			if (!seleniumEquals(s1[j], s2[j])) {
				misMatch = true;
				break;
			}
		}
		if (misMatch) {
			return "Expected " + stringArrayToString(s1) + " but saw " + stringArrayToString(s2);
		}
		return null;
	}

	private static String stringArrayToString(String[] sa) {
		StringBuffer sb = new StringBuffer("{");
		for (String element : sa) {
			sb.append(" ").append("\"").append(element).append("\"");
		}
		sb.append(" }");
		return sb.toString();
	}

	public static String join(String[] sa, char c) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < sa.length; j++) {
			sb.append(sa[j]);
			if (j < (sa.length - 1)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public void addVerificationError(Throwable e) {
		addAssertionLog(e.getMessage(), MessageTypes.Fail);
		logger.error(e.getMessage(), e);
	}

	public void addAssertionLogWithScreenShot(String msg, MessageTypes type) {
		takeScreenShot();
		addAssertionLog(msg, type);
	}

	public void addAssertionLog(String msg, MessageTypes type) {
		CheckpointResultBean bean = new CheckpointResultBean();
		bean.setMessage(msg);
		bean.setType(type);
		boolean added = addCheckpoint(bean);

		if (added && StringUtil.isBlank(getLastCapturedScreenShot())
				&& ((ApplicationProperties.FAILURE_SCREENSHOT.getBoolenVal(true) && (type == MessageTypes.Fail))
						|| ((type != MessageTypes.Info)
								&& ApplicationProperties.SUCEESS_SCREENSHOT.getBoolenVal(false)))) {

			takeScreenShot();
		}
		bean.setScreenshot(getLastCapturedScreenShot());
		setLastCapturedScreenShot("");

		if (type == MessageTypes.Fail) {
			verificationErrors.append(msg);
		}

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
		CheckpointResultBean lastCheckpoint = checkPoints > 1 ? checkPointResults.get(checkPoints - 1) : null;

		List<CheckpointResultBean> parent = MessageTypes.TestStep.name().equalsIgnoreCase(bean.getType())
				|| (lastCheckpoint == null) || !MessageTypes.TestStep.name().equalsIgnoreCase(lastCheckpoint.getType())
						? checkPointResults : lastCheckpoint.getSubCheckPoints();

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

	protected boolean isCaptureScreenShotOnFailure() {
		return captureScreenShotOnFailure;
	}

	protected void setCaptureScreenShotOnFailure(boolean captureScreetShotOnFailure) {
		captureScreenShotOnFailure = captureScreetShotOnFailure;
	}

	protected boolean isAlwaysCaptureScreenShot() {
		return alwaysCaptureScreenShot;
	}

	public void setAlwaysCaptureScreenShot(boolean alwaysCaptureScreenShot) {
		this.alwaysCaptureScreenShot = alwaysCaptureScreenShot;
	}

	private String base64ImageToFile(String base64Image) {
		String filename = "";
		try {
			filename = FileUtil.saveImageFile(base64Image, getTestCaseName(), getScreenShotDir());
			lastCapturedScreenShot = filename;
			logger.info("Capturing screen shot" + lastCapturedScreenShot);

		} catch (Exception e) {
			logger.error("Error in capturing screenshot\n" + e.getMessage());
		}
		return filename;

	}

	private String lastCapturedScreenShot;

	public String getLastCapturedScreenShotFile() {
		return lastCapturedScreenShot;
	}

	private String captureScreenShot() {
		String filename = StringUtil.createRandomString(getTestCaseName()) + ".png";
		try {
			selenium.captureEntirePageScreenshot(getScreenShotDir() + filename, "");
		} catch (Exception e) {
			try {
				selenium.windowFocus();
			} catch (Throwable t) {
				logger.error(t);
			}
			selenium.captureScreenshot(getScreenShotDir() + filename);
		}
		lastCapturedScreenShot = filename;
		logger.info("Captured screen shot: " + lastCapturedScreenShot);
		return filename;
	}

	private String captureScreenShot_remote() {
		String filename = "";
		String base64Image;
		try {
			base64Image = selenium.captureEntirePageScreenshotToString(
					ConfigurationManager.getBundle().getString("selenium.screenshots.kwargs", ""));
		} catch (Exception e) {
			try {
				selenium.windowFocus();
			} catch (Throwable t) {
				logger.error(t);
			}
			base64Image = selenium.captureScreenshotToString();
		}
		filename = base64ImageToFile(base64Image);
		return filename;
	}

	public String takeScreenShot() {

		try {
			lastCapturedScreenShot = captureScreenShot_remote();
			return lastCapturedScreenShot;
		} catch (Throwable th) {
			if ((th.getMessage() != null) && (th.getMessage().indexOf("WebDriver") >= 0)) {
				String base64Image = "";
				try {
					if (th.getCause() instanceof ScreenshotException) {
						throw th;
					}
					WebDriver driver = ((QAFWebDriverBackedSelenium) selenium).getWrappedDriver();
					driver.findElement(By.name("current screen shot")).sendKeys("capture");

					logger.info("Unable to capture ScreenShot: " + th.getMessage());
					return "";
				} catch (Throwable t) {
					Throwable cause = t.getCause();
					if (cause instanceof ScreenshotException) {
						base64Image = ((ScreenshotException) cause).getBase64EncodedScreenshot();
						lastCapturedScreenShot = base64ImageToFile(base64Image);
						return lastCapturedScreenShot;
					} else {
						System.out.println("Unable to retrive capture ScreenShot: " + t.getMessage());
					}
				}
			} else {
				try {
					lastCapturedScreenShot = captureScreenShot();
					return lastCapturedScreenShot;
				} catch (Throwable t) {
					logger.info("Unable to capture ScreenShot: " + th.getMessage() + "\n" + t.getMessage());
				}
			}
		}
		return "";
	}

	protected String getScreenShotDir() {
		return screenShotDir;
	}

	protected String getTestCaseName() {
		if (null == method) {
			return "SeleneseTest";
		}
		return method.getName();
	}

}
