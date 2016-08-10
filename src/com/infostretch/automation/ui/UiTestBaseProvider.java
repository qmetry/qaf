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


package com.infostretch.automation.ui;

import java.util.Iterator;
import java.util.Vector;

import com.infostretch.automation.ui.api.UiTestBase;

/**
 * com.infostretch.automation.api.TestBaseProvider.java
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
