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

public enum MessageTypes {
	Pass, Fail, Info, TestStep, TestStepPass, TestStepFail, Warn;

	/**
	 * Get message in HTML format for the type.
	 * 
	 * @param message
	 *            : message to be formated
	 * @return HTML formated message for the type
	 */
	public String formatMessage(String message) {
		return String.format(MSG_FORMAT, name().toLowerCase(), name().toLowerCase(), name().charAt(0), message);
	}

	/**
	 * Get message in text format for this type.
	 * 
	 * @param message
	 *            : message to be formated
	 * @return Text formated message for the type
	 */
	public String formatText(String message) {
		return String.format("%s: %s", name(), message);

	}

	private static final String MSG_FORMAT = "<div  class=\"%s\"><span class=\"%s-label\">%s </span>%s</div>";

	public boolean isFailure() {
		return name().toUpperCase().contains("FAIL");
	}

}
