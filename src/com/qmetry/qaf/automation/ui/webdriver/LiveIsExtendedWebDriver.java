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

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.Dialect;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;

import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;
import static com.qmetry.qaf.automation.util.ClassUtil.setField;

/**
 * com.qmetry.qaf.automation.ui.webdriver.QAFWebDriver.java
 * 
 * @author chirag
 */
public class LiveIsExtendedWebDriver extends QAFExtendedWebDriver {

	private Capabilities capabilities;

	public LiveIsExtendedWebDriver(URL url, Capabilities capabilities, WebDriverCommandLogger reporter) {
		super(url, capabilities, reporter);
		this.capabilities = capabilities;
	}

	//@Override
	protected void startClient() {
	}

	@Override
	public Capabilities getCapabilities() {
		return capabilities;
	}

	@Override
	protected void startSession(Capabilities desiredCapabilities) {
		String sessionId = ApplicationProperties.WEBDRIVER_REMOTE_SESSION
				.getStringVal(
						(String) desiredCapabilities.asMap().get(ApplicationProperties.WEBDRIVER_REMOTE_SESSION.key))
				.trim();
		setSessionId(sessionId);
		setCodec();
	}


	protected void startSession(Capabilities desiredCapabilities, Capabilities reqCapabilities) {
		startSession(desiredCapabilities);
	}

	@Override
	protected Response execute(String driverCommand, Map<String, ?> parameters) {
		if (driverCommand.equalsIgnoreCase(DriverCommand.QUIT)) {
			return new Response();
		}
		return super.execute(driverCommand, parameters);
	}

	private void setCodec() {
		try {
			CommandExecutor executor = getCommandExecutor();
			setField("commandCodec", executor, Dialect.W3C.getCommandCodec());
			setField("responseCodec", executor, Dialect.W3C.getResponseCodec());
		} catch (Exception e) {
			logger.error("Unable to set W3C codec", e);
		}
	}
}
