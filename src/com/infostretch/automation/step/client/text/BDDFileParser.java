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

package com.infostretch.automation.step.client.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostretch.automation.core.AutomationError;
import com.infostretch.automation.step.client.AbstractScenarioFileParser;
import com.infostretch.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class BDDFileParser extends AbstractScenarioFileParser {

	private final static Log logger = LogFactory.getLog(BehaviorScanner.class);
	private static final String LINE_BREAK = "_&";
	private static final String COMMENT_CHARS = "#!";

	@Override
	protected Collection<Object[]> parseFile(String strFile) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		File textFile;
		int lineNo = 0;

		BufferedReader br = null;
		try {

			logger.info("loading BDD file: " + strFile);
			textFile = new File(strFile);
			br = new BufferedReader(new FileReader(textFile));
			String strLine = "";
			// file line by line
			// exclude blank lines and comments
			StringBuffer currLineBuffer = new StringBuffer();
			while ((strLine = br.readLine()) != null) {
				// record line number
				lineNo++;
				/**
				 * ignore if line is empty or comment line
				 */
				if (!("".equalsIgnoreCase(strLine.trim()) || COMMENT_CHARS.contains("" + strLine.trim().charAt(0)))) {
					currLineBuffer.append((strLine.trim()));

					if (strLine.endsWith(LINE_BREAK)) {
						/*
						 * Statement not completed. Remove line break character
						 * and continue statement reading from next line
						 */
						currLineBuffer.delete(currLineBuffer.length() - LINE_BREAK.length(), currLineBuffer.length());

					} else {
						// process single statement
						Object[] cols = new Object[] { "", "", "", lineNo };
						String currLine = currLineBuffer.toString();
						if ((StringUtil.indexOfIgnoreCase(currLine, SCENARIO) == 0)
								|| (StringUtil.indexOfIgnoreCase(currLine, STEP_DEF) == 0)
								|| (StringUtil.indexOfIgnoreCase(currLine, "META") == 0)) {

							System.arraycopy(currLine.split(":", 2), 0, cols, 0, 2);

							// append meta-data in last/scenario statement
							if (StringUtil.indexOfIgnoreCase(((String) cols[0]).trim(), "META") == 0) {
								Object[] row = new Object[4];
								int prevIndex = rows.size() - 1;

								Object[] prevRow = rows.remove(prevIndex);

								System.arraycopy(prevRow, 0, row, 0, 2);
								row[2] = ((String) cols[1]).trim();
								row[3] = prevRow[3];
								rows.add(row);
								currLineBuffer = new StringBuffer();
								continue;
							}
						} else {
							// this is a statement
							cols[0] = currLine;
						}
						rows.add(cols);
						currLineBuffer = new StringBuffer();

					}
				}
			}
		} catch (Exception e) {
			String strMsg = "Exception while reading BDD file: " + strFile + "#" + lineNo;
			logger.error(strMsg + e);
			throw new AutomationError(strMsg, e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return rows;
	}

}
