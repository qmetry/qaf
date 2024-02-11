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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * com.qmetry.qaf.automation.util.JarFinderUtil.java
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
			Collection<String> lst = finderUtil.getClasses("com.qmetry.qaf.automation");
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
