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


package com.qmetry.qaf.automation.ui.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;

import com.qmetry.qaf.automation.ui.annotations.FindBy;

/**
 * com.qmetry.qaf.automation.ui.webdriver.custom.Component.java
 * 
 * @author chirag
 */
public abstract class QAFWebComponent extends QAFExtendedWebElement {
	protected final Log logger;

	public QAFWebComponent(String locator) {
		super(locator);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}

	public QAFWebComponent(QAFExtendedWebDriver driver, By by) {
		super(driver, by);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}
	
	protected QAFWebComponent(QAFExtendedWebDriver driver){
		super(driver);
		logger = LogFactory.getLog(this.getClass());
	}

	/**
	 * call this constructor for component having parent element. Such component
	 * will not supported by {@link FindBy} annotation.
	 * 
	 * @param parent
	 * @param locator
	 */
	public QAFWebComponent(QAFExtendedWebElement parent, String locator) {
		super(parent, locator);
		logger = LogFactory.getLog(this.getClass());
		initFields();
	}

	private void initFields() {
		ElementFactory elementFactory = new ElementFactory(this);
		elementFactory.initFields(this);
	}

	@SuppressWarnings("unchecked")
	public <T extends QAFWebComponent> List<T> findElements(String loc, Class<T> t) {
		List<QAFWebElement> eles = findElements(loc);
		List<T> objs = new ArrayList<T>();
		for (QAFWebElement ele : eles) {
			T obj = (T) ComponentFactory.getObject(t.getClass(), loc, this, this);
			obj.setId(((QAFExtendedWebElement) ele).getId());
			obj.parentElement = this;
			obj.cacheable = true;
			objs.add(obj);
		}
		return objs;
	}

	@SuppressWarnings("unchecked")
	public <T extends QAFWebComponent> T findElement(String loc, Class<T> t) {
		T obj = (T) ComponentFactory.getObject(t.getClass(), loc, this, this);
		obj.getId();
		return obj;
	}
}
