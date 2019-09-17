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

import java.util.HashMap;
import java.util.Map;

/**
 * com.qmetry.qaf.automation.util.KeyValuePair.java
 * 
 * @author chirag
 */
public class KeyValuePair {
	private Map<Object, Object> keyMap = new HashMap<Object, Object>();

	public KeyValuePair(Object... objects) {
		for (int i = 0; i < objects.length; i += 2) {
			keyMap.put(objects[i], objects[i + 1]);
		}
	}

	public void add(Object key, Object value) {
		keyMap.put(key, value);
	}

	public Object getValue(Object key) {

		return keyMap.get(key);
	}

	public String getStringValue(Object key) {
		return keyMap.get(key).toString();
	}

	public int getIntValue(Object key) {
		return (Integer) keyMap.get(key);
	}
}
