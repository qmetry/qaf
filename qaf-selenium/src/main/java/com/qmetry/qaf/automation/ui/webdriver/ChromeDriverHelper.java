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
package com.qmetry.qaf.automation.ui.webdriver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.openqa.selenium.chrome.ChromeDriverService;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * @author Chirag Jayswal
 */
public class ChromeDriverHelper {
	private ChromeDriverService service;
	private final Log logger;;

	private synchronized void createAndStartService() {
		if ((service != null) && service.isRunning()) {
			return;
		}
		File driverFile = new File(ApplicationProperties.CHROME_DRIVER_PATH.getStringVal("./chromedriver.exe"));
		if (!driverFile.exists()) {
			logger.error("Please set webdriver.chrome.driver property properly.");
			throw new AutomationError("Driver file not exist.");
		}
		try {
			System.setProperty("webdriver.chrome.driver", driverFile.getCanonicalPath());
			service = ChromeDriverService.createDefaultService();
			service.start();
		} catch (IOException e) {
			logger.error("Unable to start Chrome driver", e);
			throw new AutomationError("Unable to start Chrome Driver Service ", e);
		}
	}

	private void stopService() {
		if ((service != null) && service.isRunning()) {
			logger.info("Stopping chrome driver service.");
			service.stop();
		}
	}

	private ChromeDriverHelper() {
		logger = LogFactoryImpl.getLog(getClass());
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class SingletonHolder {
		public static final ChromeDriverHelper INSTANCE = new ChromeDriverHelper();
	}

	/**
	 * Starts chrome driver service if it is not running
	 * 
	 * @return
	 */
	public static ChromeDriverService getService() {
		SingletonHolder.INSTANCE.createAndStartService();
		return SingletonHolder.INSTANCE.service;
	}

	/**
	 * 
	 */
	public static void teardownService() {
		try {
			SingletonHolder.INSTANCE.stopService();
		} catch (Exception e) {
		}
	}

}
