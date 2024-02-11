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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * com.qmetry.qaf.automation.testng.report.MethodOverview.java
 * 
 * @author chirag
 */
public class MethodInfo {
	private int index;
	private Integer retryCount;

	private String type;
	private Object[] args = {};
	private Map<String, Object> metaData;
	private String[] dependsOn = {};
	private String doc;
	private long startTime;

	private long duration;
	private String result;
	private float passPer;

	/**
	 * @return the name
	 */
	public String getName() {
		return metaData.get("name").toString();
	}

	/**
	 * @param lineNo
	 *            the lineNo to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * @return the lineNo
	 */
	public int getIndex() {
		return index;
	}

	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		// FIXME: quick fix for Method object serialization error
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Method) {
					args[i] = ((Method) args[i]).toGenericString();
				}
			}
		}
		
		this.args = args;
	}

	/**
	 * @return the groups
	 */
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	/**
	 * @param metadata
	 *            the groups to set
	 */
	public void setMetaData(Map<String, Object> metadata) {
		this.metaData = metadata;
	}

	public void setDependsOn(String[] dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String[] getDependsOn() {
		return dependsOn;
	}

	/**
	 * @return the doc
	 */
	public String getDoc() {
		return doc;
	}

	/**
	 * @param doc
	 *            the doc to set
	 */
	public void setDoc(String doc) {
		this.doc = doc;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @param passPer
	 *            the passPer to set
	 */
	public void setPassPer(float passPer) {
		this.passPer = passPer;
	}

	/**
	 * @return the passPer
	 */
	public float getPassPer() {
		return passPer;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof MethodInfo)) {
			return false;
		}
		MethodInfo another = (MethodInfo) obj;
		return metaData.get("name").toString().equalsIgnoreCase(another.metaData.get("name").toString())
				&& (index == another.index) && (startTime == another.startTime) && Arrays.deepEquals(args, another.args);
	}

}
