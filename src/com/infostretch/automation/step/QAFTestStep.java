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


package com.infostretch.automation.step;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.infostretch.automation.data.MetaData;

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
