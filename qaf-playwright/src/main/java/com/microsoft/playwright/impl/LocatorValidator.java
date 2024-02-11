/**
 * 
 */
package com.microsoft.playwright.impl;

import com.microsoft.playwright.Locator;

/**
 * @author chirag
 *
 */
public class LocatorValidator extends LocatorAssertionsImpl implements ExpectImplementor {
	private final boolean isAssert;

	public LocatorValidator(Locator locator) {
		this(locator, false);
	}

	public LocatorValidator(Locator locator, boolean isAssert) {
		super(locator);
		this.isAssert = isAssert;
	}

	void expectImpl(String expression, FrameExpectOptions expectOptions, Object expected, String message) {
		validate(expression, expectOptions, expected, message, isAssert);
	}

	@Override
	public void expect(String expression, FrameExpectOptions expectOptions, Object expected, String message) {
		super.expectImpl(expression, expectOptions, expected, message);
	}

}
