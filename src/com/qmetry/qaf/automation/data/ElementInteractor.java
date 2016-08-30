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


package com.qmetry.qaf.automation.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;

// import com.thoughtworks.selenium.Wait;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.ui.SeleniumTestBase;
import com.qmetry.qaf.automation.ui.WebDriverCommandLogger;
import com.qmetry.qaf.automation.ui.annotations.UiElement.Type;
import com.qmetry.qaf.automation.ui.selenium.webdriver.QAFWebDriverBackedSelenium;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
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
	private final SeleniumTestBase stb;

	public ElementInteractor() {
		stb = new SeleniumTestBase();// TestBaseProvider.instance().get();
	}

	public Object fetchValue(String loc, Type type) {
		try {
			switch (type) {
			case textbox:
			case textarea:
			case optionbox:
				return stb.getDriver().getValue(loc);
			case checkbox:
				return stb.getDriver().isChecked(loc);
			case selectbox:
			case multiselectbox:
				return stb.getDriver().getSelectedLabel(loc);
			default:
				return stb.getDriver().getText(loc);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return "";
		}
	}

	public void fillValue(String loc, String val, Type type) {
		if (stb.getBrowser().toUpperCase().contains("DRIVER")) {
			fillValueWD(loc, val, type, ((QAFWebDriverBackedSelenium) stb.getDriver()).getWrappedDriver());
		} else {
			fillValueRC(loc, val, type);
		}
	}

	public void fillValue(String loc, String val, Type type, boolean pagewaitAfterFill) {
		fillValue(loc, val, type);
	}

	public boolean verifyValue(final String loc, final String val, final Type type) {
		Object actualVal = null;
		try {
			actualVal = fetchValue(loc, type);
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

	private QAFExtendedWebElement getElement(String loc, QAFExtendedWebDriver driver) {
		QAFExtendedWebElement ele = new QAFExtendedWebElement(loc);
		ele.waitForVisible();
		return ele;
	}

	private QAFExtendedWebElement getOptBox(String loc, QAFExtendedWebDriver driver, String val) {
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
				return new QAFExtendedWebElement(driver, By.cssSelector(parts[1] + "[value=" + val + "]"));
			}
		} else {
			xpath = String.format("//input[@name='%s' or @id='%s' or @value='%s']", loc, loc, loc);

		}
		if (val.startsWith("lineNo=")) {
			ele = new QAFExtendedWebElement(driver, By.xpath(xpath + "[" + val.split("=", 2)[1] + "]"));
		} else if (val.startsWith("xpath=")) {
			ele = new QAFExtendedWebElement(driver, By.xpath(xpath + "[" + val.split("=", 2)[1] + "]"));
		} else {
			ele = new QAFExtendedWebElement(driver, By.xpath(xpath + "[@value='" + val + "']"));
		}
		ele.waitForVisible();
		return ele;
	}

	private QAFExtendedWebElement getOptionElement(String loc, String val, QAFExtendedWebDriver driver) {
		String optLoc;
		if (!val.contains("=")) {
			if (StringUtil.isNumeric(val)) {
				optLoc = String.format(".//option[@value='%s' or @lineNo=%s or  @id='%s' or contains(.,'%s') ]", val,
						val, val, val);
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
		return new QAFExtendedWebElement(getElement(loc, driver), By.xpath(optLoc));
	}

	private void fillValueWD(String loc, String val, Type type, QAFExtendedWebDriver driver) {
		switch (type) {
		case textbox:
		case textarea:
			QAFExtendedWebElement txt = getElement(loc, driver);
			txt.clear();
			txt.sendKeys(val);
			break;
		case file:
			getElement(loc, driver).sendKeys(val);
			break;
		case checkbox:
			QAFExtendedWebElement ele = getElement(loc, driver);
			boolean isChecked = ele.isSelected();
			if (Boolean.valueOf(val) != isChecked) {
				ele.click();
			}
			break;
		case optionbox:
			QAFExtendedWebElement opttionbox = getOptBox(loc, driver, val);
			boolean isSelected = opttionbox.isSelected();
			if (!isSelected) {
				opttionbox.click();
			}
			break;
		case selectbox:
		case multiselectbox:
			try {
				QAFWebElement opt = getOptionElement(loc, val, driver);
				if (!opt.isSelected()) {
					opt.click();
				}
			} catch (Exception e) {
				QAFExtendedWebElement sel = getElement(loc, driver);
				sel.sendKeys(val);
			}

			break;
		default:
			break;
		}
	}

	private void fillValueRC(String loc, String val, Type type) {
		switch (type) {
		case textbox:
		case textarea:
		case file:
			stb.getDriver().type(loc, val);
			break;
		case checkbox:
		case optionbox:
			if (Boolean.valueOf(val)) {
				stb.getDriver().check(loc);
			} else {
				stb.getDriver().uncheck(loc);
			}
			break;
		case selectbox:
		case multiselectbox:
			stb.getDriver().select(loc, val);
			break;
		default:
			break;
		}
	}
}
