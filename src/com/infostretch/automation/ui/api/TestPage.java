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


package com.infostretch.automation.ui.api;

/**
 * com.infostretch.automation.core.ui.api.TestPage.java
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
