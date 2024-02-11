/**
 * 
 */
package com.qmetry.qaf.automation.ui.playwright;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.APIResponseAssertions;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;
import com.microsoft.playwright.impl.APIResponseAssertionsImpl;
import com.microsoft.playwright.impl.LocatorValidator;
import com.microsoft.playwright.impl.PageValidator;

/**
 * @author chirag
 *
 */
public class Validator {

	/**
	 * 
	 */
	private Validator() {
	}

	/**
	 * Creates a {@code APIResponseAssertions} object for the given
	 * {@code APIResponse}.
	 *
	 * <p>
	 * **Usage**
	 * 
	 * <pre>{@code
	 * Validator.assertThat(response).isOK();
	 * }</pre>
	 *
	 * @param response {@code APIResponse} object to use for assertions.
	 * @since v4.0.0
	 */
	public static APIResponseAssertions assertThat(APIResponse response) {
		return new APIResponseAssertionsImpl(response);
	}

	/**
	 * Creates a {@code LocatorAssertions} object for the given {@code Locator}.
	 *
	 * <p>
	 * **Usage**
	 * 
	 * <pre>{@code
	 * Validator.assertThat(locator).isVisible();
	 * }</pre>
	 *
	 * @param locator {@code Locator} object to use for assertions.
	 * @since v4.0.0
	 */
	public static LocatorAssertions assertThat(Locator locator) {
		return new LocatorValidator(locator, true);
	}

	/**
	 * Creates a {@code LocatorAssertions} object for the given {@code Locator}.
	 *
	 * <p>
	 * **Usage**
	 * 
	 * <pre>{@code
	 * Validator.verifyThat(locator).isVisible();
	 * }</pre>
	 *
	 * @param locator {@code Locator} object to use for verification.
	 * @since v4.0.0
	 */
	public static LocatorAssertions verifyThat(Locator locator) {
		return new LocatorValidator(locator);
	}

	/**
	 * Creates a {@code PageAssertions} object for the given {@code Page}.
	 *
	 * <p>
	 * **Usage**
	 * 
	 * <pre>{@code
	 * Validator.assertThat(page).hasTitle("News");
	 * }</pre>
	 *
	 * @param page {@code Page} object to use for assertions.
	 * @since v4.0.0
	 */
	public static PageAssertions assertThat(Page page) {
		return new PageValidator(page, true);
	}

	/**
	 * Creates a {@code PageAssertions} object for the given {@code Page}.
	 *
	 * <p>
	 * **Usage**
	 * 
	 * <pre>{@code
	 * Validator.verifyThat(page).hasTitle("News");
	 * }</pre>
	 *
	 * @param page {@code Page} object to use for assertions.
	 * @since v4.0.0
	 */
	public static PageAssertions verifyThat(Page page) {
		return new PageValidator(page);
	}
}
