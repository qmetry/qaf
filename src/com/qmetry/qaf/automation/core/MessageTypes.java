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

import com.qmetry.qaf.automation.keys.ApplicationProperties;

public enum MessageTypes {
	Info, Pass, Warn, Fail, TestStep, TestStepPass, TestStepFail;
	private static final boolean REPORT_SUCCESS = !ApplicationProperties.REPORT_SKIP_SUCCESS.getBoolenVal(false);
	private static final MessageTypes REPORT_LOG_LEVEL = getLogLevel();
	private Boolean shouldReport = null;
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
	
	public boolean shouldReport() {
		if (null == shouldReport) {
			shouldReport = REPORT_LOG_LEVEL.ordinal() >= ordinal()
					&& (!this.equals(MessageTypes.Pass) || REPORT_SUCCESS);
		}
		return shouldReport;
	}
	private static MessageTypes getLogLevel() {
		try {
			return MessageTypes.valueOf(ApplicationProperties.REPORT_LOG_LEVEL.getStringVal("Info"));
		} catch (Exception e) {
			System.err.println("Invalid log level value for report.log.level. It can be one of \"Info\", \"Pass\", \"Fail\".\nUsing default \"Info\".");
			return MessageTypes.Info;
		}
	}
}
