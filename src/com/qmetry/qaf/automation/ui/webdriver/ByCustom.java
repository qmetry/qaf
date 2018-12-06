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

import java.lang.reflect.Constructor;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.ConfigurationManager;

/**
 * @author Chirag
 */
public class ByCustom extends By {

	private String strategy;
	private String loc;

	private By by;

	public ByCustom(String strategy, String loc) {
		this.loc = loc;
		this.strategy = strategy;
		by = getBy(strategy, loc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
	 */
	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (null != by){
			return by.findElements(context);
		}
		return ((FindsByCustomStrategy) context).findElementsByCustomStrategy(strategy, loc);
	}

	private By getBy(String s, String loc) {
		s = ConfigurationManager.getBundle().getString(s, s);
		try {
			@SuppressWarnings("unchecked")
			Class<? extends By> cls = (Class<? extends By>) Class.forName(s);
			try {
				Constructor<? extends By> con = cls.getConstructor(String.class);
				con.setAccessible(true);
				return con.newInstance(loc);
			} catch (Exception e) {
				throw new AutomationError("Unable to create by using class" + s + " for locator " + loc, e);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("No class registerd for strategy" + s + ". Will use '" + s + "' as custom strategy");
		}
		return null;
	}
}
