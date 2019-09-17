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
package com.qmetry.qaf.automation.testng.report;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.LoggingBean;

/**
 * { "name":"m1", "groups":"", "desc":"", "doc":"", "startTime":1, "endTime":1,
 * "result":"pass", "seleniumLog":"" }
 * com.qmetry.qaf.automation.testng.report.Method.java
 * 
 * @author chirag.jayswal
 */
public class MethodResult {

	private List<LoggingBean> seleniumLog;
	private List<CheckpointResultBean> checkPoints;
	private String errorTrace;

	/**
	 * @return the seleniumLog
	 */
	public List<LoggingBean> getSeleniumLog() {
		return seleniumLog;
	}

	/**
	 * @param list
	 *            the seleniumLog to set
	 */
	public void setSeleniumLog(List<LoggingBean> list) {
		seleniumLog = list;
	}

	/**
	 * @return the checkPoints
	 */
	public List<CheckpointResultBean> getCheckPoints() {
		return checkPoints;
	}

	/**
	 * @param list
	 *            the checkPoints to set
	 */
	public void setCheckPoints(List<CheckpointResultBean> list) {
		checkPoints = list;
	}

	/**
	 * @return the errorTrace
	 */
	public String getErrorTrace() {
		return errorTrace;
	}

	/**
	 * @param errorTrace
	 *            the errorTrace to set
	 */
	public void setErrorTrace(String errorTrace) {
		this.errorTrace = errorTrace;
	}

	/**
	 * @param throwable
	 *            from which the errorTrace to set
	 */
	public void setThrowable(Throwable throwable) {
		if (null == throwable)
			return;
		ByteArrayOutputStream sw = new ByteArrayOutputStream();
		PrintStream pw = new PrintStream(sw, true);
		throwable.printStackTrace(pw);
		setErrorTrace(new String(sw.toByteArray()));
	}
}
