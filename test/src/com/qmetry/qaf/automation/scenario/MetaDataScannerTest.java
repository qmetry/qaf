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

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.data.MetaData;
import com.qmetry.qaf.automation.data.MetaDataScanner;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
//import com.qmetry.qaf.automation.integration.qmetry.QmetryTestCase;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.util.Validator;

public class MetaDataScannerTest {

	@MetaData(value = "{'TC_ID':'12110'}")
	// @QmetryTestCase(TC_ID = "12110")
	@QAFTestStep(description = "sample test step")
	void testStep() {

	}

	@MetaData(value = "{'TC_ID':'121'}")
	// @QmetryTestCase(TC_ID = "121")
	@QAFDataProvider(dataFile = "datafile.xls", sheetName = "sheet1")
	@Test(description = "sample test")
	public void tc1() throws Exception {
		Method testMethod = MetaDataScannerTest.class.getDeclaredMethod("tc1");
		Map<String, Object> testMetaData = MetaDataScanner.getMetadata(testMethod);

		Validator.verifyThat(testMetaData, Matchers.hasEntry("dataFile", (Object) "datafile.xls"));
		Validator.verifyThat(testMetaData, Matchers.hasEntry("sheetName", (Object) "sheet1"));
		Validator.verifyThat(testMetaData, Matchers.hasEntry("TC_ID", (Object) "121"));
		Validator.verifyThat("Parameter from @Test", testMetaData, Matchers.hasKey("description"));

		Method testStepMethod = MetaDataScannerTest.class.getDeclaredMethod("testStep");
		Map<String, Object> testStepMetaData = MetaDataScanner.getMetadata(testStepMethod);
		Validator.verifyThat(testStepMetaData, Matchers.hasEntry("description", (Object) "sample test step"));
		Validator.verifyThat(testStepMetaData, Matchers.hasEntry("TC_ID", (Object) "12110"));
	}

	@Test(description = "test metadata Formattor")
	public void testFormattor() {
		getBundle().setProperty(ApplicationProperties.METADATA_FORMTTOR_PREFIX.key+".custom-id", "<a herf=\"{0}\">{0}</a>");
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("custom-id", "ABC-123");

		MetaDataScanner.formatMetaData(metadata);
		Validator.assertThat(metadata.get("custom-id"), Matchers.equalTo((Object)"<a herf=\"ABC-123\">ABC-123</a>"));
		// try to format again should not mesh up value
		MetaDataScanner.formatMetaData(metadata);
		Validator.assertThat(metadata.get("custom-id"), Matchers.equalTo((Object)"<a herf=\"ABC-123\">ABC-123</a>"));
		System.out.println(metadata);
		metadata.put("custom-id", Arrays.asList("ABC-123","DEF-123"));
		MetaDataScanner.formatMetaData(metadata);
		System.out.println(metadata);

	}
	@Test(description = "test metadata rule")
	public void testRule() {
		getBundle().setProperty(ApplicationProperties.METADATA_RULES.key, "[{\"key\":\"custom-id\",\"values\":[\"\\\\d*\",\"123A\",\"123C\"]},{\"key\":\"custom-id2\",\"depends\":{\"key\":\"custom-id\",\"values\":[\"123\"]},\"required\":true,\"values\":[\"ABCD-\\\\d*\"]}]");
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("custom-id2", "ABCD-123");
		metadata.put("custom-id", "123");

		System.out.println(MetaDataScanner.applyMetaRule(metadata));
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.equalTo(""));
		
		metadata = new HashMap<String, Object>();
		metadata.put("custom-id", "123A");

		System.out.println(MetaDataScanner.applyMetaRule(metadata));
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.equalTo(""));
		
		metadata = new HashMap<String, Object>();
		System.out.println(MetaDataScanner.applyMetaRule(metadata));
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.equalTo(""));
		
		metadata = new HashMap<String, Object>();
		metadata.put("custom-id2", "ABCD-1");
		metadata.put("custom-id", "123");

		System.out.println(MetaDataScanner.applyMetaRule(metadata));
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.equalTo(""));

		metadata = new HashMap<String, Object>();
		metadata.put("custom-id2", "ABCD");
		metadata.put("custom-id", "123");
		System.out.println(MetaDataScanner.applyMetaRule(metadata));
		
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.containsString("mismatch"));
		
		metadata = new HashMap<String, Object>();
		metadata.put("custom-id2", "ABCD");
		metadata.put("custom-id", "123C");
		System.out.println(MetaDataScanner.applyMetaRule(metadata));

		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.containsString("not aplicable"));
		
		metadata = new HashMap<String, Object>();
		metadata.put("custom-id", "123");
		System.out.println(MetaDataScanner.applyMetaRule(metadata));

		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.containsString("required"));
		
		metadata = new HashMap<String, Object>();
		metadata.put("custom-id", "ABCD");
		metadata.put("custom-id2", "ABCD");
		System.out.println(MetaDataScanner.applyMetaRule(metadata));

		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.containsString("mismatch"));
		Validator.assertThat(MetaDataScanner.applyMetaRule(metadata), Matchers.containsString("not aplicable"));


		getBundle().clearProperty("metadata.rules");
	}
}
