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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * com.qmetry.qaf.automation.ui.webdriver.QAFWebElementProxyHandler.java
 * 
 * @author chirag
 */
public class QAFWebElementProxyHandler implements InvocationHandler {

	private final QAFExtendedWebElement element;

	public QAFWebElementProxyHandler(QAFExtendedWebElement element) {
		this.element = element;
	}

	@Override
	public Object invoke(Object paramObject, Method method, Object[] args) throws Throwable {
		try {
			if ("toString".equals(method.getName())) {
				return method.invoke(element, args);
			}
			Object result;
			try {
				result = method.invoke(element, args);
			} catch (Exception ite) {
				result = method.invoke(element, args);
			}
			return result;
		} catch (Throwable cause) {
			throw cause;
		}

	}

}
