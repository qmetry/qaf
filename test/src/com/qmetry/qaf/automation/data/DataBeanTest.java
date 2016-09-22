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
