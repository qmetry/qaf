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

import java.util.Collection;

import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.ui.selenium.HtmlCheckpointResultFormatter.java
 * 
 * @author chirag
 */
public class HtmlCheckpointResultFormatter implements CheckpointResultFormatter {
	private static final String SEC_Header = "<div style=\"display:block;margin-left:10px;\">";
	private static final String SEC_Footer = "</div>";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.qmetry.qaf.automation.ui.selenium.CheckpointResultFormatter#
	 * getResults (java.util.Collection)
	 */
	@Override
	public String getResults(Collection<CheckpointResultBean> bean) {
		StringBuilder sb = new StringBuilder(SEC_Header);
		for (CheckpointResultBean checkpointResultBean : bean) {
			String msg = checkpointResultBean.getMessage();
			if (StringUtil.isNotBlank(checkpointResultBean.getScreenshot())) {
				msg = msg + formatScreenshot(checkpointResultBean.getScreenshot());
			}
			sb.append(MessageTypes.valueOf(checkpointResultBean.getType()).formatMessage(msg));
			if (!checkpointResultBean.getSubCheckPoints().isEmpty()) {
				sb.append(getResults(checkpointResultBean.getSubCheckPoints()));
			}
		}
		sb.append(SEC_Footer);
		return sb.toString();
	}

	private String formatScreenshot(String sc) {
		return " <a href=\"" + sc + "\" target=\"_blank\">[View Screenshot]</a> ";
	}
}
