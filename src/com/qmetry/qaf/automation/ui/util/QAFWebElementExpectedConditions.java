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

package com.qmetry.qaf.automation.ui.util;

import java.util.List;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.core.ui.IsWebElementExpectedConditions.java
 * 
 * @author chirag.jayswal
 */
public class QAFWebElementExpectedConditions {
	// Restricted to create objects
	private QAFWebElementExpectedConditions() {

	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementVisible() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return element.isPresent() && element.isDisplayed();
			}
		};
	}

	public static ExpectedCondition<List<QAFWebElement>, Boolean> anyElementVisible() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (element.isPresent() && element.isDisplayed()) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	public static ExpectedCondition<List<QAFWebElement>, Boolean> allElementVisible() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (!(element.isPresent() && element.isDisplayed())) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementPresent() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return element.isPresent();
			}
		};
	}
	public static ExpectedCondition<List<QAFWebElement>, Boolean> anyElementPresent() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (element.isPresent()) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	public static ExpectedCondition<List<QAFWebElement>, Boolean> allElementPresent() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (element.isPresent()) {
						return false;
					}
				}
				return true;
			}
		};
	}
	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementNotPresent() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !element.isPresent();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementNotVisible() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !element.isPresent() || !element.isDisplayed();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementEnabled() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return element.isEnabled();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementDisabled() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !element.isEnabled();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementAttributeValueEq(final String attributeName,
			final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return ((StringMatcher) val).match(element.getAttribute(attributeName));
				}
				return StringUtil.seleniumEquals(element.getAttribute(attributeName), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementAttributeValueNotEq(
			final String attributeName, final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return !((StringMatcher) val).match(element.getAttribute(attributeName));
				}
				return !StringUtil.seleniumEquals(element.getAttribute(attributeName), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementCssPropertyValueEq(final String propertyName,
			final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return StringUtil.seleniumEquals(element.getCssValue(propertyName), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementCssPropertyValueNotEq(
			final String propertyName, final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !StringUtil.seleniumEquals(element.getCssValue(propertyName), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementTextEq(final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return ((StringMatcher) val).match(element.getText());
				}
				return StringUtil.seleniumEquals(element.getText(), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementTextNotEq(final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return !((StringMatcher) val).match(element.getText());
				}
				return !StringUtil.seleniumEquals(element.getText(), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementValueEq(final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return ((StringMatcher) val).match(element.getAttribute("value"));
				}
				return StringUtil.seleniumEquals(element.getAttribute("value"), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementValueNotEq(final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				if (val instanceof StringMatcher) {
					return !((StringMatcher) val).match(element.getAttribute("value"));
				}
				return !StringUtil.seleniumEquals(element.getAttribute("value"), String.valueOf(val));
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementSelected() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return element.isSelected();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementNotSelected() {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !element.isSelected();
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementHasCssClass(final String className) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return element.getAttribute("class").contains(className);
			}
		};
	}

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementHasNotCssClass(final String className) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !element.getAttribute("class").contains(className);
			}
		};
	}
}
