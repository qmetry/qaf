/**
 * 
 */
package com.qmetry.qaf.automation.ui.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

/**
 * This is a marker annotation can be used with {@link FindBy} annotation to to
 * check is page available or to wait for page to load. It will be considered only with {@link QAFWebElement}.
 * 
 * @author chirag.jayswal
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface PageIdentifier {

}
