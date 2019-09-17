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
