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


package com.infostretch.automation.step;

import static com.infostretch.automation.core.ConfigurationManager.getBundle;
import static com.infostretch.automation.util.Validator.assertThat;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.xmlmatchers.xpath.HasXPath.hasXPath;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.hamcrest.Matchers;
import org.openqa.selenium.Cookie;

import com.infostretch.automation.data.MetaData;
import com.infostretch.automation.keys.ApplicationProperties;
import com.infostretch.automation.ui.WebDriverTestBase;
import com.infostretch.automation.ui.webdriver.QAFExtendedWebElement;
import com.infostretch.automation.util.StringUtil;
import com.infostretch.automation.ws.rest.RestTestBase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * com.infostretch.automation.step.CommonStep.java
 * 
 * @author chirag
 */
public final class CommonStep {

	@QAFTestStep(description = "COMMENT: {0}")
	public static void comment(Object args) {
		System.out.printf("COMMENT: %s \n", args);
	}

	/**
	 * Store last step result with the given name. In bdd if step returns value
	 * and you want to store the return value with name for further use you can
	 * call this step.
	 * <p>
	 * Example:<br/>
	 * <code>
	 * get text of 'my.ele.loc'<br/>
	 * AND store into 'myeletext'<br/>
	 * COMMENT: '${myeletext}'<br/>
	 * </code>
	 * 
	 * @param var
	 *            : {0} : the name of variable you in which want to store last
	 *            step result
	 */
	@QAFTestStep(description = "store into {0}")
	public static void storeLastStepResultInto(String var) {
		Object val = getBundle().getProperty("last.step.result");
		getBundle().addProperty(var, val);
	}

	// @QAFTestStep(description = "{1} and say it {0}")
	public static void call_store(String var, String stepName, Object... args) {
		Object obj = new StringTestStep(stepName, args).execute();
		getBundle().addProperty(var, obj);
	}

	/**
	 * Store a value in variable to use later on. Stored The variable can be
	 * passed as argument to the other steps as ${varname}
	 * <p>
	 * Example:<br/>
	 * <code>
	 * store 5 into 'price'<br/>
	 * COMMENT: '${myeletext}'<br/>
	 * </code>
	 * 
	 * @param val
	 *            : {0} : value to be stored in variable
	 * @param var
	 *            : {1} : variable name
	 */
	@QAFTestStep(description = "store {0} into {1}")
	public static void store(Object val, String var) {
		getBundle().addProperty(var, val);
	}

	/**
	 * Insert text in the given locator
	 * <p>
	 * Example:<br/>
	 * <code>
	 * sendKeys 'infostretch' into 'my.ele.loc'<br/>
	 * <br/>
	 * </code>
	 * 
	 * @param text
	 *            : {0} : String to be insert
	 * @param loc
	 *            : {1} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "sendKeys {0} into {1}")
	public static void sendKeys(String text, String loc) {
		getElement(loc).sendKeys(text);
	}

	/**
	 * Verify that the specified element is somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is present")
	public static void assertPresent(String loc) {
		getElement(loc).assertPresent();
	}

	/**
	 * Verify that the specified link text is somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert link with text 'infostretch' is present"<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param linkText
	 *            : {0} : link text to be verified in browser page.
	 */
	@QAFTestStep(description = "assert link with text {0} is present")
	public static void assertLinkWithTextPresent(String linkText) {
		getElement("link=" + linkText).assertPresent();
	}

	/**
	 * Verify that the specified partial link is somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert link with partial text 'infostretch' is present"<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param linkText
	 *            : {0} : partial link text to be verified in browser page.
	 */
	@QAFTestStep(description = "assert link with partial text {0} is present")
	public static void assertLinkWithPartialTextPresent(String linkText) {
		getElement("partialLink=" + linkText).assertPresent();
	}

	/**
	 * Asserts that the specified element is somewhere on the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' is present<br/>
	 * KWD
	 * </code>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "verify {0} is present")
	public static boolean verifyPresent(String loc) {
		return getElement(loc).verifyPresent();
	}

	/**
	 * Asserts that the specified link with given text is somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify link with text 'About Us' is present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param linkText
	 *            : {0} : link text to be verified in browser page.
	 */
	@QAFTestStep(description = "verify link with text {0} is present")
	public static boolean verifyLinkWithTextPresent(String linkText) {
		return getElement("link=" + linkText).verifyPresent();
	}

	/**
	 * Asserts that the specified link with given partial text is somewhere on
	 * the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify link with partial text 'infostretch' is present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param linkText
	 *            : {0} : partial link text to be verified in browser page.
	 */
	@QAFTestStep(description = "verify link with partial text {0} is present")
	public static boolean verifyLinkWithPartialTextPresent(String linkText) {
		return getElement("partialLink=" + linkText).verifyPresent();
	}

	/**
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is visible")
	public static void assertVisible(String loc) {
		getElement(loc).assertVisible();
	}

	/**
	 * Verify that the specified element is visible somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' is visible<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "verify {0} is visible")
	public static boolean verifyVisible(String loc) {
		return getElement(loc).verifyVisible();
	}

	/**
	 * Opens an URL in the browser. This accepts both relative and absolute
	 * URLs. The "get" command waits for the page to load before proceeding, *
	 * <p>
	 * Example:<br/>
	 * <code>
	 * get 'http://www.infostretch.com'<br/>
	 * get '/'<br/>
	 * get '/Resources/case-studies.php'<br/>
	 * </code>
	 * 
	 * @param url
	 *            : {0} : the URL to open; may be relative or absolute
	 */
	@QAFTestStep(description = "get {0}")
	public static void get(String url) {
		new WebDriverTestBase().getDriver().get(url);
	}

	/**
	 * Switch the context to the driver to new window Example:<br/>
	 * <code>
	 * switchToWindow '2'<br/>
	 * switchToWindow 'Forgot Password Popup'<br/>
	 * </code>
	 * 
	 * @param windowNameOrIndex
	 *            : title or index of the window
	 */
	@QAFTestStep(description = "switch to {0} window")
	public static void switchToWindow(String nameOrIndex) {
		if (StringUtil.isNumeric(nameOrIndex)) {
			int index = Integer.parseInt(nameOrIndex);
			Set<String> windows = new WebDriverTestBase().getDriver().getWindowHandles();
			Iterator<String> itr = windows.iterator();
			for (int i = 0; i < windows.size() && i <= index; i++) {
				nameOrIndex = itr.next();
			}
			new WebDriverTestBase().getDriver().switchTo().window(nameOrIndex);
		} else {
			new WebDriverTestBase().getDriver().switchTo().window(nameOrIndex);
		}
	}

	/**
	 * sets the service end point URL
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * service endpoint is 'http://feeds.feedburner.com/InfostretchMobileAndQaBlog'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param arg1
	 *            : {0} : The URL to be set as end point
	 */
	@QAFTestStep(description = "service endpoint is {0}")
	public static void setServiceEndPoint(String arg1) {
		getBundle().setProperty("ws.endurl", arg1);
	}

	/**
	 * This method request for resource to the web service by passing the
	 * required parameters.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * user request for resource 'resource' with 'params'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param resource
	 *            : {0} : resource String
	 * @param params
	 *            : {1} : parameters
	 */
	@QAFTestStep(stepName = "requestForResourceWithParams", description = "user request for resource {0} with {1}")
	public static void requestForResource(String resource, Map<String, String> params) {
		requestFor(resource, params);
	}

	private static void requestFor(String resource, Map<String, String> params) {
		WebResource webResource = new RestTestBase().getWebResource(
				getBundle().getString("ws.endurl", ApplicationProperties.SELENIUM_BASE_URL.getStringVal()), resource);
		if (null != params && !params.isEmpty()) {
			MultivaluedMap<String, String> mparams = new MultivaluedMapImpl();

			for (String key : params.keySet()) {
				mparams.add(key, params.get(key));
			}
			webResource = webResource.queryParams(mparams);
		}
		webResource.get(ClientResponse.class);
	}

	/**
	 * This method request for resource to the web service.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * user request for resource 'Resource String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param resource
	 *            : {0} : resource String
	 */
	@QAFTestStep(description = "user request for resource {0}")
	public static void requestForResource(String resource) {
		requestFor(resource, null);
	}

	/**
	 * This method post the content through the web service.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * user post 'postContent' for resource 'resource'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param content
	 *            : {0} : content to be posted to service end point
	 * @param resource
	 *            : {1} : resource string
	 */
	@QAFTestStep(description = "user post {0} for resource {1}")
	public static void postContent(String content, String resource) {
		new RestTestBase().getWebResource(getBundle().getString("ws.endurl"), resource).post(content);
	}

	/**
	 * This method check for the response status of web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have status 'ResponceStatus'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param status
	 *            : {0} : Status message or response status String
	 */
	@QAFTestStep(description = "response should have status {0}")
	public static void it_should_have_response(String status) {
		assertThat("Response Status", new RestTestBase().getResponse().getStatus(),
				Matchers.equalTo(Status.valueOf(status)));
	}

	/**
	 * This method check given Xpath is there in response status of web service
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * response should have xpath 'Xpath String'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param xpath
	 *            : {0} : xpath string to be verified in respose
	 */
	@QAFTestStep(description = "response should have xpath {0}")
	public static void response_should_have_xpath(String xpath) {
		assertThat(the(new RestTestBase().getResponse().getMessageBody()), hasXPath(xpath));
	}

	/**
	 * clear the specified element value from the field
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * clear 'my.ele.loc'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "clear {0}")
	public static void clear(String loc) {
		getElement(loc).clear();
	}

	/**
	 * Retrieve the value for specified element.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * get text of 'my.ele.loc'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @return The text contains by the specified locator
	 */
	@QAFTestStep(description = "get text of {0}")
	public String getText(String loc) {
		return getElement(loc).getText();
	}

	private static QAFExtendedWebElement getElement(String loc) {
		return new QAFExtendedWebElement(loc);
	}

	/**
	 * Submit the specified page. This is particularly useful for page without
	 * submit buttons, e.g. single-input "Search" page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * submit 'my.ele.loc'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */

	@QAFTestStep(description = "submit {0}")
	public static void submit(String loc) {
		getElement(loc).submit();

	}

	/**
	 * Clicks on a link, button, checkbox or radio button. If the click action
	 * causes a new page to load (like a link usually does), call
	 * waitForPageToLoad.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * click on 'my.ele.loc'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "click on {0}")
	public static void click(String loc) {
		getElement(loc).click();
	}

	/**
	 * Wait for the specified element is visible Determines if the specified
	 * element is visible. An element can be rendered invisible by setting the
	 * CSS "visibility" property to "hidden", or the "display" property to
	 * "none", either for the element itself or one if its ancestors. This
	 * method will wait till its presence if the element is not present.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' to be visible<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} to be visible")
	public static void waitForVisible(String loc) {
		getElement(loc).waitForVisible();
	}

	// @QAFTestStep(stepName = "waitForVisibleWithTimeout", description =
	// "wait {1}sec for {0} to be visible")
	public static void waitForVisible(String loc, long sec) {
		getElement(loc).waitForVisible(sec * 1000);
	}

	/**
	 * Determines if the specified element is not visible. This method will wait
	 * till its invisibility, if the element is visible.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' not to be visible<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} not to be visible")
	public static void waitForNotVisible(String loc) {
		getElement(loc).waitForNotVisible();
	}

	// @QAFTestStep(stepName = "waitForNotVisibleWithTimeout", description =
	// "wait {1}sec for {0} not to be visible")
	public static void waitForNotVisible(String loc, long sec) {
		getElement(loc).waitForNotVisible(sec * 1000);
	}

	/**
	 * Determines if the specified element is disable. This method will wait for
	 * the element to be disable, if the element is enable.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' to be disable<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} to be disable")
	public static void waitForDisabled(String loc) {
		getElement(loc).waitForDisabled();
	}

	// @QAFTestStep(stepName = "waitForDisableWithTimeout", description =
	// "wait {1}sec for {0} to be Disable")
	public static void waitForDisabled(String loc, long sec) {
		getElement(loc).waitForDisabled(sec * 1000);

	}

	/**
	 * Determines if the specified element is enable. This method will wait for
	 * the element to be enable, if the element is disable.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' to be enable<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} to be enable")
	public static void waitForEnabled(String loc) {
		getElement(loc).waitForEnabled();

	}

	// @QAFTestStep(stepName = "waitForEnableWithTimeout", description =
	// "wait {1}sec for {0} to be Enable")
	public static void waitForEnabled(String loc, long sec) {
		getElement(loc).waitForEnabled(sec * 1000);

	}

	/**
	 * Determines if the specified element is present. This method will wait for
	 * the element to be present, if the element is not present.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' to be present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} to be present")
	public static void waitForPresent(String loc) {
		getElement(loc).waitForPresent();
		;

	}

	// @QAFTestStep(stepName = "waitForPresentWithTimeout", description =
	// "wait {1}sec for {0} to be Present")
	public static void waitForPresent(String loc, long sec) {
		getElement(loc).waitForPresent(sec * 1000);

	}

	/**
	 * Determines if the specified element is not present. This method will wait
	 * for the element not to be present, if the element is present.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' is not present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} is not present")
	public static void waitForNotPresent(String loc) {
		getElement(loc).waitForNotPresent();

	}

	// @QAFTestStep(stepName = "waitForNotPresentWithTimeout", description =
	// "wait {1}sec for {0} is not Present")
	public static void waitForNotPresent(String loc, long sec) {
		getElement(loc).waitForNotPresent(sec * 1000);

	}

	/**
	 * Gets the text of an element. This works for any element that contains
	 * text. This command uses either the textContent or the innerText of the
	 * element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' text 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 */
	@QAFTestStep(description = "wait until {0} text {1}")
	public static void waitForText(String loc, String text) {
		getElement(loc).waitForText(text);

	}

	// @QAFTestStep(stepName = "waitForTextWithTimeout", description =
	// "wait {2}sec for {0} text is {1}")
	public static void waitForText(String loc, String text, long sec) {
		getElement(loc).waitForText(text, sec * 1000);

	}

	/**
	 * Gets the text of an element. This works for any element that contains
	 * text. This command uses either the textContent or the innerText of the
	 * element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' text is not 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 */
	@QAFTestStep(description = "wait until {0} text is not {1}")
	public static void waitForNotText(String loc, String text) {
		getElement(loc).waitForNotText(text);

	}

	// @QAFTestStep(stepName = "waitForNotTextWithTimeout", description =
	// "wait {2}sec for {0} text is not {1}")
	public static void waitForNotText(String loc, String text, long sec) {
		getElement(loc).waitForNotText(text, sec * 1000);
	}

	/**
	 * Gets the (whitespace-trimmed) value of an input field (or anything else
	 * with a value parameter).
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' value is 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param value
	 *            : {1} : The object of value for element locator
	 */
	@QAFTestStep(description = "wait until {0} value is {1}")
	public static void waitForValue(String loc, Object value) {
		getElement(loc).waitForValue(value);
	}

	// @QAFTestStep(stepName = "waitForValueWithTimeout", description =
	// "wait {2}sec for {0} value is {1}")
	public static void waitForValue(String loc, Object value, long sec) {
		getElement(loc).waitForValue(value, sec * 1000);
	}

	/**
	 * Gets the (whitespace-trimmed) value of an input field (or anything else
	 * with a value parameter).
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' value is not 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param value
	 *            : {1} : The object of value for element locator
	 */
	@QAFTestStep(description = "wait until {0} value is not {1}")
	public static void waitForNotValue(String loc, Object value) {
		getElement(loc).waitForNotValue(value);

	}

	// @QAFTestStep(stepName = "waitForNotValueWithTimeout", description =
	// "wait {2}sec for {0} value is not {1}")
	public static void waitForNotValue(String loc, Object value, long sec) {
		getElement(loc).waitForNotValue(value, sec * 1000);
	}

	/**
	 * This method wait until the locator in the specified page will be
	 * selected.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' to be selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} to be selected")
	public static void waitForSelected(String loc) {
		getElement(loc).waitForSelected();
	}

	// @QAFTestStep(stepName = "waitForSelectedWithTimeout", description =
	// "wait {1}sec for {0} to be selected")
	public static void waitForSelected(String loc, long sec) {
		getElement(loc).waitForSelected(sec * 1000);

	}

	/**
	 * This method wait until the locator in the specified page will not be
	 * selected.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' is not selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "wait until {0} is not selected")
	public static void waitForNotSelected(String loc) {
		getElement(loc).waitForNotSelected();

	}

	// @QAFTestStep(stepName = "waitForNotSelectedWithTimeout", description =
	// "wait {1}sec for {0} is not selected")
	public static void waitForNotSelected(String loc, long sec) {
		getElement(loc).waitForNotSelected(sec * 1000);

	}

	/**
	 * This method wait until it gets the value of an element attribute for
	 * specified element locator.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' for attribute 'type' value is 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value to be verified
	 * @param value
	 *            : {2} : value of attribute
	 */
	@QAFTestStep(description = "wait until {0} for attribute {1} value is {2}")
	public static void waitForAttribute(String loc, String attr, String value) {
		getElement(loc).waitForAttribute(attr, value);

	}

	// @QAFTestStep(stepName = "waitForAttributeWithTimeout", description =
	// "wait {3}sec for {0} attribute {1} value is {2}")
	public static void waitForAttribute(String loc, String attr, String value, long sec) {
		getElement(loc).waitForAttribute(attr, value, sec * 1000);

	}

	/**
	 * This method wait until it can not gets the value of an element attribute
	 * for specified element locator.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' for attribute 'type' value is not 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value to be verified
	 * @param value
	 *            : {2} : value of attribute
	 */
	@QAFTestStep(description = "wait until {0} attribute {1} value is not {2}")
	public static void waitForNotAttribute(String loc, String attr, String value) {
		getElement(loc).waitForNotAttribute(attr, value);
	}

	// @QAFTestStep(stepName = "waitForNotAttributeWithTimeout", description =
	// "wait {3}sec for {0} attribute {1} value is not {2}")
	public static void waitForNotAttribute(String loc, String attr, String value, long sec) {
		getElement(loc).waitForAttribute(attr, value, sec * 1000);
	}

	/**
	 * This method wait until it gets the CSS class name for specified element
	 * locator
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' css class name is 'ClassName'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : CSS class name to be verified
	 */
	@QAFTestStep(description = "wait until {0} css class name is {1}")
	public static void waitForCssClass(String loc, String className) {
		getElement(loc).waitForCssClass(className);
	}

	// @QAFTestStep(stepName = "waitForCssClassWithTimeout", description =
	// "wait {2}sec for {0} css class name is {1}")
	public static void waitForCssClass(String loc, String className, long sec) {
		getElement(loc).waitForCssClass(className, sec * 1000);
	}

	/**
	 * This method wait until it can not gets the CSS class name for specified
	 * element locator
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' css class name is not 'ClassName'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : CSS class name to be verified
	 */
	@QAFTestStep(description = "wait until {0} css class name is not {1}")
	public static void waitForNotCssClass(String loc, String className) {
		getElement(loc).waitForNotCssClass(className);
	}

	// @QAFTestStep(stepName = "waitForNotCssClassWithTimeout", description =
	// "wait {2}sec for {0} css class name is not {1} ")
	public static void waitForNotCssClass(String loc, String className, long sec) {
		getElement(loc).waitForCssClass(className, sec * 1000);

	}

	/**
	 * This method wait until it gets the property value for specified element
	 * locator
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' property 'propertyStyle' value is 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be verified
	 * @param value
	 *            : {2} : value of property (ie. css style property value)
	 */
	@QAFTestStep(description = "wait until {0} property {1} value is {2}")
	public static void waitForCssStyle(String loc, String prop, String value) {
		getElement(loc).waitForCssStyle(prop, value);

	}

	// @QAFTestStep(stepName = "waitForCssStyleWithTimeout", description =
	// "wait {3}sec for {0} property {1} value is {2} ")
	public static void waitForCssStyle(String loc, String prop, String value, long sec) {
		getElement(loc).waitForCssStyle(prop, value, sec * 1000);

	}

	/**
	 * This method wait until it can not gets the property value for specified
	 * element locator
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * wait until 'my.ele.loc' property 'propertyStyle' value is not 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be verified
	 * @param value
	 *            : {2} : value of property (ie. css style property value)
	 */
	@QAFTestStep(description = "wait until {0} property {1} value is not {2}")
	public static void waitForNotCssStyle(String loc, String prop, String value) {
		getElement(loc).waitForNotCssStyle(prop, value);

	}

	// @QAFTestStep(stepName = "waitForNotCssStyleWithTimeout", description =
	// "wait {3}sec for {0} css property {1} vaule is {2} ")
	public static void waitForNotCssStyle(String loc, String prop, String value, long sec) {
		getElement(loc).waitForNotCssStyle(prop, value, sec * 1000);

	}

	/**
	 * Assert that the specified element is not somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' not present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @return <b>true</b> if the element locator is present, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} not present")
	public static boolean verifyNotPresent(String loc) {

		return getElement(loc).verifyNotPresent();
	}

	/**
	 * Assert that the specified element is not visible somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' not visible<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : an element locator, can be direct locator value or a locator
	 *            key stored in locator repository
	 * @return <b>true</b> if the element locator is visible, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} not visible")
	public static boolean verifyNotVisible(String loc) {
		return getElement(loc).verifyNotVisible();
	}

	/**
	 * Assert that the specified element is not enable somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' not enabled<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : an element locator, can be direct locator value or a locator
	 *            key stored in locator repository
	 * @return <b>true</b> if the element locator is enable, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} enabled")
	public static boolean verifyEnabled(String loc) {
		return getElement(loc).verifyEnabled();
	}

	/**
	 * Assert that the specified element is disabled somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' disabled<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : an element locator, can be direct locator value or a locator
	 *            key stored in locator repository
	 * @return <b>true</b> if the element locator is disabled, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} disabled")
	public static boolean verifyDisabled(String loc) {
		return getElement(loc).verifyDisabled();
	}

	/**
	 * Asserts the text of an element. This works for any element that contains
	 * text. This command uses either the textContent or the innerText of the
	 * element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' text is 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 * @return <b>true</b> if the element locator text is verified, <b>false</b>
	 *         otherwise
	 */

	@QAFTestStep(description = "verify {0} text is {1}")
	public static boolean verifyText(String loc, String text) {
		return getElement(loc).verifyText(text);
	}

	/**
	 * Asserts the text of an element. This works for any element that contains
	 * text. This command uses either the textContent or the innerText of the
	 * element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' text is not 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 * @return <b>true</b> if the element locator text is not verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} text is not {1}")
	public static boolean verifyNotText(String loc, String text) {
		return getElement(loc).verifyNotText(text);
	}

	/**
	 * Asserts the value of an element.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' value is 'Type Value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The type value of element locator
	 * @return <b>true</b> if the element locator type value is verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} value is {1}")
	public static <T> boolean verifyValue(String loc, T t) {
		return getElement(loc).verifyValue(t);
	}

	/**
	 * Asserts not the value of an element.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' value is not 'Type Value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The type value of element locator
	 * @return <b>true</b> if the element locator type value is not verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} value is not {1}")
	public static <T> boolean verifyNotValue(String loc, T t) {
		return getElement(loc).verifyNotValue(t);
	}

	/**
	 * Assert that the specified element is selected somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' is selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @return <b>true</b> if the element locator is selected, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} is selected")
	public static boolean verifySelected(String loc) {

		return getElement(loc).verifySelected();
	}

	/**
	 * Assert that the specified element is not selected somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' is not selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @return <b>true</b> if the element locator is not selected, <b>false</b>
	 *         otherwise
	 */
	@QAFTestStep(description = "verify {0} is not selected")
	public static boolean verifyNotSelected(String loc) {
		return getElement(loc).verifyNotSelected(loc);
	}

	/**
	 * Assert attribute value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' attribute 'type' value is 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value to be verified
	 * @param value
	 *            : {2} : value of attribute
	 * @return <b>true</b> if the element locator attribute value is verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} attribute {1} value is {2}")
	public static boolean verifyAttribute(String loc, String attr, String value) {
		return getElement(loc).verifyAttribute(attr, value);
	}

	/**
	 * Assert not attribute value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' attribute 'type' value is not 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value to be verified
	 * @param value
	 *            : {2} : value of attribute
	 * @return <b>true</b> if the element locator attribute value is not
	 *         verified, <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} attribute {1} value is not {2}")
	public static boolean verifyNotAttribute(String loc, String attr, String value) {
		return getElement(loc).verifyNotAttribute(attr, value);
	}

	/*
	 * @QAFTestStep(description =
	 * "verify {0} not attribute {1} value {3} to be match {2}") public boolean
	 * verifyNotAttribute(String loc, String attr, StringMatcher matcher,
	 * String... label) { return new
	 * QAFExtendedWebElement(loc).verifyNotAttribute(attr, matcher, label); }
	 */
	/**
	 * Assert css class name for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' css class name is 'class name'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : css class name not to be verified
	 * @return <b>true</b> if the element locator css class name is verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} css class name is {1}")
	public static boolean verifyCssClass(String loc, String className) {
		return getElement(loc).verifyCssClass(className);
	}

	/**
	 * Assert not css class name for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' css class name is not 'class name'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : css class name not to be verified
	 * @return <b>true</b> if the element locator css class name is not
	 *         verified, <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} css class name is not {1}")
	public static boolean verifyNotCssClass(String loc, String className) {
		return getElement(loc).verifyNotCssClass(className);
	}

	/**
	 * Assert css property value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' property 'Style' value is 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be verified
	 * @param value
	 *            : {2} : value of property (i.e css style property value)
	 * @return <b>true</b> if the element locator property value is verified,
	 *         <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} property {1} value is {2}")
	public static boolean verifyCssStyle(String loc, String prop, String value) {
		return getElement(loc).verifyCssStyle(prop, value);
	}

	/**
	 * Assert not css property value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * verify 'my.ele.loc' property 'Style' value is not 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be verified
	 * @param value
	 *            : {2} : value of property (i.e css style property value)
	 * @return <b>true</b> if the element locator property value is not
	 *         verified, <b>false</b> otherwise
	 */
	@QAFTestStep(description = "verify {0} property {1} value is not {2}")
	public static boolean verifyNotCssStyle(String loc, String prop, String value) {
		return getElement(loc).verifyNotCssStyle(prop, value);
	}

	/**
	 * Verify that the specified element is not present somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is not present<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is not present")
	public static void assertNotPresent(String loc) {
		getElement(loc).assertNotPresent();
	}

	/**
	 * Verify that the specified element is not visible somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is not visible<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is not visible")
	public static void assertNotVisible(String loc) {
		getElement(loc).assertNotVisible();
	}

	/**
	 * Verify that the specified element is enable somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is enable<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is enable")
	public static void assertEnabled(String loc) {
		getElement(loc).assertEnabled();
	}

	/**
	 * Verify that the specified element is disable somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is disable<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is disable")
	public static void assertDisabled(String loc) {
		getElement(loc).assertDisabled();
	}

	/**
	 * Verify the text of an element. This works for any element that contains
	 * text. This command uses either the textContent or the innerText of the
	 * element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' text is 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 */
	@QAFTestStep(description = "assert {0} text is {1}")
	public static void assertText(String loc, String text) {
		getElement(loc).assertText(text);
	}

	/**
	 * Verify an element is not contain the text. This works for any element
	 * that contains text. This command uses either the textContent or the
	 * innerText of the element, which is the rendered text shown to the user.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' text is not 'infostretch'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param text
	 *            : {1} : The text of element locator
	 */
	@QAFTestStep(description = "assert {0} text is not {1}")
	public static void assertNotText(String loc, String text) {
		getElement(loc).assertNotText(text);
	}

	/**
	 * Verify the (whitespace-trimmed) value of an input field (or anything else
	 * with a value parameter).
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' value is 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param value
	 *            : {1} : The value for element locator
	 */
	@QAFTestStep(description = "assert {0} value is {1}")
	public static void assertValue(String loc, String value) {
		getElement(loc).assertValue(value);
	}

	/**
	 * Not verify the (whitespace-trimmed) value of an input field (or anything
	 * else with a value parameter).
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' value is not 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param value
	 *            : {1} : The object of value for element locator
	 */
	@QAFTestStep(description = "assert {0} value is not {1}")
	public static <T> void assertNotValue(String loc, T t) {
		getElement(loc).assertNotValue(t);
	}

	/**
	 * Verify that the specified element is selected somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is selected")
	public static void assertSelected(String loc) {
		getElement(loc).assertSelected();
	}

	/**
	 * Verify that the specified element is not selected somewhere on the page
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' is not selected<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 */
	@QAFTestStep(description = "assert {0} is not selected")
	public static void assertNotSelected(String loc) {
		getElement(loc).assertNotSelected();
	}

	/**
	 * Verify attribute value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' attribute 'type' value is 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value to be asserted
	 * @param value
	 *            : {2} : value of attribute
	 */
	@QAFTestStep(description = "assert {0} attribute {1} value is {2}")
	public static void assertAttribute(String loc, String attr, String value) {
		getElement(loc).assertAttribute(attr, value);
	}

	/**
	 * Verify not attribute value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' attribute 'type' value is not 'send'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value not to be asserted
	 * @param value
	 *            : {2} : value of attribute
	 */
	@QAFTestStep(description = "assert {0} attribute {1} value is not {2}")
	public static void assertNotAttribute(String loc, String attr, String value) {
		getElement(loc).assertNotAttribute(attr, value);
	}

	// public static void assertNotAttribute(String attr, StringMatcher matcher,
	// String... label) {
	// // TODO Auto-generated method stub
	//
	// }
	/**
	 * verify css class name for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' css class name is 'class name'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : css class name to be asserted
	 */
	@QAFTestStep(description = "assert {0} css class name is {1}")
	public static void assertCssClass(String loc, String className) {
		getElement(loc).assertCssClass(className);
	}

	/**
	 * verify not css class name for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' css class name is not 'class name'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param className
	 *            : {1} : css class name not to be asserted
	 */
	@QAFTestStep(description = "assert {0} css class name is not {1}")
	public static void assertNotCssClass(String loc, String className) {
		getElement(loc).assertNotCssClass(className);
	}

	/**
	 * Verify css property value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' property 'Style' value is 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be asserted
	 * @param value
	 *            : {2} : value of property (i.e css style property value)
	 */
	@QAFTestStep(description = "assert {0} property {1} value is {2}")
	public static void assertCssStyle(String loc, String prop, String value) {
		getElement(loc).assertCssStyle(prop, value);
	}

	/**
	 * Verify not css property value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * assert 'my.ele.loc' property 'Style' value is not 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param prop
	 *            : {1} : property (css style) to be asserted
	 * @param value
	 *            : {2} : value of property (i.e css style property value)
	 */
	@QAFTestStep(description = "assert {0} property {1} value is not {2}")
	public static void assertNotCssStyle(String loc, String prop, String value) {
		getElement(loc).assertNotCssStyle(prop, value);
	}

	/**
	 * set the attribute value for specific locator somewhere in the page.
	 * <p>
	 * Example:
	 * <p>
	 * BDD
	 * </p>
	 * <code>
	 * set 'my.ele.loc' attribute 'type' value is 'value'<br/>
	 * </code>
	 * <p>
	 * KWD
	 * </p>
	 * 
	 * @param loc
	 *            : {0} : an element locator, can be direct locator value or a
	 *            locator key stored in locator repository
	 * @param attr
	 *            : {1} : attribute name which value not to be asserted
	 * @param value
	 *            : {2} : value of attribute
	 */
	@QAFTestStep(description = "set {0} attribute {1} value is {2}")
	public static void setAttribute(String loc, String attr, String value) {
		getElement(loc).setAttribute(attr, value);
	}

	/**
	 * add cookie, This will valid for the entire domain
	 * 
	 * @param name
	 *            : {0} name of the cookie
	 * @param value
	 *            : {1} value of the cookie
	 */
	@QAFTestStep(description = "add cookie {0} with value {1}")
	public static void addCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		new WebDriverTestBase().getDriver().manage().addCookie(cookie);
	}

	/**
	 * Delete the named cookie from the current domain. This is equivalent to
	 * setting the named cookie's expiry date to some time in the past.
	 * 
	 * @param name
	 *            : {0} name of the cookie to be deleted
	 */
	@QAFTestStep(description = "delete cookie with name {0}")
	public static void deleteCookie(String name) {
		new WebDriverTestBase().getDriver().manage().deleteCookieNamed(name);
	}

	/**
	 * Delete all the cookies for the current domain.
	 */
	@QAFTestStep(description = "delete all cookies")
	public static void deleteAllCookies() {
		new WebDriverTestBase().getDriver().manage().deleteAllCookies();
	}

	/**
	 * Get a cookie with a given name
	 * 
	 * @param name
	 *            : {0} name of the cookie
	 */
	@QAFTestStep(description = "get a cookie with a name {0}")
	public static void getCookieValue(String name) {
		new WebDriverTestBase().getDriver().manage().getCookieNamed(name).getValue();
	}

	@QAFTestStep(description = "mouse move on {0}")
	public static void mouseOver(String loc) {
		new WebDriverTestBase().getDriver().getMouse().mouseMove(getElement(loc).getCoordinates());
	}

	/**
	 * Start time tracking which can be stopped by subsequent call to
	 * {@link #stopTransaction()}. It will group all steps and track time with
	 * given threshold comparison.
	 * 
	 * @param name
	 * @param threshold
	 */
	@MetaData("{'qafstep-transaction':true}")
	@QAFTestStep(description = "start (transaction|time-tracker) for {task-name} with {second}s threshold")
	public static void startTransaction(String name, int threshold) {

	}

	@MetaData("{'qafstep-transaction':true}")
	@QAFTestStep(description = "stop (transaction|time-tracker)")
	public static void stopTransaction() {

	}

}
