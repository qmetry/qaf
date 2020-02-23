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

import java.lang.reflect.Field;

import org.testng.IExecutionListener;
import org.testng.ITestRunnerFactory;
import org.testng.TestNG;
import org.testng.internal.IConfiguration;

import com.qmetry.qaf.automation.testng.TestRunnerFactory;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * @author chirag.jayswal
 *
 */
public class QAFExecutionListener implements IExecutionListener {

	public QAFExecutionListener() {
		// doing at the time this get registered instead of on execution
		// start!...
		try {
			TestNG tng = getTestNG();
			if (tng != null) {
				tng.addMethodSelector(QAFMethodSelector.class.getName(), 0);
				System.out.println("Added \"QAFMethodSelector\"");
				IConfiguration configuration = (IConfiguration) ClassUtil.getField("m_configuration", tng);
				ITestRunnerFactory testRunnerFactory = new TestRunnerFactory(configuration );
				ClassUtil.setField("m_testRunnerFactory", tng, testRunnerFactory);
			} else {
				System.err.println("Unable to add \"QAFMethodSelector\". You may add it in you xml configuration file.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void onExecutionStart() {

	}

	@Override
	public void onExecutionFinish() {

	}

	@SuppressWarnings("deprecation")
	private TestNG getTestNG() {
		try {
			return TestNG.getDefault();
		} catch (Exception e) {
			try {
				Field field = ClassUtil.getField("m_instance", TestNG.class);
				field.setAccessible(true);
				return (TestNG) field.get(null);
			} catch (Exception e1) {
			}
		}
		return null;
	}
}
