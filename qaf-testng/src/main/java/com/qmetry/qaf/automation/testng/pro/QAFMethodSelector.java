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
package com.qmetry.qaf.automation.testng.pro;

import static com.qmetry.qaf.automation.data.MetaDataScanner.applyMetafilter;

import java.util.List;

import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;

/**
 * This is a method selector class that will selects the method with custom
 * meta-data filter.
 * 
 * @author chirag jayswal
 */
public class QAFMethodSelector implements IMethodSelector {

	/**
	 * 
	 */
	private static final long serialVersionUID = -681517529788899839L;

	/**
	 * 
	 */
	public QAFMethodSelector() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IMethodSelector#includeMethod(org.testng.
	 * IMethodSelectorContext , org.testng.ITestNGMethod, boolean)
	 */
	@Override
	public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod method, boolean isTestMethod) {

		boolean include = applyMetafilter(method);
		if (!include) {
			context.setStopped(true);
		}
		return include;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IMethodSelector#setTestMethods(java.util.List)
	 */
	@Override
	public void setTestMethods(List<ITestNGMethod> testMethods) {
		// do nothing...

	}



}
