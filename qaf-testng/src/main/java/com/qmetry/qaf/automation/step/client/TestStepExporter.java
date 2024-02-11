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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.BaseTestStep;
import com.qmetry.qaf.automation.step.JavaStep;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.client.TestSteps.TestStep;
import com.qmetry.qaf.automation.step.client.TestSteps.TestStep.Arg;

/**
 * com.qmetry.qaf.automation.step.client.TestStepExporter.java
 * 
 * @author chirag
 */
public class TestStepExporter {

	public static void export(PrintStream stream) {

		ConfigurationManager.getBundle().setProperty("method.recording.mode", true);
		TestSteps steps = new TestSteps();

		Map<String, com.qmetry.qaf.automation.step.TestStep> stepMapping = ConfigurationManager.getStepMapping();
		for (String stepName : stepMapping.keySet()) {
			BaseTestStep astep = (BaseTestStep) stepMapping.get(stepName);
			if (!(astep instanceof JavaStep)) {
				continue;
			}
			JavaStep javaStep = (JavaStep) astep;

			// record parameter names using aspectj reporter!...
			if (javaStep.getMethod().isAnnotationPresent(QAFTestStep.class)) {
				try {
					javaStep.execute();
				} catch (Exception e) {
					// ignore!...
				}
			}
			String[] paramNames = ConfigurationManager.getBundle().getStringArray("method.param.names",
					new String[] {});
			Method method = javaStep.getMethod();
			TestStep step = new TestStep();
			step.setName(stepName);
			step.setDescription(javaStep.getDescription());
			int noOfArgs = method.getParameterTypes().length;

			step.setNoOfaArgs(String.valueOf(noOfArgs));
			step.setReturns(method.getReturnType().getCanonicalName());

			for (int i = 0; i < noOfArgs; i++) {
				Arg arg = new Arg();
				arg.setIndex(String.valueOf(i));
				arg.setType(method.getParameterTypes()[i].getCanonicalName());
				if ((paramNames != null) && (paramNames.length > i)) {
					arg.setName(paramNames[i]);
				}
				step.getArg().add(arg);

			}
			ConfigurationManager.getBundle().clearProperty("method.param.names");

			steps.getTestStep().add(step);
		}

		try {
			JAXBContext jc = JAXBContext.newInstance("com.qmetry.qaf.automation.step.client");
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			m.marshal(steps, stream);

		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			stream.close();
		}

	}

	public static void main(String[] args) throws IOException {
		PrintStream printStream;
		if ((args.length > 0)) {
			File file = new File(args[0]);
			file.createNewFile();
			printStream = new PrintStream(file);
		} else {
			printStream = System.out;
		}
		export(printStream);
		System.out.println("Export Completed...");
	}
}
