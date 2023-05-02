---
title: Keyword driven scenario
sidebar: qaf_3_1_0-sidebar
permalink: qaf-3.1.0/keyword_driven_scenario.html
folder: qaf-3.1.0
---

## Scenario

Scenario consist of meta-data and sequence of steps to represents a single test case. Each step can be started with stepName. Refer list of available keywords.

## Structure

```
SCENARIO|<Name of the Scenario>|{"description":<description>,<meta-key>:<meta-value>}
 
 
<first step> | [<first argument>,<second argument>] | <output-parameter> (optional)
…
<nth step> | [<arguments>] | <output-parameter> (optional)
 
END||
```

Want to iterate with set of test data from file? You got it with data-driven Scenario.

## Meta-data

* There are predefined meta-key available to use which are list in table below.
* You can define your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.
* The meta-data are collected as part of the scenario parsing and made available for different uses, e.g. Scenario selection, setting priority

| Pre-Defined Meta-Keys | Type | Comments |
|-------|--------|---------|
| description | Text | Text to describe scenario in detail |
| dataFile | Data&nbsp;-&nbsp;file&nbsp;path | Xls or xlsx  file name for data driven scenario
| sheetName | Text | Xls or xlsx file sheet name for the data driven senario
| key | Text | key which is node of xml tree 
| SQL query | Text | sql query is required to get data from database.
| enabled | True&nbsp;/&nbsp;false | Switch to consider scenario executable
| groups | Array&nbsp;of&nbsp;String | List of groups of test case, i.e. smoke, regression, P1 etc.
| priority | Number | Defines the order in which scenario should be executed. Higher the priority, earlier it will execute
| dependsOnGroups | Array&nbsp;of&nbsp;String | Scenarios of Groups, to be executed before
| dependsOnMethods | Array&nbsp;of&nbsp;String | Scenarios to be executed before


