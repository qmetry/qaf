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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author chirag.jayswal
 *
 */
public class ListUtils {

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(Object arrayOrIterator) {
		if (arrayOrIterator instanceof Iterator)
			return toList((Iterator<T>) arrayOrIterator);
		if(arrayOrIterator.getClass().isArray()){
			return toList((T[])arrayOrIterator);
		}
		if(List.class.isAssignableFrom(arrayOrIterator.getClass()))
			return new ArrayList<T>((List<T>)arrayOrIterator);
		
		List<T> lstToReturn = new ArrayList<T>();
		lstToReturn.add((T)arrayOrIterator);
		
		return lstToReturn;
	}

	public static <T> List<T> toList(T[] array) {
		return Arrays.asList(array);
	}

	public static <T> List<T> toList(Iterator<T> iter) {
		ArrayList<T> list = new ArrayList<T>();
		if (null != iter) {
			while (iter.hasNext()) {
				list.add(iter.next());
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		Object[][] aa = {{"a",10},{"b",20}};
		Object a = aa;
		
		System.out.printf("%s" ,toList(a).get(0));
		
	}
}
