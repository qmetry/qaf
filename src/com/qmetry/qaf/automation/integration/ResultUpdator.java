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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.util.HashMap;
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
	 * 5-Mar-2011 Removed ThreadPoolExecutor form BaseTestCase and
	 * integrated here
	 * 
	 * Last updated: 22-Mar-2020 Added single thread executor support.
	 */
	private static final Log logger = LogFactoryImpl.getLog(ResultUpdator.class);
	private TestCaseRunResult result;
	// IntegarionTools tool;
	private TestCaseResultUpdator updator;

	private static boolean hasActivePool = false;
	private static boolean hasActiveSingleThreadedPool = false;
	private static String[] updators = getBundle().getStringArray("result.updator",new String[] {});
	
	protected ResultUpdator() {
	}

	protected ResultUpdator(TestCaseRunResult result, TestCaseResultUpdator updator) {
		this.result = result;
		this.updator = updator;
		logger.info(String.format("Result updator: %s", result.getMetaData()));
	}

	@Override
	public void run() {
		try {
			logger.debug("started to update result");
			updator.updateResult(result);
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
	 * Provides thread safe {@link LinkedBlockingDeque} without a predefined
	 * capacity will cause new tasks to wait in the queue when all corePoolSize
	 * threads are busy. Thus, no more than corePoolSize threads will ever be
	 * created.(And the value of the maximumPoolSize therefore doesn't have any
	 * effect.)
	 * 
	 * @author chirag
	 */
	private static class SingleTheradExecutorHolder {

		private static final ThreadPoolExecutor INSTANCE = new ThreadPoolExecutor(0, 1, 5, TimeUnit.MINUTES,
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

	/**
	 * Provides thread safe lazy initialized ThreadPoolExecutor
	 * 
	 * @return
	 */
	public static ThreadPoolExecutor getSingleThreadedPool() {
		hasActiveSingleThreadedPool = true;
		return SingleTheradExecutorHolder.INSTANCE;
	}

	public static int getActiveCount() {
		if (hasActivePool) {
			getPool().getActiveCount();
		}
		if (hasActiveSingleThreadedPool) {
			getSingleThreadedPool().getActiveCount();
		}
		return 0;
	}

	public static void awaitTermination() {
		if (hasActiveSingleThreadedPool || hasActivePool) {
			if (hasActiveSingleThreadedPool) {
				ThreadPoolExecutor pool = getSingleThreadedPool();
				awaitTermination(pool);
				hasActiveSingleThreadedPool = false;
			}
			if (hasActivePool) {
				ThreadPoolExecutor pool = getPool();
				awaitTermination(pool);
				hasActivePool = false;
			}
			for (String updator : updators) {
				try {
					Class<?> updatorCls = Class.forName(updator);
					TestCaseResultUpdator tcu = (TestCaseResultUpdator) updatorCls.newInstance();
					tcu.beforeShutDown();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					logger.error("Unable to call beforeShutDown for " + updator, e);
				}
			}
		}
	}
	
	private static void awaitTermination(ThreadPoolExecutor pool) {
		while (pool.getActiveCount() > 0) {
			logger.info("Result updator: Completed "+ pool.getActiveCount()+" Remaining " + pool.getActiveCount() + " result to be update.");
			try {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Result updator: Completed "+ pool.getActiveCount()+" Remaining " + pool.getActiveCount() + " result to be update.");
		try {
			pool.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
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
	private static void updateResult(TestCaseRunResult result, TestCaseResultUpdator toolUpdator) {
		ResultUpdator updator = new ResultUpdator(result, toolUpdator);
		if(toolUpdator.allowConfigAndRetry() || (result.isTest() && !result.willRetry())) {
			ThreadPoolExecutor executor = toolUpdator.allowParallel() ? getPool() : getSingleThreadedPool();
			executor.execute(updator);
		}
	}

	public static void updateResult(TestCaseRunResult result) {
		for(String updator: updators) {
			try {
				Class<?> updatorCls = Class.forName(updator);
				updateResult(result, (TestCaseResultUpdator)updatorCls.newInstance());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("Unable to update result using " + updator, e);
			}
		}
	}
	
	public static int getResultUpdatorsCnt() {
		return null==updators?0:updators.length;
	}
}
