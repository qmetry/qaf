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
package com.qmetry.qaf.automation.step.client;

import javax.xml.bind.annotation.XmlRegistry;

import com.qmetry.qaf.automation.step.client.TestSteps.TestStep;
import com.qmetry.qaf.automation.step.client.TestSteps.TestStep.Arg;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.qmetry.qaf.automation.step.client
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.qmetry.qaf.automation.step.client
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Arg }
	 */
	public Arg createTestStepsTestStepArg() {
		return new Arg();
	}

	/**
	 * Create an instance of {@link TestStep }
	 */
	public TestStep createTestStepsTestStep() {
		return new TestStep();
	}

	/**
	 * Create an instance of {@link TestSteps }
	 */
	public TestSteps createTestSteps() {
		return new TestSteps();
	}

}
