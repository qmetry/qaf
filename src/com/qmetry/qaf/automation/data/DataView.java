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

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qmetry.qaf.automation.ui.annotations.UiElement;
import com.qmetry.qaf.automation.ui.annotations.UiElement.Type;

public abstract class DataView<B extends BaseFormDataBean> {

	ElementInteractor interactor;
	protected final Log logger = LogFactory.getLog(getClass());

	public DataView() {
		interactor = new ElementInteractor();
	}

	public String getData(String loc, Type type, int... index) {
		Object val = interactor.fetchValue(String.format(loc, index), type);

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
							getType(field));

				}
			} catch (Exception e) {
				logger.warn("Unable to verify " + field.getName() + ": " + e.getMessage());
			}

		}

		return outcome;

	}
}
