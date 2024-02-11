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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.Reporter;

/**
 * com.qmetry.qaf.automation.step.JavaStepReporter.java
 * 
 * @author chirag
 */
@Aspect
public class JavaStepReporter {

	@Around("execution(@QAFTestStep * *.*(..))")
	public Object javaTestStep(ProceedingJoinPoint jp,
			JoinPoint.StaticPart thisJoinPointStaticPart) throws Throwable {
		JavaStep testStep = null;
		Signature sig = null;

		try {
			sig = thisJoinPointStaticPart.getSignature();
			if ((sig instanceof MethodSignature) && TestBaseProvider.instance().get()
					.getContext().getBoolean(JavaStep.ATTACH_LISTENER, true)) {
				// this must be a call or execution join point
				Method method = ((MethodSignature) sig).getMethod();

				testStep = new MockJavaStep(method, jp);
				if (null != jp.getArgs()) {
					testStep.setActualArgs(jp.getArgs());
				}
			}
		} catch (Exception e) {
			// ignore it...
		}

		if (ConfigurationManager.getBundle().getBoolean("method.recording.mode", false)) {
			ConfigurationManager.getBundle().setProperty("method.param.names",
					((MethodSignature) sig).getParameterNames());
			return null;
		} else {
			// unblock for sub-steps
			TestBaseProvider.instance().get().getContext()
					.setProperty(JavaStep.ATTACH_LISTENER, true);
			if (null != testStep) {
				try {
					return testStep.execute();
				} catch (JPThrowable e) {
					throw e.getCause();
				}
			} else {

				// this call is from text client (bdd/kwd/excel)
				testStep = (JavaStep) TestBaseProvider.instance().get().getContext()
						.getProperty("current.teststep");
				testStep.setFileName(jp.getSourceLocation().getFileName());
				testStep.setLineNumber(jp.getSourceLocation().getLine());
				testStep.signature = jp.getSignature().toLongString();

				return jp.proceed();

			}

		}

	}

	private class JPThrowable extends Error {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3472971476885434113L;

		public JPThrowable(Throwable cause) {
			super(cause);
		}
	}

	class MockJavaStep extends JavaStep {
		private final ProceedingJoinPoint jp;

		public MockJavaStep(Method method, ProceedingJoinPoint jp) {
			super(method);
			this.jp = jp;
			setFileName(jp.getSourceLocation().getFileName());
			setLineNumber(jp.getSourceLocation().getLine());
			signature = jp.getSignature().toLongString();
		}

		@Override
		protected Object doExecute() {
			try {
				if (ApplicationProperties.DRY_RUN_MODE.getBoolenVal(false)){
					Reporter.log(getDescription(), MessageTypes.TestStepPass);
					return true;
				}else 
					return jp.proceed();

			} catch (InvocationTargetException ite) {
				throw new JPThrowable(ite);
			} catch (Throwable t) {

				throw new JPThrowable(t);
			}
		}
	}
}
