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
package com.qmetry.qaf.automation.ui;

import static com.qmetry.qaf.automation.ui.webdriver.ElementFactory.$;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.qmetry.qaf.automation.ui.annotations.PageIdentifier;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.WebDriverTestPage;
import com.qmetry.qaf.automation.ui.webdriver.ComponentFactory;
import com.qmetry.qaf.automation.ui.webdriver.ElementFactory;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebComponent;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * com.qmetry.qaf.automation.core.ui.WebDriverBaseTestPage.java
 * <p>
 * This is a base class to develop test page using Webdriver API. This class
 * excepts one parameter that is parent page in hierarchy which must be
 * {@link WebDriverTestPage}. If the page has no parent page then the parameter
 * value should be {@link WebDriverTestPage} interface. You can apply different
 * page design like Hierarchical page, Linked page, Template page.
 * <dl>
 * <dt>Methods that need to be implement
 * <dd></dd>
 * </dl>
 * 
 * @author chirag
 */
public abstract class WebDriverBaseTestPage<P extends WebDriverTestPage>
		extends AbstractTestPage<P, QAFExtendedWebDriver> implements WebDriverTestPage {
	
	protected List<QAFWebElement> pageIdentifiers;

	public WebDriverBaseTestPage() {
		this(null);
	}

	public WebDriverBaseTestPage(P parent) {
		super(new WebDriverTestBase(), parent);
		pageIdentifiers = getPageIdentifiers();
	}

	@Override
	public String getText() {
		return $("tagName=body").getText();
	}

	@Override
	public WebDriverTestBase getTestBase() {
		return (WebDriverTestBase) super.getTestBase();
	}

	@Override
	public boolean isPageActive(PageLocator loc, Object... args) {
		for(QAFWebElement element:pageIdentifiers){
			if(!element.isPresent())
				return false;
		}
		return pageIdentifiers.size()>0;
	}
	
	/**
	 * This method should be called to wait for page load. It will wait for all element annotated with {@link PageIdentifier} to be present. 
	 * Override this method to provide page specific custom wait implementation. 
	 */
	@Override
	public void waitForPageToLoad() {
		driver.waitForAllElementPresent(pageIdentifiers.toArray(new QAFWebElement[]{}));
	}

	@SuppressWarnings("unchecked")
	public <T extends QAFWebComponent> List<T> findElements(String loc, Class<T> t) {
		List<QAFWebElement> eles = driver.findElements(loc);
		List<T> objs = new ArrayList<T>();
		for (QAFWebElement ele : eles) {
			T obj = (T) ComponentFactory.getObject(t.getClass(), loc, this, driver);
			obj.setId(((QAFExtendedWebElement) ele).getId());
			try {
				Field cacheable = obj.getClass().getDeclaredField("cacheable");
				cacheable.setAccessible(true);
				cacheable.set(obj, true);
			} catch (Exception e) {
				logger.error(e);
			}
			objs.add(obj);
		}
		return objs;
	}

	@SuppressWarnings("unchecked")
	public <T extends QAFWebComponent> T findElement(String loc, Class<T> t) {
		T obj = (T) ComponentFactory.getObject(t.getClass(), loc, this, driver);
		obj.getId();
		return obj;
	}

	@Override
	public void waitForAjaxToComplete() {
		getTestBase().getDriver().waitForAjax();
	}

	public void waitForTextPresent(String text) {
		$("partialLinkText=text").waitForPresent();
	}

	private List<QAFWebElement> getPageIdentifiers() {
		List<QAFWebElement> identifiers = new ArrayList<QAFWebElement>();

		Field[] flds = ClassUtil.getAllFields(this.getClass(), WebDriverBaseTestPage.class);
		for (Field fld : flds) {
			if(fld.isAnnotationPresent(PageIdentifier.class) && QAFWebElement.class.isAssignableFrom(fld.getType())){
				try {
					fld.setAccessible(true);
					identifiers.add((QAFWebElement) fld.get(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return identifiers;
	}
	
	protected void initWebElements() {
		ElementFactory elementFactory = new ElementFactory();
		elementFactory.initFields(this);
	}
}
