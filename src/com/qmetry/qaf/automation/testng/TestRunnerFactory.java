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
package com.qmetry.qaf.automation.testng;

import java.util.Collection;
import java.util.List;

import org.testng.IClassListener;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.ITestRunnerFactory;
import org.testng.TestRunner;
import org.testng.internal.IConfiguration;
import org.testng.internal.TestNGMethod;
import org.testng.xml.XmlTest;

import com.qmetry.qaf.automation.step.client.TestNGScenario;

/**
 * 
 * @author chirag.jayswal
 *
 */
public class TestRunnerFactory implements ITestRunnerFactory {
	
	private IConfiguration configuration;
	public TestRunnerFactory(IConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public TestRunner newTestRunner(ISuite suite, XmlTest test, Collection<IInvokedMethodListener> listeners,
			List<IClassListener> classListeners) {
		TestRunner runner =
	              new TestRunner(configuration, suite, test,
	                  false /*skipFailedInvocationCounts */,
	                  listeners,classListeners);
		init(runner);
		return runner;
	}
	private void init(TestRunner runner){
		convert(runner.getAllTestMethods());
		convert(runner.getAfterSuiteMethods());
		convert(runner.getAfterTestConfigurationMethods());
		convert(runner.getBeforeSuiteMethods());
		convert(runner.getBeforeTestConfigurationMethods());
	}
	private void convert(ITestNGMethod[] m){
		for(int i=0;i<m.length;i++){
			if(m[i] instanceof TestNGMethod && !(m[i] instanceof TestNGScenario)){
				m[i]=new TestNGScenario((TestNGMethod) m[i]);
			}
		}
	}
	
	
}
