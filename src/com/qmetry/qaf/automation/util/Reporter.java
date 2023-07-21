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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.core.TestBaseProvider.instance;

import org.testng.ITestResult;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.client.TestNGScenario;

/**
 * This class provides utility methods to log message in report.
 * 
 * @author chirag
 */
public class Reporter {

	private Reporter() {

	}

	/**
	 * You can specify the message type with message. If message is with type
	 * {@link MessageTypes#Fail} the test case will get fail. When you want to
	 * create step within test case you can use {@link MessageTypes#TestStep}
	 * 
	 * @param msg
	 * @param type
	 */
	public static void log(String msg, MessageTypes type) {
		instance().get().addAssertionLog(msg, type);
	}

	// @QAFTestStep
	public static void log(String msg) {
		log(msg, MessageTypes.Info);
	}

	/**
	 * Capture screen shot and attach with message in report.
	 * 
	 * @param msg
	 * @param type
	 */
	public static void logWithScreenShot(String msg, MessageTypes type) {
		instance().get().addAssertionLogWithScreenShot(msg, type);
	}

	/**
	 * Capture screen shot and attach with message in report.
	 * 
	 * @param msg
	 */
	// @QAFTestStep
	public static void logWithScreenShot(String msg) {
		logWithScreenShot(msg, MessageTypes.Info);
	}
	
	/**
	 * Add meta-data to current test case. Useful to link cloud session, video etc.
	 * @param key
	 * @param val
	 */
	public static void addMetadata(String key, String val) {
		ITestResult tr = (ITestResult) getBundle().getProperty(ApplicationProperties.CURRENT_TEST_RESULT.key);
		addMetadata(tr, key, val);
	}
	
	/**
	 * Add meta-data to test case. Useful to link cloud session, video etc.
	 * @param key
	 * @param val
	 */
	public static void addMetadata(ITestResult tr, String key, String val) {		
		if(null!=tr && tr.getMethod() instanceof TestNGScenario) {
			((TestNGScenario) tr.getMethod()).getMetaData().put(key, val);
		}
	}

}
