---
title: QAF Gherkin Scenario Factory
sidebar: qaf_2_1_13-sidebar
permalink: qaf-2.1.13/gherkin_client.html
summary: "Author tests in standard Gherkin format that Cucumber understands with power of QAF as back-end"
folder: qaf-2.1.13
tags: [bdd,scenario,cucumber,gherkin]
---

## About

QAF Gherkin Scenario Factory allows to author test case in Gherkin format that Cucumber understands, so the Cucumber users can easily use QAF with Gherkin for test authoring.
The predominant benefit is, you can run Gherkin as QAF scenario so it will have all QAF execution **features like run configuration, reporting, parallel execution, step listener**.


## Why QAF-Gherkin Scenario Factory
Gherkin is well known behavior driven language that Cucumber understands. However with Cucumber JVM there are few challenges like:

-  Lake of run configuration: you need to be dependent on other java unit testing framework JUnit or TestNG just for run configuration. Even if you use TestNG with Cucumber, you can't take full advantage of all features provided by TestNG!
-  Using test data form outside the feature file (this is quite important when you need to run feature file for 2 different environment, let say staging and test, where test data defers)
-  running scenario in parallel
-  Limited hooks (for example you can not have step hook)
-  Integrations (ex: test-management tools)
-  Cannot reuse common steps and hooks across multiple project by having steps and hooks in packaged jar and so on...  

There are different non-standard ways people use to overcome with one or other such challenges. 

Another open challenge for web and mobile test automation is, cucumber is unit test testing framework and you have to create your own implementation to support web, mobile web or mobile native support.

In order to overcome with such challenges, We provided Scenario Factory for Gherkin so Gherkin can be used as QAF test step client in addition to BDD, CSV, Excel, and XML client.  


### Benefits of using QAF-Gherkin Scenario Factory

- While you are authoring test in Gherkin format you can provide step implementation using `@QAFTestStep` annotation or Cucumber step annotations (`@Given`, `@When`, `@Then`, `@And`, `@But`). 

- It will support test data outside feature file (all QAF data provider support) using QAF data-provider meta-data with Examples you can separate out data from feature file to **text, csv, xml, excel or json file or DB**

- Another useful value addition is regardless of which step implementation (cucumber or QAF) you have, you can use [step listeners]( https://qmetry.github.io/qaf/latest/qaf_listeners.html#teststep-listener).

- You can use either QAS BDD editor or standard Cucumber editor for authoring feature file.

- Scenario level parallelism support
- Standard TestNG Configuration file 
- Utilize effectively all TestNG and QAF listeners including step listener
- Recognize steps/listeners from jar files

- It will enable migration from cucumber to QAF as with this feature you can continue using step implementation with cucumber step annotation.


## How to use it

1.	Author test cases in Gherkin format. Refer [feature files](https://github.com/qmetry/qaf/tree/master/test/resources/features). You can use QAF Gherkin editor or standard Cucumber editor for authoring feature file.

2.  Step Implementation
	1.	You can use in-built QAF steps. 
	2.  In order to create new steps either use QAF Step implementation way refer example[QAFTestStepImpl.java](https://github.com/qmetry/qaf/blob/master/test/src/com/qmetry/qaf/automation/impl/step/qaf/QAFTestStepImpl.java) or cucumber way Refer example[CucumberStepImpl.java](https://github.com/qmetry/qaf/blob/master/test/src/com/qmetry/qaf/automation/impl/step/cucumber/CucumberStepImpl.java) of step implementation. You need to provide either one of the implementation (you can have mixed implementation as well)

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

### For Existing cucumber implementation
Existing project implemented using cucumber can also be run using QAF-Gherkin Scenario Factory.

-  Use GherkinScenarioFactory to run feature files written in **standard gherkin language**
-  Convert Cucumber hooks to appropriate TestNG listener, for example convert @before implementation to testng method listener's beforeMethod
-  Place @QAFTestStepProvider annotation at class defining cucumber steps
-  Use ‘step.provider.pkg’ instead of glue
-  You can use tags as groups for run-configuration-filter


