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
