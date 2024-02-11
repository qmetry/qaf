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
package com.qmetry.qaf.automation.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.json.JSONObject;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.ui.annotations.UiElement;
import com.qmetry.qaf.automation.ui.annotations.UiElement.Type;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * Base class to create FormDatabean which is kind of Data-bean specifically
 * design to interact with UI forms. You can map bean properties with UI form
 * fields by providing {@link UiElement} annotation with property and fill/fetch
 * data to/from UI form.
 * 
 * @see UiElement
 * @author Chirag Jayswal.
 */
public class BaseFormDataBean extends BaseDataBean {
	transient private Field[] allFields;
	transient protected final ElementInteractor interactor;

	public BaseFormDataBean() {
		interactor = new ElementInteractor();
	}

	/**
	 * it will fill up UI form fields given in argument with the property value
	 * one by one in order provided by {@link UiElement#order()}. If no argument
	 * if provided then it will fill all fields except read-only. To provide
	 * property specific custom implementation create filler method just like
	 * setter with Object as argument. For example if bean property is "
	 * <code>String foo</code>" then the filler method would be
	 * <code>fillFoo(){}</code>.
	 * 
	 * @see UiElement
	 * @param fieldLocs
	 *            optional field locators
	 */
	public void fillUiElements(String... fieldLocs) {
		List<String> includeLst = null;
		if ((fieldLocs != null) && (fieldLocs.length > 0)) {
			includeLst = Arrays.asList(fieldLocs);
		}
		for (Field field : getFields()) {
			if (field.isAnnotationPresent(UiElement.class)
					&& ((includeLst == null) || includeLst.contains(field.getName())
							|| includeLst.contains(field.getAnnotation(UiElement.class).fieldLoc()))) {
				fillUiData(field);
			}
		}
	}

	public void fillUiRequiredElements() {
		for (Field field : getFields()) {
			if ((field.isAnnotationPresent(UiElement.class) && field.getAnnotation(UiElement.class).required())) {
				fillUiData(field);
			}
		}
	}

	final public void fetchUiElements(String... fieldLocs) {
		Field[] flds = getFields();// this.getClass().getDeclaredFields();
		List<String> includeLst = null;
		if ((fieldLocs != null) && (fieldLocs.length > 0)) {
			includeLst = Arrays.asList(fieldLocs);
		}
		for (Field fld : flds) {
			fld.setAccessible(true);
			if (!Modifier.isFinal(fld.getModifiers()) && fld.isAnnotationPresent(UiElement.class)) {
				UiElement map = fld.getAnnotation(UiElement.class);
				if ((includeLst == null) || includeLst.contains(fld.getName()) || includeLst.contains(map.fieldLoc())) {
					Object val = fetchUiData(fld);
					setField(fld, String.valueOf(val));
				}
			}
		}
	}

	final public boolean verifyUiElements(String... fieldmapnames) {
		Field[] flds = this.getClass().getDeclaredFields();
		List<String> includeLst = null;
		boolean outcome = true;
		if ((fieldmapnames != null) && (fieldmapnames.length > 0)) {
			includeLst = Arrays.asList(fieldmapnames);
		}
		for (Field fld : flds) {
			fld.setAccessible(true);
			if (fld.isAnnotationPresent(UiElement.class)) {
				UiElement map = fld.getAnnotation(UiElement.class);
				String mapName = StringUtil.isNotBlank(map.viewLoc()) ? map.viewLoc() : fld.getName();
				if ((includeLst == null) || includeLst.contains(mapName)) {
					outcome = outcome && verifyUiData(fld);
				}
			}
		}
		return outcome;
	}

	final public boolean verifyUiVaules(String... fieldmapnames) {
		Field[] flds = this.getClass().getDeclaredFields();
		List<String> includeLst = null;
		boolean outcome = true;
		if ((fieldmapnames != null) && (fieldmapnames.length > 0)) {
			includeLst = Arrays.asList(fieldmapnames);
		}
		for (Field fld : flds) {
			fld.setAccessible(true);
			if (fld.isAnnotationPresent(UiElement.class)) {
				UiElement map = fld.getAnnotation(UiElement.class);
				String mapName = StringUtil.isNotBlank(map.viewLoc()) ? map.viewLoc() : fld.getName();
				if ((includeLst == null) || includeLst.contains(mapName)) {
					outcome = outcome && verifyUiData(fld);
				}
			}
		}
		return outcome;
	}

	final public <T> boolean setBeanData(String propNameOrMapping, T data) {
		Field fld = getField(propNameOrMapping);
		try {
			setField(fld, String.valueOf(data));
			return true;
		} catch (Exception e) {
			logger.error("Error while geting field " + fld.getName() + "  data", e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	final public <T> T getBeanData(String propNameOrMapping) {
		Field fld = getField(propNameOrMapping);
		try {
			fld.setAccessible(true);
			return (T) fld.get(this);
		} catch (Exception e) {
			logger.error("Error while geting field " + fld.getName() + "  data", e);
		}
		return null;
	}

	/**
	 * finds bean property with given name or filedLoc or viewLoc
	 * 
	 * @param nameOrLoc
	 * @return field or throws runtime exception.
	 */
	protected Field getField(String nameOrLoc) {
		Field[] flds = getFields();// this.getClass().getDeclaredFields();

		for (Field fld : flds) {
			if (fld.getName().equalsIgnoreCase(nameOrLoc)) {
				return fld;
			}
			UiElement element = fld.getAnnotation(UiElement.class);
			if ((null != element) && (element.fieldLoc().equalsIgnoreCase(nameOrLoc)
					|| element.viewLoc().equalsIgnoreCase(nameOrLoc))) {
				return fld;
			}
		}
		throw new RuntimeException("No property found to map with " + nameOrLoc);
	}

	protected String getMappedFieldLoc(Field fld) {
		if (fld.isAnnotationPresent(UiElement.class)) {
			UiElement map = fld.getAnnotation(UiElement.class);

			return map.fieldLoc();
		}
		return "";
	}

	protected String getMappedViewLoc(Field fld) {
		if (fld.isAnnotationPresent(UiElement.class)) {
			UiElement map = fld.getAnnotation(UiElement.class);
			return map.viewLoc();
		}
		return "";
	}

	protected boolean checkParent(String parent, String depVal) {
		String parentval = String.valueOf((Object)getBeanData(parent));
		if (depVal.equalsIgnoreCase(parentval) || resolveExpr(depVal)) {
			return true;
		}
		return false;
	}

	/**
	 * returns ordered fields array. Order can be defined by
	 * {@link UiElement#order()}
	 */
	@Override
	protected Field[] getFields() {
		if (null == allFields) {
			allFields = ClassUtil.getAllFields(this.getClass(), BaseFormDataBean.class);
			Arrays.sort(allFields, new FieldsComparator());
		}
		return allFields;
	}

	private static boolean isExpr(String str) {
		Pattern p = Pattern.compile("\\$\\{(\\w+)\\}", Pattern.MULTILINE);
		Matcher m = p.matcher(str);
		return m.find();
	}

	private Boolean resolveExpr(String strExpr) {
		// the pattern we want to search for
		Pattern p = Pattern.compile("\\$\\{(\\w+)\\}", Pattern.MULTILINE);
		Matcher m = p.matcher(strExpr);
		logger.info("Evaluating expr: " + strExpr);

		// print all the matches that we find
		while (m.find()) {
			String param = m.group(1);
			Object paramVal = getBeanData(m.group(1));
			logger.info("parameter " + param + ": " + paramVal);
			strExpr = strExpr.replaceAll("\\$\\{" + param + "\\}", String.valueOf(paramVal));
		}
		try {
			return (Boolean)StringUtil.eval(strExpr, toMap());
		} catch (ScriptException e) {
			logger.error("Unable to evaluate dependency condition: " + strExpr, e);
			throw new AutomationError("Unable to evaluate dependency condition: " + strExpr, e);
		}
	}

	private Object fetchUiData(Field field) {
		try {
			Method m = this.getClass().getDeclaredMethod("fetch" + StringUtil.getTitleCase(field.getName()));
			m.setAccessible(true);
			logger.debug("invoking custom fetch method for field " + field.getName());
			return m.invoke(this);

		} catch (Exception e) {
		}
		UiElement map = field.getAnnotation(UiElement.class);
		if ((null == map)) {
			return null;
		}
		Type type = map.fieldType();
		String loc = map.fieldLoc();
		Class<? extends QAFExtendedWebElement> eleClass = map.elementClass();

		return interactor.fetchValue(loc, type,eleClass);
	}

	private void fillUiData(Field fld) {
		UiElement params = fld.getAnnotation(UiElement.class);
		if ((null == params) || params.readonly()) {
			return;
		}
		Type type = params.fieldType();
		String loc = params.fieldLoc();
		String val = params.defaultValue();
		String depends = params.dependsOnField();
		String depVal = params.dependingValue();
		Class<? extends QAFExtendedWebElement> eleClass = params.elementClass();

		try {
			fld.setAccessible(true);
			Object o = fld.get(this);
			logger.debug("value " + o + " for" + fld.getName());

			val = (null == o ? null : String.valueOf(o));
		} catch (NullPointerException e) {
			val = null;
		} catch (Exception e) {
			logger.error("Unable to get data from bean for " + fld.getName(), e);

		}

		if ((val != null)
				&& (StringUtil.isBlank(depends) || StringUtil.isBlank(depVal) || checkParent(depends, depVal))) {
			try {
				Method m = ClassUtil.getMethod(this.getClass(), "fill" + StringUtil.getTitleCase(fld.getName()));
				m.setAccessible(true);
				logger.debug("invoking custom fill method for field " + fld.getName());

				m.invoke(this);

			} catch (NoSuchMethodException nse) {
				interactor.fillValue(loc, val, type, eleClass);
			} catch (Exception e) {
				logger.error("Unable to invoke custom fill method for field " + fld.getName(), e);
				throw new AutomationError("Unable to invoke custom fill method for field " + fld.getName(), e);
			}
		}
	}

	private boolean verifyUiData(Field fld) {
		UiElement map = fld.getAnnotation(UiElement.class);
		Type type = map.fieldType();
		String loc = map.fieldLoc();
		String depends = map.dependsOnField();
		String depVal = map.dependingValue();
		Class<? extends QAFExtendedWebElement> eleClass = map.elementClass();

		try {
			if (StringUtil.isBlank(depends) || checkParent(depends, depVal)) {
				return interactor.verifyValue(loc, String.valueOf(getBeanData(loc)), type,eleClass);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		// Skipped so just return success
		return true;

	}

	private class FieldsComparator implements Comparator<Field> {

		@Override
		public int compare(Field o1, Field o2) {
			return getOrder(o1) - getOrder(o2);
		}

		private int getOrder(Field f) {
			return f.isAnnotationPresent(UiElement.class) ? f.getAnnotation(UiElement.class).order()
					: Integer.MAX_VALUE;
		}

	}

}
