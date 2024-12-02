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
package com.qmetry.qaf.automation.step;

//import org.testng.SkipException;

import com.qmetry.qaf.automation.core.SkipTestException;
import com.qmetry.qaf.automation.util.StackTraceUtils;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.StepInvocationException.java
 * 
 * @author chirag.jayswal
 */
public class StepInvocationException extends SkipTestException {

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
		setStackTrace(StackTraceUtils.getStackTrace(null, step));
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
		if (cause instanceof SkipTestException) {
			isSkip = ((SkipTestException) cause).isSkip();
		}
	}

	public StepInvocationException(TestStep step, Throwable cause, boolean isSkip) {
		super(getMessageFromCause(cause), cause);
		setStackTrace(StackTraceUtils.getStackTrace(cause, step));
		this.isSkip = isSkip;
	}

	private static String getMessageFromCause(Throwable cause) {
		if (cause != null && StringUtil.isNotBlank(cause.getMessage())) {
			return cause.getMessage();
		}
		return cause == null ? "Unknown cause"
				: cause.toString().replaceAll(StepInvocationException.class.getCanonicalName() + ": ", "");
	}


}
