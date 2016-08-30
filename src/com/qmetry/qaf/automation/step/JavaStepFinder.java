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


package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.keys.ApplicationProperties.STEP_PROVIDER_PKG;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.qmetry.qaf.automation.ui.webdriver.QAFWebComponent;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * com.qmetry.qaf.automation.step.JavaStepFinder.java
 * 
 * @author chirag.jayswal
 */
public final class JavaStepFinder {
	public static final String STEPS_PACKAGE = "com.qmetry.qaf.automation.step";

	public static Map<String, TestStep> getAllJavaSteps() {
		Map<String, TestStep> stepMapping = new HashMap<String, TestStep>();
		Collection<URL> urls = ClasspathHelper.forPackage("com.qmetry.qaf.automation.step");
		FilterBuilder filter = new FilterBuilder();
		filter.include(FilterBuilder.prefix(STEPS_PACKAGE));
		if (getBundle().containsKey(STEP_PROVIDER_PKG.key)) {
			for (String pkg : getBundle().getStringArray(STEP_PROVIDER_PKG.key)) {
				System.out.println("pkg: " + pkg);
				urls.addAll(ClasspathHelper.forPackage(pkg));
				filter.include(FilterBuilder.prefix(pkg));
			}
		}

		// Specify reflector adapter explicitly
		ConfigurationBuilder configurationBuilder = ConfigurationBuilder.build();
		configurationBuilder.setUrls(urls).filterInputsBy(filter).setScanners(
				new org.reflections.scanners.MethodAnnotationsScanner(), new TypeAnnotationsScanner(),
				new SubTypesScanner(false));
		Reflections reflections = new Reflections(configurationBuilder);
		Set<Class<? extends Object>> classes = reflections.getSubTypesOf(Object.class);
		classes.add(CommonStep.class);
		Set<Method> steps = new LinkedHashSet<Method>();
		steps.addAll(reflections.getMethodsAnnotatedWith(QAFTestStep.class));
		steps.addAll(getAllMethodsWithAnnotation(classes, QAFTestStep.class));

		Set<Class<?>> stepProviders = reflections.getTypesAnnotatedWith(QAFTestStepProvider.class);

		for (Class<?> stepProvider : stepProviders) {
			if (QAFWebComponent.class.isAssignableFrom(stepProvider)) {

			} else {
				steps.addAll(Arrays.asList(stepProvider.getMethods()));
			}
		}

		for (Method step : steps) {

			add(stepMapping, new JavaStep(step));
		}

		return stepMapping;
	}

	private static void add(Map<String, TestStep> stepMapping, TestStep step) {
		TestStep oldStep = stepMapping.put(step.getName().toUpperCase(), step);

		if (oldStep != null) {

			// ensure the priority specified while providing step provider
			// package. If list of packages provided, last package has highest
			// priority.
			String[] pkgs = getBundle().getStringArray(STEP_PROVIDER_PKG.key);
			int oldStepPriority = getStepPriority(oldStep, pkgs);
			int curStepPriority = getStepPriority(step, pkgs);
			if (oldStepPriority > curStepPriority) {
				step = oldStep;
				oldStep = stepMapping.put(step.getName().toUpperCase(), oldStep);
			}
		}

	}

	private static int getStepPriority(TestStep step, String[] pkgs) {
		String stepPackage = step.getFileName().replaceAll("/", ".");
		int i = 0;
		for (; i < pkgs.length; i++) {
			if (stepPackage.startsWith(pkgs[i]))
				return i;
		}
		return i;
	}

	private static Set<Method> getAllMethodsWithAnnotation(Set<Class<?>> classes,
			Class<? extends Annotation> annotation) {

		Set<Method> methods = new HashSet<Method>();
		for (Class<?> cls : classes) {
			if (cls.isInterface())
				continue;

			for (Method method : cls.getMethods()) {
				if (ClassUtil.hasAnnotation(method, annotation)) {
					methods.add(method);
				}
			}
		}

		return methods;
	}
}
