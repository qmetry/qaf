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
