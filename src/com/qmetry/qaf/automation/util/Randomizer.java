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
