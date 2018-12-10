/**
 * 
 */
package com.qmetry.qaf.automation.data;

import java.util.Map;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.impl.CustomDataProvider;

/**
 * @author chirag
 *
 */
public class DataProviderTest{
	
	@MetaData("{'filter':'value==\"OK1\"'}")
	@Test(dataProvider="dp-without-injection", dataProviderClass=CustomDataProvider.class)
	public void testProcessing(Map<String, Object> data){
		System.out.println("testProcessing:" +data);
	}

}
