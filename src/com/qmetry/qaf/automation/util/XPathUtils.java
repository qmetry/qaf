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

package com.qmetry.qaf.automation.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

/**
 * @author Chirag Jayswal
 *
 */
public class XPathUtils {

	/**
	 * 
	 * @param xmlFile
	 * @return
	 * @throws IOException
	 */
	public static XMLConfiguration read(File xmlFile) throws IOException{
		String xmlsrc = FileUtil.readFileToString(xmlFile, "UTF-8");
		return read(xmlsrc);
	}
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public static XMLConfiguration read(String src) {
		try {
			// remove all namespaces from xml
			src = removeNSAndPreamble(src);
			XMLConfiguration config = new XMLConfiguration();
			config.setDelimiterParsingDisabled(true);
			config.load(new ByteArrayInputStream(src.getBytes()));
			config.setExpressionEngine(new XPathExpressionEngine());
			return config;

		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param xmlStr
	 * @return
	 */
	private static String removeNSAndPreamble(String xmlStr) {
		String xsltString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\" indent=\"yes\" encoding=\"UTF-8\"/><xsl:template match=\"/\"><xsl:copy><xsl:apply-templates/></xsl:copy></xsl:template><xsl:template match=\"@*\"><xsl:attribute name=\"{local-name()}\"><xsl:value-of select=\"current()\"/></xsl:attribute></xsl:template><xsl:template match=\"*\"><xsl:element name=\"{local-name()}\"><xsl:apply-templates select=\"@* | * | text()\"/></xsl:element></xsl:template><xsl:template match=\"text()\"><xsl:copy><xsl:value-of select=\"current()\"/></xsl:copy></xsl:template></xsl:stylesheet>";
		try (ByteArrayOutputStream bo = new ByteArrayOutputStream()) {
			TransformerFactory factory = TransformerFactory.newInstance();
			Source xslt = new StreamSource(new ByteArrayInputStream(xsltString.getBytes()));
			Transformer transformer = factory.newTransformer(xslt);

			Source xmlSrc = new StreamSource(new ByteArrayInputStream(xmlStr.getBytes()));
			transformer.transform(xmlSrc, new StreamResult(bo));
			return bo.toString();
		} catch (TransformerException e) {
			System.err.println(e.getMessage());
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		}

		System.err.println("Unable to clean Namespace and Preamble from xml source, will use unmodified xml.");
		return xmlStr;
	}
}
