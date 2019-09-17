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

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;

import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * This listener provides capability of defining scroll behavior, encrypted
 * value for password based on element on meta-data.
 * <p>
 * Examples:
 * <ul>
 * <li>{'locator':'name=q','scroll': 'OnFail','sendkeys-options': 'click clear','scroll-options': '{block: \'center\'}'}
 * <li>{'locator':'name=pwdTxt','sendkeys-options': 'clear','type': 'password'}
 * <li>{}
 * </ul>
 * 
 * @since 2.1.13
 * @author chirag.jayswal
 *
 */
public class ElementMetaDataListener extends QAFWebElementCommandAdapter {
	/**
	 * meta-key to specify scroll behavior, possible values:
	 * <ul>
	 * <li>Always/true - always scroll before commands required scroll
	 * <li>OnFail - retry with scroll on failure for commands required scroll
	 * </ul>
	 */
	public static final String SCROLL = "scroll";

	/**
	 * meta-key to specify scroll options, A value that indicates the type of
	 * the align::
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>{'scroll-options': 'true'}
	 * <li>{'scroll-options': 'false'}
	 * <li>{'scroll-options': '{block: \'center\'}'}
	 * </ul>
	 * 
	 * @see https://developer.mozilla.org/en-US/docs/Web/API/Element/scrollIntoView
	 */
	public static final String SCROLL_OPTIONS = "scroll-options";

	/**
	 * meta-key to specify one or more send-keys options, possible values:
	 * <ul>
	 * <li>click: to specify click before send-keys
	 * <li>clear: to specify clear before send-keys
	 * </ul>
	 * Examples:
	 * <ul>
	 * <li>{'sendkeys-options': 'clear'}
	 * <li>{'sendkeys-options': 'click'}
	 * <li>{'sendkeys-options': 'click clear'}
	 * </ul>
	 */
	public static final String SENDKEYS_OPTIONS = "sendkeys-options";

	/**
	 * meta-key for send-keys to specify element type, possible values:
	 * <ul>
	 * <li>password/encrypted - required to decode before send keys
	 * <li>select - specify this is select (basic support only) with options and
	 * choose option
	 * </ul>
	 * Examples:
	 * <ul>
	 * <li>{'type': 'password'}
	 * <li>{'type': 'select'}
	 * </ul>
	 */
	public static final String TYPE = "type";

	private static final List<String> COMMANDS_REQUIRES_SCROLL = Arrays.asList(DriverCommand.CLICK_ELEMENT,
			DriverCommand.SEND_KEYS_TO_ELEMENT, DriverCommand.CLICK, DriverCommand.CLICK_ELEMENT,
			DriverCommand.DOUBLE_CLICK, DriverCommand.MOVE_TO, DriverCommand.MOUSE_DOWN, DriverCommand.MOUSE_UP,
			DriverCommand.ELEMENT_SCREENSHOT);

	@Override
	public void beforeCommand(QAFExtendedWebElement element, CommandTracker commandTracker) {
		boolean isScrollRequired = getScrollBehavoir(element).startsWith("A")
				|| getScrollBehavoir(element).startsWith("T");
		if (isScrollRequired && COMMANDS_REQUIRES_SCROLL.contains(commandTracker.getCommand())) {
			scrollToElement(element);
		}

		processSendKeys(element, commandTracker);
	}

	private void processSendKeys(QAFExtendedWebElement element, CommandTracker commandTracker) {
		if (DriverCommand.SEND_KEYS_TO_ELEMENT.equalsIgnoreCase(commandTracker.getCommand())) {
			CharSequence[] values = ((CharSequence[]) commandTracker.getParameters().get("value"));

			String sendkeysOpts = getSendkeysOptions(element);
			if (sendkeysOpts.indexOf("CLICK") >= 0) {
				element.click();
			}
			if (sendkeysOpts.indexOf("CLEAR") >= 0) {
				element.clear();
				if (values[0] == null || values[0].length() == 0) {
					// request to have empty field, which is done with
					// clear.....
					commandTracker.setResponce(new Response());
					return;
				}
			}
			if (isEncrypted(element)) {
				String encriptedPassword = values[0].toString();
				//decrypt encrypted text
				values[0] = ConfigurationManager.getBundle().getPasswordDecryptor()
						.getDecryptedPassword(encriptedPassword);
				Response response=element.executeWitoutLog(commandTracker.getCommand(), commandTracker.getParameters());
				commandTracker.setResponce(response);
				//reset encrypted text
				values[0] =encriptedPassword;
			} else if (isSelect(element)) {
				String locateOptionBy = "byText";
				List<WebElement> options = element.findElements(By.tagName("option"));
				for (CharSequence value : values) {
					String str = value.toString();
					if (str.equalsIgnoreCase("byValue") || str.equalsIgnoreCase("byText")
							|| str.equalsIgnoreCase("byIndex")) {
						locateOptionBy = str;
						continue;
					}
					for (int i = 0; i < options.size(); i++) {
						WebElement option = options.get(i);
						if (locateOptionBy.equalsIgnoreCase("byIndex")
								&& String.valueOf(i).equalsIgnoreCase(value.toString())) {
							element.executeScript("selectedIndex=" + i);
						} else if (locateOptionBy.equalsIgnoreCase("byValue")
								&& String.valueOf(str).equalsIgnoreCase(option.getAttribute("value"))) {
							element.executeScript("selectedIndex=" + i);
						} else if (locateOptionBy.equalsIgnoreCase("byValue")
								&& String.valueOf(str).equalsIgnoreCase(option.getText())) {
							element.executeScript("selectedIndex=" + i);
						}
					}
				}
				// done using js
				commandTracker.setResponce(new Response());
			}
		}

	}

	@Override
	public void onFailure(QAFExtendedWebElement element, CommandTracker commandTracker) {
		boolean isScrollRequired = getScrollBehavoir(element).startsWith("O");
		if (isScrollRequired && COMMANDS_REQUIRES_SCROLL.contains(commandTracker.getCommand()) && element.isPresent()) {
			scrollToElement(element);
			commandTracker.setRetry(true);
		}
	}

	private void scrollToElement(QAFExtendedWebElement element) {
		element.executeScript("scrollIntoView(" + getScrollOptions(element) + ");");
		// element.getWrappedDriver().executeScript(
		// "arguments[0].scrollIntoView(false);arguments[0].scrollIntoView({block:
		// 'center'});", element);
	}

	private String getScrollBehavoir(QAFExtendedWebElement element) {
		return element.getMetaData().containsKey(SCROLL) ? element.getMetaData().get(SCROLL).toString().toUpperCase()
				: "";
	}

	private String getScrollOptions(QAFExtendedWebElement element) {
		return element.getMetaData().containsKey(SCROLL_OPTIONS) ? element.getMetaData().get(SCROLL_OPTIONS).toString()
				: "";
	}

	private String getSendkeysOptions(QAFExtendedWebElement element) {
		return element.getMetaData().containsKey(SENDKEYS_OPTIONS)
				? element.getMetaData().get(SENDKEYS_OPTIONS).toString().toUpperCase() : "";
	}

	private boolean isEncrypted(QAFExtendedWebElement element) {
		return element.getMetaData().containsKey(TYPE)
				&& (element.getMetaData().get(TYPE).toString().toUpperCase().startsWith("P")
						|| element.getMetaData().get(TYPE).toString().toUpperCase().startsWith("E"));
	}

	private boolean isSelect(QAFExtendedWebElement element) {
		return element.getMetaData().containsKey(TYPE)
				&& element.getMetaData().get(TYPE).toString().toUpperCase().startsWith("S");
	}

}
