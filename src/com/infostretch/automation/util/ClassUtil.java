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

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * com.infostretch.automation.util.ClassUtil.java
 * 
 * @author chirag.jayswal
 */
public final class ClassUtil {

	public static Set<Method> getAllMethodsWithAnnotation(String packageName, Class<? extends Annotation> annotation) {
		Set<Method> methods = new HashSet<Method>();
		try {
			for (Class<?> cls : getClasses(packageName)) {
				for (Method method : cls.getMethods()) {
					if (hasAnnotation(method, annotation)) {
						methods.add(method);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			System.err.println("ClassUtil.getAllMethods: " + e.getMessage());
		} catch (SecurityException e) {
			System.err.println("ClassUtil.getAllMethods: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("ClassUtil.getAllMethods: " + e.getMessage());
		}

		return methods;
	}

	public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
		if (method.isAnnotationPresent(annotation))
			return true;
		Class<?>[] intfaces = method.getDeclaringClass().getInterfaces();
		for (Class<?> intface : intfaces) {
			try {
				if (intface.getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(annotation))
					return true;
			} catch (NoSuchMethodException e) {
				// Ignore!...
			} catch (SecurityException e) {
				// Ignore!...
			}

		}
		return false;
	}

	public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
		if (method.isAnnotationPresent(annotation))
			return method.getAnnotation(annotation);
		Class<?>[] intfaces = method.getDeclaringClass().getInterfaces();
		for (Class<?> intface : intfaces) {
			try {
				Method iMethod = intface.getMethod(method.getName(), method.getParameterTypes());
				if (iMethod.isAnnotationPresent(annotation))
					return iMethod.getAnnotation(annotation);
			} catch (NoSuchMethodException e) {
				// Ignore!...
			} catch (SecurityException e) {
				// Ignore!...
			}

		}
		return null;
	}

	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
		if (clazz.isAnnotationPresent(annotation))
			return clazz.getAnnotation(annotation);
		Class<?>[] intfaces = clazz.getInterfaces();
		for (Class<?> intface : intfaces) {
			try {

				if (intface.isAnnotationPresent(annotation))
					return intface.getAnnotation(annotation);
			} catch (SecurityException e) {
				// Ignore!...
			}

		}
		return null;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and sub packages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	/**
	 * Get all methods of the class including parent.
	 * 
	 * @param clazz
	 * @param name
	 *            case-insensitive method name to get
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException {
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		if (null != clazz.getSuperclass()) {
			getMethod(clazz.getSuperclass(), name);
		}
		throw new NoSuchMethodException();
	}

	/**
	 * Get all fields of the class including parent up to given level in
	 * hierarchy.
	 * 
	 * @param clazz
	 * @param uptoParent
	 *            - restrict hierarchy - to exclude fields from provided class
	 *            and it's parent(s) in hierarchy
	 * @return
	 */
	public static Field[] getAllFields(Class<?> clazz, Class<?> uptoParent) {
		Collection<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if ((clazz.getSuperclass() != null) && !clazz.getSuperclass().equals(uptoParent)) {
			fields.addAll(Arrays.asList(getAllFields(clazz.getSuperclass(), uptoParent)));
		}
		return fields.toArray(new Field[] {});
	}

	public static void extractInterfaces(Set<Class<?>> iSet, Class<?> clazz) {
		if (Object.class.equals(clazz)) {
			return;
		}
		Class<?>[] classes = clazz.getInterfaces();
		iSet.addAll(Arrays.asList(classes));
		extractInterfaces(iSet, clazz.getSuperclass());
	}

	/**
	 * Get instance of Parameterized class by calling default constructor. Will
	 * work only if Parameterized class has default constructor
	 * 
	 * @param <C>
	 * @return instance of the parameter
	 */
	@SuppressWarnings("unchecked")
	public static <C> C getInstance() {
		try {
			Class<C> class1 = (Class<C>) ((ParameterizedType) ClassUtil.class.getMethod("getInstance")
					.getGenericReturnType()).getActualTypeArguments()[0].getClass();

			return class1.newInstance();
		} catch (Exception e) {
			return null;
		}

	}

	public static String getMethodSignture(Method m, boolean includeClass) {

		StringBuilder signBuilder = new StringBuilder();
		if (includeClass) {
			signBuilder.append(m.getDeclaringClass().getCanonicalName());
			signBuilder.append("#");
		}

		signBuilder.append(m.getName());
		signBuilder.append("(");
		Iterator<Class<?>> iterator = Arrays.asList(m.getParameterTypes()).iterator();
		while (iterator.hasNext()) {
			Class<?> parameterType = iterator.next();
			signBuilder.append(parameterType.getCanonicalName());
			if (iterator.hasNext()) {
				signBuilder.append(", ");
			}
		}
		signBuilder.append(")");
		return signBuilder.toString();
	}

	public static Class getTemplateParameterOfInterface(Class base, Class desiredInterface) {
		Object rtn = getSomething(base, desiredInterface);
		if ((rtn != null) && (rtn instanceof Class)) {
			return (Class) rtn;
		}
		return null;
	}

	private static Object getSomething(Class base, Class desiredInterface) {
		for (int i = 0; i < base.getInterfaces().length; i++) {
			Class intf = base.getInterfaces()[i];
			if (intf.equals(desiredInterface)) {
				Type generic = base.getGenericInterfaces()[i];
				if (generic instanceof ParameterizedType) {
					ParameterizedType p = (ParameterizedType) generic;
					Type type = p.getActualTypeArguments()[0];
					Class rtn = getRawTypeNoException(type);
					if (rtn != null) {
						return rtn;
					}
					return type;
				} else {
					return null;
				}
			}
		}
		if ((base.getSuperclass() == null) || base.getSuperclass().equals(Object.class)) {
			return null;
		}
		Object rtn = getSomething(base.getSuperclass(), desiredInterface);
		if ((rtn == null) || (rtn instanceof Class)) {
			return rtn;
		}
		if (!(rtn instanceof TypeVariable)) {
			return null;
		}

		String name = ((TypeVariable) rtn).getName();
		int index = -1;
		TypeVariable[] variables = base.getSuperclass().getTypeParameters();
		if ((variables == null) || (variables.length < 1)) {
			return null;
		}

		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getName().equals(name)) {
				index = i;
			}
		}
		if (index == -1) {
			return null;
		}

		Type genericSuperclass = base.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			return null;
		}

		ParameterizedType pt = (ParameterizedType) genericSuperclass;
		Type type = pt.getActualTypeArguments()[index];

		Class clazz = getRawTypeNoException(type);
		if (clazz != null) {
			return clazz;
		}
		return type;
	}

	/**
	 * Given an interface Method, look in the implementing class for the method
	 * that implements the interface's method to obtain generic type
	 * information. This is useful for templatized interfaces like:
	 * <p/>
	 * 
	 * <pre>
	 * interface Foo&lt;T&gt; {
	 * 	&#064;GET
	 * 	List&lt;T&gt; get();
	 * }
	 * </pre>
	 * 
	 * @param clazz
	 * @param method
	 *            interface method
	 * @return
	 */
	public static Type getGenericReturnTypeOfGenericInterfaceMethod(Class clazz, Method method) {
		if (!method.getDeclaringClass().isInterface()) {
			return method.getGenericReturnType();
		}

		try {
			Method tmp = clazz.getMethod(method.getName(), method.getParameterTypes());
			return tmp.getGenericReturnType();
		} catch (NoSuchMethodException e) {

		}
		return method.getGenericReturnType();
	}

	/**
	 * Given an interface Method, look in the implementing class for the method
	 * that implements the interface's method to obtain generic type
	 * information. This is useful for templatized interfaces like:
	 * <p/>
	 * 
	 * <pre>
	 * interface Foo&lt;T&gt; {
	 * 	&#064;GET
	 * 	List&lt;T&gt; get();
	 * }
	 * </pre>
	 * 
	 * @param clazz
	 * @param method
	 *            interface method
	 * @return
	 */
	public static Type[] getGenericParameterTypesOfGenericInterfaceMethod(Class clazz, Method method) {
		if (!method.getDeclaringClass().isInterface()) {
			return method.getGenericParameterTypes();
		}

		try {
			Method tmp = clazz.getMethod(method.getName(), method.getParameterTypes());
			return tmp.getGenericParameterTypes();
		} catch (NoSuchMethodException e) {

		}
		return method.getGenericParameterTypes();
	}

	public static Class<?> getRawType(Type type) {
		if (type instanceof Class<?>) {
			// type is a normal class.
			return (Class<?>) type;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			return (Class<?>) rawType;
		} else if (type instanceof GenericArrayType) {
			final GenericArrayType genericArrayType = (GenericArrayType) type;
			final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
			return Array.newInstance(componentRawType, 0).getClass();
		} else if (type instanceof TypeVariable) {
			final TypeVariable typeVar = (TypeVariable) type;
			if ((typeVar.getBounds() != null) && (typeVar.getBounds().length > 0)) {
				return getRawType(typeVar.getBounds()[0]);
			}
		}
		throw new RuntimeException("Unable to determine base class from Type");
	}

	public static Class<?> getRawTypeNoException(Type type) {
		if (type instanceof Class<?>) {
			// type is a normal class.
			return (Class<?>) type;

		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			return (Class<?>) rawType;
		} else if (type instanceof GenericArrayType) {
			final GenericArrayType genericArrayType = (GenericArrayType) type;
			final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
			return Array.newInstance(componentRawType, 0).getClass();
		}
		return null;
	}

	/**
	 * Returns the type argument from a parameterized type
	 * 
	 * @param genericType
	 * @return null if there is no type parameter
	 */
	public static Class<?> getTypeArgument(Type genericType) {
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType parameterizedType = (ParameterizedType) genericType;
		Class<?> typeArg = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		return typeArg;
	}

	public static class TypeInfo {
		private Class<?> type;
		private Type genericType;

		public TypeInfo(Class<?> type, Type genericType) {
			this.type = type;
			this.genericType = genericType;
		}

		public Class<?> getType() {
			return type;
		}

		public Type getGenericType() {
			return genericType;
		}
	}

	public static Class getCollectionBaseType(Class type, Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type componentGenericType = parameterizedType.getActualTypeArguments()[0];
			return getRawType(componentGenericType);
		} else if (genericType instanceof GenericArrayType) {
			final GenericArrayType genericArrayType = (GenericArrayType) genericType;
			Type componentGenericType = genericArrayType.getGenericComponentType();
			return getRawType(componentGenericType);
		} else if (type.isArray()) {
			return type.getComponentType();
		}
		return null;
	}

	public static Class getMapKeyType(Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type componentGenericType = parameterizedType.getActualTypeArguments()[0];
			return getRawType(componentGenericType);
		}
		return null;
	}

	public static Class getMapValueType(Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type componentGenericType = parameterizedType.getActualTypeArguments()[1];
			return getRawType(componentGenericType);
		}
		return null;
	}

	/**
	 * Finds an actual value of a type variable. The method looks in a class
	 * hierarchy for a class defining the variable and returns the value if
	 * present.
	 * 
	 * @param clazz
	 * @param typevariable
	 * @return actual type of the type variable
	 */
	public static Type getActualValueOfTypevariable(Class<?> clazz, TypeVariable<?> typevariable) {
		if (typevariable.getGenericDeclaration() instanceof Class<?>) {
			Class<?> classDeclaringTypevariable = (Class<?>) typevariable.getGenericDeclaration();

			// find the generic version of classDeclaringTypevariable

			Type fromInterface = getTypeVariableViaGenericInterface(clazz, classDeclaringTypevariable, typevariable);
			if (fromInterface != null) {
				return fromInterface;
			}

			while (clazz.getSuperclass() != null) {
				if (clazz.getSuperclass().equals(classDeclaringTypevariable)) {
					// found it
					ParameterizedType parameterizedSuperclass = (ParameterizedType) clazz.getGenericSuperclass();

					for (int i = 0; i < classDeclaringTypevariable.getTypeParameters().length; i++) {
						TypeVariable<?> tv = classDeclaringTypevariable.getTypeParameters()[i];
						if (tv.equals(typevariable)) {
							return parameterizedSuperclass.getActualTypeArguments()[i];
						}
					}
				}

				clazz = clazz.getSuperclass();
			}
		}

		throw new RuntimeException("Unable to determine value of type parameter " + typevariable);
	}

	public static void setField(String fieldName, Object classObj, Object value) {
		try {

			Field field = null;
			try {
				field = classObj.getClass().getField(fieldName);
			} catch (NoSuchFieldException e) {
				Field[] fields = ClassUtil.getAllFields(classObj.getClass(), Object.class);
				for (Field f : fields) {
					if (f.getName().equalsIgnoreCase(fieldName)) {
						field = f;
						break;
					}
				}
			}

			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(classObj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Type getTypeVariableViaGenericInterface(Class<?> clazz, Class<?> classDeclaringTypevariable,
			TypeVariable<?> typevariable) {
		for (Type genericInterface : clazz.getGenericInterfaces()) {

			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;

				for (int i = 0; i < classDeclaringTypevariable.getTypeParameters().length; i++) {
					TypeVariable<?> tv = classDeclaringTypevariable.getTypeParameters()[i];
					if (tv.equals(typevariable)) {
						return parameterizedType.getActualTypeArguments()[i];
					}
				}
			} else if (genericInterface instanceof Class) {
				return getTypeVariableViaGenericInterface((Class<?>) genericInterface, classDeclaringTypevariable,
						typevariable);
			}
		}
		return null;
	}

	private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<Class<?>>(
			Arrays.asList(new Class<?>[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
					Long.class, Float.class, Double.class, Void.class, String.class }));

	/**
	 * @param clazz
	 * @return return true if it is any of the wrapper class (i.e. Double, Long)
	 *         or String or Void
	 */
	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	public static boolean isPrimitiveOrWrapperType(Class<?> clazz) {
		return clazz.isPrimitive() || isWrapperType(clazz);

	}

}
