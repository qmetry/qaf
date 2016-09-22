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
package com.qmetry.qaf.automation.scenario;

import java.lang.reflect.Method;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.data.MetaData;
import com.qmetry.qaf.automation.data.MetaDataScanner;
//import com.qmetry.qaf.automation.integration.qmetry.QmetryTestCase;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.util.Validator;

public class MetaDataScannerTest {

	@MetaData(value="{'TC_ID':'12110'}")
	//@QmetryTestCase(TC_ID = "12110")
	@QAFTestStep(description = "sample test step")
	void testStep() {

	}

	@MetaData(value="{'TC_ID':'121'}")
	//@QmetryTestCase(TC_ID = "121")
	@QAFDataProvider(dataFile = "datafile.xls", sheetName = "sheet1")
	@Test(description = "sample test")
	public void tc1() throws Exception {
		Method testMethod = MetaDataScannerTest.class.getDeclaredMethod("tc1");
		Map<String, Object> testMetaData = MetaDataScanner.getMetadata(testMethod);

		Validator.verifyThat(testMetaData,
				Matchers.hasEntry("dataFile", (Object) "datafile.xls"));
		Validator.verifyThat(testMetaData,
				Matchers.hasEntry("sheetName", (Object) "sheet1"));
		Validator.verifyThat(testMetaData, Matchers.hasEntry("TC_ID", (Object) "121"));
		Validator.verifyThat("Parameter from @Test", testMetaData,
				Matchers.hasKey("description"));

		Method testStepMethod = MetaDataScannerTest.class.getDeclaredMethod("testStep");
		Map<String, Object> testStepMetaData =
				MetaDataScanner.getMetadata(testStepMethod);
		Validator.verifyThat(testStepMetaData,
				Matchers.hasEntry("description", (Object) "sample test step"));
		Validator.verifyThat(testStepMetaData,
				Matchers.hasEntry("TC_ID", (Object) "12110"));
	}
	
	

}
