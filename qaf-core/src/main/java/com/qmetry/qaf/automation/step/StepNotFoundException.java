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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.testng.SkipException;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.StackTraceUtils;

/**
 * com.qmetry.qaf.automation.step.StepInvocationException.java
 * 
 * @author chirag.jayswal
 */
public class StepNotFoundException extends SkipException {

	private static final long serialVersionUID = -6115930100096312628L;
	private static final Log logger = LogFactoryImpl.getLog(StepNotFoundException.class);

	public StepNotFoundException(StringTestStep step) {
		super(step.getSignature()
				+ " TestStep implementation not found. \n Please provide implementation or ensure '"
				+ ApplicationProperties.STEP_PROVIDER_PKG.key
				+ "' property value includes appropriate package.");
		setStackTrace(StackTraceUtils.getStackTrace(null, step));
		String snippet = step.getCodeSnippet();
		logger.error(snippet);
		System.err.println(snippet);
	}

	@Override
	public boolean isSkip() {
		return true;
	}

}
