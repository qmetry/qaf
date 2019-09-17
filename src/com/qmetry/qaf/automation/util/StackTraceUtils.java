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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.qmetry.qaf.automation.step.JavaStep;
import com.qmetry.qaf.automation.step.TestStep;

public final class StackTraceUtils {

	public static final String LINE_NUMBER_SEPARATOR = "#";
	private final static String WAIT_CLASS_NAME = "Wait";

	private StackTraceUtils() {
		// Static Class
	}

	public static boolean isWaitInvolved() {
		return StackTraceUtils.isClassInStackTrace(Thread.currentThread().getStackTrace(), "." + WAIT_CLASS_NAME);
	}

	public static StackTraceElement getCurrentCallingClassAsStackTraceElement() {
		return StackTraceUtils.getCurrentCallingClassAsStackTraceElement(Thread.currentThread().getStackTrace(),
				"DefaultSelenium");
	}

	/**
	 * Debugs the current stack-trace on stdout .
	 */
	public static void debugStackTrace() {
		StackTraceElement[] testElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTraceElement : testElements) {
			System.out.println(stackTraceElementWithLinenumber(stackTraceElement));
		}
	}

	/**
	 * Generates a string from trace for the current class with linenumber.
	 * 
	 * @param stackTraceElement
	 *            to get class name and linenumber
	 * @return string representation
	 */
	public static String stackTraceElementWithLinenumber(StackTraceElement stackTraceElement) {
		if (null != stackTraceElement) {
			return stackTraceElement.getClassName() + LINE_NUMBER_SEPARATOR + stackTraceElement.getLineNumber();
		} else {
			return "Internal ERROR stackTraceElement should not be null";
		}
	}

	/**
	 * @param stackTraceElement
	 *            search className here
	 * @param classNameToSearch
	 *            search for this name
	 * @return found or not
	 */
	public static boolean containsClassName(StackTraceElement stackTraceElement, String classNameToSearch) {
		String className = stackTraceElement.getClassName();
		return className.contains(classNameToSearch);
	}

	/**
	 * Search through the current StackTrace looking for the first element after
	 * preceedingClassName.
	 * 
	 * @param traceElements
	 *            StackTrace Array to be searched in
	 * @param preceedingClassName
	 *            ClassName to be just before wanted element in the trace
	 * @return element following preceedingClassName or preceedingClassName if
	 *         no further element left. null if preceedingClassName wasn't there
	 */
	public static StackTraceElement getCurrentCallingClassAsStackTraceElement(StackTraceElement[] traceElements,
			String preceedingClassName) {
		boolean found = false;
		StackTraceElement currentCallingClassAsStackTraceElement = null;
		for (StackTraceElement stackTraceElement : traceElements) {
			if (found) {
				currentCallingClassAsStackTraceElement = stackTraceElement;
				break;
			}
			if (StackTraceUtils.containsClassName(stackTraceElement, preceedingClassName)) {
				found = true;
				currentCallingClassAsStackTraceElement = stackTraceElement;
			}
		}
		return currentCallingClassAsStackTraceElement;
	}

	/**
	 * Is given class in StackTrace?
	 * 
	 * @param traceElements
	 *            StackTrace Array to be searched in
	 * @param className
	 *            ClassName to be searcher for
	 * @return true if class seen in testElements false else
	 */
	public static boolean isClassInStackTrace(StackTraceElement[] traceElements, String className) {
		boolean result = false;
		for (StackTraceElement stackTraceElement : traceElements) {
			if (stackTraceElement.getClassName().endsWith(className)) {
				return true;
			}
		}
		return result;
	}
	
	public static StackTraceElement[] getStackTrace(Throwable t, TestStep step) {
		String filePath = StringUtil.isBlank(step.getFileName()) ? ""
				: FileUtil.getRelativePath(step.getFileName(), "./");
		String declaringClass = "";
		if (step instanceof JavaStep) {
			JavaStep javaStep = (JavaStep) step;
			declaringClass = javaStep.getMethod().getDeclaringClass().getCanonicalName();
		}
		StackTraceElement stackTraceElement = new StackTraceElement(declaringClass, step.getName(), filePath,
				step.getLineNumber());
		ArrayList<StackTraceElement> l = t != null ? new ArrayList<StackTraceElement>(Arrays.asList(t.getStackTrace()))
				: new ArrayList<StackTraceElement>();
		l.add(0, stackTraceElement);

		Iterator<StackTraceElement> eles = l.iterator();
		while (eles.hasNext()) {
			StackTraceElement ele = eles.next();
			if (StringUtil.isNotBlank(ele.getClassName())
					&& StringMatcher.like(".*(\\.reflect\\.|AjcClosure|org\\.testng\\.).*").match(ele.getClassName())) {
				eles.remove();
			}
		}

		return l.toArray(new StackTraceElement[] {});
	}
}
