/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to author 
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven approach
 *                
 * Copyright 2016 Infostretch Corporation
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
 *
 * You should have received a copy of the GNU General Public License along with this program in the name of LICENSE.txt in the root folder of the distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 *
 * See the NOTICE.TXT file in root folder of this source files distribution 
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 *
 * For any inquiry or need additional information, please contact support-qaf@infostretch.com
 *******************************************************************************/


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
