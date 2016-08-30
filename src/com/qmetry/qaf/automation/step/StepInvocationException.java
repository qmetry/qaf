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


package com.qmetry.qaf.automation.step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.testng.SkipException;

import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.StepInvocationException.java
 * 
 * @author chirag.jayswal
 */
public class StepInvocationException extends SkipException {

	private static final long serialVersionUID = 5737290921256174216L;
	private boolean isSkip = false;

	public StepInvocationException(String message) {
		this(message, false);
	}

	/**
	 * @param message
	 */
	public StepInvocationException(String message, boolean isSkip) {
		super(message);
		this.isSkip = isSkip;
		setStackTrace(new StackTraceElement[] {});
	}

	public StepInvocationException(TestStep step, String message, boolean isSkip) {
		super(message);
		this.isSkip = isSkip;
		setStackTrace(getStackTrace(null, step));
	}

	@Override
	public boolean isSkip() {
		return isSkip;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StepInvocationException(TestStep step, Throwable cause) {
		this(step, cause, false);
		if (cause instanceof SkipException) {
			isSkip = ((SkipException) cause).isSkip();
		}
	}

	public StepInvocationException(TestStep step, Throwable cause, boolean isSkip) {
		super(getMessageFromCause(cause), cause);
		setStackTrace(getStackTrace(cause, step));
		this.isSkip = isSkip;
	}

	private static String getMessageFromCause(Throwable cause) {
		if (cause != null && StringUtil.isNotBlank(cause.getMessage())) {
			return cause.getMessage();
		}
		return cause == null ? "Unknown cause"
				: cause.toString().replaceAll(StepInvocationException.class.getCanonicalName() + ": ", "");
	}

	private StackTraceElement[] getStackTrace(Throwable t, TestStep step) {
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
