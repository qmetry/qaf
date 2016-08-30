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


package com.qmetry.qaf.automation.step.codegeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * com.qmetry.qaf.automation.step.codegeneration.CodeGenerator.java
 * 
 * @author chirag
 */
public class CodeGenerator {
	private static final String LINE_BREAK = "_&";
	private static final String PKG_DECLARATION = "package ${pkg};";
	private static final String[] IMPORT_STATEMENTS = {
			"import static com.qmetry.qaf.automation.step.StringTestStep.execute;",
			"import org.testng.annotations.Test;", "import com.qmetry.qaf.automation.ui.WebDriverTestCase;" };
	private static final String CLASS_DECLARATION_STRAT = "public class ${cls} extends WebDriverTestCase {";

	private static final String CLASS_DECLARATION_END = "}";
	private static final String TEST_ANNOTATION_DECLARATION = "\t@Test(${testng.params})";
	private static final String DATAPROVIDER_ANNOTATION_DECLARATION = "\t@com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider(${dp.params})";

	private static final String METHOD_DECLARATION_STRAT = "\tpublic void \"${test}\"(){";
	private static final String METHOD_DECLARATION_END = "\t}";

	public static void generateClass(String file) {

	}

	private void generateSrc(String filePath) throws IOException {
		File file = new File(filePath);
		File dir = file.getParentFile();
		LinkedList<Statement> statements = scan(new FileReader(file));
	}

	private static LinkedList<Statement> scan(Reader source) throws IOException {
		BufferedReader br = null;
		LinkedList<Statement> statements = new LinkedList<Statement>();
		String strLine = "";
		StringBuffer currLineBuffer = new StringBuffer();
		int lineNo = 0;
		// file line by line
		try {
			br = new BufferedReader(source);
			while ((strLine = br.readLine()) != null) {
				lineNo++;
				currLineBuffer.append((strLine.trim()));
				if (!strLine.endsWith(LINE_BREAK)) {

					statements.add(new Statement(currLineBuffer.toString(), lineNo));
					currLineBuffer = new StringBuffer();
				} else {
					currLineBuffer.delete(currLineBuffer.length() - LINE_BREAK.length(), currLineBuffer.length());
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return statements;
	}

	static class Statement {
		public enum Type {
			Annotation("@"), Comment("#"), Scenario("Scenario"), StepDef("Step-def"), End("End"), MetaData(
					"Meta-data"), EmptyLine, Other;

			String indicator;

			private Type() {
			}

			private Type(String indicator) {
				this.indicator = indicator;
			}

			public static Type getType(String stmt) {
				if (StringUtil.isBlank(stmt)) {
					return EmptyLine;
				}
				for (Type type : Type.values()) {
					if (type.equals(EmptyLine)) {
						break;
					}
					if (StringMatcher.startsWithIgnoringCase(type.indicator).match(stmt)) {
						return type;
					}
				}
				return Other;
			}
		}

		String text;
		int lineNo;

		public Statement(String text, int lineNo) {
			this.text = text;
			this.lineNo = lineNo;
		}

	}

	static class CodeBlock {
		enum Type {
			Scenario("Scenario"), Statement, comment("#"), emptyLine, None, Annotation("@");
			String indicator;

			private Type() {
			}

			private Type(String indicator) {
				this.indicator = indicator;
			}

		}

		String text;
		int startIndex;
		String type;
	}

	static class ScanarioBlock {
		private Map<String, Object> metadata;
		private String name;
		private ArrayList<StringTestStep> steps;
	}

}
