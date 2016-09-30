---
title:  "QAF Gherkin Client"
published: true
permalink: latest/qaf-gherkin-client.html
summary: "QAF Gherkin Client"
tags: [news]
sidebar: qaf_latest-sidebar
folder: latest

---


## QAF pre release 2.1.9 RC2 with Gherkin Client 

We strongly recommand QAF-BDD for authoring test case in behavior driven way. Nevertheless, in addition to BDD, CSV, Excel, and XML client, we are going to provide gherkin client for those who already married with cucumber and wish to use all the QAF features. It will allow to author test in gherkin format. While you are authoring test in gherkin format you can provide step implementation using `@QAFTestStep` annotation or Cucumber step annotations (`@Given`, `@When`, `@Then`, `@And`, `@But`). 

### What are the benefits:
We found that existing cucumber users struggling with execution (especially **parallel execution**) and run configuration. Many of the times duplication of feature file because of hard-coded data in feature file.

This feature will allow to author test case in gherkin format, so the cucumber users can easily use QAF for test authoring. 
The predominant benefit is, you can run gherkin as QAF scenario so it will have all QAF execution **features like run configuration, reporting, parallel execution, step listener**.

It will support test data outside feature file (all QAF data provider support) using QAF data-provider meta-data with Examples you can separate out data from feature file to **text, csv, xml, excel or json file or DB**

Another useful value addition is regardless of which step implementation (cucumber or QAF) you have, you can use [step listeners]( https://qmetry.github.io/qaf/latest/qaf_listeners.html#teststep-listener).

You can use either QAS BDD editor or standard Cucumber editor for authoring feature file.
It will enable migration from cucumber to QAF as with this feature you can continue using step implementation with cucumber step annotation.

### How to use

1.	Author test cases in gherkin format. Refer [feature files](https://github.com/qmetry/qaf/tree/master/test/resources/features) 

2.	Implement steps or you can use in-built QAF steps. Refer [CucumberStepImpl.java](https://github.com/qmetry/qaf/blob/master/test/src/com/qmetry/qaf/automation/impl/step/cucumber/CucumberStepImpl.java) example of cucumber step implementation and [QAFTestStepImpl.java](https://github.com/qmetry/qaf/blob/master/test/src/com/qmetry/qaf/automation/impl/step/qaf/QAFTestStepImpl.java) for QAF step implementation example. You need to provide either one of the implementation (you can have mixed implementation as well)

3.	Use `com.qmetry.qaf.automation.step.client.gherkin.GherkinScenarioFactory` Factory class for run configuration XML

	> ``` xml
	> 
	> <test name="Gherkin-QAF-Test">
	>    <parameter name="step.provider.pkg" value="com.qmetry.qaf.automation.impl.step.qaf" />
	>    <parameter name="scenario.file.loc" value="resources/features" />
	>    <classes>
	>       <class name="com.qmetry.qaf.automation.step.client.gherkin.GherkinScenarioFactory" />
	>    </classes>
	> </test>
	> 
	> ```
	> 

4.	Provide  `step.provider.pkg` to configure package from where your step implementation need to be loaded
5.	Provide `scenario.file.loc` to configure feature file(s) or directory 
6.	You can use tags as groups in XML configuration file.



{% include links.html %}
