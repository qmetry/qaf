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
package com.qmetry.qaf.automation.data;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
//import com.qmetry.qaf.automation.data.DataProviderException;
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
