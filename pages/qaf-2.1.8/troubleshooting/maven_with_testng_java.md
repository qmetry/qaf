---
title: Maven With TestNG Suite Not Working For Java Test.
sidebar: troubleshoot_sidebar
permalink: qaf-2.1.8/maven_with_testng_java.html
folder: qaf_2_1_7b
---

Solution for below error is as follows:

Execute command ‘mvn clean install’ first.
After running above command, run testNG suite for Java. It will run for Java.
 

In Maven Project when user run Java test using testNG than following error appears in console.

```java
testng.TestNGException:
Cannot find class in classpath:
at org.testng.xml.XmlClass.loadClass(java:81)
at org.testng.xml.XmlClass.init(java:73)
at org.testng.xml.XmlClass.<init>(java:59)
at org.testng.xml.TestNGContentHandler.startElement(java:544)
at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.startElement(java:509)
at com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator.startElement(java:745)
at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanStartElement(java:1363)
at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl$FragmentContentDriver.next(java:2786)
at com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.next(java:606)
at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(java:510)
at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(java:848)
at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(java:777)
at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(java:141)
at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.parse(java:1213)
at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl$JAXPSAXParser.parse(java:649)
at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl.parse(java:333)
at javax.xml.parsers.SAXParser.parse(java:195)
at org.testng.xml.XMLParser.parse(java:39)
at org.testng.xml.SuiteXmlParser.parse(java:17)
at org.testng.xml.SuiteXmlParser.parse(java:10)
at org.testng.xml.Parser.parse(java:168)
at org.testng.TestNG.initializeSuitesAndJarFile(java:311)
at org.testng.remote.AbstractRemoteTestNG.run(java:103)
at org.testng.remote.RemoteTestNG.initAndRun(java:137)
at org.testng.remote.RemoteTestNG.main(java:58)
 
```
 

 

