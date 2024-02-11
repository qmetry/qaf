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
package com.qmetry.qaf.automation.report;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * com.qmetry.qaf.automation.testng.report.Overview.java
 * 
 * @author chirag
 */
public class TestOverview {
	private int total;
	private int pass;
	private int fail;
	private int skip;
	private Set<String> classes;
	private Map<String, Object> envInfo;
	private long startTime;
	private long endTime;

	public TestOverview() {
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	/**
	 * @param classes
	 *            the classes to set
	 */
	public void setClasses(Set<String> classes) {
		this.classes = classes;
	}

	/**
	 * @return the classes
	 */
	public Set<String> getClasses() {
		if (classes == null) {
			classes = Collections.synchronizedSet(new LinkedHashSet<String>());
		}
		return classes;
	}

	/**
	 * @param envInfo
	 *            the envInfo to set
	 */
	public void setEnvInfo(Map<String, Object> envInfo) {
		this.envInfo = envInfo;
	}

	/**
	 * @return the envInfo
	 */
	public Map<String, Object> getEnvInfo() {
		return envInfo;
	}

	/**
	 * @return the startTime
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
