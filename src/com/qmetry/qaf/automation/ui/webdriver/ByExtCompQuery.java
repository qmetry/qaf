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
