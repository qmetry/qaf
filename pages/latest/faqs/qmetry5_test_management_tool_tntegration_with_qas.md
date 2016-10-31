---
title: Qmetry5 Test Management Tool Integration with Qmetry Automation framework
sidebar: faq_sidebar
permalink: qaf-2.1.9/qmetry5_test_management_tool_tntegration_with_qas.html
folder: qaf-2.1.9
---

First of all please make sure that **QmetryWSClient.jar** and **QMScheduler.jar** file is present in your lib folder. If you do not have these jar files, please find attachment of this document.

 * when you what to integrate Qmetry Test cases with Qmetry Automation Framework, Please ensure that below dependencies should be present in your IVY.XML file.

```xml	

<dependency org="org.apache.axis" name="axis" rev="1.4"/>
<dependency org="org.apache.axis" name="axis-jaxrpc" rev="1.4"/>
<dependency org="org.apache.axis" name="axis-saaj" rev="1.4"/>    
<dependency org="org.apache.axis" name="axis-ant" rev="1.4"/>          
<dependency org="commons-discovery" name="commons-discovery" rev="0.4"/><dependency org="wsdl4j" name="wsdl4j" rev="1.6.2"/>
<dependency org="org.apache.ws.commons.axiom" name="axiom-impl" rev="1.2.7"/>

```

* To integrate your QMetry test cases with QMetry Automation Framework, you need to set below properties in your application.properties file.

**Following are the required properties.**

```xml

integration.tool.qmetry=1

integration.param.qmetry.service.url=http://<qmetry-instance>/WEB-INF/ws/service.php

integration.param.qmetry.user=user name

integration.param.qmetry.pwd=password

integration.param.qmetry.project= name of your Qmetry Project (i.e MyQmetryProject)

integration.param.qmetry.release=your Qmetry Release name (i.e Release 1.0.0-beta)

integration.param.qmetry.build=Build number (i.e 201408111111)

integration.param.qmetry.platform=Platform ID

integration.param.qmetry.suitid=TestSuit ID
		
```
**Following are the optional properties to be set.**

```xml

integration.param.qmetry.suit.path

integration.param.qmetry.suit.rundesc

qmetry.schedule.file

integration.tool.qmetry.uploadattachments
		
```		

You can provide these properties in application.properties file.

{% include note.html content="If you are using QMetry scheduler provided xml then only required to provide url, user and password related properties from above." %}

## Add listener

In your xml configuration file add following listener entry in :

```xml

<suite … >

<listeners>

<listener class-name="com.qmetry.qaf.automation.integration.qmetry.testnglistener.QmetrySchedulerFilter" />

</listeners>
…
</suite>

```

## TestCase Mapping:

* Mapping with Prefix

Test cases with Qmery testcase id and prefix TC. For example if test case id in qmetry is 12345 then your test method will be: TC12345.

**Mapping with annotation**

If you want to map a test case that’s method name not follows above standard than use QmetryTestCase annotation for example:

**For  java testsuite,**

```
@QmetryTestCase(TC_ID="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
        //test code
}
```
      

**Running test that are scheduled:**

If you want to run with Qmetry scheduler provided xml then provide **qmetry.schedule.file** property value.
You can provide it in multiple ways:

**1. Provide seleniumtestrunner.properties file under scripts dir:**
    qmetry.schedule.file=<run_schedule_file>

**2. Edit bat file “seleniumTestRunner.bat” under scripts dir**
    ant -f scripts/seleniumtestrunner.xml -Dtestng.suite.file=testNG_config.xml -Dqmetry.schedule.file=<run_schedule_file>

**3.Edit bat file to get value at runtime**
    ant -f scripts/seleniumtestrunner.xml -Dtestng.suite.file=testNG_config.xml %1
    -Then you need to pass command line arg to bat file as
    -Dqmetry.schedule.file=<run_schedule_file>

**4.Form Project home execute ant as:**
    ant -Dtestng.suite.file=testNG_config.xml -Dqmetry.schedule.file=<run_schedule_file>
	
[QMScheduler.jar](https://infostretch.zendesk.com/hc/en-us/article_attachments/200390215/QMScheduler.jar)

[QmetryWSClient.jar](https://infostretch.zendesk.com/hc/en-us/article_attachments/200390225/QmetryWSClient.jar)
      



