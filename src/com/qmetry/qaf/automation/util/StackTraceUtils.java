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

package com.qmetry.qaf.automation.util;

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
}
