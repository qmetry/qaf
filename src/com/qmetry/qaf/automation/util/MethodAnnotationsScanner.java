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
package com.qmetry.qaf.automation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.reflections.scanners.AbstractScanner;

;

/**
 * This class is another implementation of
 * {@link org.reflections.scanners.MethodAnnotationsScanner}, that also scanning
 * methods with annotation in interface.
 * 
 * @author chirag
 */
public class MethodAnnotationsScanner extends AbstractScanner {

	@SuppressWarnings("unchecked")
	@Override
	public void scan(Object cls) {
		System.out.println("scanning using " + getMetadataAdapter().getClass());
		for (Object method : getMetadataAdapter().getMethods(cls)) {

			for (String methodAnnotation : getMethodAnnotationNames((Member) method)) {
				if (acceptResult(methodAnnotation)) {
					getStore().put(methodAnnotation, getMetadataAdapter().getMethodFullKey(cls, method));
				}
			}
		}

	}

	public List<String> getMethodAnnotationNames(Member method) {
		Annotation[] annotations = method instanceof Method ? getMethodAnnotations((Method) method)
				: method instanceof Constructor ? ((Constructor<?>) method).getAnnotations() : null;
		List<String> allannotations = getAnnotationNames(annotations);
		return allannotations;
	}

	private List<String> getAnnotationNames(Annotation[] annotations) {
		List<String> names = new ArrayList<String>(annotations.length);
		for (Annotation annotation : annotations) {
			names.add(annotation.annotationType().getName());
		}
		return names;
	}

	private Annotation[] getMethodAnnotations(Method method) {
		System.err.println("get Method Annotations....");

		List<Annotation> annotations = Arrays.asList(method.getAnnotations());

		Class<?>[] intfaces = method.getDeclaringClass().getInterfaces();
		for (Class<?> intface : intfaces) {
			Method imethod;
			try {
				imethod = intface.getMethod(method.getName(), method.getParameterTypes());
				annotations.addAll(Arrays.asList(imethod.getAnnotations()));
			} catch (NoSuchMethodException e) {
				System.err.println(e.getMessage());
			} catch (SecurityException e) {
				System.err.println(e.getMessage());
			}

		}

		return annotations.toArray(new Annotation[annotations.size()]);
	}
}
