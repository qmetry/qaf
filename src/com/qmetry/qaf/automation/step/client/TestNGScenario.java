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


package com.qmetry.qaf.automation.step.client;

import static com.qmetry.qaf.automation.data.MetaDataScanner.getMetadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.internal.TestNGMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

/**
 * com.qmetry.qaf.automation.step.client.TestNGScenario.java
 * 
 * @author chirag.jayswal
 */
public class TestNGScenario extends TestNGMethod {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225163528424712337L;
	private Scenario scenario;
	private Map<String, Object> metadata;

	public TestNGScenario(Method method, IAnnotationFinder finder, XmlTest xmlTest, Object instance) {
		super(method, finder, xmlTest, instance);
		init(instance);
	}

	private void init(Object instance) {
		if (Scenario.class.isAssignableFrom(getRealClass())) {
			scenario = (Scenario) instance;
			setPriority(scenario.getPriority());
			setGroups(scenario.getM_groups());
			setGroupsDependedUpon(scenario.getM_groupsDependedUpon(), new ArrayList<String>());
			setMethodsDependedUpon(scenario.getM_methodsDependedUpon());
			setDescription(scenario.getDescription());
			setEnabled(scenario.isM_enabled());
			setAlwaysRun(scenario.isM_isAlwaysRun());
			setIgnoreMissingDependencies(scenario.getIgnoreMissingDependencies());
			metadata = scenario.getMetadata();
		} else {
			metadata = getMetadata(getMethod(), true);
		}
		metadata.put("name", getMethodName());
		metadata.put("sign", getSignature());

	}

	@Override
	public String getMethodName() {
		return scenario != null ? scenario.getTestName() : super.getMethodName();
	}

	@Override
	public String getSignature() {
		return scenario != null ? computeSign() : super.getSignature();
	}

	private String computeSign() {
		StringBuilder result = new StringBuilder(scenario.getSignature());

		result.append("[pri:").append(getPriority()).append(", instance:").append(getInstance()).append("]");
		return result.toString();
	}

	public Map<String, Object> getMetaData() {
		return metadata;
	}

	// useful to correct invocation count in case of retry
	public int decAndgetCurrentInvocationCount() {
		m_currentInvocationCount = new AtomicInteger(getCurrentInvocationCount() - 1);
		return super.getCurrentInvocationCount();
	}

}
