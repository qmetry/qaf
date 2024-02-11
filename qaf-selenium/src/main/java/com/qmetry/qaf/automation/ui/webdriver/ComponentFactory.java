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
import java.lang.reflect.Modifier;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * com.qmetry.qaf.automation.ui.webdriver.custom.ComponentFactory.java
 * 
 * @author chirag
 */
public class ComponentFactory {
	public static Object getObject(Class<?> component, String loc, Object declaringClass) {
		try {
			if ((component.getDeclaringClass() != null) && !Modifier.isStatic(component.getModifiers())) {
				Constructor<?> con = component.getDeclaredConstructor(declaringClass.getClass(), String.class);
				con.setAccessible(true);
				return con.newInstance(declaringClass, loc);
			} else {
				Constructor<?> con = component.getDeclaredConstructor(String.class);
				con.setAccessible(true);
				return con.newInstance(loc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Object getObject(Class<?> component, String loc, Object declaringClass, SearchContext parent) {
		if ((parent == null) || !(parent instanceof WebElement)) {
			return getObject(component, loc, declaringClass);
		}
		try {
			if ((component.getDeclaringClass() != null) && !Modifier.isStatic(component.getModifiers())) {
				Constructor<?> con = component.getDeclaredConstructor(declaringClass.getClass(),
						QAFExtendedWebElement.class, String.class);
				con.setAccessible(true);
				return con.newInstance(declaringClass, parent, loc);
			} else {
				Constructor<?> con = component.getDeclaredConstructor(QAFExtendedWebElement.class, String.class);
				con.setAccessible(true);
				return con.newInstance(parent, loc);
			}
		} catch (Exception e) {
			Object obj = getObject(component, loc, declaringClass);
			if (obj != null && (parent instanceof QAFExtendedWebElement)
			// && (component.getDeclaringClass() != null)
			// && (QAFWebElement.class
			// .isAssignableFrom(component.getDeclaringClass()))
			) {
				((QAFExtendedWebElement) obj).parentElement = (QAFExtendedWebElement) parent;
			}
			return obj;
		}
	}
}
