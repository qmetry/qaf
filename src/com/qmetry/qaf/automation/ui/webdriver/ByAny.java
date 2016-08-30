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
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.ispl.automation.sample.test.ByAny.java
 * 
 * @author chirag
 */
public class ByAny extends By {
	private String[] locators;

	public ByAny(String... locator) {
		locators = locator;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		List<WebElement> elements = findElements(context);
		if (elements.isEmpty()) {
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		}
		return elements.get(0);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (locators.length == 0) {
			return new ArrayList<WebElement>();
		}
		List<WebElement> elems = new ArrayList<WebElement>();

		for (String locator : locators) {
			By by = LocatorUtil.getBy(locator);
			elems.addAll(by.findElements(context));
			if (!elems.isEmpty()) {
				break;
			}
		}

		return elems;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("Using any of");
		stringBuilder.append(Arrays.toString(locators));

		return stringBuilder.toString();
	}

}
