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
package com.qmetry.qaf.automation.data;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.testng.DataProviderException;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag.jayswal
 */
public class DataBeanTest {

	@Test(description = "Databean population with random data")
	public void fillfromdataBean() {
		TestDataBean bean = new TestDataBean();
		bean.fillRandomData();

		Validator.verifyThat(bean.getFirstName(), Matchers.endsWith("FN"));
		Validator.verifyThat(bean.getLastName(), Matchers.startsWith("LN"));
		Validator.verifyThat(bean.getAge(), Matchers.lessThanOrEqualTo(30));
		Validator.verifyThat(bean.getAge(), Matchers.greaterThanOrEqualTo(20));
		Validator.verifyThat(bean.getGender(), Matchers.isOneOf("male", "female"));

		int dateDiff = DateUtil.getDateDifference(bean.getDate(), DateUtil.getDate(0));
		Validator.verifyThat(dateDiff, Matchers.lessThanOrEqualTo(7));
		Validator.verifyThat(dateDiff, Matchers.greaterThanOrEqualTo(-7));

	}

	@Test(description = "Fill none exist data from config should throw an exception", expectedExceptions = DataProviderException.class)
	public void fillfromconfigfileWithoutData() {
		TestDataBean bean = new TestDataBean();
		bean.fillFromConfig("not.available.data");
	}

	@Test(description = "Fill data from config should fill data from configuration")
	public void fillfromconfigfile() {
		ConfigurationManager.getBundle().setProperty("temp.data.databeantest.firstName",
				"First name");
		ConfigurationManager.getBundle().setProperty("temp.data.databeantest.date", "0");

		TestDataBean bean = new TestDataBean();
		bean.fillFromConfig("temp.data.databeantest");

		Validator.verifyThat(bean.getFirstName(),
				Matchers.equalToIgnoringCase("First name"));
		int dateDiff = DateUtil.getDateDifference(bean.getDate(), DateUtil.getDate(0));
		Validator.verifyThat(dateDiff, Matchers.equalTo(0));

	}


}
