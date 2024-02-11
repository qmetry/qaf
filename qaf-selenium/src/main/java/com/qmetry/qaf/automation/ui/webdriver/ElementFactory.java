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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.data.MetaDataHelper;
import com.qmetry.qaf.automation.ui.AbstractTestPage;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.StringUtil;

public class ElementFactory {
	private static final Log logger = LogFactory.getLog(ElementFactory.class);

	private SearchContext context;

	public ElementFactory() {
		context = new WebDriverTestBase().getDriver();
	}

	public ElementFactory(SearchContext context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public void initFields(Object classObj) {
		Field[] flds = ClassUtil.getAllFields(classObj.getClass(), AbstractTestPage.class);
		for (Field field : flds) {
			try {
				field.setAccessible(true);
				if (isDecoratable(field)) {
					Object value = null;
					if (hasAnnotation(field, FindBy.class, FindBys.class)) {
						Annotations annotations = new Annotations(field);
						boolean cacheElement = annotations.isLookupCached();
						By by = annotations.buildBy();
						if (List.class.isAssignableFrom(field.getType())) {
							value = initList(by, context);
						} else {
							if (context instanceof WebElement) {
								value = new QAFExtendedWebElement((QAFExtendedWebElement) context, by);
							} else {
								value = new QAFExtendedWebElement((QAFExtendedWebDriver) context, by, cacheElement);
							}
							initMetadata(classObj, field, (QAFExtendedWebElement) value);

						}
					} else {
						com.qmetry.qaf.automation.ui.annotations.FindBy findBy = field
								.getAnnotation(com.qmetry.qaf.automation.ui.annotations.FindBy.class);
						if (List.class.isAssignableFrom(field.getType())) {
							value = initList(field, findBy.locator(), context, classObj);
						} else {
							if (QAFWebComponent.class.isAssignableFrom(field.getType())) {
								value = ComponentFactory.getObject(field.getType(), findBy.locator(), classObj,
										context);

							} else {
								value = $(findBy.locator());

								if (context instanceof QAFExtendedWebElement) {
									((QAFExtendedWebElement) value).parentElement = (QAFExtendedWebElement) context;
								}
							}
							initMetadata(classObj, field, (QAFExtendedWebElement) value);
						}
					}

					field.set(classObj, value);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private void initMetadata(Object classObj, Field field, QAFExtendedWebElement value) {
		if (null == value)
			return;
		value.getMetaData().put("pageClass", classObj.getClass());
		value.getMetaData().put("objectName", field.getName());
		value.getMetaData().putAll(MetaDataHelper.getMetadata(field));
	}

	private boolean hasAnnotation(Field field, Class<? extends Annotation>... classes) {
		for (Class<? extends Annotation> cls : classes) {
			if (field.isAnnotationPresent(cls)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isDecoratable(Field field) {
		if (!hasAnnotation(field, com.qmetry.qaf.automation.ui.annotations.FindBy.class, FindBy.class, FindBys.class)) {
			return false;
		}
		if (WebElement.class.isAssignableFrom(field.getType())) {
			return true;
		}
		if (!(List.class.isAssignableFrom(field.getType()))) {
			return false;
		}
		Type genericType = field.getGenericType();
		if (!(genericType instanceof ParameterizedType)) {
			return false;
		}
		Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

		return WebElement.class.isAssignableFrom((Class<?>) listType);
	}

	private Class<?> getListType(Field field) {
		Type genericType = field.getGenericType();
		Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
		return (Class<?>) listType;

	}

	private Object initList(By by, SearchContext context) throws Exception {
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { List.class },
				new QAFExtendedWebElementListHandler(context, by));
	}

	@SuppressWarnings("unchecked")
	private Object initList(Field field, String loc, SearchContext context, Object clsObject) throws Exception {
		loc = ConfigurationManager.getBundle().getString(loc, loc);
		Class<? extends QAFExtendedWebElement> cls = (Class<? extends QAFExtendedWebElement>) getListType(field);
		InvocationHandler iHandler = QAFWebComponent.class.isAssignableFrom(cls)
				? new ComponentListHandler(context, loc, cls, clsObject)
				: new ComponentListHandler(context, loc, getElemenetClass(), clsObject);
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { List.class }, iHandler);
	}

	public static QAFWebElement $(String loc) {
		QAFExtendedWebElement eleToReturn = new QAFExtendedWebElement(loc);
		String compClass = eleToReturn.getMetaData().containsKey("component-class")
				? (String) eleToReturn.getMetaData().get("component-class")
				: ConfigurationManager.getBundle().getString("default.element.impl");
		if (StringUtil.isNotBlank(compClass)) {
			try {
				return (QAFWebElement) ComponentFactory.getObject(Class.forName(compClass), loc, null);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return eleToReturn;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends QAFExtendedWebElement> getElemenetClass(){
		try {
			Class<?> cls = Class.forName(ConfigurationManager.getBundle().getString("default.element.impl", QAFExtendedWebElement.class.getCanonicalName()));
			return (Class<? extends QAFExtendedWebElement>) cls;
		} catch (Exception e) {
			return QAFExtendedWebElement.class;
		}

	}
}
