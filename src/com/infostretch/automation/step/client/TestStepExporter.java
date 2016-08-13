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


package com.infostretch.automation.step.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.infostretch.automation.core.ConfigurationManager;
import com.infostretch.automation.step.BaseTestStep;
import com.infostretch.automation.step.JavaStep;
import com.infostretch.automation.step.QAFTestStep;
import com.infostretch.automation.step.client.TestSteps.TestStep;
import com.infostretch.automation.step.client.TestSteps.TestStep.Arg;

/**
 * com.infostretch.automation.step.client.TestStepExporter.java
 * 
 * @author chirag
 */
public class TestStepExporter {

	public static void export(PrintStream stream) {

		ConfigurationManager.getBundle().setProperty("method.recording.mode", true);
		TestSteps steps = new TestSteps();

		Map<String, com.infostretch.automation.step.TestStep> stepMapping = ConfigurationManager.getStepMapping();
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
			JAXBContext jc = JAXBContext.newInstance("com.infostretch.automation.step.client");
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
