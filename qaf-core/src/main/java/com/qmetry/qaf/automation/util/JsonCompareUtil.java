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

import static com.qmetry.qaf.automation.util.StringMatcher.getMatcherOrNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

/**
 * Utility class to compare two JSON.
 * 
 * @author chirag.jayswal
 *
 */
public class JsonCompareUtil {

	/**
	 * If not strict mode it will check actual (argument 1) JSON contains expected
	 * (argument 2) in any order. For values * can be used as any value (including complex object) and
	 * prefix from string matcher can be used for value match. * is useful when you want to make sure key exist with any not null value
	 * 
	 * @param actualJsonStr   - json string
	 * @param expectedJsonStr - json string
	 * @param strict          - boolean to specify either strict compare or not. If
	 *                        true, it will do exact match ignoring order.
	 * @return {@link MapDifference}
	 */
	public static MapDifference<String, Object> jsonCompare(String actualJsonStr, String expectedJsonStr,
			boolean strict) {

		Map<String, Object> expectedMap = toMap(expectedJsonStr);
		Map<String, Object> actualMap = toMap(actualJsonStr);

		Equivalence<Object> equivalenceImpl = strict ? new StrictEquivalenceImpl() : new ContainsEquivalenceImpl();

		return Maps.difference(expectedMap, actualMap, equivalenceImpl);
	}

	private static boolean contains(Collection<?> c1, Collection<?> c2) {
		Iterator<?> iter = c1.iterator();
		while(iter.hasNext()) {
			ObjectWrapper c = new ObjectWrapper(iter.next());
			if(!c2.remove(c)) {
				//didn't found a match in target collection
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> toMap(String json) {
		JsonElement je = JSONUtil.getGsonElement(json);
	
		if (je.isJsonObject()) {
			return JSONUtil.toMap(json);
		}
	
		Map<String, Object> result = new HashMap<String, Object>();
		if (je.isJsonArray()) {
			result.put("value", JSONUtil.toObject(json, List.class));//g.fromJson(je, List.class));
			return result;
		}
		result.put("value", JSONUtil.toObject(json));//g.fromJson(je, Object.class));
		return result;
	}

	private static boolean match(Object o1, Object o2) {
		StringMatcher matcher = getMatcherOrNull(String.valueOf(o1));
		if (matcher != null) {
			return matcher.match(String.valueOf(o2));
		}
		return o1.equals(o2);
	}

	private static class StrictEquivalenceImpl extends Equivalence<Object> {
		@SuppressWarnings("rawtypes")
		@Override
		protected boolean doEquivalent(Object arg0, Object arg1) {
			//no need to null check Equivalence.equivalent takes care from where this method will be called 

			if ("*".equals(arg0)) {
				return true;
			}
			if (arg0 instanceof Map && arg1 instanceof Map) {
				@SuppressWarnings("unchecked")
				MapDifference<String, Object> res = Maps.difference((Map) arg0, (Map) arg1, this);
				return res.areEqual();
			}
			if (arg0 instanceof Collection && arg1 instanceof Collection) {
				return ((Collection<?>) arg1).size() == ((Collection<?>) arg0).size()
						&& contains ((Collection<?>) arg0, (Collection<?>) arg1);
			}
			return match(arg0,arg1);
		}
	
		@Override
		protected int doHash(Object arg0) {
			return arg0.hashCode();
		}
	}
	private static class ContainsEquivalenceImpl extends Equivalence<Object> {
		
		@SuppressWarnings("rawtypes")
		@Override
		protected boolean doEquivalent(Object arg0, Object arg1) {
			//no need to null check
			if ("*".equals(arg0)) {
				return true;
			}
			if (arg0 instanceof Map && arg1 instanceof Map) {
				@SuppressWarnings("unchecked")
				MapDifference<String, Object> res = Maps.difference((Map) arg0, (Map) arg1, this);
				return res.entriesOnlyOnLeft().isEmpty() && res.entriesDiffering().isEmpty();
			}
			if (arg0 instanceof Collection && arg1 instanceof Collection) {
				return contains ((Collection<?>) arg0, (Collection<?>) arg1);
			}
			return match(arg0,arg1);
		}

		@Override
		protected int doHash(Object arg0) {
			return arg0.hashCode();
		}
	}
	
	private static class ObjectWrapper{
		private Object o;
		private Equivalence<Object> e;
		
		ObjectWrapper(Object o){
			this.o=o;
			e = new ContainsEquivalenceImpl();
		}
		
		@Override
		public boolean equals(Object obj) {
			return e.equivalent(o, obj);
		}
		@Override
		public int hashCode() {
			return o.hashCode();
		}
		
	}

}
