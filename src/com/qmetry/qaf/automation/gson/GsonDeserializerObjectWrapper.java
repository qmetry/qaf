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
package com.qmetry.qaf.automation.gson;

import static com.qmetry.qaf.automation.util.ClassUtil.isAssignableFrom;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.qmetry.qaf.automation.util.ClassUtil;

/**
 * @author chirag.jayswal
 */
public class GsonDeserializerObjectWrapper implements JsonDeserializer<ObjectWrapper> {
	JsonDeserializationContext context;
	private Type type;

	public GsonDeserializerObjectWrapper(Type type) {
		this.type = type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public ObjectWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		this.context = context;
		return new ObjectWrapper(read(json, type));
	}

	private Object read(JsonElement in, Type typeOfT) {
		
		if (in.isJsonArray()) {
			JsonArray arr = in.getAsJsonArray();

			boolean isArray = isArray(typeOfT);
			Collection<Object> list = isArray ? new ArrayList<Object>()
					: context.deserialize(new JsonArray(0), typeOfT);
			for (JsonElement anArr : arr) {
				((Collection<Object>) list).add(read(anArr, getTypeArguments(typeOfT, 0)));
			}
			if (isArray) {
				return toArray((List<Object>) list);
			}
			try {
				return ClassUtil.getClass(typeOfT).cast(list);
			} catch (Exception e) {
				return context.deserialize(in, typeOfT);
			}
		} else if (in.isJsonObject()
				&& (isAssignableFrom(typeOfT, Map.class) || ClassUtil.getClass(typeOfT).equals(Object.class))) {
			Map<Object, Object> map = context.deserialize(new JsonObject(), typeOfT);//new LinkedTreeMap<Object, Object>();
			JsonObject obj = in.getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
			for (Map.Entry<String, JsonElement> entry : entitySet) {
				map.put(entry.getKey(), read(entry.getValue(), getTypeArguments(typeOfT, 1)));
			}
			return map;
		} else if (in.isJsonPrimitive() && ClassUtil.getClass(typeOfT).equals(Object.class)) {
			JsonPrimitive prim = in.getAsJsonPrimitive();
			if (prim.isBoolean()) {
				return prim.getAsBoolean();
			} else if (prim.isString()) {
				return prim.getAsString();
			} else if (prim.isNumber()) {
				if (prim.getAsString().contains("."))
					return prim.getAsDouble();
				else {
					return prim.getAsLong();
				}
			}
		}
		return context.deserialize(in, typeOfT);
	}

	private Type getTypeArguments(Type typeOfT, int index) {
		try {
			return ((ParameterizedType) typeOfT).getActualTypeArguments()[index];
		} catch (Exception e) {
			try {
				Class<?> ctype = ClassUtil.getClass(typeOfT).getComponentType();
				if (null != ctype) {
					return ctype;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return Object.class;
	}

	@SuppressWarnings("unchecked")
	private <T> T[] toArray(List<T> list) {
		Class<?> clazz = list.get(0).getClass(); // check for size and null before
		T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, list.size());
		return list.toArray(array);
	}

	private boolean isArray(Type typeOfT) {
		return ClassUtil.getClass(typeOfT).isArray();
	}

}
