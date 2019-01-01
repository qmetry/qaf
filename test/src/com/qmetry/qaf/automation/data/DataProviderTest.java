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

import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.impl.CustomDataProvider;
import com.qmetry.qaf.automation.impl.Item;
import com.qmetry.qaf.automation.impl.LoginBean;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag
 *
 */
public class DataProviderTest{
	
	@MetaData("{'filter':'name==\"a\"'}")
	@Test(description="filter name==a",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterEq(Map<String, Object> data){
		Validator.assertThat(data.get("name").toString(), Matchers.equalToIgnoringCase("a"));
	}

	@MetaData("{'filter':'id==3'}")
	@Test(description="filter id==3",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterNum(Map<String, Object> data){
		Validator.assertThat((int)data.get("id"), Matchers.equalTo(3));
	}
	
	@MetaData("{'filter':'id>3'}")
	@Test(description="filter id>3",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterNumGt(Map<String, Object> data){
		Validator.assertThat((int)data.get("id"), Matchers.greaterThan(3));
	}
	
	@MetaData("{'indices':[1,3]}")
	@Test(description="filter indices [1,3]",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterIndices(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{2,4}));
	}
	
	@MetaData("{'from':3}")
	@Test(description="filter start index 3", dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterFrom(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{4,5}));
	}
	
	@MetaData("{'to':3}")
	@Test(description="filter end index 3", dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterTo(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{1,2,3,4}));
	}
	
	@MetaData("{'from':1,'to':3}")
	@Test(description="filter from 1 to 3",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterFromTo(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{2,3,4}));
	}
	
	@Test(dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testArgs(LoginBean bean, Item item){
		Validator.assertThat(bean, Matchers.notNullValue());
		Validator.assertThat(item, Matchers.notNullValue());

	}
	
	
}
