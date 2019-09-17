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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;

/**
 * com.qmetry.qaf.automation.step.WebElementStep.java
 * 
 * @author chirag
 */
public class WebElementStep extends JavaStep {
	private String loc;
	private Class<?> component;

	public WebElementStep(Method method) {
		super(method);
	}

	public WebElementStep(Method method, Class<?> component) {
		super(method);
		this.component = component;
	}

	@Override
	public void setActualArgs(Object... args) {
		if ((args == null) || (args.length == 0)) {
			return;
		}
		loc = (String) args[0];
		actualArgs = new Object[method.getParameterTypes().length];
		if (args.length > 1) {
			try {
				System.arraycopy(args, 1, actualArgs, 0, args.length - 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("args: " + args.length + " method params: " + method.getParameterTypes().length);
				throw new StepInvocationException("Wrong number of arguments", true);
			}
		}
	}

	@Override
	protected Object getStepProvider() {

		if (null != component) {
			try {
				Constructor<?> con = component.getDeclaredConstructor(String.class);
				con.setAccessible(true);

				return con.newInstance(loc);
			} catch (Exception e) {
				throw new StepInvocationException("Unable to initialize step: " + getDescription(), true);
			}
		}
		return new QAFExtendedWebElement(loc);
	}

	@Override
	public TestStep clone() {
		WebElementStep weStep = new WebElementStep(method);

		if (null != actualArgs) {
			weStep.actualArgs = actualArgs.clone();
		}
		weStep.loc = loc;
		return weStep;
	}
}
