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
package com.qmetry.qaf.automation.ui.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * This class allows to find element using ExtJS component query where ExtJS library used in UI.
 * Refer sencha extjs Component Query <a href=
 * "https://docs.sencha.com/extjs/6.2.0/classic/Ext.ComponentQuery.html">documentation</a>
 * and <a href="https://www.extjs-tutorial.com/extjs/extjs-componentquery">tutorial</a>
 * <h5>Few Component query Examples:</h5>
 * <ul>
 * <li>field[fieldLabel=name] : will locate field(s) with label "name"
 * <li>field[fieldLabel$=name] : will locate all fields with label ends with
 * "name"
 * <li>datefield : will locate all elements with xtype "datefield"
 * </ul>
 * <h5>Usage</h5>
 * <code>
 * <li>
 * extComp=&lt;component-query&gt;
 * <li>extComp=classtree
 * <li>extComp=field[fieldLabel=name]
 * </code>
 * 
 * @author chirag.jayswal
 */
public class ByExtCompQuery extends By {
	private String querySelector;

	private static final String COMP_QUERY = "var elements = new Array();"
			+ "var res = Ext.ComponentQuery.query(arguments[0]); " + "if(Ext.isArray(res)){"
			+ "		Ext.Object.each(res, function(i, value) {" + " 		elements[i]=value.getEl().dom;" + " 	});"
			+ "}else{" + "		elements[0]=res.getEl().dom;" + "}" + "return elements;";
	private static final String CHILD_COMP_QUERY = "var elements = new Array();"
			// "var res = Ext.ComponentQuery.query('#'+ arguments[0].id +' '+
			// arguments[1]);"
			+ "var res = Ext.ComponentQuery.query('#'+arguments[0].id)[0].query(arguments[1]);"
			+ "if(Ext.isArray(res)){" + "		Ext.Object.each(res, function(i, value) {"
			+ " 		elements[i]=value.getEl().dom;" + " 	});" + "}else{" + "		elements[0]=res.getEl().dom;"
			+ "}" + "return elements;";

	public ByExtCompQuery(String querySelector) {
		this.querySelector = querySelector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WebElement> findElements(SearchContext context) {
		Object res;
		if (context instanceof RemoteWebElement) {
			res = ((JavascriptExecutor) ((RemoteWebElement) context).getWrappedDriver()).executeScript(CHILD_COMP_QUERY,
					context, querySelector);
		} else {
			res = ((JavascriptExecutor) context).executeScript(COMP_QUERY, querySelector);
		}

		return (List<WebElement>) res;
	}

	@Override
	public String toString() {
		return "Using ExtJs Component Query: " + querySelector;
	}

}
