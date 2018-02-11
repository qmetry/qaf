/**
 * 
 */
package com.qmetry.qaf.automation.impl.step.qaf;

import javax.inject.Inject;

import org.hamcrest.Matchers;

import com.qmetry.qaf.automation.impl.step.common.MessageBean;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.Validator;

/**
 * @author chirag
 *
 */
public class StepsLibWithArgConstructor {
	private MessageBean bean;
	
	@Inject
	private MessageBean anotherBean;
	
	public StepsLibWithArgConstructor(MessageBean bean) {
		this.bean = bean;
	}
	
	@QAFTestStep(description="i have object with {prop}")
	public void setProperty(String prop){
		bean.setProperty1(prop);
		anotherBean.setProperty1(prop);
	}
	
	@QAFTestStep(description="assert property")
	public void assertProperty(){
		Validator.assertThat(bean,Matchers.notNullValue());
		Validator.assertThat(anotherBean,Matchers.notNullValue());
		Validator.assertThat(bean.getProperty1(),Matchers.notNullValue());
		Validator.assertThat(anotherBean.getProperty1(),Matchers.notNullValue());

		Validator.assertThat(bean.getProperty1(), Matchers.equalToIgnoringCase(anotherBean.getProperty1()));
	}
}
