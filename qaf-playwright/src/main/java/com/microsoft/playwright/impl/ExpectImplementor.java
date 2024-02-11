/**
 * 
 */
package com.microsoft.playwright.impl;

import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.Reporter;

/**
 * @author chirag
 *
 */
public interface ExpectImplementor {

	void expect(String expression, FrameExpectOptions expectOptions, Object expected, String message);

	default void validate(String expression, FrameExpectOptions expectOptions, Object expected, String message,
			boolean isAssert) {
		try {
			if (expectOptions.timeout == null) {
				expectOptions.timeout = (double) ApplicationProperties.SELENIUM_WAIT_TIMEOUT
						.getIntVal(Double.valueOf(AssertionsTimeout.defaultTimeout).intValue());
			}
			expect(expression, expectOptions, expected, message);

			if (expectOptions.isNot) {
				message = message.replace("expected to", "expected not to");
			}
			message += ": " + expected;
			Reporter.log(message, MessageTypes.Pass);
		} catch (AssertionError e) {
			Reporter.log(e.getLocalizedMessage(), MessageTypes.Fail);
			if (isAssert) {
				throw e;
			}

		}

	}

}
