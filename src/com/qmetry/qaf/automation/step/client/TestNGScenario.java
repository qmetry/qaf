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
package com.qmetry.qaf.automation.step.client;

import static com.qmetry.qaf.automation.data.MetaDataScanner.formatMetaData;
import static com.qmetry.qaf.automation.data.MetaDataScanner.getMetadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.internal.TestNGMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import com.qmetry.qaf.automation.step.TestStep;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.StringUtil;

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
	private String qualifiledName;

	public TestNGScenario(TestNGMethod testNGMethod) {
		this(testNGMethod.getConstructorOrMethod().getMethod(), (IAnnotationFinder) ClassUtil.getField("m_annotationFinder", testNGMethod), testNGMethod.getXmlTest(), testNGMethod.getInstance());
		setTestClass(testNGMethod.getTestClass());
	}
	
	public TestNGScenario(Method method, IAnnotationFinder finder, XmlTest xmlTest, Object instance) {
		super(method, finder, xmlTest, instance);
		init(instance);
	}

	private void init(Object instance) {
		if (Scenario.class.isAssignableFrom(getRealClass())) {
			scenario = (Scenario) instance;
			if (scenario.getPriority() < 1000 || !getXmlTest().getParallel().isParallel()
					|| getXmlTest().getParallel().equals(ParallelMode.TESTS)) {
				setPriority(scenario.getPriority());
			}
			setGroups(scenario.getM_groups());
			setGroupsDependedUpon(scenario.getM_groupsDependedUpon(), new ArrayList<String>());
			setMethodsDependedUpon(scenario.getM_methodsDependedUpon());
			setDescription(scenario.getDescription());
			setEnabled(scenario.isM_enabled());
			setAlwaysRun(scenario.isM_isAlwaysRun());
			setIgnoreMissingDependencies(scenario.getIgnoreMissingDependencies());
			metadata = scenario.getMetadata();
			qualifiledName = scenario.getTestName();
			setTimeOut(scenario.getTimeOut());
			
		} else {
			metadata = getMetadata(getMethod(), true);
			qualifiledName = getRealClass().getName() + "." + getMethodName();
		}
		metadata.put("name", getMethodName());
		metadata.put("sign", getSignature());

		formatMetaData(metadata);
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

	@Override
	public String getQualifiedName() {
		return qualifiledName;
	}

	public Collection<String> getSteps() {
		if (scenario != null) {
			List<String> steps = new ArrayList<String>();
			for (TestStep step : scenario.getSteps()) {
				steps.add(step.getDescription());
			}
			return steps;
		}
		return Collections.emptyList();
	}
	
	public String getClassOrFileName(){
		if (scenario != null) {
			if (StringUtil.isNotBlank(scenario.getFileName())) {
				return scenario.getFileName();
			}
		}
		return getRealClass().getName();
	}
}
