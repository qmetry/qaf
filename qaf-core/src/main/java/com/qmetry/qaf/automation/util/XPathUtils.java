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
