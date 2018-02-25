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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.core.QAFTestBase.pause;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.qmetry.qaf.automation.keys.ApplicationProperties;

/**
 * @author chirag.jayswal
 *
 */
public class DynamicWait<T> implements Wait<T> {
	private T input;
	private long timeout = getDefaultTimeout();
	private long interval = getDefaultTimeout();
	private Supplier<String> messageSupplier = new StringSupplier(null);
	private List<Class<? extends Throwable>> ignoredExceptions = Lists.newLinkedList();

	public DynamicWait(T input) {
		this.input = checkNotNull(input);
	}

	/**
	 * Sets the message to be displayed when time expires.
	 *
	 * @param message
	 *            to be appended to default.
	 * @return A self reference.
	 */
	public DynamicWait<T> withMessage(final String message) {
		this.messageSupplier = new StringSupplier(message);
		return this;
	}

	/**
	 * Sets the message to be evaluated and displayed when time expires.
	 *
	 * @param messageSupplier
	 *            to be evaluated on failure and appended to default.
	 * @return A self reference.
	 */
	public DynamicWait<T> withMessage(Supplier<String> messageSupplier) {
		this.messageSupplier = messageSupplier;
		return this;
	}

	/**
	 * Sets how often the condition should be evaluated.
	 *
	 * <p>
	 * In reality, the interval may be greater as the cost of actually
	 * evaluating a condition function is not factored in. The default polling
	 * interval is {@link #FIVE_HUNDRED_MILLIS}.
	 *
	 * @param duration
	 *            The timeout duration.
	 * @param unit
	 *            The unit of time.
	 * @return A self reference.
	 */
	public DynamicWait<T> pollingEvery(long duration, TimeUnit unit) {
		this.interval = duration(duration, unit);
		return this;
	}

	/**
	 * Repeatedly applies this instance's input value to the given predicate
	 * until the timeout expires or the predicate evaluates to true.
	 *
	 * @param isTrue
	 *            The predicate to wait on.
	 * @throws TimeoutException
	 *             If the timeout expires.
	 */
	public void until(final Predicate<T> isTrue) {
		until(new Function<T, Boolean>() {
			public Boolean apply(T input) {
				return isTrue.apply(input);
			}

			public String toString() {
				return isTrue.toString();
			}
		});
	}

	/**
	 * Repeatedly applies this instance's input value to the given function
	 * until one of the following occurs:
	 * <ol>
	 * <li>the function returns neither null nor false,</li>
	 * <li>the function throws an unignored exception,</li>
	 * <li>the timeout expires,
	 * <li>
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 *
	 * @param isTrue
	 *            the parameter to pass to the {@link ExpectedCondition}
	 * @param <V>
	 *            The function's expected return type.
	 * @return The functions' return value if the function returned something
	 *         different from null or false before the timeout expired.
	 * @throws TimeoutException
	 *             If the timeout expires.
	 */
	public <V> V until(Function<? super T, V> isTrue) {
		long end = laterBy(timeout);
		Throwable lastException = null;
		while (true) {
			try {
				V value = isTrue.apply(input);
				if (value != null && Boolean.class.equals(value.getClass())) {
					if (Boolean.TRUE.equals(value)) {
						return value;
					}
				} else if (value != null) {
					return value;
				}
			} catch (Throwable e) {
				lastException = propagateIfNotIgnored(e);
			}

			// Check the timeout after evaluating the function to ensure
			// conditions
			// with a zero timeout can succeed.
			if (!isNowBefore(end)) {
				String message = messageSupplier != null ? messageSupplier.get() : null;

				String toAppend = message == null ? " waiting for " + isTrue.toString() : ": " + message;

				String timeoutMessage = String.format("Timed out after %d seconds%s", SECONDS.convert(timeout,MILLISECONDS), toAppend);
				throw new TimeoutException(timeoutMessage, lastException);
			}
				pause(interval);
		}
	}

	/**
	 * Sets how long to wait for the evaluated condition to be true. The default
	 * timeout is {@link #FIVE_HUNDRED_MILLIS}.
	 *
	 * @param duration
	 *            The timeout duration.
	 * @param unit
	 *            The unit of time.
	 * @return A self reference.
	 */
	public DynamicWait<T> withTimeout(long duration, TimeUnit unit) {
		this.timeout = duration(duration, unit);
		return this;
	}

	private Throwable propagateIfNotIgnored(Throwable e) {
		for (Class<? extends Throwable> ignoredException : ignoredExceptions) {
			if (ignoredException.isInstance(e)) {
				return e;
			}
		}
		throw Throwables.propagate(e);
	}

	/**
	 * Configures this instance to ignore specific types of exceptions while
	 * waiting for a condition. Any exceptions not whitelisted will be allowed
	 * to propagate, terminating the wait.
	 *
	 * @param types
	 *            The types of exceptions to ignore.
	 * @param <K>
	 *            an Exception that extends Throwable
	 * @return A self reference.
	 */
	@SafeVarargs
	public final DynamicWait<T>  ignoring( Class<? extends Throwable>... types) {
		ignoredExceptions.addAll(Arrays.asList(types));
		return this;
	}

	private static class StringSupplier implements Supplier<String> {
		String message;

		public StringSupplier(String message) {
			this.message = message;
		}

		@Override
		public String get() {
			return message;
		}
	}

	public static long getDefaultTimeout() {
		return getBundle().getLong("selenium.explicit.wait.timeout",
				ApplicationProperties.SELENIUM_WAIT_TIMEOUT.getIntVal(5000));
	}

	public static long getDefaultInterval() {
		return getBundle().getLong("selenium.explicit.wait.interval",
				getBundle().getLong("selenium.wait.interval", 1000));
	}

	private long laterBy(long durationInMillis) {
		return now() + durationInMillis;
	}

	private boolean isNowBefore(long endInMillis) {
		return now() < endInMillis;
	}

	private long now() {
		return System.currentTimeMillis();
	}
	
	public static long duration(long time, TimeUnit unit){
		return unit.convert(time, MILLISECONDS);
	}
}
