---
title: Scenario
sidebar: qaf_2_1_11-sidebar
permalink: qaf-2.1.11/scenario.html
summary: "Scenario consist of meta-data and sequence of steps to represents a single test case. Each step can be started with standard BDD keywords [Given | When | Then | And | Having]. Refer list of available keywords."
folder: latest
tags: [bdd,scenario]
---

Scenario consist of meta-data and sequence of steps to represents a single test case.
Each step can be started with standard BDD keywords.

## List of available keywords.

|keywords|
|-------|
|Given| 
|When|
|Then|
|And|
|Having|

## Structure 

```
Scenario: <Name of the Scenario>
 
Meta-data: {<meta-key>:<meta-value>[,<meta-key>:<meta-value>]}
 
[Keyword] <first step description with parameters>
 
…
 
 [Keyword] <nth step description>
 
End
```
Want to iterate with set of test data from file? You got it with data-driven Scenario. 

## Comment and line-break

  - Comment: # or !
  - Line break: _&


## Meta-data 

* There are predefined meta-key available to use which are list in table below.
* You can define your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.
* The meta-data are collected as part of the scenario parsing and made available for different uses, e.g. Scenario selection, setting priority
 
|Pre-Defined Meta-Keys|Type|Comments|
|-------|--------|---------|
|description|Text|Text to describe scenario in detail|
|dataFile|Data-file path|Xls or xlsx  file name for data driven scenario|
|sheetName|Text|Xls or xlsx file sheet name for the data driven senario|
|key|Text|key which is node of xml tree|
|SQL query|Text|sql query is required to get data from database.|
|enabled| True or false|Switch to consider scenario executable|
|groups|Array of String|List of groups of test case, i.e. smoke, regression, P1 etc.|
|priority|Number|Defines the order in which scenario should be executed. Higher the priority, earlier it will execute|
|dependsOnGroups|Array of String|Scenarios of Groups, to be executed before|
|dependsOnMethods|Array of String|Scenarios to be executed before|
 
 
 
