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
package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.ClassUtil.getAnnotation;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONException;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.data.MetaData;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.JSONUtil;

/**
 * com.qmetry.qaf.automation.step.JavaStep.java
 * 
 * @author chirag.jayswal
 */
@XmlRootElement
public class JavaStep extends BaseTestStep {
	/**
	 * For internal use only.
	 */
	public static final String ATTACH_LISTENER = "attach.javastep.listener";
	protected transient Method method;
	private Object stepProvider;
	// package access
	String signature = "";
	private boolean qafStepImpl = true;

	public JavaStep(Method method) {
		this(method, "", "");
	}

	public JavaStep(Method method, String name, String description) {
		this.method = method;
		this.name = name;
		this.description = description;
		init();
	}

	public boolean isQafStepImpl() {
		return qafStepImpl;
	}

	private void init() {
		fileName = method.getDeclaringClass().getName();
		MetaData stepMetaData = getAnnotation(method, MetaData.class);
		MetaData classMetaData = getAnnotation(method.getDeclaringClass(), MetaData.class);
		QAFTestStep step = getAnnotation(method, QAFTestStep.class);

		if (null != classMetaData && isNotBlank(classMetaData.value())) {
			try {
				metaData = JSONUtil.toMap(classMetaData.value());
			} catch (JSONException e) {
				System.err.println(metaData + " is not valid json map for step meta-data");
			}
		}

		setMetaData();
		if (null != stepMetaData && isNotBlank(stepMetaData.value())) {
			try {
				// keep class meta-data which is not in step meta-data, override
				// common
				metaData.putAll(JSONUtil.toMap(stepMetaData.value()));
			} catch (JSONException e) {
				System.err.println(metaData + " is not valid json map for step meta-data");
			}
		}

		if (isBlank(name)) {
			QAFTestStepProvider provider = method.getDeclaringClass().getAnnotation(QAFTestStepProvider.class);

			String prefix = (provider != null) && isNotBlank(provider.prefix()) ? provider.prefix() + "." : "";
			name = prefix + ((step != null) && isNotBlank(step.stepName()) ? step.stepName() : method.getName());

		}
		if (step != null) {
			threshold = step.threshold();
			if (isNotBlank(step.description())) {
				// highest priority to QAFTestStep annotation if multiple step
				// definition way opted
				description = step.description();
				qafStepImpl = true;
			}
		}
		if (isBlank(description)) {
			description = name;
		}
		stepMatcher = BDDStepMatcherFactory.getStepMatcher(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.step.TestStep#execute(java.lang.Object[])
	 */
	@Override
	protected Object doExecute() {
		try {
			Object stepProvider = null;
			try {
				stepProvider = getStepProvider();
			} catch (Exception e) {
				if(!Modifier.isStatic(method.getModifiers())){
					throw new StepInvocationException(this,
							"Unable to Instantiate JavaStep: " + getName() + Arrays.toString(actualArgs) + getSignature(),
							true);
				}
			}
			// block joint-point listener
			TestBaseProvider.instance().get().getContext().setProperty(ATTACH_LISTENER, false);
			TestBaseProvider.instance().get().getContext().setProperty("current.teststep", this);
			method.setAccessible(true);
			Object[] args = processArgs(method, actualArgs);
			return method.invoke(stepProvider, args);
		} catch (IllegalArgumentException e) {
			throw new StepInvocationException(this, "Unable to invoke JavaStep with given arguments: " + getName()
					+ Arrays.toString(actualArgs) + "\nat " + getSignature(), true);

		} catch (IllegalAccessException e) {
			throw new StepInvocationException(this,
					"Unable to invoke JavaStep: " + getName() + Arrays.toString(actualArgs) + "\nat " + getSignature(),
					true);

		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Error) {
				throw (Error) e.getCause();
			}
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw new StepInvocationException(this, e.getCause());
		}
	}

	protected Object getStepProvider()
			throws Exception {
		return stepProvider == null ? ObjectFactory.INSTANCE.getObject(method.getDeclaringClass()) : stepProvider;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@SuppressWarnings("unchecked")
	protected Object[] processArgs(Method method, Object... objects) {
		int noOfParams = method.getParameterTypes().length;
		if (noOfParams == 0) {
			return null;
		}
		Object[] params = new Object[noOfParams];
		Map<String, Object> context = getStepExecutionTracker().getContext();

		try {
			if ((noOfParams == (objects.length - 1)) && method.getParameterTypes()[noOfParams - 1].isArray()) {
				// case of optional arguments!...
				System.arraycopy(objects, 0, params, 0, objects.length);
				params[noOfParams - 1] = "[]";
			} else {
				System.arraycopy(objects, 0, params, 0, noOfParams);
			}
		} catch (Exception e) {
			throw new RuntimeException("Wrong number of parameters, Expected " + noOfParams
					+ " parameters but Actual is " + (objects == null ? "0" : objects.length));
		}


		description = StrSubstitutor.replace(description, context);
		description = getBundle().getSubstitutor().replace(description);
		Annotation[][] paramsAnnotations = method.getParameterAnnotations();
		context.put("__method", method);
		QAFTestStepArgumentFormatter<Object> defaultFormatter = new QAFTestStepArgumentFormatterImpl();
		for (int i = 0; i < noOfParams; i++) {
			Class<?> paramType = method.getParameterTypes()[i];
			context.put("__paramType", paramType);
			context.put("__paramIndex", i);

			Annotation[] paramAnnotations = paramsAnnotations[i];
			Class<QAFTestStepArgumentFormatter<?>> formatter = null;
			for (Annotation paramAnnotation : paramAnnotations) {
				if (paramAnnotation instanceof Formatter) {
					formatter = (Class<QAFTestStepArgumentFormatter<?>>) ((Formatter) paramAnnotation).value();
				}
			}
			if (null != formatter) {
				try {
					params[i] = formatter.newInstance().format(params[i], context);
					continue;
				} catch (InstantiationException e) {
					throw new AutomationError("Unable to use formatter " + formatter , e);
				} catch (IllegalAccessException e) {
					throw new AutomationError("Unable to use formatter " + formatter , e);
				}
			}else{
				params[i]=defaultFormatter.format(params[i], context);
			}

		}
		return params;
	}

	@Override
	public TestStep clone() {
		JavaStep cloneObj = new JavaStep(method);
		if (null != actualArgs) {
			cloneObj.actualArgs = actualArgs.clone();
		}
		setStepMatcher(getStepMatcher());
		return cloneObj;
	}

	public void getSubSteps() {
		if (method.getReturnType().isInstance(QAFWebElement.class)) {

		}
	}
	
	private void setMetaData() {

		Annotation[] allAnnotations = method.getAnnotations();
		for (Annotation annotation : allAnnotations) {
			if (annotation instanceof MetaData || annotation instanceof QAFTestStep)
				continue;

			Method[] annotationMethods = annotation.annotationType().getDeclaredMethods();
			for (Method annotationMethod : annotationMethods) {
				Object objVal;
				try {
					objVal = annotationMethod.invoke(annotation);
					String key = annotationMethod.getName();
					metaData.put(key, objVal);
					if (key.equalsIgnoreCase("value") && isTestStepAnnotation(annotation)) {
						description = (String) objVal;
						qafStepImpl = false;

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isTestStepAnnotation(Annotation annotation) {
		List<String> annotationPkgs = getBundle().getList("step.annotation.pkgs", Arrays.asList("cucumber.api.java","io.cucumber.java"));

		for (String pkg : annotationPkgs) {
			if (annotation.annotationType().getName().indexOf(pkg) >= 0) {
				return true;
			}
		}
		return false;
	}

}
