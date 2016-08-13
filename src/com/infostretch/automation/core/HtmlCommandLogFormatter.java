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


package com.infostretch.automation.core;

import java.text.MessageFormat;
import java.util.Collection;

/**
 * com.infostretch.automation.ui.selenium.HtmlCommandLogFormatter.java
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
	 * @seecom.infostretch.automation.selenium.CommandLogFormatter#getLog(com.
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
