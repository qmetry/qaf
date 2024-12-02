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
package com.qmetry.qaf.automation.core;

//import org.testng.SkipException;

/**
 * To indicate automation error,not an AUT failure so that the test case can be
 * in skip state instead of fail com.qmetry.qaf.automation.AutomationError.java
 * 
 * @author chirag
 */
public class AutomationError extends SkipTestException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2820870863950734300L;

	public AutomationError(String msg) {
		super(msg);
	}

	public AutomationError(String msg, Throwable cause) {
		super(msg, cause);
	}

	public AutomationError(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	@Override
	public boolean isSkip() {
		return true;
	}
}
