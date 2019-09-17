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

import java.text.MessageFormat;
import java.util.Collection;

/**
 * com.qmetry.qaf.automation.ui.selenium.HtmlCommandLogFormatter.java
 * 
 * @author chirag
 */
public class HtmlCommandLogFormatter implements CommandLogFormatter {
	private static final String HEADER = "<table style=\"width:100%\"><tbody><tr>" + "<td><b>Selenium-Command</b></td>"
			+ "<td><b>Parameter-1</b></td>" + "<td><b>Parameter-2</b></td>" + "<td><b>Res.RC</b></td>" + "</tr>";

	private static final String FOOTER = "</tbody></table>";
	private static final String COMMAND_LOG_FORMAT = "<tr>" + "<td>{0}</td>" + "<td>{1}</td>" + "<td>{2}</td>"
			+ "<td>{3}</td>" + "</tr>";

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.qmetry.qaf.automation.selenium.CommandLogFormatter#getLog(com.
	 * infostretch.automation.selenium.LoggingBean[])
	 */
	@Override
	public String getLog(Collection<LoggingBean> collection) {
		if ((collection == null) || (collection.isEmpty())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(HEADER);
		for (LoggingBean b : collection) {
			String param1 = quoteHtml(b.getArgs()[0]);
			String param2 = b.getArgs().length > 1 ? quoteHtml(b.getArgs()[1]) : "";
			String result = b.getResult();
			int warningIndex = result == null ? 0 : result.toUpperCase().indexOf("(WARNING");
			sb.append(MessageFormat.format(COMMAND_LOG_FORMAT, b.getCommandName(), param1, param2,
					warningIndex > 0 ? result.substring(0, warningIndex) + "[WARNING(s) retrurned!]" : result));
		}
		sb.append(FOOTER);
		return sb.toString();
	}

	public static final String quoteHtml(String unquoted) {
		String quoted = unquoted == null ? "" : unquoted;
		quoted = quoted.replace("&", "&amp;");
		quoted = quoted.replace("<", "&lt;");
		quoted = quoted.replace(">", "&gt;");
		return quoted;
	}
}
