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
package com.qmetry.qaf.automation.ui;

import java.util.Iterator;
import java.util.Vector;

import com.qmetry.qaf.automation.ui.api.UiTestBase;

/**
 * com.qmetry.qaf.automation.api.TestBaseProvider.java
 * 
 * @author chirag
 */
public class UiTestBaseProvider<T extends UiTestBase<?>> extends ThreadLocal<T> {
	Vector<T> lst = new Vector<T>();
	final Class<T> clazz;

	@Override
	protected T initialValue() {
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		lst.add(obj);
		return obj;
	}

	@Override
	public void remove() {
		get().tearDown();
		super.remove();
	}

	@Override
	public void set(T value) {
		if (null == value) {
			remove();
		} else {
			super.set(value);
		}
	}

	public Vector<T> getAll() {
		return lst;
	}

	public void stopAll() {
		Iterator<T> iter = lst.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
	}

	public void prepareForShutDown() {
		Iterator<T> iter = lst.iterator();
		while (iter.hasNext()) {
			(iter.next()).prepareForShutDown();
		}
	}

	/**
	 * UiTestBaseProvider<WebDriverTestBase> is loaded on the first access to
	 * WebDriverTestBaseHolder.get(), not before.
	 */
	public static class WebDriverTestBaseHolder {
		public static final UiTestBaseProvider<WebDriverTestBase> INSTANCE = new UiTestBaseProvider<WebDriverTestBase>(
				WebDriverTestBase.class);

		public static WebDriverTestBase get() {
			return INSTANCE.get();
		}
	}

	/**
	 * UiTestBaseProvider<SeleniumTestBase> is loaded on the first access to
	 * SeleniumTestBaseHolder.get(), not before.
	 */
	public static class SeleniumTestBaseHolder {
		public static final UiTestBaseProvider<SeleniumTestBase> INSTANCE = new UiTestBaseProvider<SeleniumTestBase>(
				SeleniumTestBase.class);

		public static SeleniumTestBase get() {
			return INSTANCE.get();
		}
	}

	// Restricted from outside
	private UiTestBaseProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

}
