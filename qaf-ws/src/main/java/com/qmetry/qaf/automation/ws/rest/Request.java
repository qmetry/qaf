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
package com.qmetry.qaf.automation.ws.rest;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

/**
 * Use this annotation to override default parameters at class or test level. If
 * Scheduler xml file is provided it will use this parameters for filter.
 * 
 * @author chirag
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface Request {
	/**
	 * @return
	 */
	String resourceName();

	/**
	 * @return
	 */
	String serviceEndPoint() default "";

	/**
	 * JSON multi-value map for exampl:
	 * <code>						"{'param1':['valuep1'];'param2':['valueparam2','value2param2']}",
	</code>
	 * 
	 * @return
	 */
	String parameters() default "{}";

	/**
	 * <ul>
	 * One of the HTTP methods
	 * <li>{@link HttpMethod#GET}
	 * <li>{@link HttpMethod#POST}
	 * <li>{@link HttpMethod#PUT}
	 * <li>{@link HttpMethod#DELETE}
	 * </ul>
	 * Default value is {@link HttpMethod#GET}
	 * 
	 * @return
	 */
	String method() default HttpMethod.GET;

	/**
	 * Constants are available with {@link MediaType} class.
	 * 
	 * @return
	 */
	String type() default MediaType.APPLICATION_XML;

	String data() default "";

}
