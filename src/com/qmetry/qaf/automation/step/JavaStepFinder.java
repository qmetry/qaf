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
package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.keys.ApplicationProperties.STEP_PROVIDER_PKG;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.qmetry.qaf.automation.core.ClassFinder;
import com.qmetry.qaf.automation.core.ClassFinderFactory;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * com.qmetry.qaf.automation.step.JavaStepFinder.java
 * 
 * @author chirag.jayswal
 */
public final class JavaStepFinder {
	public static final String STEPS_PACKAGE = "com.qmetry.qaf.automation.step";
	private static final Log logger = LogFactoryImpl.getLog(JavaStepFinder.class);
    private static final ClassFinder CLASS_FINDER = ClassFinderFactory.getClassFinder();
	public static Map<String, TestStep> getAllJavaSteps() {
		Map<String, TestStep> stepMapping = new HashMap<String, TestStep>();
		Set<Method> steps = new LinkedHashSet<Method>();

		List<String> pkgs = new ArrayList<String>();
		pkgs.add(STEPS_PACKAGE);

		if (getBundle().containsKey(STEP_PROVIDER_PKG.key)) {
			pkgs.addAll(Arrays.asList(getBundle().getStringArray(STEP_PROVIDER_PKG.key)));
		}
		for (String pkg : pkgs) {
			logger.info("pkg: " + pkg);
			try {
				List<Class<?>> classes = CLASS_FINDER.getClasses(pkg);
				steps.addAll(getAllMethodsWithAnnotation(classes, QAFTestStep.class));
			} catch (Exception e) {
				System.err.println("Unable to load steps for package: " + pkg);
			}
		}

		for (Method step : steps) {
			if (!Modifier.isPrivate(step.getModifiers())) {
				// exclude private methods.
				// Case: step provided using QAFTestStepProvider at class level
				add(stepMapping, new JavaStep(step));
			}
		}
		
		ServiceLoader<StepFinder> stepFinderServicesLoader =  ServiceLoader.load(StepFinder.class);
		Iterator<StepFinder> stepFinderServices = stepFinderServicesLoader.iterator();
		while(stepFinderServices.hasNext()){
			Set<TestStep> osteps = stepFinderServices.next().getAllJavaSteps(pkgs);
			for(TestStep step:osteps){
				add(stepMapping, step);
			}
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

			logger.debug(String.format(
					"Found duplicate step to load [%s] with [%s] prority then [%s]",
					oldStep.getSignature(),
					(oldStepPriority > curStepPriority ? "higher" : "lower"),
					step.getSignature()));

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

	private static Set<Method> getAllMethodsWithAnnotation(Collection<Class<?>> classes,
			Class<? extends Annotation> annotation) {

		Set<Method> methods = new HashSet<Method>();
		for (Class<?> cls : classes) {

			if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
				continue;

			boolean isStepProvider = cls.isAnnotationPresent(QAFTestStepProvider.class);

			for (Method method : cls.getMethods()) {
				if (isStepProvider || ClassUtil.hasAnnotation(method, annotation)) {
					methods.add(method);
				}
			}

		}

		return methods;
	}
}
