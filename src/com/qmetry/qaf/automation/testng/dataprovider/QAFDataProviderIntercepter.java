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
package com.qmetry.qaf.automation.testng.dataprovider;

import java.util.List;

import org.testng.ITestContext;

import com.qmetry.qaf.automation.core.QAFListener;
import com.qmetry.qaf.automation.step.client.TestNGScenario;

/**
 * Implementation of this interface can be registered as {@link QAFListener} to
 * intercept test data provided by Data provider in data driven test case. you
 * can use it to process or apply filter.
 * 
 * @author Chirag.Jayswal
 *
 */
public interface QAFDataProviderIntercepter extends QAFListener {
	
	/**
	 * This method will be called before fetching the data from data-provider.
	 * @param scenario
	 * @param context
	 */
	public void beforeFech(TestNGScenario scenario, ITestContext context);
	
	/**
	 * This method will called after test data fetched from the data-provider
	 * after applied filter and data-conversion if any.
	 * 
	 * @param scenario
	 * @param context
	 * @param testdata
	 * @return
	 */
	public List<Object[]> intercept(TestNGScenario scenario, ITestContext context, List<Object[]> testdata);
}
