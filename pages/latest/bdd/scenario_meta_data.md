---
title: Scenario meta-data
sidebar: qaf_latest-sidebar
permalink: latest/g and made available for different uses, e.g. grouping scenario, setting data-provider. It can be used for scenario selection by providing [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html).

Parameters of `@Test` and `@QAFDataProvider` can be used as meta-data and it will work same as working for test case written in Java. predefined meta-key available to use which are listed in table below. In addition to predefined meta-data, you can have your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.

## Pre-Defined Meta-data 

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
|dataProviderClass|Fully qualified class name|required if you want to use custom data provider (since 2.1.12)|
|dataProvider|name of the data provider|required when you want to use custom data provider (since 2.1.12)|

Meta data can be provided with following elements:
 * Feature
 * Scenario

 
Meta data provided to feature will be inherited by each scenario.