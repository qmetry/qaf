/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qmetry.qaf.automation.data.BaseFormDataBean;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;

/**
 * Use to map Form Bean property with UI form fields.
 * com.qmetry.qaf.automation.core.ui.annotations.UiElement.java
 * 
 * @author chirag
 */

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UiElement {
	public enum Type {

		textbox,
		selectbox,
		checkbox,
		file,
		/**
		 * Radio button(s)
		 */
		optionbox,
		textarea,
		multiselectbox,
		/**
		 * HTML element other than form field
		 */
		text;
	}

	/**
	 * specify locator of form field.
	 * 
	 * @return
	 */
	public String fieldLoc();

	/**
	 * specify type of form field. Default is {@link Type#textbox}
	 * 
	 * @return
	 */
	public Type fieldType() default Type.textbox;

	/**
	 * mapping with data-view : if this form field value reflected in other view
	 * page then specify locator of that field
	 * 
	 * @return
	 */
	public String viewLoc() default "";

	/**
	 * specify type of view element. default is {@link Type#text}
	 * 
	 * @return
	 */
	public Type viewType() default Type.text;

	public String defaultValue() default "";

	/**
	 * specify filed name on which this field is depends. This is use full in
	 * case of parent-child fields where child filed enabled/appears depending
	 * on value of parent field.
	 * 
	 * @return
	 */
	public String dependsOnField() default "";

	/**
	 * Specify value of parent field which enables this field. Used with
	 * {@link #dependsOnField()}. You can use JavaScript notation for value
	 * comparison. Some valid Example:
	 * 
	 * <pre>
	 * <code>	
	 * &#64;UiElement(fieldLoc = DOMAIN_SELECT_LOC, fieldType = Type.selectbox, order = 1)
	 * private String domain;
	 * 	
	 * &#64;UiElement(fieldLoc = NAME_INPUT_LOC, fieldType = Type.selectbox, dependsOnField = "domain", dependingValue = "DUNS", order=3)
	 * public String name;
	 * </code>
	 * <code>		
	 * This can also be written as:
	 * &#64;UiElement(fieldLoc = NAME_INPUT_LOC, fieldType = Type.selectbox, dependsOnField = "domain", dependingValue = "${domain}=='DUNS'", order=3)
	 * public String name;
	 * </code>
	 * &#64;UiElement(fieldLoc = PASSWORD_INPUT_LOC, fieldType = Type.selectbox, dependsOnField = "domain", dependingValue = "${domain}!='DUNS' && ${domain}!='GLN'", order=3)
	 * public String password;
	 * </pre>
	 * 
	 * <br>
	 * Make sure that you have specified {@link #order()} of parent filed less
	 * then child field.
	 * 
	 * @return
	 */
	public String dependingValue() default "";

	/**
	 * specify whether this form field is read-only? Default is false.
	 * 
	 * @return
	 */
	public boolean readonly() default false;

	/**
	 * specify whether is this a required form field? Default is false. It is
	 * used when you want to fill just required form fields.
	 * 
	 * @see BaseFormDataBean#fillUiRequiredElements()
	 * @return
	 */
	public boolean required() default false;

	/**
	 * Specify the order in which form fields should be filled in UI. Default is
	 * {@link Integer#MAX_VALUE}
	 * 
	 * @return
	 */
	public int order() default Integer.MAX_VALUE;

	/**
	 * Specify whether wait for page load required or not, after fill UI data.
	 * Default is false
	 * @deprecated
	 * @return
	 */
	public boolean pagewait() default false;

	/**
	 * Optional, can be used to specify custom component to interact with UI. In case
	 * of custom component, it must have constructor with one argument as string
	 * to accept locator
	 * 
	 * @since 2.1.9
	 * @return
	 */
	public Class<? extends QAFExtendedWebElement> elementClass() default QAFExtendedWebElement.class;

}
