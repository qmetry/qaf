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
import static com.qmetry.qaf.automation.core.ConfigurationManager.setBundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.util.PropertyUtil;

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
	private static Set<TestCaseResultUpdator> updators = registerUpdators();

	private TestCaseRunResult result;
	// IntegarionTools tool;
	private TestCaseResultUpdator updator;
	private PropertyUtil context;

	private static boolean hasActivePool = false;
	private static boolean hasActiveSingleThreadedPool = false;
	
	protected ResultUpdator(TestCaseRunResult result, TestCaseResultUpdator updator) {
		context = getBundle();
		this.result = result;
		if (updator.allowParallel()) {
			try {
				this.updator = updator.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error(e);
				this.updator = updator;
			}
		} else {
			//use same instance for single threaded
			this.updator = updator;
		}
		logger.info(String.format("%s: %s",updator.getToolName(), result.getMetaData()));
	}

	@Override
	public void run() {
		try {
			logger.debug(updator.getToolName() + ": started to update result");
			setBundle(context);
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
			for (TestCaseResultUpdator updator : updators) {
				try {
					updator.beforeShutDown();
				} catch (Exception e) {
					logger.error("Unable to call beforeShutDown for " + updator.getToolName(), e);
				}
			}
		}
	}
	
	private static void awaitTermination(ThreadPoolExecutor pool) {
		while (pool.getActiveCount() > 0) {
			logger.info("Result updator: Completed "+ pool.getCompletedTaskCount()+" Remaining " + pool.getActiveCount() + " result to be update.");
			try {
				pool.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		System.out.println("Result updator: Completed "+ pool.getCompletedTaskCount()+" Remaining " + pool.getActiveCount() + " result to be update.");
		try {
			pool.shutdownNow();
		} catch (Exception e) {
			logger.error(e);
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
		for(TestCaseResultUpdator updator: updators) {
			try {
				updateResult(result, updator);
			} catch (Exception e) {
				logger.error("Unable to update result using " + updator.getToolName(), e);
			}
		}
	}
	
	public static int getResultUpdatorsCnt() {
		return updators.size();
	}
	
	private static Set<TestCaseResultUpdator> registerUpdators() {
		 Set<TestCaseResultUpdator> allUpdators = new LinkedHashSet<TestCaseResultUpdator>();
		 try {
			String[] testCaseResultUpdatorCls = getBundle().getStringArray("result.updator");
			 if(null!=testCaseResultUpdatorCls) {
				 for(String updaterCls : testCaseResultUpdatorCls) {
					 try {
						TestCaseResultUpdator updator = (TestCaseResultUpdator) Class.forName(updaterCls).newInstance();
						if(updator.enabled()) {
							if(allUpdators.add(updator))
							logger.info("Registered " + updator.getToolName());
						}
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				 }
			 }
			ServiceLoader<TestCaseResultUpdator> testCaseResultUpdators =  ServiceLoader.load(TestCaseResultUpdator.class);
			Iterator<TestCaseResultUpdator> testCaseResultUpdatorsIter = testCaseResultUpdators.iterator();
			while(testCaseResultUpdatorsIter.hasNext()) {
				TestCaseResultUpdator updator = testCaseResultUpdatorsIter.next();
				if(updator.enabled()) {
					if(allUpdators.add(updator))
					logger.info("Registered " + updator.getToolName());
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return allUpdators;
	}
}
