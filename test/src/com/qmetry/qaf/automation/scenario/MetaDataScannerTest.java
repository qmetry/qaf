/*******************************************************************************
 * Copyright 2016 Infostretch Corporation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
