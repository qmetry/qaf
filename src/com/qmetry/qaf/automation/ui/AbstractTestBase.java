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


package com.qmetry.qaf.automation.ui;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.ui.api.UiTestBase;

/**
 * com.qmetry.qaf.automation.core.ui.AbstractTestBase.java
 * 
 * @author chirag
 */
public abstract class AbstractTestBase<D> implements UiTestBase<D> {
	protected String browser = "", browserToLaunch = "";
	protected String baseUrl;
	protected final Log logger;
	protected boolean prepareForShutDown;
	protected boolean inUse;
	protected D driver;
	protected Method method;
	String stbargs[];

	public AbstractTestBase() {
		logger = LogFactory.getLog(this.getClass());
	}

	protected String getDefaultBrowser() {
		return "";
	}

	protected void setTestDone() {
		inUse = false;
	}

	protected abstract void launch(String baseurl);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#getDriver()
	 */
	@Override
	public abstract D getDriver();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase #isPreparedForShutDown()
	 */
	@Override
	public boolean isPreparedForShutDown() {
		return prepareForShutDown;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.core.ui.TestBase#prepareForShutDown()
	 */
	@Override
	public void prepareForShutDown() {
		this.prepareForShutDown = true;

	}

	public String getBaseUrl() {
		return getBase().getBaseUrl();
	}

	@Override
	public String getBrowser() {
		return getBase().getBrowser();
	}

	@Override
	public void tearDown() {
		getBase().tearDown();

	}

	protected QAFTestBase getBase() {
		return TestBaseProvider.instance().get();
	}

	/**
	 * UiTestBase provider can check this flag timely basis and can remove
	 * unused reference
	 * 
	 * @return
	 */
	protected boolean isInUse() {
		return inUse;
	}

	@Override
	public void addTestStepLog(String msg) {
		getBase().addAssertionLog(msg, MessageTypes.TestStep);
	}

	@Override
	public void addAssertionsLog(String msg) {
		getBase().addAssertionLog(msg, MessageTypes.Info);
	}

	@Override
	public void addAssertionsLog(String msg, MessageTypes type) {
		getBase().addAssertionLog(msg, type);
	}

}
