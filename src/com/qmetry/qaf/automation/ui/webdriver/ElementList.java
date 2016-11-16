/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.ui.webdriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;

import com.qmetry.qaf.automation.ui.util.ExpectedCondition;
import com.qmetry.qaf.automation.ui.util.QAFWebDriverWait;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.LocatorUtil;

/**
 * com.qmetry.qaf.automation.ui.webdriver.List.java
 * 
 * @author chirag
 */
public class ElementList<T extends QAFWebElement> extends ArrayList<T> {
	private static final long serialVersionUID = -703633828271567272L;
	private SearchContext context;
	private String description;
	private boolean cacheable = false;
	private By by;

	public ElementList(SearchContext context, String loc) {
		this.context = context;
		initLoc(loc);
	}

	@Override
	public T get(final int index) {
		if (!cacheable) {
			clear();
		}
		if (isEmpty()) {
			waitForIndex(index);
		}
		return super.get(index);
	}

	@Override
	public boolean contains(Object o) {
		waitForIndex(0);
		return super.contains(o);
	}

	private void initLoc(String locator) {
		by = LocatorUtil.getBy(locator);

		if (JSONUtil.isValidJsonString(locator)) {
			try {
				Map<String, Object> map = JSONUtil.toMap(locator);
				description = map.containsKey("desc") ? (String) map.get("desc")
						: map.containsKey("description") ? (String) map.get("description")
								: by.toString();
				cacheable = map.containsKey("cacheable") ? (Boolean) map.get("cacheable")
						: false;
			} catch (JSONException e) {
			}
		}
	}

	public void waitForEmpty() {
		waitForIndex(-1);
	}

	public void waitForIndex(final int index) {
		new QAFWebDriverWait()
				.withMessage(String.format("Wait timeout for list of %s with size %d",
						description, index + 1))
				.until(new ExpectedCondition<QAFExtendedWebDriver, Boolean>() {
					@SuppressWarnings("unchecked")
					@Override
					public Boolean apply(QAFExtendedWebDriver driver) {
						try {
							clear();
							addAll((Collection<T>) context.findElements(by));
							return size() > index;
						} catch (WebDriverException e) {
							return false;
						}
					}
				});
	}
}
