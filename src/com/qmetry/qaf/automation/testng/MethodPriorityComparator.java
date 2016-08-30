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


package com.qmetry.qaf.automation.testng;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.testng.IMethodInstance;
import org.testng.log4testng.Logger;

public class MethodPriorityComparator implements Comparator<IMethodInstance> {
	String orderedGroups;
	private static final Logger log = Logger.getLogger(MethodPriorityComparator.class);

	public MethodPriorityComparator(String orderedGroup) {
		orderedGroups = orderedGroup;

	}

	public MethodPriorityComparator() {
	}

	@Override
	public int compare(IMethodInstance o1, IMethodInstance o2) {
		log.debug(o1.getMethod().getMethodName() + " O2: " + o2.getMethod().getMethodName() + " Order : "
				+ orderedGroups);
		String method1Name = o1.getMethod().getMethodName();
		String method2Name = o2.getMethod().getMethodName();
		Set<String> depends, o1Set, o2Set, o1Dependency, o2Dependency;

		// check method dependency
		o1Dependency = new HashSet<String>(Arrays.asList(o1.getMethod().getMethodsDependedUpon()));
		o2Dependency = new HashSet<String>(Arrays.asList(o1.getMethod().getMethodsDependedUpon()));
		if (o1Dependency.contains(method2Name)) {
			return -1;
		}
		if (o2Dependency.contains(method1Name)) {
			return 1;
		}
		// check group dependency
		o1Set = new HashSet<String>(Arrays.asList(o1.getMethod().getGroups()));
		o2Set = new HashSet<String>(Arrays.asList(o1.getMethod().getGroups()));
		o1Dependency = new HashSet<String>(Arrays.asList(o1.getMethod().getGroupsDependedUpon()));
		o2Dependency = new HashSet<String>(Arrays.asList(o1.getMethod().getGroupsDependedUpon()));

		depends = new HashSet<String>(o1Dependency);
		depends.retainAll(o2Set);
		if (!depends.isEmpty()) {
			return -1;
		}
		depends = new HashSet<String>(o2Dependency);
		depends.retainAll(o1Set);
		if (!depends.isEmpty()) {
			return 1;
		}
		int o1Priority = getGroupOrder(o1);
		int o2Priority = getGroupOrder(o2);
		if (o1Priority == o2Priority) {
			o1Priority = getClassOrder(o1);
			o2Priority = getClassOrder(o2);
			if (o1Priority == o2Priority) {
				o1Priority = getMethodOrder(o1);
				o2Priority = getMethodOrder(o2);
			}
		}
		if (o1Priority == -1) {
			o1Priority = o2Priority + 1;
		}
		if (o2Priority == -1) {
			o2Priority = o1Priority + 1;
		}
		return o1Priority - o2Priority;

	}

	private Integer getGroupOrder(IMethodInstance o) {
		if (orderedGroups == null) {
			return 0;
		}
		String groupName = (o.getMethod().getGroups() != null) && (o.getMethod().getGroups().length > 0)
				? o.getMethod().getGroups()[0] : "NONE";
		if (!orderedGroups.contains("NONE")) {
			orderedGroups = orderedGroups + ", NONE";
		}
		if (!orderedGroups.contains(groupName)) {
			orderedGroups = orderedGroups + "," + groupName;
		}

		log.debug(o.getMethod().getMethodName() + " Group: " + groupName + " Order : " + orderedGroups);

		return Integer.valueOf(orderedGroups.toUpperCase().indexOf(groupName.toUpperCase()));
	}

	private int getClassOrder(IMethodInstance mi) {
		int result = -1;
		Class<?> cls = mi.getMethod().getConstructorOrMethod().getMethod().getDeclaringClass();
		Priority classPriority = cls.getAnnotation(Priority.class);
		if (classPriority != null) {
			result = classPriority.value();
		}

		return result;
	}

	private int getMethodOrder(IMethodInstance mi) {
		int result = -1;
		Method method = mi.getMethod().getConstructorOrMethod().getMethod();
		Priority a1 = method.getAnnotation(Priority.class);

		if (a1 != null) {
			result = a1.value();
		}
		return result;
	}
}
