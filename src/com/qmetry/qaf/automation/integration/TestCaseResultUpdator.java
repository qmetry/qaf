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
package com.qmetry.qaf.automation.integration;

/**
 * @author chirag
 */
public interface TestCaseResultUpdator {
	/**
	 * 
	 * @param result
	 *            - test case result
	 * @return status of update call
	 */
	public boolean updateResult(TestCaseRunResult result);

	/**
	 * @return tool name
	 */
	public String getToolName();

	/**
	 * By default result updator uses separate multi-threaded pool, if you want to
	 * run in single thread set it to false.
	 * 
	 * @return
	 */
	default public boolean allowParallel() {
		return true;
	}
	
	/**
	 * Set weather configuration methods and retry also should be reported or not.
	 * @return
	 */
	default public boolean allowConfigAndRetry() {
		return false;
	}
	/**
	 * Each Updator class can define implementation for this method. This method
	 * will be invoked at the end of execution either all test completed or force
	 * exit. Separate instance will be created to execute this method.
	 * 
	 * @return
	 */
	default public void beforeShutDown() {
	}
	
	/**
	 * Useful when registered with service, to enable disable based on some condition.
	 * @return
	 */
	default public boolean enabled() {
		return true;
	}
}
