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
package com.qmetry.qaf.automation.step;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qmetry.qaf.automation.data.MetaData;

/**
 * Methods annotated with this annotation will be considered as a test step.
 * <p>
 * Whenever Method with this annotation get called you will get the step
 * description in report with pass/fail/not-run status. Furthermore, This
 * annotation facilitate to authoring tests in Behavior driven (BDD) way or in
 * Keyword driven (KWD) way. For BDD, use {@link #description() description} as
 * behavior and for KWD use {@link #stepName() step name} as keyword.
 * 
 * @see {@link QAFTestStepListener}, {@link MetaData}
 * @author chirag.jayswal
 */
@Inherited
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface QAFTestStep {
	/**
	 * Specify description of the step including arguments. No of arguments in
	 * description must match number of arguments in method signature. Argument
	 * in description can be provided by using curly braces {}. For example,
	 * Method sign is login(String userName, String pwd) then you can provide
	 * description as below:
	 * <ul>
	 * <li>User login with {0} and {1}</li>
	 * <li>User login with {userName} and {password}</li>
	 * </ul>
	 * 
	 * @return description of the step
	 */
	String description() default "";

	/**
	 * if step name is not provided than method name will be considered as step
	 * name.
	 * 
	 * @return step name
	 */
	String stepName() default "";

	/**
	 * Optional threshold value in seconds that specifies threshold time for
	 * step to complete. If threshold specified it will be available in JSON
	 * report.
	 * 
	 * @return threshold in seconds
	 */
	int threshold() default 0;

}
