/**
 * 
 */
package com.microsoft.playwright.impl;


import com.microsoft.playwright.Page;
/**
 * @author chirag
 *
 */
public class PageValidator extends PageAssertionsImpl implements ExpectImplementor {
	private final boolean isAssert;

	/**
	 * @param page
	 */
	public PageValidator(Page page) {
		this(page, false);
	}

	public PageValidator(Page page, boolean isAssert) {
		super(page);
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
