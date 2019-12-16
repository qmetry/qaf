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
package com.qmetry.qaf.automation.ui.webdriver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;
import org.testng.SkipException;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.util.QAFWebElementExpectedConditions;
import com.qmetry.qaf.automation.ui.util.QAFWebElementWait;
import com.qmetry.qaf.automation.ui.webdriver.CommandTracker.Stage;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.LocatorUtil;
import com.qmetry.qaf.automation.util.StringMatcher;

/**
 * com.qmetry.qaf.automation.ui.webdriver.extended.QAFExtendedWebElement.java
 * 
 * @see ElementMetaDataListener 
 * @see ApplicationProperties#ELEMENT_GLOBAL_METADATA
 * @see ApplicationProperties#ELEMENT_ATTACH_DEFAULT_LISTENER 
 * @see ApplicationProperties#QAF_LISTENERS
 * @author chirag.jayswal
 */
public class QAFExtendedWebElement extends RemoteWebElement implements QAFWebElementCommandListener, QAFWebElement {
	private static final Map<String, Object> GLOBAL_METADATA = JSONUtil
			.toMap(ApplicationProperties.ELEMENT_GLOBAL_METADATA.getStringVal("{'scroll':'OnFail'}"));
	protected final Log logger = LogFactory.getLog(getClass());
	transient protected By by;
	protected QAFExtendedWebElement parentElement;
	protected String locator;
	private Map<String, Object> metaData;
	protected boolean cacheable = false;
	private Set<QAFWebElementCommandListener> listners = new LinkedHashSet<QAFWebElementCommandListener>();
	private String description;

	protected QAFExtendedWebElement(QAFExtendedWebDriver driver) {
		setParent(driver);
		id = "-1";
		metaData = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);// new HashMap<String, Object>();
		metaData.putAll(GLOBAL_METADATA);
		
		//default value true to avoid issue for direct initialization by driver.
		cacheable = metaData.containsKey("cacheable") ? (Boolean) metaData.get("cacheable") : true;
		listners.add(driver.getReporter());
		if(ApplicationProperties.ELEMENT_ATTACH_DEFAULT_LISTENER.getBoolenVal(true)){
			listners.add(new ElementMetaDataListener());
		}

		try {
			setFileDetector(parent.getFileDetector());

		} catch (Exception e) {
			logger.debug("FileDetector not found!", e);
		}
		String[] listners = ConfigurationManager.getBundle()
				.getStringArray(ApplicationProperties.WEBELEMENT_COMMAND_LISTENERS.key);
		for (String listenr : listners) {
			registerListeners(listenr);
		}
		listners = ConfigurationManager.getBundle().getStringArray(ApplicationProperties.QAF_LISTENERS.key);
		for (String listener : listners) {
			try {
				QAFListener cls = (QAFListener) Class.forName(listener).newInstance();
				if (QAFWebElementCommandListener.class.isAssignableFrom(cls.getClass()))
					this.listners.add((QAFWebElementCommandListener) cls);
			} catch (Exception e) {
				logger.error("Unable to register class as element listener:  " + listener, e);
			}
		}
	}

	/**
	 * 
	 * @param by
	 */
	public QAFExtendedWebElement(By by) {
		this(new WebDriverTestBase().getDriver(), by);
	}

	/**
	 * @param locator
	 *            : locator or json string in {locator:'id=eleId';desc:'sample
	 *            element'} format. locator can be selenium-1 style, for ex:
	 *            id=eleId or name=eleName ...
	 */
	public QAFExtendedWebElement(String locator) {
		this((By) null);
		initLoc(locator);
	}

	/**
	 * 
	 * @param parentElement
	 * @param locator
	 */
	public QAFExtendedWebElement(QAFExtendedWebElement parentElement, String locator) {
		this(parentElement, (By) null);
		initLoc(locator);
	}

	/**
	 * 
	 * @param driver
	 * @param by
	 */
	public QAFExtendedWebElement(QAFExtendedWebDriver driver, By by) {
		this(driver, by, false);
	}

	/**
	 * 
	 * @param driver
	 * @param by
	 * @param cacheable
	 */
	public QAFExtendedWebElement(QAFExtendedWebDriver driver, By by, boolean cacheable) {
		this(driver);
		this.by = by;
		this.cacheable = cacheable;
	}

	public QAFExtendedWebElement(QAFExtendedWebElement parentElement, By by) {
		this(parentElement.getWrappedDriver(), by, parentElement.cacheable);
		this.parentElement = parentElement;
	}

	protected void setBy(By by) {
		this.by = by;
	}

	protected By getBy() {
		if ((null == by) && StringUtils.isNotBlank(locator)) {
			by = LocatorUtil.getBy(locator);
		}

		return by;
	}

	/**
	 * @return
	 */

	/**
	 * @param label
	 *            optional element description
	 * @return description of the element that is provided as optional argument
	 *         of method or with locator string otherwise. For ex:
	 *         {locator:'id=eleId';desc:'sample element'} it will consider desc
	 *         as element description
	 */
	public String getDescription(String... label) {
		return (label != null) && (label.length > 0) ? label[0]
				: StringUtils.isBlank(description) ? this.toString() : description;
	}

	public void setDescription(String description) {
		if (JSONUtil.isValidJsonString(description)) {
			try {
				Map<String, Object> map = JSONUtil.toMap(description);
				this.description = map.containsKey("desc") ? (String) map.get("desc")
						: map.containsKey("description") ? (String) map.get("description") : "";
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			this.description = description;
		}
	}

	protected void initLoc(String locator) {
		this.locator = ConfigurationManager.getBundle().getString(locator, locator);
		this.locator = ConfigurationManager.getBundle().getSubstitutor().replace(this.locator);
		if (JSONUtil.isValidJsonString(this.locator)) {
			try {
				metaData.putAll(JSONUtil.toMap(this.locator));

				description = metaData.containsKey("desc") ? (String) metaData.get("desc")
						: metaData.containsKey("description") ? (String) metaData.get("description") : "";
				cacheable = metaData.containsKey("cacheable") ? (Boolean) metaData.get("cacheable") : false;
				if (metaData.containsKey("child") && !(Boolean) metaData.get("child")) {
					parentElement = null;
				}
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected Response execute(String command, Map<String, ?> parameters) {
		CommandTracker commandTracker = new CommandTracker(command, parameters);
		try {
			load();
			@SuppressWarnings("rawtypes")
			Map m = new HashMap<String, String>();
			m.putAll(parameters);
			m.put("id", id);
			commandTracker.setParameters(m);
			beforeCommand(this, commandTracker);
			// already handled in before command?
			if (commandTracker.getResponce() == null) {
				commandTracker.setStartTime(System.currentTimeMillis());
				commandTracker.setResponce(((QAFExtendedWebDriver) parent).executeWitoutLog(commandTracker.getCommand(),
						commandTracker.getParameters()));
				commandTracker.setEndTime(System.currentTimeMillis());

			}
			afterCommand(this, commandTracker);
			
			if ((null != getBy()) && !cacheable) {
				id = "-1";
			}
		} catch (RuntimeException e) {
			commandTracker.setException(e);
			onFailure(this, commandTracker);
		}

		if (commandTracker.hasException()) {
			if (commandTracker.retry) {
				commandTracker.setResponce(((QAFExtendedWebDriver) parent).executeWitoutLog(commandTracker.getCommand(),
						commandTracker.getParameters()));
				commandTracker.setException(null);
				commandTracker.setEndTime(System.currentTimeMillis());
			} else {
				throw commandTracker.getException();
			}
		}
		return commandTracker.getResponce();
	}

	protected Response executeWitoutLog(String command, Map<String, ?> parameters){
		return ((QAFExtendedWebDriver) parent).executeWitoutLog(command,
				parameters);
	}
	@Override
	public void setId(String id) {
		super.setId(id);
	}

	private void load() {
		if (null==id || (id == "-1")) {
			Map<String, ?> parameters = new HashMap<String, String>();
			CommandTracker commandTracker = new CommandTracker(DriverCommand.FIND_ELEMENT, parameters);
			if (parentElement == null) {
				beforeCommand(this, commandTracker);
				((QAFExtendedWebDriver) parent).load(this);
				afterCommand(this, commandTracker);
			} else {
				parentElement.load();
				beforeCommand(this, commandTracker);
				setId(parentElement.findElement(getBy()).id);
				afterCommand(this, commandTracker);
			}
			
		}
	}

	@Override
	public String getId() {
		if ((id == null) || (id == "-1")) {
			load();
		}
		return id;
	}

	@Override
	public Point getLocation() {
		id = getId();
		return super.getLocation();
	}

	@Override
	public String getCssValue(String propertyName) {
		Response response = execute("getElementValueOfCssProperty",
				ImmutableMap.of("id", id, "propertyName", propertyName));
		return ((String) response.getValue());
	}

	@Override
	public boolean isDisplayed() {
		id = getId();
		return super.isDisplayed();
	}

	@Override
	public Dimension getSize() {
		id = getId();
		return super.getSize();
	}

	public boolean isPresent() {
		if (StringUtils.isNotBlank(id) && id != "-1" && cacheable) {
			return true;
		}
		try {
			List<WebElement> eles = null;
			if ((parentElement != null)) {
				if (!parentElement.isPresent()) {
					return false;
				}
				eles = parentElement.findElements(getBy());

			} else {
				eles = getWrappedDriver().findElements(getBy());
			}
			if ((eles != null) && (eles.size() > 0)) {
				if (StringUtils.isBlank(id)) {
					id = ((QAFExtendedWebElement) eles.get(0)).id;
				}
				return true;
			}
			return false;
		} catch (WebDriverException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return null == getBy() ? id != "-1" ? "id: " + id : "New WebElement" : getBy().toString();
	}

	public WebDriverCommandLogger getReporter() {
		return getWrappedDriver().getReporter();
	}

	@Override
	public QAFExtendedWebDriver getWrappedDriver() {

		return (QAFExtendedWebDriver) parent;
	}

	public static class JsonConvertor extends JsonToWebElementConverter {
		private final RemoteWebDriver driver;

		public JsonConvertor(QAFExtendedWebDriver driver) {
			super(driver);
			this.driver = driver;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object apply(Object result) {
			if (result instanceof Collection<?>) {
				Collection<QAFExtendedWebElement> results = (Collection<QAFExtendedWebElement>) result;
				return Lists.newArrayList(Iterables.transform(results, this));
			}

			result = super.apply(result);
			if (result instanceof RemoteWebElement) {
				if (!(result instanceof QAFExtendedWebElement)) {
					QAFExtendedWebElement ele = newRemoteWebElement();
					ele.setId(((RemoteWebElement) result).getId());
					return ele;
				}
			}
			return result;
		}

		@Override
		protected QAFExtendedWebElement newRemoteWebElement() {
			String elemImpl = ConfigurationManager.getBundle().getString("default.element.impl");
			if(StringUtils.isBlank(elemImpl)){
				QAFExtendedWebElement toReturn = new QAFExtendedWebElement((QAFExtendedWebDriver) driver);
				return toReturn;
			}
			try {
				Class<?> cls = Class.forName(ConfigurationManager.getBundle().getString("default.element.impl", QAFExtendedWebElement.class.getCanonicalName()));
				Constructor<?> con = cls.getDeclaredConstructor(QAFExtendedWebDriver.class);
				con.setAccessible(true);
				Object toReturn = con.newInstance(driver);
				return (QAFExtendedWebElement) toReturn;
			} catch (ClassNotFoundException e) {
				throw new AutomationError("Unable to find class "+elemImpl+" to create element. ", e);
			} catch (Exception e) {
				throw new AutomationError("Unable to create element using "+elemImpl+". Make sure it is subclass of QAFExtendedWebElement and has consrtuctor excepting QAFExtendedWebDriver argument", e);
			}
		}
	}

	@Override
	public QAFExtendedWebElement findElement(By by) {
		load();
		QAFExtendedWebElement ele = (QAFExtendedWebElement) super.findElement(by);
		ele.parentElement = this;
		return ele;
	}

	public QAFExtendedWebElement findElement(String loc) {
		QAFExtendedWebElement ele = findElement(LocatorUtil.getBy(loc));
		ele.initLoc(loc);
		return ele;
	}

	public List<WebElement> findElements(By by) {
		try {
			return super.findElements(by);
		} catch (Exception e) {
			// may be this element present earlier and now not present so
			// returning element not found exception instead of returning empty
			// array.
			return new ArrayList<WebElement>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<QAFWebElement> findElements(String loc) {
		return (List<QAFWebElement>) (List<? extends WebElement>) findElements(LocatorUtil.getBy(loc));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends QAFExtendedWebElement> List<T> findElements(String loc, Class<T> t) {
		List<QAFWebElement> eles = findElements(loc);
		List<T> objs = new ArrayList<T>();
		for (QAFWebElement ele : eles) {
			T obj = (T) ComponentFactory.getObject(t, loc, this, this);
			obj.setId(((QAFExtendedWebElement) ele).getId());
			obj.parentElement = this;
			obj.cacheable = true;
			objs.add(obj);
		}
		return objs;
	}

	@SuppressWarnings("unchecked")
	public <T extends QAFExtendedWebElement> T findElement(String loc, Class<T> t) {
		T obj = (T) ComponentFactory.getObject(t, loc, this, this);
		obj.parentElement = this;
		obj.getId();
		return obj;
	}

	@Override
	public void afterCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(Stage.executingAfterMethod);

		for (QAFWebElementCommandListener listener : listners) {
			listener.afterCommand(element, commandTracker);
		}
	}

	@Override
	public void beforeCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(Stage.executingBeforeMethod);
		for (QAFWebElementCommandListener listener : listners) {
			listener.beforeCommand(element, commandTracker);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onFailure(QAFExtendedWebElement element, CommandTracker commandTracker) {
		commandTracker.setStage(Stage.executingOnFailure);
		commandTracker.setEndTime(System.currentTimeMillis());

		if (commandTracker.getException() instanceof StaleElementReferenceException) {
			logger.warn(commandTracker.getException().getMessage());
			element.setId("-1");
			Map parameters = commandTracker.getParameters();
			parameters.put("id", element.getId());
			commandTracker.setException(null);
			commandTracker.setStage(Stage.executingMethod);
			Response response = element.execute(commandTracker.command, parameters);
			commandTracker.setEndTime(System.currentTimeMillis());
			commandTracker.setResponce(response);
		}
		for (QAFWebElementCommandListener listener : listners) {
			// whether handled previous listener
			if (!commandTracker.hasException()) {
				break;
			}
			logger.debug("Executing listener " + listener.getClass().getName());
			listener.onFailure(element, commandTracker);
		}
	}

	// Wait service
	public void waitForVisible(long... timeout) {
		new QAFWebElementWait(this, timeout)
				.ignoring(RuntimeException.class, NoSuchElementException.class, StaleElementReferenceException.class)
				.withMessage("Wait time out for " + getDescription() + " to be visible")
				.until(QAFWebElementExpectedConditions.elementVisible());
	}

	public void waitForNotVisible(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(RuntimeException.class)
				.until(QAFWebElementExpectedConditions.elementNotVisible());

	}

	public void waitForDisabled(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(RuntimeException.class, NoSuchElementException.class)
				.withMessage("Wait time out for " + getDescription() + " to be disabled")
				.until(QAFWebElementExpectedConditions.elementDisabled());
	}

	public void waitForEnabled(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " to be enabled")
				.until(QAFWebElementExpectedConditions.elementEnabled());
	}

	public void waitForPresent(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " to be present")
				.until(QAFWebElementExpectedConditions.elementPresent());
	}

	public void waitForNotPresent(long... timeout) {
		new QAFWebElementWait(this, timeout).withMessage("Wait time out for " + getDescription() + " to not be present")
				.until(QAFWebElementExpectedConditions.elementNotPresent());
	}

	@Override
	public void waitForText(StringMatcher matcher, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " text " + matcher.toString())
				.until(QAFWebElementExpectedConditions.elementTextEq(matcher));
	}

	public void waitForText(String text, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " text " + text)
				.until(QAFWebElementExpectedConditions.elementTextEq(text));
	}

	public void waitForNotText(StringMatcher matcher, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " text not: " + matcher.toString())
				.until(QAFWebElementExpectedConditions.elementTextNotEq(matcher));
	}

	public void waitForNotText(String text, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " text not: " + text)
				.until(QAFWebElementExpectedConditions.elementTextNotEq(text));
	}

	public void waitForValue(Object value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " value" + value)
				.until(QAFWebElementExpectedConditions.elementValueEq(value));
	}

	public void waitForNotValue(Object value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " value not " + value)
				.until(QAFWebElementExpectedConditions.elementValueNotEq(value));
	}

	public void waitForSelected(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " to be selected")
				.until(QAFWebElementExpectedConditions.elementSelected());
	}

	public void waitForNotSelected(long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " to not selected")
				.until(QAFWebElementExpectedConditions.elementNotSelected());
	}

	public void waitForAttribute(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " " + name + " = " + value)
				.until(QAFWebElementExpectedConditions.elementAttributeValueEq(name, value));
	}

	@Override
	public void waitForAttribute(String attr, StringMatcher value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " " + attr + " = " + value)
				.until(QAFWebElementExpectedConditions.elementAttributeValueEq(attr, value));
	}

	public void waitForNotAttribute(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " " + name + "!=" + value)
				.until(QAFWebElementExpectedConditions.elementAttributeValueNotEq(name, value));
	}

	@Override
	public void waitForNotAttribute(String attr, StringMatcher value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " " + attr + " = " + value)
				.until(QAFWebElementExpectedConditions.elementAttributeValueNotEq(attr, value));
	}

	public void waitForCssClass(String name, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have css class " + name)
				.until(QAFWebElementExpectedConditions.elementHasCssClass(name));
	}

	public void waitForNotCssClass(String name, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have not css class" + name)
				.until(QAFWebElementExpectedConditions.elementHasNotCssClass(name));
	}

	public void waitForCssStyle(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have css style " + name + "=" + value)
				.until(QAFWebElementExpectedConditions.elementCssPropertyValueEq(name, value));
	}

	public void waitForNotCssStyle(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have css style " + name + "!=" + value)
				.until(QAFWebElementExpectedConditions.elementCssPropertyValueNotEq(name, value));
	}

	public void waitForCssStyleColor(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have css style " + name + "=" + value)
				.until(QAFWebElementExpectedConditions.elementCssColorPropertyValueEq(name, value));
	}

	public void waitForNotCssStyleColor(String name, String value, long... timeout) {
		new QAFWebElementWait(this, timeout).ignoring(NoSuchElementException.class, RuntimeException.class)
				.withMessage("Wait time out for " + getDescription() + " have css style " + name + "!=" + value)
				.until(QAFWebElementExpectedConditions.elementCssColorPropertyValueNotEq(name, value));
	}
	/**
	 * will only report if failed
	 * 
	 * @param label
	 * @return
	 */
	private boolean ensurePresent(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForPresent();
		} catch (Exception e) {
			outcome = false;
			report("present", outcome, msgFor);
		}

		return outcome;
	}

	// verifications
	public boolean verifyPresent(String... label) {
		boolean outcome = ensurePresent(label);

		if (outcome) {
			// Success message
			String msgFor = getDescription(label);
			report("present", outcome, msgFor);
		}

		return outcome;
	}

	/**
	 * @param label
	 *            to provide in message
	 * @return outcome of verification
	 */
	public boolean verifyNotPresent(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotPresent();
		} catch (Exception e) {
			outcome = false;

		}
		report("notpresent", outcome, msgFor);
		return outcome;
	}

	public boolean verifyVisible(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForVisible();
		} catch (Exception e) {
			outcome = false;

		}
		report("visible", outcome, msgFor);
		return outcome;
	}

	public boolean verifyNotVisible(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotVisible();
		} catch (Exception e) {
			outcome = false;

		}
		report("notvisible", outcome, msgFor);
		return outcome;
	}

	public boolean verifyEnabled(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForEnabled();
		} catch (Exception e) {
			outcome = false;

		}
		report("enabled", outcome, msgFor);
		return outcome;
	}

	public boolean verifyDisabled(String... label) {

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForDisabled();
		} catch (Exception e) {
			outcome = false;

		}
		report("disabled", outcome, msgFor);
		return outcome;
	}

	public boolean verifyText(String text, String... label) {
		if (!ensurePresent(label))
			return false;
		boolean outcome = true;
		String msgFor = getDescription(label);

		try {
			waitForText(text);
		} catch (Exception e) {
			outcome = false;

		}
		report("text", outcome, msgFor, text, getText());
		return outcome;
	}

	public boolean verifyNotText(String text, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotText(text);
		} catch (Exception e) {
			outcome = false;

		}
		report("nottext", outcome, msgFor, text, getText());
		return outcome;
	}

	@Override
	public boolean verifyNotText(StringMatcher matcher, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotText(matcher);
		} catch (Exception e) {
			outcome = false;

		}
		report("nottext", outcome, msgFor, matcher.toString(), getText());
		return outcome;
	}

	@Override
	public boolean verifyText(StringMatcher matcher, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForText(matcher);
		} catch (Exception e) {
			outcome = false;

		}
		report("text", outcome, msgFor, matcher.toString(), getText());
		return outcome;
	}

	public <T> boolean verifyValue(T value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForValue(value);
		} catch (Exception e) {
			outcome = false;

		}
		report("value", outcome, msgFor, value, getAttribute("value"));
		return outcome;
	}

	public <T> boolean verifyNotValue(T value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotValue(value);
		} catch (Exception e) {
			outcome = false;

		}
		report("notvalue", outcome, msgFor, value, getAttribute("value"));
		return outcome;
	}

	public boolean verifySelected(String... label) {
		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForSelected();
		} catch (Exception e) {
			outcome = false;

		}
		report("selected", outcome, msgFor);
		return outcome;
	}

	public boolean verifyNotSelected(String... label) {

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotSelected();
		} catch (Exception e) {
			outcome = false;

		}
		report("notselected", outcome, msgFor);
		return outcome;
	}

	public boolean verifyAttribute(String name, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForAttribute(name, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("attribute", outcome, msgFor, value, getAttribute(name));
		return outcome;
	}

	@Override
	public boolean verifyAttribute(String attr, StringMatcher matcher, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForAttribute(attr, matcher);
		} catch (Exception e) {
			outcome = false;

		}
		report("attribute", outcome, msgFor, matcher, getAttribute(attr));
		return outcome;
	}

	public boolean verifyNotAttribute(String name, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotAttribute(name, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("notattribute", outcome, msgFor, value, getAttribute(name));
		return outcome;
	}

	@Override
	public boolean verifyNotAttribute(String attr, StringMatcher matcher, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotAttribute(attr, matcher);
		} catch (Exception e) {
			outcome = false;

		}
		report("notattribute", outcome, msgFor, matcher, getAttribute(attr));
		return outcome;
	}

	public boolean verifyCssClass(String name, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForCssClass(name);
		} catch (Exception e) {
			outcome = false;

		}
		report("cssclass", outcome, msgFor, name, getAttribute("class"));
		return outcome;
	}

	public boolean verifyNotCssClass(String name, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotCssClass(name);
		} catch (Exception e) {
			outcome = false;

		}
		report("notcssclass", outcome, msgFor, name, getAttribute("class"));
		return outcome;
	}

	public boolean verifyCssStyle(String name, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForCssStyle(name, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("cssstyle", outcome, msgFor, value, getCssValue(name));
		return outcome;
	}

	public boolean verifyNotCssStyle(String name, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotCssStyle(name, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("notcssstyle", outcome, msgFor, value, getCssValue(name));
		return outcome;
	}

	@Override
	public boolean verifyCssStyleColor(String prop, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForCssStyleColor(prop, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("cssstyle", outcome, msgFor, value, getCssValue(prop));
		return outcome;
	}

	@Override
	public boolean verifyNotCssStyleColor(String prop, String value, String... label) {
		if (!ensurePresent(label))
			return false;

		boolean outcome = true;
		String msgFor = getDescription(label);
		try {
			waitForNotCssStyleColor(prop, value);
		} catch (Exception e) {
			outcome = false;

		}
		report("notcssstyle", outcome, msgFor, value, getCssValue(prop));
		return outcome;
	}

	// preconditions
	public void givenPresent() {
		if (!verifyPresent()) {
			throw new SkipException("Precondition failed:" + getDescription() + " should be present");
		}
	}

	public void givenNotPresent(String... label) {
		if (!verifyNotPresent(label)) {
			throw new SkipException("Precondition failed:"
					+ WebDriverCommandLogger.getMsgForElementOp("notpresent", false, getDescription(label)));
		}
	}

	// assertions
	public void assertPresent(String... label) {
		if (!verifyPresent(label)) {
			throw new AssertionError();
		}
	}

	public void assertNotPresent(String... label) {
		if (!verifyNotPresent(label)) {
			throw new AssertionError();
		}
	}

	public void assertVisible(String... label) {
		if (!verifyVisible(label)) {
			throw new AssertionError();
		}
	}

	public void assertNotVisible(String... label) {
		if (!verifyNotVisible(label)) {
			throw new AssertionError();
		}
	}

	public void assertEnabled(String... label) {
		if (!verifyEnabled(label)) {
			throw new AssertionError();
		}
	}

	public void assertDisabled(String... label) {
		if (!verifyDisabled(label)) {
			throw new AssertionError();
		}
	}

	public void assertText(String text, String... label) {
		if (!verifyText(text, label)) {
			throw new AssertionError();
		}
	}

	public void assertNotText(String text, String... label) {
		if (!verifyNotText(text, label)) {
			throw new AssertionError();
		}
	}

	@Override
	public void assertText(StringMatcher matcher, String... label) {
		if (!verifyText(matcher, label)) {
			throw new AssertionError();
		}
	}

	@Override
	public void assetNotText(StringMatcher matcher, String... label) {
		if (!verifyNotText(matcher, label)) {
			throw new AssertionError();
		}
	}

	public <T> void assertValue(T value, String... label) {
		if (!verifyValue(value, label)) {
			throw new AssertionError();
		}
	}

	public <T> void assertNotValue(T value, String... label) {
		if (!verifyNotValue(value, label)) {
			throw new AssertionError();
		}
	}

	public void assertSelected(String... label) {
		if (!verifySelected(label)) {
			throw new AssertionError();
		}
	}

	public void assertNotSelected(String... label) {
		if (!verifyNotSelected(label)) {
			throw new AssertionError();
		}
	}

	public void assertAttribute(String name, String value, String... label) {
		if (!verifyAttribute(name, value, label)) {
			throw new AssertionError();
		}
	}

	@Override
	public void assertAttribute(String attr, StringMatcher matcher, String... label) {
		if (!verifyAttribute(attr, matcher, label)) {
			throw new AssertionError();
		}
	}

	public void assertNotAttribute(String name, String value, String... label) {
		if (!verifyNotAttribute(name, value, label)) {
			throw new AssertionError();
		}
	}

	@Override
	public void assertNotAttribute(String attr, StringMatcher matcher, String... label) {
		if (!verifyNotAttribute(attr, matcher, label)) {
			throw new AssertionError();
		}
	}

	public void assertCssClass(String name, String... label) {
		if (!verifyCssClass(name, label)) {
			throw new AssertionError();
		}
	}

	public void assertNotCssClass(String name, String... label) {
		if (!verifyNotCssClass(name, label)) {
			throw new AssertionError();
		}
	}

	public void assertCssStyle(String name, String value, String... label) {
		if (!verifyCssStyle(name, value, label)) {
			throw new AssertionError();
		}
	}

	public void assertNotCssStyle(String name, String value, String... label) {
		if (!verifyNotCssStyle(name, value, label)) {
			throw new AssertionError();
		}
	}

	@Override
	public void assertCssStyleColor(String prop, String value, String... label) {
		if (!verifyCssStyleColor(prop, value, label)) {
			throw new AssertionError();
		}		
	}

	@Override
	public void assertNotCssStyleColor(String prop, String value, String... label) {
		if (!verifyNotCssStyleColor(prop, value, label)) {
			throw new AssertionError();
		}		
	}

	@SuppressWarnings("unchecked")
	public <T> T executeScript(String js) {
		JavascriptExecutor executor = getWrappedDriver();
		return (T) executor.executeScript("arguments[0]." + js, this);
	}

	@SuppressWarnings("unchecked")
	public <T> T executeAsyncScript(String js) {
		JavascriptExecutor executor = getWrappedDriver();
		return (T) executor.executeAsyncScript("arguments[0]." + js, this);
	}

	public void setAttribute(String name, String value) {
		executeScript(name + "=" + value);
	}

	public void registerListeners(QAFWebElementCommandListener listener) {
		listners.add(listener);

	}

	protected void report(String op, boolean outcome, Object... args) {
		getReporter().addMessage(WebDriverCommandLogger.getMsgForElementOp(op, outcome, args),
				(outcome ? MessageTypes.Pass : MessageTypes.Fail));
	}

	private void registerListeners(String className) {
		try {
			QAFWebElementCommandListener cls = (QAFWebElementCommandListener) Class.forName(className).newInstance();
			listners.add(cls);
		} catch (Exception e) {
			logger.error("Unable to register listener class " + className, e);
		}
	}

	@Override
	public void setParent(RemoteWebDriver parent) {
		if (null == parent) {
			parent = new WebDriverTestBase().getDriver();
		}
		super.setParent(parent);
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public QAFWebElement findElementByCustomStretegy(String stetegy, String loc) {
		return (QAFWebElement) findElement(stetegy, loc);
	}

	@SuppressWarnings("unchecked")
	public List<WebElement> findElementsByCustomStretegy(String stetegy, String loc) {
		return (List<WebElement>) (List<? extends WebElement>) findElements(stetegy, loc);
	}
}
