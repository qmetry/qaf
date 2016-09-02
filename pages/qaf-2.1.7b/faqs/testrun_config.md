---
title: testrun_config.xml
sidebar: faq_sidebar
permalink: qaf-2.1.7b/testrun_config.html
folder: qaf-2.1.7b
---

``` xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="QAS Demo" verbose="0">

	<parameter name="driver.name" value="firefoxRemoteDriver" />
	<parameter name="remote.server"
	value="http://hirenpan123:a3c53906-5257-483f-b7f7-fbd43a3e2693@ondemand.saucelabs.com:80/wd/hub"></parameter>
	<parameter name="firefox.additional.capabilities" value="{'name':'SampleTest','platform':'Windows 8','version':'41.0'}" />

	<test name="KWD Test" enabled="false">
		<classes>
			<class name="com.infostretch.automation.step.client.csv.KwdTestFactory"></class>
		</classes>
	</test>

	<test name="BDD Test" enabled="true">
		<classes>
			<class name="com.infostretch.automation.step.client.text.BDDTestFactory"></class>
		</classes>
	</test>

</suite>
```


