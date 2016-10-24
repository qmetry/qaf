/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/
package com.qmetry.qaf.automation.ui.selenium.webdriver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.qmetry.qaf.automation.core.LoggingBean;
import com.qmetry.qaf.automation.core.QAFTestBase.STBArgs;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.SeleniumCommandLogger;
import com.qmetry.qaf.automation.ui.UiDriver;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;
import com.qmetry.qaf.automation.ui.selenium.AutoWaitInjector;
import com.qmetry.qaf.automation.ui.selenium.IEScreenCaptureListener;
import com.qmetry.qaf.automation.ui.selenium.QAFCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.SeleniumCommandProcessor;
import com.qmetry.qaf.automation.ui.selenium.SubmitCommandListener;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;

/**
 * @author chiragjayswal
 */
public class SeleniumDriverFactory {

	public UiDriver getDriver(WebDriverCommandLogger cmdLogger, String[] stb) {
		String browser = STBArgs.browser_str.getFrom(stb);
		String baseUrl = STBArgs.base_url.getFrom(stb);

		QAFCommandProcessor commandProcessor =
				new SeleniumCommandProcessor(STBArgs.sel_server.getFrom(stb),
						Integer.parseInt(STBArgs.port.getFrom(stb)),
						browser.split("_")[0], baseUrl);
		CommandExecutor executor = getObject(commandProcessor);
		QAFExtendedWebDriver driver =
				new QAFExtendedWebDriver(executor, new DesiredCapabilities(), cmdLogger);
		QAFWebDriverBackedSelenium selenium =
				new QAFWebDriverBackedSelenium(commandProcessor, driver);

		commandProcessor.addListener(new SubmitCommandListener());

		commandProcessor.addListener(new SeleniumCommandLogger(new ArrayList<LoggingBean>()));
		commandProcessor.addListener(new AutoWaitInjector());
		if (browser.contains("iexproper") || browser.contains("iehta")) {
			commandProcessor.addListener(new IEScreenCaptureListener());
		}
		String listners = ApplicationProperties.SELENIUM_CMD_LISTENERS.getStringVal("");

		if (!listners.equalsIgnoreCase("")) {
			commandProcessor.addListener(listners.split(","));
		}

		return selenium;
	}

	private CommandExecutor getObject(Object commandProcessor) {

		try {
			Class<?> clazz = Class.forName("org.openqa.selenium.SeleneseCommandExecutor");
			Class<?> commandProcessorclazz =
					Class.forName("com.thoughtworks.selenium.CommandProcessor");
			Constructor<?> ctor = clazz.getConstructor(commandProcessorclazz);
			return (CommandExecutor) ctor.newInstance(new Object[]{commandProcessor});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage()
					+ "SeleneseCommandExecutor is not available. Please try with selenium 2.32 or older.");
		}
	}
}
