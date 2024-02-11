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
