---
title: QMetry Automation Framework
tags: [getting_started]
sidebar: qaf_latest-sidebar
permalink: latest/docs.html
folder: latest
---
## Overview

**Powerful Automation Platform for Test Authoring, Test Execution and Execution Analysis**

It provides a powerful and versatile platform to author Test Cases in Behavior Driven, Keyword Driven or Code Driven approach. It helps to significantly reduce costs involved in setting up Test Automation at any organization. It is a right tool for Web Platform, Mobile Platform (Native, Mobile Web, HTML5,etc) and Web Service test automation solution using Selenium and other related technologies. QMetry Automation Framework benefits any DEV/QA Team in developing highly maintainable and repeatable tests that utilize reusable test assets, proper modularity and semantic structure. Descriptive Reporting satisfies high-level as well as low-level (debugging) aspects.

QMetry Automation Framework is designed to solve common industry problems related to testing complex web systems. The framework is best suited for writing Automated Web, Mobile Web and Mobile Native Application UI Tests simulate real user activities on the page.
The framework is built upon java and integrates TestNG, Selenium/Webdriver, Appium and Perfecto. Due to the framework's thorough design, test developer does not need to worry about common tasks such as thread safe browser session for running test in parallel, reporting or to incorporate result with test management tools. The tests run can be configured through standard testing configuration files, the test run filters and behavior can be changed within the config File.
The architecture of the framework ensures a low cost of maintenance while supporting extensibility by:

 * Abstracting the technical implementation away from the operational components
 * Following accepted design patterns in creating the core functionalities within the framework.
 * Providing an approach to develop highly maintainable and repeatable tests that utilize reusable test assets, proper modularity and semantic structure. Framework has readymade services for assertions, browser, reporting and data. Also, framework concept is based on page services so your page and related actions will be reusable from any test case. There is a good logging functionality and screen shot facility for assertions.


## Key Features

QMetry Automation Framework supports integrations with Test Management Platform (QMetry, Rally, ApTest, HP ALM, etc), Continuous Integration Systems (Jenkins, Bamboo, etc), Mobile Device Cloud Solutions (SauceLabs, Perfecto Mobile, etc). 

**QMetry Automation Framework Key Features:**

**Test Authoring:** Behavior Driven Development (BDD), Keyword Driven and Code Driven Development

**Test Data Management:** Test Data Support (CSV, JSON, XML etc), Different Locales support, Different Environments support

**Execution Reporting:** Execution Dashboard, Detailed Analysis and Screenshot Capturing, Integrations with Test Management Tools, etc

Some benefits of using the framework are:

 * Less maintenance
 * More reusability of code
 * Reduced execution time
 * Data-driven capability
 * Easy configurable parallel execution
 * Descriptive report
 * Utility classes
 * Test Results integration with test management tools like QMetry, Rally.
 * Integration with Sauce labs, Supports parallel execution
 * Enabling testing across multiple platforms with or without selenium grid

Question over here is how the framework reduces maintenance, execution time and reuses code?
QMetry Automation Framework provides high level construct to satisfy automation needs. It includes top level interfaces, abstract base classes, service classes and their implementations and custom annotations. Test developer only need to concentrate on writing the tests and not spend time on adjusting the underlying framework.
This framework provides test page concept in a best efficient way by which you can manipulate page navigation same as on actual web application under test. Once page get created page objects/functionalities can be used in any test case, makes code more reusable. The framework takes care of not only launching that page but the entire page hierarchy to reach that specific page. Furthermore it also checks that is page already active in browser? If so then it will continue from there, results in reduced execution time.
When functionality changes only the specific test page file needs to be updated: if there is any change in page/ui of web application under test you need to update just in particular page rather than each and every test case, thus result in less maintenance.


In case of sequential execution it will take advantage of sharing browser sessions between multiple test cases. No special coding or design required to run test in parallel, you just need to set parallel attribute’s appropriate value in configuration file (eg. false, Test, methods, classes) and framework will take care for providing thread safe browser sessions with maximum level of sharing browser session between multiple test cases.  This will result in reducing time by parallel processing as well as by some level of sharing browser session(depends on configuration). You also can configure to run parallel in different browser (eg. iexplorer, firefox) with or without selenium grid. If you are not planning for physical distributed selenium server then, without selenium grid, you can achieve higher performance by the framework as compare to grid that configured different selenium server instance on the same physical machine. While integration with Sauce labs, though Sauce labs not provides parallel processing, still you can achieve parallel processing using this framework.


Reporting of test run result includes selenium commands log, each test-step details with screenshot on failure. One can configure to capture screenshots for pass verifications too.

