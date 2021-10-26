---
title: Configuring BDD for execution
sidebar: qaf_3_0_0-sidebar
permalink: qaf-3.0.0/bdd-configuration.html
summary: "QAF BDD supports single line and multiline comment"
folder: qaf-3.0.0
tags: [bdd,execution]
---

QAF considers each BDD Scenario as TestNG test and Scenario Outline as data-driven test. You can run sequential or parallel methods (scenarios) or groups or xml test on one or more browser same as test case in Java.  

In order to run your test cases written using [BDD syntax](bdd-syntax.html), you need to create TestNG [XML](http://testng.org/doc/documentation-main.html#testng-xml) or [YAML](http://testng.org/doc/documentation-main.html#yaml) configuration file with appropriate [factory class](#factory-class). You can set scenario file to run and step implementation package using properties. To set properties refer [Different ways of providing properties](different_ways_of_providing_prop.html) 

## Properties
Following are properties used for bdd execution configuration

| Property | Value | Description | 
|-------|---------|-------|
| step.provider.pkg | one or more package |  Specify where to look up for step implementation. |
| scenario.file.loc | bdd file or folder |  Specify where to look up for scenarios. Default value is `scenarios` directory. You can specify one or more bdd file, folder separated by `;` |
| txt.scenario.file.ext | bdd file extension |  Specify extension of file containing scenarios. Default is `.bdd` for qaf bdd parser and `.feature` for qaf bdd2 parser. Not applicable for Gherkin parser |


## Factory class
BDD:
```
com.qmetry.qaf.automation.step.client.text.BDDTestFactory
```
BDD2
```
com.qmetry.qaf.automation.step.client.text.BDDTestFactory2
```
Gherkin
```
com.qmetry.qaf.automation.step.client.gherkin.GherkinScenarioFactory
```
## Configuration file example
Considering following structure, few configuration examples provided here.
<pre>
ProjectHome
 |
 |--scenarios
 |	 |--module1
 |	 |	|--suite1.feature
 |	 |	|--suite2.feature
 |	 |	|
 |	 |
 |	 |--module2
 |	 |	|--suite1.feature
 |	 |	|--suite2.feature
 |	 |  |
 |	 |
 |	 | 
 |--resources
 |	 |--common
 |	 |	|--env.properties
 |	 |
 |	 |--web
 |	 |	|--env.properties
 |	 |
 |	 |--mobile
 |	 |	|--env.properties
 |	 | 
 </pre>
  
 **resource/web/env.properties**
 ```
  step.provider.pkg=my.aut.common;my.aut.web
 
 ```
  **resource/mobile/env.properties**
 ```
  step.provider.pkg=my.aut.common;my.aut.mobile
 
 ```
### Basic example
```xml
<test name="QAF-BDD-Test">
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
### Running tests with specific group(s)

Only test with group `smoke`.
```xml
<test name="QAF-BDD-Test">
   <groups>
      <run>
        <include name="smoke" />
      </run>
   </groups>
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
### Running tests with specific meta-data

Only test with `channel` value `web`. Refer [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html) for more details.
```xml
<test name="QAF-BDD-Test">
   <parameter name="include" value="{'channel': ['web']}" />
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
### Running specific file or folder
```xml
<test name="QAF-BDD-Test">
   <parameter name="scenario.file.loc" value="resources/module1" />
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
Above example will run all bdd files under folder `resources/module1`. You can provide one or more file/folder as values of `scenario.file.loc` property. Blow are few more examples:
```
   <parameter name="scenario.file.loc" value="resources/module1/suite1.feature" />
```
```
   <parameter name="scenario.file.loc" value="resources/module1/suite1.feature;resources/module2/suite2.feature" />
```
```
   <parameter name="scenario.file.loc" value="resources/module1/suite1.feature;resources/module2" />
```

### Different step implementation for two or more platform
```xml
<test name="QAF-BDD-Test-Web">
   <parameter name="step.provider.pkg" value="my.aut.common;my.aut.web" />
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
<test name="QAF-BDD-Test-Mobile">
   <parameter name="step.provider.pkg" value="my.aut.common;my.aut.mobile" />
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
Step provider package provided in properties.
```xml
<test name="QAF-BDD-Test-Web">
   <parameter name="env.resources" value="resources/common;resources/web">
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
<test name="QAF-BDD-Test-Mobile">
   <parameter name="env.resources" value="resources/common;resources/mobile">
   <classes>
      <class name="com.qmetry.qaf.automation.step.client.text.BDDTestFactory2" />
   </classes>
</test>
```
