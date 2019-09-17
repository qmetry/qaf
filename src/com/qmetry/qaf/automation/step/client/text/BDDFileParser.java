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
package com.qmetry.qaf.automation.step.client.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.step.client.AbstractScenarioFileParser;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 */
public class BDDFileParser extends AbstractScenarioFileParser {

	private final static Log logger = LogFactory.getLog(BehaviorScanner.class);
	private static final String LINE_BREAK = "_&";
	private static final String COMMENT_CHARS = "#!";
	private static final String BACKGROUND = "Background";
	private static final String MULTI_LINE_COMMENT = "\"\"\"";

	@Override
	protected Collection<Object[]> parseFile(String strFile) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		ArrayList<Object[]> background = new ArrayList<Object[]>();

		File textFile;
		int lineNo = 0;
		int lastScenarioIndex=-1;

		BufferedReader br = null;
		try {

			logger.info("loading BDD file: " + strFile);
			textFile = new File(strFile);
			br = new BufferedReader(new FileReader(textFile));
			String strLine = "";
			boolean bIsBackground = false;
			// file line by line
			// exclude blank lines and comments
			StringBuffer currLineBuffer = new StringBuffer();
			while ((strLine = br.readLine()) != null) {
				// record line number
				lineNo++;
				/**
				 * ignore if line is empty or comment line
				 */
				if (!("".equalsIgnoreCase(strLine.trim())
						|| COMMENT_CHARS.contains("" + strLine.trim().charAt(0)))) {
					currLineBuffer.append((strLine.trim()));

				if (strLine.endsWith(LINE_BREAK)) {
						/*
						 * Statement not completed. Remove line break character
						 * and continue statement reading from next line
						 */
						currLineBuffer.delete(
								currLineBuffer.length() - LINE_BREAK.length(),
								currLineBuffer.length());

					} else {
						// process single statement
						Object[] cols = new Object[]{"", "", "", lineNo};
						String currLine = currLineBuffer.toString();
						if ((StringUtil.indexOfIgnoreCase(currLine, SCENARIO) == 0)
								|| (StringUtil.indexOfIgnoreCase(currLine,
										BACKGROUND) == 0)
								|| (StringUtil.indexOfIgnoreCase(currLine, STEP_DEF) == 0)
								|| (StringUtil.indexOfIgnoreCase(currLine,
										"META") == 0)) {

							System.arraycopy(currLine.split(":", 2), 0, cols, 0, 2);

							// append meta-data in last/scenario statement
							if (StringUtil.indexOfIgnoreCase(((String) cols[0]).trim(),
									"META") == 0) {
//								Object[] row = new Object[4];
//								int prevIndex = rows.size() - 1;

								Object[] prevRow = rows.get(lastScenarioIndex);

								//System.arraycopy(prevRow, 0, row, 0, 2);
								prevRow[2] = ((String) cols[1]).trim();
								//row[3] = prevRow[3];
								//rows.add(row);
								currLineBuffer = new StringBuffer();
								continue;
							} else if (StringUtil.indexOfIgnoreCase(currLine,
									BACKGROUND) == 0) {
								bIsBackground = true;
								currLineBuffer = new StringBuffer();
								continue;
							} else {
								lastScenarioIndex=rows.size();//step or scenario
								bIsBackground = false;
							}
						} else {
							
							if (currLineBuffer.toString().startsWith(MULTI_LINE_COMMENT)) {
								if (StringUtil.indexOfIgnoreCase(currLine, MULTI_LINE_COMMENT, 3) > 0) {
									cols[1] = new Gson().toJson(new String[] { currLine.replace("\"\"\"", "") });
								}else{
									currLineBuffer.append("\n");
									continue;
								}
							}else{
							// this is a statement
							cols[0] = currLine;
							}
						}
						if (bIsBackground) {
							background.add(cols);
						} else {
							if(lastScenarioIndex>=0)
							rows.add(cols);
							if(StringUtil.indexOfIgnoreCase((String) cols[0], SCENARIO) == 0){
								rows.addAll(background);
							}
						}
						
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
