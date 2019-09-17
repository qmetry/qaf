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
package com.qmetry.qaf.automation.core;

import java.io.IOException;
import java.util.List;

import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class ClassFinderFactory {

	public static ClassFinder getClassFinder() {
		String clsFinderImpl = ConfigurationManager.getBundle().getString("class.finder");
		if(StringUtil.isNotBlank(clsFinderImpl)){
			try {
				Class<?> cls = (Class<?>) Class.forName(clsFinderImpl);
				return (ClassFinder) cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Unable to initiate " + clsFinderImpl + "\n Will use default class finder");
			}
		}
		return new DefaultClassFinder();
	}

	public static class DefaultClassFinder implements ClassFinder {

		/*
		 * (non-Javadoc)
		 * @see com.qmetry.qaf.automation.core.ClassFinder#getClasses(java.lang.
		 * String)
		 */
		@Override
		public List<Class<?>> getClasses(String pkg) throws IOException {
			return ClassUtil.getClasses(pkg);
		}

	}

}
