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


package com.qmetry.qaf.automation.ui.webdriver;

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;

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

	@Override
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

	@Override
	public Object executeScript(String script, Object... args) {
		return execute("executeScript", validateScriptCommand(script, args)).getValue();
	}

	@Override
	public Object executeAsyncScript(String script, Object... args) {
		return execute("executeAsyncScript", validateScriptCommand(script, args)).getValue();
	}

	private boolean isJavaScriptEnabled() {
		return capabilities.isJavascriptEnabled();
	}

	private Map<String, ?> validateScriptCommand(String script, Object... args) {
		if (!(isJavaScriptEnabled())) {
			throw new UnsupportedOperationException(
					"You must be using an underlying instance of WebDriver that supports executing javascript");
		}

		script = script.replaceAll("\"", "\\\"");
		Iterable<?> convertedArgs = Iterables.transform(Lists.newArrayList(args), new WebElementToJsonConverter());
		Map<String, ?> params = ImmutableMap.of("script", script, "args", Lists.newArrayList(convertedArgs));
		return params;
	}
}
