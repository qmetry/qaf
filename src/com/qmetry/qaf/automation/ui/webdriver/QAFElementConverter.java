/**
 * 
 */
package com.qmetry.qaf.automation.ui.webdriver;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * @author chirag
 *
 */
public class QAFElementConverter implements Function<Object, Object> {
	private final QAFExtendedWebDriver driver;

	public QAFElementConverter(QAFExtendedWebDriver driver) {
		this.driver = driver;
	}
	@Override
	public Object apply(Object result) {
		if (result instanceof Collection<?>) {
			Collection<?> results = (Collection<?>) result;
			return Lists.newArrayList(Iterables.transform(results, this));
		}
		if (result instanceof RemoteWebElement) {
			if (!(result instanceof QAFExtendedWebElement)) {
				QAFExtendedWebElement ele = newRemoteWebElement();
				ele.setId(((RemoteWebElement) result).getId());
				return ele;
			}
		}
		return result;
	}
	
	private QAFExtendedWebElement newRemoteWebElement() {
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
