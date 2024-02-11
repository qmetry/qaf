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
package com.qmetry.qaf.automation.core;

import java.util.ArrayList;
import java.util.List;

/**
 * com.qmetry.qaf.automation.ui.selenium.StepResultBean.java
 * 
 * @author chirag
 */
public class CheckpointResultBean {
	private String message;
	private String type;
	private String screenshot;
	private int duration;
	private int threshold;
	private List<CheckpointResultBean> subCheckPoints;

	public CheckpointResultBean() {
		subCheckPoints = new ArrayList<CheckpointResultBean>();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public void setType(MessageTypes type) {
		this.type = type.name();
	}

	/**
	 * @return the screenshot
	 */
	public String getScreenshot() {
		return screenshot;
	}

	/**
	 * @param screenshot
	 *            the screenshot to set
	 */
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public List<CheckpointResultBean> getSubCheckPoints() {
		return subCheckPoints;
	}

	public void setSubCheckPoints(List<CheckpointResultBean> subCheckPoints) {
		this.subCheckPoints = subCheckPoints;
	}

	@Override
	public boolean equals(Object other) {
		if (null == other) {
			return false;
		}
		if (other instanceof String) {
			return ((String) other).equalsIgnoreCase(getMessage());
		}
		if (other instanceof CheckpointResultBean) {
			CheckpointResultBean o2 = (CheckpointResultBean) other;
			return (getMessage() == null ? o2.getMessage() == null : getMessage().equalsIgnoreCase(o2.getMessage()))
					&& (getType() == null ? o2.getType() == null : getType().equals(o2.getType()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}
}
