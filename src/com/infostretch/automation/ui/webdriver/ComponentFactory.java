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


package com.infostretch.automation.ui.webdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * com.infostretch.automation.ui.webdriver.custom.ComponentFactory.java
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
