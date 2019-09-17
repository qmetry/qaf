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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * Updates result on test management tool. Use
 * {@link #updateResult(TestCaseRunResult, String, IntegarionTools, HashMap)}
 * method to update test case run result on test management tool.
 * 
 * @author chirag
 */
public class ResultUpdator extends Thread {
	/****
	 * Last updated: 5-Mar-2011 Removed ThreadPoolExecutor form BaseTestCase and
	 * integrated here
	 */
	private static final Log logger = LogFactoryImpl.getLog(ResultUpdator.class);
	private Map<String, ?> params;
	private TestCaseRunResult result;
	private String details;
	// IntegarionTools tool;
	private TestCaseResultUpdator updator;

	protected ResultUpdator() {
	}

	protected ResultUpdator(TestCaseRunResult result, String details, TestCaseResultUpdator updator,
			Map<String, ?> params) {
		this.result = result;
		this.updator = updator;
		this.details = details;
		this.params = params;
		logger.info(String.format("Result updator: %s", params));
	}

	@Override
	public void run() {
		try {
			System.out.println("started to updated result");
			updator.updateResult(params, result, details);
		} catch (Throwable t) {
			logger.error("Unable to update result on " + updator.getToolName(), t);
		}
	}

	/**
	 * Provides thread safe {@link LinkedBlockingDeque} without a predefined
	 * capacity will cause new tasks to wait in the queue when all corePoolSize
	 * threads are busy. Thus, no more than corePoolSize threads will ever be
	 * created.(And the value of the maximumPoolSize therefore doesn't have any
	 * effect.)
	 * 
	 * @author chirag
	 */
	private static class ExecutorHolder {

		private static final ThreadPoolExecutor INSTANCE = new ThreadPoolExecutor(0, 5, 5, TimeUnit.MINUTES,
				new LinkedBlockingDeque<Runnable>());;
	}

	/**
	 * Provides thread safe lazy initialized ThreadPoolExecutor
	 * 
	 * @return
	 */
	public static ThreadPoolExecutor getPool() {
		hasActivePool = true;
		return ExecutorHolder.INSTANCE;
	}

	private static boolean hasActivePool = false;

	public static int getActiveCount() {
		if (hasActivePool) {
			getPool().getActiveCount();
		}
		return 0;
	}

	public static void awaitTermination() {
		if (hasActivePool) {
			ThreadPoolExecutor pool = getPool();
			while (pool.getActiveCount() > 0) {
				logger.info("Result updator : Remaining " + pool.getActiveCount() + " result to be update.");
				try {
					pool.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Result updator : Remaining " + pool.getActiveCount() + " result to be update.");
			try {
				pool.shutdownNow();
			} catch (Exception e) {
				e.printStackTrace();
			}
			hasActivePool = false;
		}
	}

	/**
	 * Executes result updating process in separate thread so that test runner
	 * can continue for next test case.
	 * 
	 * @param result
	 * @param assertLog
	 * @param tool
	 * @param params
	 */
	public static void updateResult(TestCaseRunResult result, String assertLog, TestCaseResultUpdator toolUpdator,
			Map<String, ?> params) {

		ResultUpdator updator = new ResultUpdator(result, assertLog, toolUpdator, params);

		getPool().execute(updator);
	}

}
