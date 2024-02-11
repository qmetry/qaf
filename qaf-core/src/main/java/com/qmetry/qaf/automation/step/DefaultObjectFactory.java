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
package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.inject.Inject;

import com.qmetry.qaf.automation.ui.api.TestPage;
//import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

/**
 * Default {@link ObjectFactory} used by step factory to create object of step
 * provider class. To share instance between step and scenario set
 * `step.provider.sharedinstance` property to `true`
 * 
 * @author chirag.jayswal
 *
 */
public class DefaultObjectFactory implements ObjectFactory {
	public static final String OBJ_STORE_PREFIX = "shared.object.store";

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(Class<T> cls) throws Exception {
		if (getBundle().getBoolean("step.provider.sharedinstance", false) && isSharableInstance(cls)) {
			// allow class variable sharing among steps
			String key = OBJ_STORE_PREFIX + "." + cls.getName();
			Object obj = getBundle().getObject(key);
			if (null == obj) {
				obj = createInstance(cls);
				inject(obj);
				getBundle().setProperty(key, obj);
			}
			return (T) obj;
		}
		Object obj = createInstance(cls);
		inject(obj);
		return (T) obj;
	}

	private void inject(Object obj) {
		try {
			// new ElementFactory().initFields(obj);
			Field[] flds = obj.getClass().getDeclaredFields();
			for (Field fld : flds) {
				if (fld.isAnnotationPresent(Inject.class)) {
					fld.setAccessible(true);
					Object value = getObject(fld.getType());
					fld.set(obj, value);
				}
			}
		} catch (Exception e) {

		}
	}

	private Object createInstance(Class<?> cls) throws Exception {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			// only public constructors with or without parameter(s) to be
			// considered!...
			Constructor<?> con = cls.getConstructors()[0];
			con.setAccessible(true);
			ArrayList<Object> args = new ArrayList<Object>();
			for (Class<?> param : con.getParameterTypes()) {
				args.add(getObject(param));
			}
			return con.newInstance(args.toArray(new Object[args.size()]));

		}
	}

	private boolean isSharableInstance(Class<?> cls) {

		if (TestPage.class.isAssignableFrom(cls) ) { //|| QAFWebElement.class.isAssignableFrom(cls)) {
			return false;
		}
		return true;
	}
}
