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
package com.qmetry.qaf.automation.ui;

import java.lang.reflect.ParameterizedType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.TestPage;
import com.qmetry.qaf.automation.ui.api.UiTestBase;
import com.qmetry.qaf.automation.ui.webdriver.ElementFactory;
import com.qmetry.qaf.automation.util.PropertyUtil;

/**
 * High level page class for different UI driver implementation.
 * <p>
 * This class provides you a way to define static or dynamic page hierarchy. In
 * the case of page hierarchy, you just need to call launch page method of the
 * object. it takes care of not only launching that page but also the entire
 * page hierarchy to reach that specific page. Furthermore it also checks that
 * is page already active in browser? If so then it will continue from there,
 * results in reduced execution time. It also allows you to set one of the
 * {@link #setLaunchStrategy(com.qmetry.qaf.automation.ui.api.TestPage.LaunchStrategy)
 * launch strategy} so that you can specify launch behavior before launching the
 * page. The default launch strategy is {@link LaunchStrategy#onlyIfRequired
 * launch only if required}.
 * <p>
 * When functionality changes only the specific test page file needs to be
 * updated: if there is any change in page/ui of web/native AUT you need to
 * update just in particular page rather than each and every test case, thus
 * result in less maintenance.
 * <p>
 * By implementation of test page concept in a best efficient way, you can
 * manipulate page navigation same as on actual web application under test. Once
 * page get created page object's functionalities can be used in any test case,
 * makes code more reusable.
 * 
 * @author Chirag Jayswal
 * @param <P>
 *            parent page in hierarchy
 * @param <D>
 *            Driver implementation - e.g. selenium or webdriver
 */
public abstract class AbstractTestPage<P extends TestPage<D>, D> implements TestPage<D> {
	protected P parent;
	protected UiTestBase<D> testbase;
	protected D driver;
	protected PropertyUtil pageProps;
	protected PageLocator pageLocator;
	protected Object[] launchArguments;

	protected final Log logger;
	protected LaunchStrategy launchStrategy = LaunchStrategy.onlyIfRequired;

	public AbstractTestPage(UiTestBase<D> testBase) {
		this.testbase = testBase;

		logger = LogFactoryImpl.getLog(this.getClass());
		driver = testbase.getDriver();

		this.pageProps = ConfigurationManager.getBundle();
		initWebElements();
	}

	public AbstractTestPage(UiTestBase<D> testBase, P parent2) {
		this(testBase);
		this.parent = parent2;
	}

	@Override
	public UiTestBase<D> getTestBase() {
		return testbase;
	}

	@Override
	public PageLocator getPageLocator() {
		return this.pageLocator;
	}

	@Override
	public void setLaunchStrategy(LaunchStrategy strategy) {
		launchStrategy = strategy;
	}

	@Override
	public void launchPage(PageLocator locator, Object... args) {

		this.pageLocator = locator;
		this.launchArguments = args;
		boolean reqToLaunch = false;

		parent = getParent();
		try {
			reqToLaunch = !launchStrategy.equals(LaunchStrategy.onlyIfRequired) || !this.isPageActive(locator, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reqToLaunch) {
			logger.debug("Launching page: " + this.getClass().getCanonicalName());
			if (parent != null) {
				if (launchStrategy.equals(LaunchStrategy.alwaysRelaunchFromRoot)) {
					parent.setLaunchStrategy(launchStrategy);
				} else {
					parent.setLaunchStrategy(LaunchStrategy.onlyIfRequired);
				}
				parent.launchPage(locator != null ? locator.getParentLocator() : null, args);
			}
			beforeLaunch(args);
			openPage(locator, args);
			waitForPageToLoad();
			afterLaunch();
		}
	}

	public void waitForPageToLoad() {
		
	}

	@Override
	public boolean isPageActive(PageLocator loc, Object... args) {
		return false;
	}

	/**
	 * this method can be override to provide tasks to be done immediate after
	 * page launch. It will ensure that page is launched. It is
	 */
	protected void afterLaunch() {
	}

	/**
	 * you can override this method to fech any information/data from parent
	 * page before launching the page. FW will ensure that when this method get
	 * executed parent page is open.
	 * <p>
	 * <b>Note:</b> If launch method found this page is not opened and going to
	 * open the page then only this method will get executed.
	 * 
	 * @param args
	 *            passed in {@link #launchPage(PageLocator, Object...)}
	 */
	protected void beforeLaunch(Object... args) {
	}

	@SuppressWarnings("unchecked")
	protected void initParent() {
		try {
			Class<P> class1 = (Class<P>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			if (!class1.isInterface()) {
				parent = class1.newInstance();
			}
		} catch (Exception e) {
			logger.warn("Unable to init parent class" + e.getMessage());
		}

	}

	/**
	 * Do not provide steps to load this page in this method, Instead provide
	 * steps in method of parent and call that method over here.<br>
	 * In case of multiple parent or referrer page, provide code to get parent
	 * by casting. For instance:
	 * 
	 * <pre>
	 * <code>
	 * if(parent instanceof UploadStatus ){
	 * UploadStatus parent = (UploadStatus) getParent();
	 * parent.methodToLoadThisPage(args_if_any);
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param locator
	 * @param args
	 */
	abstract protected void openPage(PageLocator locator, Object... args);

	@Override
	public final P getParent() {
		if (this.parent == null) {
			initParent();
		}
		return this.parent;
	}

	private void initWebElements() {
		ElementFactory elementFactory = new ElementFactory();
		elementFactory.initFields(this);
	}

	public void assertActive() {
		String msg = String.format("Expected %s is active.", this.getClass().getSimpleName());
		AbstractTestCase.assertTrue(isPageActive(pageLocator, launchArguments), msg, msg);
	}
	
	@Deprecated
	public void waitForImageToLoad(final String imgLoc) {
		
	}
	
	
	/**
	 * for any kit
	 */
	public void waitForAjaxToComplete() {
	}

}
