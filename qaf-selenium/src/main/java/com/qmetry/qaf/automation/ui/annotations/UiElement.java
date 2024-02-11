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
