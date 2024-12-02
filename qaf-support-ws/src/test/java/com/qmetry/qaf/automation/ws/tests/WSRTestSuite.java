package com.qmetry.qaf.automation.ws.tests;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.data.MetaData;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.WsRequestBean;
/**
 * @author chirag.jayswal
 *
 */
public class WSRTestSuite {
	
	@Test
	public void test1(){
		WsRequestBean bean = new WsRequestBean();
		bean.fillData("sample");
		bean.resolveParameters(null);
		Validator.verifyThat(bean.getBaseUrl(), Matchers.equalTo(getBundle().getString("reference.baseUrl")));
		Validator.verifyThat(bean.getEndPoint(), Matchers.equalTo(getBundle().getString("sample.endpoint")));
		System.out.println(bean);
	}
	
	@MetaData("{'bug':'#11'}")
	@Test
	public void test2(){
		WsRequestBean bean = new WsRequestBean();
		bean.fillData("overrideheaders");
		bean.resolveParameters(null);
		Validator.verifyThat(bean.getBaseUrl(), Matchers.equalTo(getBundle().getString("reference.baseUrl")));
		Validator.verifyThat(bean.getHeaders().get("Content-type").toString(), Matchers.equalTo("application/xml"));
		System.out.println(bean);
	}

}
