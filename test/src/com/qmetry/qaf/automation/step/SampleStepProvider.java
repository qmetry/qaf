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

import java.util.HashMap;
import java.util.Map;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.step.TestStep;

/**
 * In this class some methods are test.step.SampleStepProvider.java
 * 
 * @author chirag
 */
public class SampleStepProvider {

	public void normalMethod() {
		System.out.println("step invoked!.....");
	}

	@QAFTestStep(stepName = "testStep", description = "say {0}")
	public static void testStep(String s) {

		System.out.println("testStep invoked!....." + s);
	}

	@QAFTestStep(stepName = "testStep2")
	public void step() {
		System.out.println("testStep without args invoked!.....");
	}

	@QAFTestStep(description = "step that Returns complex object")
	public TestStep stepReturnObj() {
		return new StringTestStep("testStep",
				"step returns step that Returns complex object");
	}

	@QAFTestStep(description = "step  Requires complex object")
	public void stepRequiresObj(TestStep step) {
		step.execute();
	}

	@QAFTestStep(description = "step that Returns map object")
	public Map<String, String> setMap(String[] vals) {
		Map<String, String> retObj = new HashMap<String, String>();
		try {
			for (int i = 0; i < vals.length; i += 2) {
				retObj.put(vals[i], vals[i + 1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retObj;
	}

	@QAFTestStep(description = "step  Requires map object")
	public void printMap(Map<String, String> map) {
		System.out.printf("%s\n", map);
	}

}
