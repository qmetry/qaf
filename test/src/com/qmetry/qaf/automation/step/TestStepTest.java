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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.JavaStepFinder;
import com.qmetry.qaf.automation.step.StringTestStep;

public class TestStepTest {
	@BeforeClass
	public void setUp() {
		ConfigurationManager.getBundle().setProperty("step.provider.pkg", "test.step");
		StringTestStep.addSteps(JavaStepFinder.getAllJavaSteps());
	}

	@Test
	public void stepProviderTest() {
		// StringTestStep.execute("step");
		ConfigurationManager.getBundle().setProperty("aaa", "bbb");

		StringTestStep.execute("step2", "aaa");
		DefaultStepProvider.step("direct call");
		Object o = StringTestStep.execute("setMap",
				(Object) (new String[]{"val1", "aaa", "val2", "bbb"}));
		ConfigurationManager.getBundle().setProperty("aaa", o);
		StringTestStep.execute("printMap", "${aaa}");

	}

	@Test(description = "Method annotted with QAFTestStep in class which is not step provider")
	public void stepExecuterTest() {
		StringTestStep.execute("testStep", "aaa");
		StringTestStep.execute("testStep2");
	}

	@Test()
	public void stepExecuterCommentTest() {
		StringTestStep.execute("comment", "aaahjg kjhkjh");
		// StringTestStep.execute("comment", new Object[]{"aaahjg",
		// " kjhkjh"});

		// StringTestStep.execute("comment");

	}

	@Test(description = "Method not annotted with QAFTestStep in class which is not step provider", expectedExceptions = RuntimeException.class)
	public void stepExecuterTest2() {
		StringTestStep.execute("normalMethod");
	}
}
