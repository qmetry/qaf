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

package com.qmetry.qaf.automation.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.core.AutomationError;
// import com.thoughtworks.selenium.Wait;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.annotations.UiElement.Type;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebComponent;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.core.ui.ElementInteractor.java
 * 
 * @author chirag
 */
public class ElementInteractor {
	protected final Log logger = LogFactory.getLog(getClass());

	public Object fetchValue(String loc, Type type,
			Class<? extends QAFExtendedWebElement> eleClass) {
		try {
			WebElement ele = getElement(loc, eleClass);
			switch (type) {
				case optionbox :
					return ele.getAttribute("value");
				case checkbox :
					return ele.isSelected();
				case selectbox :
					return new SelectBox(loc).getSelectedLable();
				case multiselectbox :
					return new SelectBox(loc).getSelectedLables();
				default :
					return ele.getText();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return "";
		}
	}

	public void fillValue(String loc, String val, Type type,
			Class<? extends QAFExtendedWebElement> eleClass) {
		QAFExtendedWebElement ele = getElement(loc, eleClass);
		switch (type) {
			case textbox :
			case textarea :
				ele.clear();
				ele.sendKeys(val);
				break;
			case file :
				ele.sendKeys(val);
				break;
			case checkbox :
				boolean isChecked = ele.isSelected();
				if (Boolean.valueOf(val) != isChecked) {
					ele.click();
				}
				break;
			case optionbox :
				QAFExtendedWebElement opttionbox = getOptBox(loc, val, eleClass);
				boolean isSelected = opttionbox.isSelected();
				if (!isSelected) {
					opttionbox.click();
				}
				break;
			case selectbox :
			case multiselectbox :
				try {
					QAFWebElement opt = getOptionElement(loc, val, eleClass);
					if (!opt.isSelected()) {
						opt.click();
					}
				} catch (Exception e) {
					ele.sendKeys(val);
				}

				break;
			default :
				break;
		}

	}

	public boolean verifyValue(final String loc, final String val, final Type type,
			Class<? extends QAFExtendedWebElement> eleClass) {
		Object actualVal = null;
		try {
			actualVal = fetchValue(loc, type, eleClass);
			boolean result = StringUtil.seleniumEquals(String.valueOf(actualVal), val);
			report("value", result, val, actualVal);
			return result;
		} catch (Exception e) {
			report("value", false, val, actualVal);
			return false;
		}
	}

	protected void report(String op, boolean outcome, Object... args) {
		// assertEquals(stb.getDriver().getText(locator), text, message);

		Reporter.log(WebDriverCommandLogger.getMsgForElementOp(op, outcome, args),
				(outcome ? MessageTypes.Pass : MessageTypes.Fail));
	}

	private QAFExtendedWebElement getElement(String loc,
			Class<? extends QAFExtendedWebElement> eleClass) {

		try {
			Constructor<? extends QAFExtendedWebElement> con =
					eleClass.getConstructor(String.class);
			con.setAccessible(true);
			QAFExtendedWebElement ele = con.newInstance(loc);
			ele.waitForVisible();
			return ele;
		} catch (Exception e) {
			throw new AutomationError(e);
		}

	}

	private QAFExtendedWebElement getOptBox(String loc, String val,
			Class<? extends QAFExtendedWebElement> eleClass) {
		QAFExtendedWebElement ele = null;
		String xpath = "";
		if (loc.startsWith("//")) {
			xpath = loc;
		} else if (loc.indexOf("=") > 0) {
			String parts[] = loc.split("=", 2);
			if (parts[0].equalsIgnoreCase("name")) {
				xpath = "//input[@name='" + parts[1] + "']";
			} else if (parts[0].equalsIgnoreCase("id")) {
				xpath = "//input[@id='" + parts[1] + "']";
			} else if (parts[0].equalsIgnoreCase("xpath")) {
				xpath = parts[1];
			} else if (parts[0].equalsIgnoreCase("css")) {
				return new QAFExtendedWebElement(
						By.cssSelector(parts[1] + "[value=" + val + "]"));
			}
		} else {
			xpath = String.format("//input[@name='%s' or @id='%s' or @value='%s']", loc,
					loc, loc);

		}
		if (val.startsWith("lineNo=")) {
			ele = new QAFExtendedWebElement(
					By.xpath(xpath + "[" + val.split("=", 2)[1] + "]"));
		} else if (val.startsWith("xpath=")) {
			ele = new QAFExtendedWebElement(
					By.xpath(xpath + "[" + val.split("=", 2)[1] + "]"));
		} else {
			ele = new QAFExtendedWebElement(By.xpath(xpath + "[@value='" + val + "']"));
		}
		ele.waitForVisible();
		return ele;
	}

	private QAFExtendedWebElement getOptionElement(String loc, String val,
			Class<? extends QAFExtendedWebElement> eleClass) {
		String optLoc;
		if (!val.contains("=")) {
			if (StringUtil.isNumeric(val)) {
				optLoc = String.format(
						".//option[@value='%s' or @lineNo=%s or  @id='%s' or contains(.,'%s') ]",
						val, val, val, val);
			} else {
				optLoc = String.format(
						".//option[translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s' or translate(@value,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s' or translate(@id,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s']",
						val.toUpperCase(), val.toUpperCase(), val.toUpperCase());
			}
		} else {
			String[] parts = val.split("=", 2);
			if (parts[0].equalsIgnoreCase("label") || parts[0].equalsIgnoreCase("text")) {
				optLoc = String.format(".//option[contains(.,'%s')]", parts[1]);
			} else if (parts[0].equalsIgnoreCase("lineNo")) {
				optLoc = String.format(".//option[%d]", Integer.parseInt(parts[1]) + 1);
			} else {
				optLoc = String.format(".//option[@%s='%s']", parts[0], parts[1]);
			}

		}
		return new QAFExtendedWebElement(getElement(loc, eleClass), By.xpath(optLoc));
	}

	public static class SelectBox extends QAFWebComponent {

		public SelectBox(String locator) {
			super(locator);
		}

		public List<QAFWebElement> getSelectedOptions() {
			return findElements("xpath=.//option[@selected]");
		}
		public List<QAFWebElement> getAllOptions() {
			return findElements("xpath=.//option");
		}

		public QAFWebElement getOption(String val) {
			String optLoc;
			if (!val.contains("=")) {
				if (StringUtil.isNumeric(val)) {
					optLoc = String.format(
							".//option[@value='%s' or @lineNo=%s or  @id='%s' or contains(.,'%s') ]",
							val, val, val, val);
				} else {
					optLoc = String.format(
							".//option[translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s' or translate(@value,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s' or translate(@id,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s']",
							val.toUpperCase(), val.toUpperCase(), val.toUpperCase());
				}
			} else {
				String[] parts = val.split("=", 2);
				if (parts[0].equalsIgnoreCase("label")
						|| parts[0].equalsIgnoreCase("text")) {
					optLoc = String.format(".//option[contains(.,'%s')]", parts[1]);
				} else if (parts[0].equalsIgnoreCase("lineNo")) {
					optLoc = String.format(".//option[%d]",
							Integer.parseInt(parts[1]) + 1);
				} else {
					optLoc = String.format(".//option[@%s='%s']", parts[0], parts[1]);
				}

			}
			return findElement("xpath=" + optLoc);
		}

		public String getSelectedLable() {
			List<QAFWebElement> options = getSelectedOptions();

			return options.isEmpty() ? "" : options.get(0).getText();
		}

		public List<String> getSelectedLables() {
			ArrayList<String> labels = new ArrayList<String>();
			List<QAFWebElement> options = getSelectedOptions();
			for (QAFWebElement option : options) {
				labels.add(option.getText());
			}
			return labels;
		}
	}

}
