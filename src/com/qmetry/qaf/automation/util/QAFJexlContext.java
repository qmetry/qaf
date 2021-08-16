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
package com.qmetry.qaf.automation.util;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.jexl3.JexlContext;

/**
 * Each entry in the map, fully qualified class names, properties are considered
 * a variable name, value pair. If entry key is not following variable name
 * standards (contains space, - or other special characters) , it can be
 * accessed by _ctx['variable name']
 * 
 * @author chirag.jayswal
 *
 */
public class QAFJexlContext implements JexlContext {

	private Map<String, Object> _ctx;

	public QAFJexlContext() {
		_ctx = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		set("_ctx", this);
	}

	/**
	 * Constructs a <code>QAFJexlContext</code> that wraps the existing
	 * <code>Map</code> of bean names to bean values.
	 * 
	 * @param beans A <code>Map</code> of bean names to bean values.
	 */
	public QAFJexlContext(Map<? extends String, ? extends Object> beans) {
		this();
		if (null != beans) {
			_ctx.putAll(beans);
		}
	}

	@Override
	public boolean has(String name) {
		return _ctx.containsKey(name) || getClass(name) != null || getBundle().containsKey(name);
	}

	@Override
	public Object get(String name) {
		Object value = _ctx.get(name);
		// Check for a legitimate null value for a variable name before
		// attempting to resolve a class name.
		if (value == null && !_ctx.containsKey(name) && (value = getObject(name)) == null) {
			value = getClass(name);
		}
		return value;
	}

	@Override
	public void set(String name, Object value) {
		_ctx.put(name, value);
	}

	/**
	 * Clears all variables.
	 */
	public void clear() {
		_ctx.clear();
	}

	private Class<?> getClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	private Object getObject(String name) {
		Object value = getBundle().getProperty(name);
		if (value instanceof String) {
			String res = getBundle().getSubstitutor().replace((String) value);
			value = JSONUtil.toObject(res);
		}
		return value;
	}
}
