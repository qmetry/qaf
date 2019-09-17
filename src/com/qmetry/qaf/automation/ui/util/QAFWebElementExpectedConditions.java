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
package com.qmetry.qaf.automation.ui.util;

import java.util.List;

import org.openqa.selenium.support.Color;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.core.ui.QAFWebElementExpectedConditions.java
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
			String msg = "any of elements to be visible";

			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				msg = "any of elements to be visible";
				for (QAFWebElement element : elements) {
					if (element.isPresent() && element.isDisplayed()) {
						return true;
					}
					msg = msg+" "+((QAFExtendedWebElement)element).getDescription();
				}
				return false;
			}
			@Override
			public String toString() {
				return msg;
			}
		};
	}
	
	public static ExpectedCondition<List<QAFWebElement>, Boolean> allElementVisible() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			String msg = "elements to be visible";
			
			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (!(element.isPresent() && element.isDisplayed())) {
						msg = "elements to be visible "+((QAFExtendedWebElement)element).getDescription();
						return false;
					}
				}
				return true;
			}
			
			@Override
			public String toString() {
				return msg;
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
			String msg = "any of elements to be present";

			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				msg = "any of elements to be present";

				for (QAFWebElement element : elements) {
					if (element.isPresent()) {
						return true;
					}
					msg = " "+((QAFExtendedWebElement)element).getDescription();
				}
				return false;
			}
			
			@Override
			public String toString() {
				return msg;
			}
		};
	}
	
	public static ExpectedCondition<List<QAFWebElement>, Boolean> allElementPresent() {
		return new ExpectedCondition<List<QAFWebElement>, Boolean>() {
			String msg = "all of elements to be present";

			@Override
			public Boolean apply(List<QAFWebElement> elements) {
				for (QAFWebElement element : elements) {
					if (!element.isPresent()) {
						msg = "elements to be present " + ((QAFExtendedWebElement)element).getDescription();
						return false;
					}
				}
				return true;
			}
			
			@Override
			public String toString() {
				return msg;
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

	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementCssColorPropertyValueEq(final String propertyName,
			final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return Color.fromString(element.getCssValue(propertyName)).asRgba().equals(Color.fromString(String.valueOf(val)).asRgba());
			}
		};
	}
	public static ExpectedCondition<QAFExtendedWebElement, Boolean> elementCssColorPropertyValueNotEq(
			final String propertyName, final Object val) {
		return new ExpectedCondition<QAFExtendedWebElement, Boolean>() {
			@Override
			public Boolean apply(QAFExtendedWebElement element) {
				return !Color.fromString(element.getCssValue(propertyName)).asRgba().equals(Color.fromString(String.valueOf(val)).asRgba());
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
