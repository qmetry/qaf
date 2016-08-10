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


package com.infostretch.automation.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * com.infostretch.automation.util.JarFinderUtil.java
 * 
 * @author chirag
 */
public class JarFinderUtil {
	private JarFile jarFile;

	public JarFinderUtil(String jarFile) throws IOException {
		this.jarFile = new JarFile(jarFile);

	}

	public Collection<String> getClasses(String pkg) {
		Enumeration<JarEntry> entries = jarFile.entries();
		boolean inclucdeChildPkg = false;
		if (pkg.endsWith(".*")) {
			inclucdeChildPkg = true;
			pkg = pkg.substring(0, pkg.length() - 2);
		}
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName().replaceAll("/", ".");
			if (name.endsWith(".class") && name.toLowerCase().startsWith(pkg.toLowerCase()) && (inclucdeChildPkg
					|| (StringUtil.countMatches(name, ".") == StringUtil.countMatches(pkg, ".") + 2))) {
				lst.add(name);
			}

		}
		return lst;
	}

	public Collection<String> getClassesBySimpleName(String name) {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().toLowerCase().endsWith(name.toLowerCase() + ".class")) {
				lst.add(entry.getName().replaceAll("/", "."));
			}
		}
		return lst;
	}

	public Collection<String> getAllClasses() {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".class")) {
				lst.add(entry.getName().replaceAll("/", "."));
			}
		}
		return lst;

	}

	public List<String> getAllPackages() {
		Enumeration<JarEntry> entries = jarFile.entries();
		ArrayList<String> lst = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				String s = entry.getName();
				lst.add(s.substring(0, s.length()).replaceAll("/", "."));
			}
		}
		return lst;
	}

	public Collection<String> getAllMethods(String className) {
		return null;
	}

	public static void main(String[] args) {
		try {
			JarFinderUtil finderUtil = new JarFinderUtil("./dist/selenium-automation-framework.jar");
			Collection<String> lst = finderUtil.getClasses("com.infostretch.automation");
			for (Object element : lst) {
				System.out.println((String) element);

			}

			lst = finderUtil.getClassesBySimpleName("ConfigurationManager");
			for (Object element : lst) {
				System.out.println((String) element);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
