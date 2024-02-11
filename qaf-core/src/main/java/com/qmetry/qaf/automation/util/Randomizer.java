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
package com.qmetry.qaf.automation.util;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;

/**
 * This annotation can be used with {@link BaseDataBean} properties. It will be
 * used by {@link BaseDataBean#fillRandomData()} method.
 * <p>
 * com.qmetry.qaf.automation.util.Randomizer.java
 * 
 * @author chirag
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Randomizer {
	/**
	 * @return
	 */
	RandomizerTypes type() default RandomizerTypes.MIXED;

	/**
	 * data length default is 10
	 * 
	 * @return
	 */
	int length() default 10;

	String prefix() default "";

	String suffix() default "";

	/**
	 * can be used with RandomizerTypes.DIGITS_ONLY to set minimum value.When
	 * you use min and max then length will not be considered
	 * 
	 * @return
	 */
	long minval() default 0;

	/**
	 * can be used with RandomizerTypes.DIGITS_ONLY to set maximum value. When
	 * you use min and max then length will not be considered. Can be used for
	 * Date type as well.
	 * 
	 * @return
	 */
	long maxval() default 0;

	boolean skip() default false;

	/**
	 * can be used to generate random data in given format. For example:
	 * aaa-999-aaa will generate random string with xJa-123-abc.
	 * 
	 * @see StringUtil#getRandomString(String)
	 * @return
	 */
	String format() default "";

	String[] dataset() default {};

}
