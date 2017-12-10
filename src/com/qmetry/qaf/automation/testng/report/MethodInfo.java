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


package com.qmetry.qaf.automation.testng.report;

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
