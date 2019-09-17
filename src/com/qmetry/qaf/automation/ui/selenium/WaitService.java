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
package com.qmetry.qaf.automation.ui.selenium;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.ui.SeleniumTestBase;
import com.qmetry.qaf.automation.util.StringUtil;
import com.thoughtworks.selenium.Wait;

/**
 * @deprecated use wait methods available with driver or element.
 * Wait service provides custom wait conditions
 * 
 * @author chirag
 */
public class WaitService {
	private IsSelenium selenium;
	private final Log logger;

	/**
	 * {@link Deprecated} use {@link com.qmetry.qaf.automation.ui.JsToolkit} instead
	 * @author chirag.jayswal
	 *
	 */
	public enum JsToolkit {
		DOJO("dojo", "dojo.io.XMLHTTPTransport.inFlight.length==0"), EXTJS("Ext",
				"Ext.Ajax.isLoading()==false"), JQUERY("jQuery", "jQuery.active==0"), YUI("YAHOO",
						"YAHOO.util.Connect.isCallInProgress==false"), PHPJS("PHP_JS",
								"PHP_JS.resourceIdCounter==0"), PROTOTYPE("Ajax", "Ajax.activeRequestCount==0");

		String identifier;
		String expr;

		private JsToolkit(String identifier, String expr) {
			this.identifier = identifier;
			this.expr = expr;
		}

		private String IsNotPresent() {
			return "!selenium.browserbot.getCurrentWindow()." + identifier;
		}

		private String isAjaxCallComplete() {
			return "selenium.browserbot.getCurrentWindow()." + expr;
		}

		public String waitCondition() {
			return "(" + IsNotPresent() + " || " + isAjaxCallComplete() + ")";
		}

		public String waitConditionWD() {
			return "return (!" + identifier + " || " + expr + ");";
		}

	}

	public WaitService(IsSelenium selenium) {
		this.selenium = selenium;
		logger = LogFactory.getLog(getClass());

	}

	/**
	 * be careful while from initializing out side test
	 */
	public WaitService() {
		this(new SeleniumTestBase().getDriver());
	}

	public enum ReadyState {
		complete, interactive, loading, uninitialized;
	}

	public void waitForState(ReadyState state, String... timeout) {
		String tout = (timeout != null) && (timeout.length > 0) && StringUtils.isNotEmpty(timeout[0]) ? timeout[0]
				: getDefaultPageWaitTime();
		try {
			waitForNotState(state, (Integer.parseInt(tout) / 5) + "");
		} catch (Throwable t) {
			logger.warn(t.getMessage());
		}
		String expr = JavaScriptHelper.getExpression("document.readyState=='" + state.name() + "'");
		selenium.waitForCondition(expr, tout);

	}

	public void waitForNotState(ReadyState state, String... timeout) {
		String tout = (timeout != null) && (timeout.length > 0) ? timeout[0] : getDefaultPageWaitTime();
		String expr = JavaScriptHelper.getExpression("document.readyState!='" + state.name() + "'");
		selenium.waitForCondition(expr, tout);
	}

	public void waitForPageToLoad() {
		selenium.waitForPageToLoad(getDefaultPageWaitTime());
	}

	public void waitForPageToLoad(String timeToWait) {
		selenium.waitForPageToLoad(timeToWait);
	}

	public void waitForAjaxToComplete(JsToolkit kit) {

		selenium.waitForCondition(kit.waitCondition(), getDefaultPageWaitTime());
	}

	/**
	 * for any kit
	 */
	public void waitForAjaxToComplete() {
		String waitCondition = "";
		for (JsToolkit kit : JsToolkit.values()) {
			waitCondition += waitCondition.equalsIgnoreCase("") ? kit.waitCondition() : " && " + kit.waitCondition();
		}
		waitCondition += " && (null == selenium.browserbot.getCurrentWindow().event)";
		try {
			selenium.waitForCondition(waitCondition, getDefaultPageWaitTime());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	public void waitForFrameToLoad(String frameLocator) {
		selenium.waitForFrameToLoad(frameLocator, getDefaultPageWaitTime());
	}

	public void waitForFrameToLoad(String frameLocator, String timeToWait) {
		selenium.waitForFrameToLoad(frameLocator, timeToWait);
	}

	public void waitForElementVisible(final String elementVisibleIndicator) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isElementPresent(elementVisibleIndicator)
						&& selenium.isVisible(elementVisibleIndicator);
			}
		}.wait("Element is not visible", Long.parseLong(getDefaultPageWaitTime()), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementEditable(final String element) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isElementPresent(element) && selenium.isEditable(element);
			}
		}.wait("Element is not Editable", Long.parseLong(getDefaultPageWaitTime()), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementNotEditable(final String element) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isElementPresent(element) && !selenium.isEditable(element);
			}
		}.wait("Element is Editable", Long.parseLong(getDefaultPageWaitTime()), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementInVisible(final String elementVisibleIndicator) {
		new Wait() {
			@Override
			public boolean until() {
				return !selenium.isVisible(elementVisibleIndicator);
			}
		}.wait("Element is visible", Long.parseLong(getDefaultPageWaitTime()), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementInVisible(final String elementVisibleIndicator, String failMsg, long timeOut) {
		new Wait() {
			@Override
			public boolean until() {
				return !selenium.isVisible(elementVisibleIndicator);
			}
		}.wait(failMsg, timeOut, getDefaultWaitIntervalTimeNum());
	}

	public void waitForCSSClassPresent(String elementVisibleIndicator, final String cssClass) {
		final String ele;
		if (StringUtil.isXpath(elementVisibleIndicator)) {
			ele = StringUtil.getWellFormedXPATH(elementVisibleIndicator);
		} else {
			ele = elementVisibleIndicator;
		}
		new Wait() {
			@Override
			public boolean until() {
				return selenium.getAttribute(ele + "@class").contains(cssClass);
			}
		}.wait("CSS class not present", Long.parseLong(getDefaultPageWaitTime()));
	}

	public void waitForCSSClassNotPresent(String elementVisibleIndicator, final String cssClass) {
		final String ele;
		if (StringUtil.isXpath(elementVisibleIndicator)) {
			ele = StringUtil.getWellFormedXPATH(elementVisibleIndicator);
		} else {
			ele = elementVisibleIndicator;
		}
		new Wait() {
			@Override
			public boolean until() {
				return !selenium.getAttribute(ele + "@class").contains(cssClass);
			}
		}.wait("CSS class present", Long.parseLong(getDefaultPageWaitTime()), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementNotPresent(final String elementLocator) {
		waitForElementNotPresent(elementLocator, getDefaultPageWaitTimeNum());
	}

	public void waitForElementNotPresent(final String elementLocator, final long waitTime) {

		new Wait() {
			@Override
			public boolean until() {
				return !selenium.isElementPresent(elementLocator);
			}
		}.wait("Element present", waitTime, getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementPresent(final String element) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isElementPresent(element);
			}
		}.wait("Element is not present", Long.parseLong(getDefaultPageWaitTime()));

	}

	/**
	 * Wait until the specified text pattern not appears somewhere on the
	 * rendered page shown to the user.
	 * 
	 * @param loc
	 * @param text
	 */
	public void waitForTextNotPresent(final String text) {
		new Wait() {
			@Override
			public boolean until() {
				return !selenium.isTextPresent(text);
			}
		}.wait("Text: " + text + " present", getDefaultPageWaitTimeNum(), getDefaultWaitIntervalTimeNum());

	}

	/**
	 * Wait until element not contains given text. This works for any element
	 * that contains text.
	 * 
	 * @param loc
	 * @param text
	 */
	public void waitForTextNotPresent(final String loc, final String text) {
		new Wait() {
			@Override
			public boolean until() {
				return !selenium.getText(loc).toUpperCase().contains(text.toUpperCase());
			}
		}.wait("Text " + text + " present", getDefaultPageWaitTimeNum(), getDefaultWaitIntervalTimeNum());

	}

	/**
	 * Wait until element not contains given text. This works for any element
	 * that contains text.
	 * 
	 * @param loc
	 * @param text
	 */
	public void waitForTextPresent(final String loc, final String text) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isElementPresent(loc)
						&& selenium.getText(loc).toUpperCase().contains(text.toUpperCase());
			}
		}.wait("Text present", getDefaultPageWaitTimeNum(), getDefaultWaitIntervalTimeNum());

	}

	public void waitForTextPresent(final String text) {
		new Wait() {
			@Override
			public boolean until() {
				return selenium.isTextPresent(text.toUpperCase());
			}
		}.wait("Text " + text + " not present", getDefaultPageWaitTimeNum(), getDefaultWaitIntervalTimeNum());
	}

	public void waitForElementAttribute(String ele, String attr, String val, String timeout) {
		if (StringUtil.isXpath(ele)) {
			ele = StringUtil.getWellFormedXPATH(ele);
		}
		selenium.waitForCondition("var attr=selenium.getAttribute('" + ele + "@" + attr + "');attr==" + val + ";",
				timeout);
	}

	public void waitForElementCSSClassPresent(String ele, String cssClass, String timeout) {
		selenium.waitForCondition(JavaScriptHelper.getConditionforCSSClassExist(ele, cssClass), timeout);
	}

	public void waitForElementCSSClassNotPresent(String ele, String cssClass, String timeout) {

		selenium.waitForCondition(JavaScriptHelper.getConditionforCSSClassNotExist(ele, cssClass), timeout);
	}

	public void waitForImageToLoad(final String imgLoc) {
		waitForElementPresent(imgLoc);
		selenium.waitForCondition(JavaScriptHelper.getConditionforImageLoaded(imgLoc), getDefaultPageWaitTime());
	}

	public void waitForElementClassNamePresent(String ele, String val) {
		waitForElementCSSClassPresent(ele, val, getDefaultPageWaitTime());
	}

	public void waitForElementCSSClassNotPresent(String ele, String cssClass) {
		waitForElementCSSClassNotPresent(ele, cssClass, getDefaultPageWaitTime());
	}

	public static String getDefaultPageWaitTime() {
		return ConfigurationManager.getBundle().getString(QAFTestBase.SELENIUM_DEFAULT_TIMEOUT, "5000");
	}

	public static long getDefaultPageWaitTimeNum() {
		return Long.valueOf(getDefaultPageWaitTime());
	}

	public static String getDefaultWaitIntervalTime() {
		return ConfigurationManager.getBundle().getString("selenium.wait.interval", "1000");
	}

	public static long getDefaultWaitIntervalTimeNum() {
		return Long.valueOf(getDefaultWaitIntervalTime());
	}
}
