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

import java.lang.reflect.Array;

/**
 * set get object array by enumeration
 * 
 * @author chirag
 */
public class EnumUtil {

	@SuppressWarnings("unchecked")
	public static final <T> Object[] set(Enum<?> arg, T val, T... args) {
		if ((args != null) && (args.length > arg.ordinal())) {
			args[arg.ordinal()] = val;
			return args;
		}
		T[] extended = (T[]) Array.newInstance(Object.class, arg.ordinal());// new
		extended[arg.ordinal()] = val;
		if (args != null) {
			System.arraycopy(args, 0, extended, 0, args.length);
		}
		return extended;

	}

	public static final Object getFrom(Enum<?> arg, Object... args) {
		if ((args != null) && (args.length > arg.ordinal())) {
			return args[arg.ordinal()];
		}
		return null;
	}

	public static final Object[] setIfNull(Enum<?> arg, Object val, Object... args) {
		if (null == getFrom(arg, args)) {
			return set(arg, val, args);
		}
		return args;
	}

}
