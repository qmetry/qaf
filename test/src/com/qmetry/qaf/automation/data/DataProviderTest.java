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

import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.impl.CustomDataProvider;
import com.qmetry.qaf.automation.impl.Item;
import com.qmetry.qaf.automation.impl.LoginBean;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
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
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{3,4,5}));
	}
	
	@MetaData("{'to':3}")
	@Test(description="filter end index 3", dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterTo(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{1,2,3}));
	}
	
	@MetaData("{'from':1,'to':3}")
	@Test(description="filter from 1 to 3",dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testFilterFromTo(Map<String, Object> data){
		Validator.assertThat(data.get("id"), Matchers.isIn(new Object[]{1,2,3}));
	}
	
	@Test(dataProvider="dp-for-filter", dataProviderClass=CustomDataProvider.class)
	public void testArgs(LoginBean bean, Item item){
		Validator.assertThat(bean, Matchers.notNullValue());
		Validator.assertThat(item, Matchers.notNullValue());

	}
	
	
	@QAFDataProvider(key="testdata.${method}.data", filter="uname.equalsIgnoreCase('user1')")
	@Test
	public void tc01(LoginBean bean, Item item){
		Validator.assertThat(bean, Matchers.notNullValue());
		Validator.assertThat(item, Matchers.notNullValue());
		Validator.assertThat(bean.getUname(), Matchers.equalToIgnoringCase("user1"));
	}
	
	@MetaData("{'user':'user2','tname':'tc01'}")
	@QAFDataProvider(key="testdata.${tname}.data", filter="uname.equalsIgnoreCase('${user}')")
	@Test
	public void testMetadataResolution(Map<String, Object> data){
		Validator.assertThat(data, Matchers.notNullValue());
		Validator.assertThat((String)data.get("uname"), Matchers.equalToIgnoringCase("user2"));
	}
}
