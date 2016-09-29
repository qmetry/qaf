/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.step;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.util.ClassUtil.getAnnotation;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		MetaData classMetaData =
				getAnnotation(method.getDeclaringClass(), MetaData.class);
		QAFTestStep step = getAnnotation(method, QAFTestStep.class);

		if (null != classMetaData && isNotBlank(classMetaData.value())) {
			try {
				metaData = JSONUtil.toMap(classMetaData.value());
			} catch (JSONException e) {
				System.err
						.println(metaData + " is not valid json map for step meta-data");
			}
		}

		setMetaData();
		if (null != stepMetaData && isNotBlank(stepMetaData.value())) {
			try {
				// keep class meta-data which is not in step meta-data, override
				// common
				metaData.putAll(JSONUtil.toMap(stepMetaData.value()));
			} catch (JSONException e) {
				System.err
						.println(metaData + " is not valid json map for step meta-data");
			}
		}

		if (isBlank(name)) {
			QAFTestStepProvider provider =
					method.getDeclaringClass().getAnnotation(QAFTestStepProvider.class);

			String prefix = (provider != null) && isNotBlank(provider.prefix())
					? provider.prefix() + "." : "";
			name = prefix + ((step != null) && isNotBlank(step.stepName())
					? step.stepName() : method.getName());

		}
		if (isBlank(description)) {
			description = (step != null) && isNotBlank(step.description())
					? step.description() : name;
		}
		if (step != null) {
			threshold = step.threshold();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.qmetry.qaf.automation.step.TestStep#execute(java.lang.Object[])
	 */
	@Override
	protected Object doExecute() {
		try {
			Object stepProvider = getStepProvider();
			// block joint-point listener
			TestBaseProvider.instance().get().getContext().setProperty(ATTACH_LISTENER,
					false);
			TestBaseProvider.instance().get().getContext().setProperty("current.teststep",
					this);
			method.setAccessible(true);
			return method.invoke(stepProvider, processArgs(method, actualArgs));
		} catch (IllegalArgumentException e) {
			throw new StepInvocationException(this,
					"Unable to invoke JavaStep with given arguments: " + getName()
							+ Arrays.toString(actualArgs) + "\nat " + getSignature(),
					true);

		} catch (IllegalAccessException e) {
			throw new StepInvocationException(this,
					"Unable to invoke JavaStep: " + getName()
							+ Arrays.toString(actualArgs) + "\nat " + getSignature(),
					true);

		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Error) {
				throw (Error) e.getCause();
			}
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw new StepInvocationException(this, e.getCause());
		} catch (InstantiationException e) {
			throw new StepInvocationException(this, "Unable to Instantiate JavaStep: "
					+ getName() + Arrays.toString(actualArgs) + getSignature(), true);
		}
	}

	protected Object getStepProvider()
			throws InstantiationException, IllegalAccessException {

		return stepProvider == null ? getClassInstance() : stepProvider;
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

	protected Object[] processArgs(Method method, Object... objects) {
		int noOfParams = method.getParameterTypes().length;
		if (noOfParams == 0) {
			return null;
		}
		Object[] params = new Object[noOfParams];
		Map<String, Object> context = getStepExecutionTracker().getContext();

		try {
			if ((noOfParams == (objects.length - 1))
					&& method.getParameterTypes()[noOfParams - 1].isArray()) {
				// case of optional arguments!...
				System.arraycopy(objects, 0, params, 0, objects.length);
				params[noOfParams - 1] = "[]";
			} else {
				System.arraycopy(objects, 0, params, 0, noOfParams);
			}
		} catch (Exception e) {
			throw new RuntimeException("Wrong number of parameters, Expected "
					+ noOfParams + " parameters but Actual is "
					+ (objects == null ? "0" : objects.length));
		}
		Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
		description = StrSubstitutor.replace(description, context);
		description = getBundle().getSubstitutor().replace(description);
		for (int i = 0; i < noOfParams; i++) {

			if ((params[i] instanceof String)) {
				String pstr = (String) params[i];

				if (pstr.startsWith("${") && pstr.endsWith("}")) {
					String pname = pstr.substring(2, pstr.length() - 1);
					params[i] = context.containsKey(pstr) ? context.get(pstr)
							: context.containsKey(pname) ? context.get(pname)
									: getBundle().containsKey(pstr)
											? getBundle().getObject(pstr) : getBundle()
													.getObject(pname);
				} else if (pstr.indexOf("$") >= 0) {
					pstr = getBundle().getSubstitutor().replace(pstr);
					params[i] = StrSubstitutor.replace(pstr, context);
				}

			}
			Class<?> paramType = method.getParameterTypes()[i];
			if (String.class.isAssignableFrom(paramType)) {
				continue;
			}
			try {
				String strVal = gson.toJson(params[i]);
				if (params[i] instanceof String) {
					strVal = String.valueOf(params[i]);
				}

				strVal = getBundle().getSubstitutor().replace(strVal);
				strVal = StrSubstitutor.replace(strVal, context);
				Object o = gson.fromJson(strVal, paramType);
				params[i] = o;
			} catch (Exception e) {
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
		return cloneObj;
	}

	public void getSubSteps() {
		if (method.getReturnType().isInstance(QAFWebElement.class)) {

		}
	}

	private Object getClassInstance()
			throws InstantiationException, IllegalAccessException {
		Class<?> cls = method.getDeclaringClass();
		if (getBundle().getBoolean("step.provider.sharedinstance", true)) {
			// allow class variable sharing among  steps
			Object obj = getBundle().getObject(cls.getName());
			if (null == obj) {
				obj = cls.newInstance();
				getBundle().setProperty(cls.getName(), obj);
				return obj;
			}
		}
		return cls.newInstance();
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
					if (key.equalsIgnoreCase("value")
							&& isTestStepAnnotation(annotation)) {
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
	private boolean isTestStepAnnotation(Annotation annotation){
		List<String> annotationPkgs = getBundle().getList("step.annotation.pkgs",Arrays.asList("cucumber.api.java"));
		
		for(String pkg: annotationPkgs){
			if(annotation.annotationType().getName().indexOf(pkg) >= 0){
				return true;
			}
		}
		return false;
	}
}
