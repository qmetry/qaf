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

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.ui.annotations.UiElement;
import com.qmetry.qaf.automation.ui.annotations.UiElement.Type;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;

public abstract class DataView<B extends BaseFormDataBean> {

	ElementInteractor interactor;
	protected final Log logger = LogFactory.getLog(getClass());

	public DataView() {
		interactor = new ElementInteractor();
	}

	public String getData(String loc, Type type, int... index) {
		Object val = interactor.fetchValue(String.format(loc, index), type, QAFExtendedWebElement.class);

		return String.valueOf(val);
	}

	Type getType(Field fld) {
		UiElement map = fld.getAnnotation(UiElement.class);
		return (null != map) ? map.fieldType() : Type.text;
	}

	public B populateBean(B bean, int... index) {
		Field[] flds = this.getClass().getDeclaredFields();

		for (Field field : flds) {
			String loc;
			try {
				loc = (String) field.get(this);

				bean.setBeanData(loc, getData(loc, getType(field), index));

			} catch (Exception e) {
				logger.warn("Unable to set bean field using view field " + field.getName() + ": " + e.getMessage());
			}
		}
		return bean;
	}

	public boolean verifyData(B bean, int... index) {
		Field[] flds = this.getClass().getDeclaredFields();
		boolean outcome = true;

		for (Field field : flds) {
			String loc;
			try {
				loc = (String) field.get(this);
				Object beanData = bean.getBeanData(loc);
				if (beanData != null) {
					outcome = interactor.verifyValue(String.format(loc, index), String.valueOf(bean.getBeanData(loc)),
							getType(field), QAFExtendedWebElement.class);

				}
			} catch (Exception e) {
				logger.warn("Unable to verify " + field.getName() + ": " + e.getMessage());
			}

		}

		return outcome;

	}
}
