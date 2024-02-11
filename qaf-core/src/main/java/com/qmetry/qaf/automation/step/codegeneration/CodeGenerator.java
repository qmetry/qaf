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
