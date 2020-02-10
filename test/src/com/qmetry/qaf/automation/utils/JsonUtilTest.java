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
package com.qmetry.qaf.automation.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.qmetry.qaf.automation.data.MetaData;
import com.qmetry.qaf.automation.impl.Item;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.Validator;


public class JsonUtilTest {

	@Test
	public void testToMap() {
		String jsonString = "{'locator':'css=footer.footer';'a':1}";
		Map<String, Object> result = JSONUtil.toMap(jsonString);
		Validator.assertThat(result, Matchers.hasKey("locator"));
	}

	@Test(expectedExceptions = JsonSyntaxException.class)
	public void testToObjectInvalid() {
		String json = "[1,2,3]";
		final Type DATASET_TYPE = new TypeToken<List<Map<String, Object>>>() {
		}.getType();
		Object o = JSONUtil.toObject(json, DATASET_TYPE);
		System.out.println(o);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testToObject()
			throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String json = "[{'l':[1,'a',1.0,'1.0']}]";
		Object o = JSONUtil.toObject(json);
		Validator.assertThat(o, Matchers.instanceOf(List.class));
		Validator.assertThat(((List<?>) o).get(0), Matchers.instanceOf(Map.class));
		Map<String, Object> m = (Map<String, Object>) ((List<?>) o).get(0);
		Object l = m.get("l");
		Validator.assertThat(l, Matchers.instanceOf(List.class));
		Validator.assertThat(((List<Object>) l).get(0), Matchers.instanceOf(Long.class));
		Validator.assertThat(((List<Object>) l).get(2), Matchers.instanceOf(Double.class));
		Validator.assertThat(((List<Object>) l).get(3), Matchers.instanceOf(String.class));

		json = "[1.5,2,3]";
		o = JSONUtil.toObject(json);
		Validator.assertThat(l, Matchers.instanceOf(List.class));
		Validator.assertThat(((List<?>) o).get(0), Matchers.instanceOf(Double.class));
		Validator.assertThat(((List<?>) o).get(1), Matchers.instanceOf(Long.class));

		o = JSONUtil.toObject(json, Double[].class);
		Validator.assertThat((Object[]) o, Matchers.arrayWithSize(3));
		System.out.println("Double: " + ((Double[]) o)[0]);
		Validator.assertThat(((Object[]) o)[0], Matchers.instanceOf(Double.class));

		Type t = TypeToken.get(Float[].class).getType();
		o = JSONUtil.toObject(json, t);
		Validator.assertThat((Object[]) o, Matchers.arrayWithSize(3));
		System.out.println("Float: " + ((Float[]) o)[0]);
		Validator.assertThat(((Object[]) o)[0], Matchers.instanceOf(Float.class));

		Short[] s = JSONUtil.toObject(json, Short[].class);
		Validator.assertThat(s, Matchers.arrayWithSize(3));
		System.out.println("Short: " + s[0]);
		Validator.assertThat(s[0], Matchers.instanceOf(Short.class));

		json = "{'name':'a','price':2.10,'id':1,'description':'item-1'}";

		Method method = ClassUtil.getMethod(JsonUtilTest.class, "mystep");

		o = JSONUtil.toObject(json, Item.class);
		Validator.assertThat(o, Matchers.instanceOf(Item.class));

		String listOfItems = "[{'name':'a','price':2.10,'id':1,'description':'item-1'},{'name':'b','price':2,'id':1,'description':'item-2'},{'name':'a','price':1.99,'id':3}]";

		method.invoke(null, JSONUtil.toObject(json, method.getGenericParameterTypes()[0]),
				JSONUtil.toObject(listOfItems, method.getGenericParameterTypes()[1]),
				JSONUtil.toObject(listOfItems, method.getGenericParameterTypes()[2]));

		// method.invoke(null, JSONUtil.toObject(json,
		// method.getGenericParameterTypes()[0]),
		// JSONUtil.toObject(listOfItems,
		// method.getGenericParameterTypes()[1]),JSONUtil.toObject(json,
		// method.getGenericParameterTypes()[2]));

	}

	@Test()
	public void testToString() {
		Map<String, Object> map = new LinkedTreeMap<String, Object>();
		map.put("name", "a");
		map.put("price", 2.1);
		map.put("id", 1);
		map.put("description", "item-1");
		String actualString = JSONUtil.toString(map);

		Validator.assertThat(actualString, Matchers.containsString("\"price\":2.1"));
		Validator.assertThat(actualString, Matchers.containsString("\"id\":1"));

		Object[] o = new Object[] { "xyz", map, "abc" };
		actualString = JSONUtil.toString(o);

		Validator.assertThat(actualString, Matchers.containsString("\"price\":2.1"));
		Validator.assertThat(actualString, Matchers.containsString("\"id\":1"));
		Validator.assertThat(actualString, Matchers.containsString("abc"));
		Validator.assertThat(actualString, Matchers.startsWith("["));


		boolean b = true;
		actualString = JSONUtil.toString(b);
		Validator.assertThat(actualString, Matchers.equalTo("true"));

		Map<Object, Object> m = Maps.newHashMap();
		m.put("name", "a");
		m.put("uname", "a");

		m.put("id", 1);

		Item item = new Gson().fromJson(new Gson().toJson(m), Item.class);
		actualString = JSONUtil.toString(item);

		Validator.assertThat(actualString, Matchers.containsString("\"name\":\"a\""));
		Validator.assertThat(actualString, Matchers.containsString("\"id\":1"));

		Validator.assertThat(actualString, Matchers.startsWith("{"));

		List<Object> l = new ArrayList<Object>();
		l.add(o);
		l.add("some {s}");
		l.add(map);
		actualString = JSONUtil.toString(l);

		Validator.assertThat(actualString, Matchers.containsString("\"price\":2.1"));
		Validator.assertThat(actualString, Matchers.containsString("\"id\":1"));
		Validator.assertThat(actualString, Matchers.containsString("abc"));
		Validator.assertThat(actualString, Matchers.startsWith("["));


		String s = "some {string}";
		actualString = JSONUtil.toString(s);
		Validator.assertThat(actualString, Matchers.equalToIgnoringCase(s));
	}

	@DataProvider(name = "toStringDP")
	public Iterator<Object[]> getPramData() {
		List<Object[]> data = new ArrayList<Object[]>();
		Map<String, Object> map = new LinkedTreeMap<String, Object>();
		map.put("name", "a");
		map.put("price", 2.10);
		map.put("id", 1);
		map.put("description", "item-1");

		data.add(new Object[] { map, "{\"name\":\"a\",\"price\":2.10,\"id\":1,\"description\":\"item-1\"}" });
		List<Object> l = new ArrayList<Object>();
		l.add(map);
		data.add(new Object[] { l, "[{\"name\":\"a\",\"price\":2.10,\"id\":1,\"description\":\"item-1\"}]" });
		Object[] o = new Object[] { map };
		data.add(new Object[] { o, "[{\"name\":\"a\",\"price\":2.10,\"id\":1,\"description\":\"item-1\"}]" });

		return data.iterator();
	}

	@MetaData("{'issue':'285'}")
	@Test
	public void getJsonArrayOfMapsTest() {
		String listOfItems = "[{'name':'a','price':2.10,'id':1,'description':'item-1'},{'name':'b','price':2,'id':1,'description':'item-2'},{'name':'a','price':1.99,'id':3}]";
		Object[][] result = JSONUtil.getJsonArrayOfMaps(listOfItems);
		Validator.assertThat(result[0][0], Matchers.instanceOf(Map.class));
		@SuppressWarnings("unchecked")
		Map<String, Object> map0 = (Map<String, Object>) result[0][0];
		Validator.assertThat(map0.get("price"), Matchers.instanceOf(Double.class));
		Validator.assertThat(map0.get("id"), Matchers.instanceOf(Long.class));
		Validator.assertThat(map0.get("id").toString(), Matchers.is("1"));
	}

	// @Test
	public void testGetJsonObjectFromFile() {

	}

	Object[][] getJsonFiles() throws IOException {
		List<String> files = new ArrayList<String>();
		File f = File.createTempFile("file1", ".json");
		final String[] array = { "a", "b", "c" };
		// JSONUtil.writeJsonObjectToFile(f, a);
		files.add(f.getAbsolutePath());
		return null;
	}

	public static void mystep(Item ll, List<Item> list, Item... items) {

		System.out.println(list);

		System.out.println(ll.getClass());
		Validator.assertThat(ll, Matchers.instanceOf(Item.class));

		System.out.println(list.get(0).getClass());
		Validator.assertThat(list.get(0), Matchers.instanceOf(Item.class));

		System.out.println("items " + items.length);

		Validator.assertThat(items[0], Matchers.instanceOf(Item.class));

	}
}
