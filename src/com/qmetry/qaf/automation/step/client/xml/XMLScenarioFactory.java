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

package com.qmetry.qaf.automation.step.client.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import com.google.gson.Gson;
import com.qmetry.qaf.automation.step.client.AbstractScenarioFileParser;
import com.qmetry.qaf.automation.step.client.ScenarioFactory;
import com.qmetry.qaf.automation.step.client.ScenarioFileParser;
import com.qmetry.qaf.automation.util.StringUtil;

/**
 * @author chirag.jayswal
 *
 */
public class XMLScenarioFactory extends ScenarioFactory {
	private ScenarioFileParser xmlParser;
	Gson gson = new Gson();

	public XMLScenarioFactory() {
		super(Arrays.asList("xml"));
		xmlParser = new XmlScenarioFileParser();
	}

	@Override
	protected ScenarioFileParser getParser() {
		return xmlParser;
	}

	public class XmlScenarioFileParser extends AbstractScenarioFileParser {

		@Override
		protected Collection<Object[]> parseFile(String xmlFile) {
			ArrayList<Object[]> statements = new ArrayList<Object[]>();
			try {
				HierarchicalConfiguration processor = new XMLConfiguration(xmlFile);
				List<?> definations = processor.getRoot().getChildren();
				for (Object definationObj : definations) {
					ConfigurationNode defination = (ConfigurationNode) definationObj;
					String type = defination.getName();
					String[] entry = new String[3];

					if (type.equalsIgnoreCase("SCENARIO") || type.equalsIgnoreCase("STEP-DEF")) {
						entry[0] = type;

						Map<?, ?> metaData = getMetaData(defination);
						entry[1] = (String) metaData.get("name");
						metaData.remove("name");
						entry[2] = gson.toJson(metaData);
						statements.add(entry);
						System.out.println("META-DATA:" + entry[2]);
						addSteps(defination, statements);
						statements.add(new String[] { "END", "", "" });
					}
				}
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}

			return statements;
		}

		private void addSteps(ConfigurationNode defination, ArrayList<Object[]> statements) {
			for (Object o : defination.getChildren()) {
				Node stepNode = (Node) o;
				if (stepNode.getName().equalsIgnoreCase("STEP")) {
					String name = getAttribute(stepNode, "name", null);
					String inParams = getAttribute(stepNode, "params", "[]");
					if (!inParams.startsWith("[")) {
						Object[] params = new Object[] { toObject(inParams) };
						inParams = gson.toJson(params);
					}
					String outParams = getAttribute(stepNode, "result", "");
					statements.add(new String[] { name, inParams, outParams });
				}
			}
		}

		private Map<?, ?> getMetaData(ConfigurationNode defination) {
			Map<String, Object> metaData = new HashMap<String, Object>();
			for (Object obj : defination.getAttributes()) {
				Node node = (Node) obj;
				metaData.put(node.getName(), toObject((String) node.getValue()));
			}
			return metaData;
		}

		private String getAttribute(Node node, String attrName, String defValue) {
			List<?> attribute = node.getAttributes(attrName);
			if (attribute.size() > 0) {
				return (String) ((Node) attribute.get(0)).getValue();
			}

			if (null == defValue) {
				throw new RuntimeException("Missing attribute " + attrName + " in " + node.getName() + " xml element");
			}
			return defValue;

		}
	}

	private Object toObject(String s) {
		if (StringUtil.isNumeric(s))
			return gson.fromJson(s, Long.class);
		if (StringUtil.startsWith(s, "[")) {
			return (Object) gson.fromJson(s, List.class);
		}
		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return Boolean.valueOf(s);
		return s;
	}

}
