---
title: QAF Reporting
sidebar: qaf_2_1_14-sidebar

permalink: qaf-2.1.14/qaf_reporting.html
folder: qaf-2.1.14
---

## Summary

* QAF reporting is a powerful and customizable reporting engine ensures that you have access to all relevant test data like test results,check points,test case time, test step time and environment information.Use powerful filters to slice and dice the data to drill down to exact result you seek.
* Comprehensive drill-down reporting, with each step result, step command log and screenshots.
* Live reporting enables you to view reports of executed tests without waiting for entire suite to finish.
* Get Detailed Reporting including Trending,root cause analysis and Automated screen capture. 

 
**Reporting of test run includes below details**

## Overview

It summarize details of Over All Execution. It will display basic information about All Test. It shows test name, test execution duration, pass test case count, fail test case count, skip test case count, total test case count execution and rate of passing. Multiple Test Execution can be displayed here. View All Result link shows all test case of all test executed.

{% include inline_image.html file="ReportTestCaseExecution.png" alt="Test Case Execution Summary" %}

## Tools and Filter
There 3 types of operation can be perform using Tools And Filter.

* **Filter** - Test Case can be filtered by pass/fail/skip test case over here. Only Configuration type test case can be filtered by checking Configuration check box.
* **Order** - Test Case can be ordered by pass/fail/skip or by execution or name.
* **Details** - All test case details can be expand using Expand All radio button and collapse all test case details using Collapse All radio button.


{% include inline_image.html file="toolsAndFilter.png" alt="Tools and Filter "  %}

## Environment Information 

It provides information related to environment with following parameter.

* **execution on** - To show OS version, java version, operating system etc.
* **build information** - To shows build related information like QAF version with revision, QAF build time.
* **desired capabilities** - To shows desired capabilities which describes a series of key/value pairs that encapsulate aspects of a browser. Basically, the Desired Capabilities help to set properties for the Web Driver.
* **actual capabilities** - To shows actual capabilities which are actually at run time.
* **run parameters** - To shows run parameters used at run time.

{% include inline_image.html file="environmentInfo.png" alt="Environment Information "  %}

## Error Analysis 
It provides high level information about automation failure, data failure and application failure. Exceptions categorized into different categories. 

{% include inline_image.html file="errorAnalysis.png" alt="Error Analysis "  %}

User can customize this error bucket categories in **isfw_dashboard.js** file.
 

 
## Test Details

After selecting test, it displays test details of test case. It shows useful parameter for test. It has following parameters.

* Description - It shows Test Case Description
* Groups - It shows List of groups on which test cases are executed
* Reference - It shows Location of BDD/KWD
* Start time - It shows Start time of Test Case
* End time - It shows End time of Test Case
* Meta-Data - It shows Meta-Data of Test Case.
* Actual time - It shows Actual Time of Test Case
* Check Points - It shows all steps and sub steps.

{% include inline_image.html file="TestCaseDetails.png" alt="Test Case Execution Report " %}

##  Test Case Tabs

There are four tabs available provides detailed steps, timimings, command log and error of steps.

* **Check Points**({% include inline_image.html file="checkpoints_bullet.png" alt="Test Case Execution Report " %}) :- It Shows executed  steps and sub steps as checkpoints. Sub checkpoints will be displayed in collapse mode. 

{% include inline_image.html file="checkPointsDetails.png" alt="Test Case Execution Report " %}

For verification/assertion checkpoint, attachment is also available. Click on {% include inline_image.html file="AttachmentIcon.png" alt="Attachment" %} to see screenshots.
{% include inline_image.html file="AttachmentScreen.png" alt="Attachment" %} 

* **Step Time Analysis**({% include inline_image.html file="stepTimeAnalysis_icon.png" %}) - It shows graph for comparing threshold and Duration of steps.

{% include inline_image.html file="stepTimeAnalysis_Graph.png" alt="Step Time Analysis Graph " %} 

* **Command Log**({% include inline_image.html file="commandlogICON.png" %}) - It shows log of all command which are executed with command name, arguments, status and duration.

{% include inline_image.html file="commandLogDetails.png" alt="Detailed Command Log  "  %}


* **Error Trace**({% include inline_image.html file="errorTraceICON.png" %}) - If any error occurred in test case during execution, it will show as descriptive details.. It shows error trace of individual test case. For fail or skip test case, it shows well descriptive error.

{% include inline_image.html file="errorTrace.png" alt="Detailed Error Trace "  %}


## Trending
It shows the trends of all execution respect to all execution and number of test cases.

{% include inline_image.html file="trends.png" alt="Trends "  %}


## Live Reporting

Live Reporting can be useful to show result automatically as test results created. Enable live reporting by click on Auto Refresh icon.
<div style="position: fixed;border: 3px solid #73AD21;right:5px;top:100px"><a href="dashboard.htm" target="_blank">Live Demo</a></div>

{% include inline_image.html file="AutoRefreshButton.png" alt="Auto Refresh "  %}

