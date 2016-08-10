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


package com.infostretch.automation.ui.webdriver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.chrome.ChromeDriverService;

import com.infostretch.automation.core.AutomationError;
import com.infostretch.automation.keys.ApplicationProperties;

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
			service.stop();
		}
	}

	private ChromeDriverHelper() {
		logger = LogFactory.getLog(getClass());
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
		SingletonHolder.INSTANCE.stopService();
	}

}
