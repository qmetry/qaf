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
package com.qmetry.qaf.automation.ui.api;

/**
 * com.qmetry.qaf.automation.core.ui.api.TestPage.java
 * <p>
 * The root interface in the test page hierarchy. A test page represents a
 * web-page of your a web/mobile-web application or a view of mobile-native
 * application. TestPage increases re-usability by reduces the amount of
 * duplicated code and means that if the UI changes, the fix need only be
 * applied in one place.
 * 
 * @author chirag
 */
public interface TestPage<D> {
	public TestPage<D> getParent();

	public PageLocator getPageLocator();

	/***
	 * This method check for existence of page in browser so we can proceed
	 * further with page functionality.
	 * 
	 * @param args
	 *            optional arguments required to identify page.
	 * @return
	 */
	public boolean isPageActive(PageLocator loc, Object... args);

	/**
	 * selenium base instance to provide selenium
	 * 
	 * @return
	 */
	public UiTestBase<D> getTestBase();

	/**
	 * Returns the body text of the current page
	 * 
	 * @return
	 */
	public String getText();

	/**
	 * Use only launchPage method to load a page. For more usability you can
	 * overload this method in your page class but make sure to call this method
	 * within it to load page. eg:
	 * 
	 * <pre>
	 * <code>
	 * void launchPage(){
	 * 		launchPage(null)
	 * }
	 * OR
	 * void launchPage(String locator){
	 * 		launchPage(new DefaultPageLocator(locator));
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param locator
	 *            to locate page on parent page.
	 *            <p>
	 *            This locator will be passed to other fw methods for example
	 *            {@link #isPageActive(PageLocator, Object...)}
	 * @param args
	 *            optional page identifiers to verify is this page for what we
	 *            aspect? for example: view details page for specific user or
	 *            item, you can identify page by user or item id/name
	 *            <p>
	 *            This args will be passed to other fw methods for example
	 *            {@link #isPageActive(PageLocator, Object...)}
	 */
	public void launchPage(PageLocator locator, Object... args);

	/**
	 * You can specify launch behavior before launching the page by calling this
	 * method.
	 * 
	 * @param strategy
	 *            launch strategy.
	 */
	public void setLaunchStrategy(LaunchStrategy strategy);

	public enum LaunchStrategy {
		/**
		 * To specify that even if the page is active, launch page form the
		 * parent page.
		 */
		alwaysRelaunchFromParent,
		/**
		 * To specify that even if the page or its parent page is active, launch
		 * form the root by navigating entire hierarchy.
		 */
		alwaysRelaunchFromRoot,
		/**
		 * To specify that launch the page if it is not active.
		 */
		onlyIfRequired;
	}

}
