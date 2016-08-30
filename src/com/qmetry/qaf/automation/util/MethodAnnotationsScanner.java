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
