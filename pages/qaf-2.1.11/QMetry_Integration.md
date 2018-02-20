---
title: QMetry Integration
sidebar: qaf_2_1_11-sidebar
permalink: qaf-2.1.11/QMetry_Integration.html
folder: latest
---



## Project Properties Configuration:
<b>Connection toward QMetry requires following mandatory information that can be provided by setting appropriate property (Mandatory):</b>

|Property|Description|
|--------|--------|
|integration.param.qmetry.service.url|&lt;QMetry server URL&gt;|
|integration.param.qmetry.user|&lt;username&gt;|
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.suitid	suite_id|integration.param.qmetry.build	build_id|
|integration.param.qmetry.platform	platform_id|integration.param.qmetry.drop	drop_id|

Following are the optional properties to be set.

|Property|Value|
|---|---|
|integration.param.qmetry.suitrunid	|suite_run_id|
|integration.param.qmetry.suit.path|	suite_path|
|integration.param.qmetry.suit.rundesc|	suite run description|
|qmetry.schedule.file|	qmetry schedular file|
|integration.tool.qmetry.uploadattachments	|path of upload attachment|

You can provide these properties in application.properties file.

Note: If you are using QMetry scheduler provided xml then you only required to provide url, user and password related properties from above.

## TestCase Mapping:

<b>Mapping with annotation</b>

If you want to map a test case that’s method name not follows above standard than use QmetryTestCase annotation for example:

```java
@QmetryTestCase(TC_ID="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```

## Running test that are scheduled:

If you want to run with Qmetry scheduler provided xml then provide qmetry.schedule.file property value.a

* 	You can provide it in multiple ways:
	Provide seleniumtestrunner.properties file under scripts dir:
	qmetry.schedule.file=&lt;run_schedule_file&gt;

*	Edit bat file “seleniumTestRunner.bat” under scripts dir
	ant -f scripts/seleniumtestrunner.xml -Dtestng.suite.file=testNG_config.xml -qmetry.schedule.file=&lt;run_schedule_file&gt;

*	Edit bat file to get value at runtime
	ant -f scripts/seleniumtestrunner.xml -Dtestng.suite.file=testNG_config.xml,
	then you need to pass command line arg to bat file as -Dqmetry.schedule.file=&lt;run_schedule_file&gt;

 
*	Form Project home execute ant as:
	ant -Dtestng.suite.file=testNG_config.xml -Dqmetry.schedule.file=&lt;run_schedule_file&gt;
	
You can use in different 4 scenarios by using apply test run id or testcase run id.

<b>Scenario 1:</b>

Given: Suite_id & TC_ID
TC_ID is given in QMetryTestCase annotation and suite id is provided using integration.param.qmetry.suitid as a property.

```java
@QmetryTestCase(TC_ID="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```


Following properties must be available in addition to suite_id.

|Property|Description|
|-----|-----|
|integration.param.qmetry.service.url|&lt;QMetry server URL&gt;|
|integration.param.qmetry.user|&lt;username>|
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.suitid|	suite_id|
|integration.param.qmetry.build	|build_id|
|integration.param.qmetry.platform|	platform_id|
|integration.param.qmetry.drop	|drop_id|

<b>Scenario 2:</b>
Given: Suite_run_id & TC_ID

TC_ID is given in QMetryTestCase annotation and suite run id is provided using integration.param.qmetry.suitrunid as a property.

```java
@QmetryTestCase(TC_ID="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```

Following properties must be available.

|Property|Description|
|-----|-----|
|integration.param.qmetry.service.url| &lt;QMetry server URL&gt; |
|integration.param.qmetry.user| &lt;username&gt; |
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.suitrunid	|suite_run_id|
|integration.param.qmetry.build	|build_id|
|integration.param.qmetry.platform	|platform_id|
|integration.param.qmetry.drop	|drop_id|



<b>Scenario 3:</b> 

Given: Suite_Run_ID & TC_Run_ID

TC_ID is given in QMetryTestCase annotation and suite id is provided using integration.param.qmetry.suitrunid as a property.

```java
@QmetryTestCase(runId="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```

Following properties must be available.

|Property|Description|
|-----|-----|
|integration.param.qmetry.service.url|&lt;QMetry server URL&gt;|
|integration.param.qmetry.user|&lt;username&gt;|
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.suitrunid	|suite_run_id|
|integration.param.qmetry.build	|build_id|
|integration.param.qmetry.platform	|platform_id|
|integration.param.qmetry.drop	|drop_id|

<b>Scenario 4:</b>

Given: Suite_Run_ID & TC_Run_ID

TC_ID is given in QMetryTestCase annotation and suite id is provided using integration.param.qmetry.suitrunid as a property.

```java
@QmetryTestCase(runId="12345")
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```

Following properties must be available in addition to suite_id.

|Property|Description|
|-----|-----|
|integration.param.qmetry.service.url|&lt;QMetry server URL&gt;|
|integration.param.qmetry.user|&lt;username&gt;|
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.suitrunid	|suite_run_id|
|integration.param.qmetry.build|	build_id|
|integration.param.qmetry.platform|	platform_id|
|integration.param.qmetry.drop|	drop_id|


<b>Scenario 5:</b>

Given: Do Not Give AnyThing

TC_ID is not given in QMetryTestCase annotation then it will create new test case in qmetry. Provide required property as per below.

```java
@QmetryTestCase()
@Test(description = "Sample test")
public void TCtest() throws Exception {
    //test code
}
```

Following properties must be available.

|Property|Description|
|-----|-----|
|integration.param.qmetry.service.url|&lt;QMetry server URL&gt;|
|integration.param.qmetry.user|&lt;username&gt;|
|integration.param.qmetry.pwd|password|
|integration.param.qmetry.project|project_id|
|integration.param.qmetry.release|release_id|
|integration.param.qmetry.cycle|cycle_id|
|integration.param.qmetry.build|	build_id|
|integration.param.qmetry.platform|	platform_id|
|integration.param.qmetry.drop|	drop_id|
