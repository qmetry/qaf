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
