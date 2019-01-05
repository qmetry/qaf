---
title: Scenario meta-data
sidebar: qaf_latest-sidebar
permalink: latest/scenario-meta-data.html
summary: "Scenario metadata provides information to help manage a scenario.The meta-data are collected as part of the BDD parsing."
folder: latest
tags: [bdd,scenario, meta-data]
---
## Meta-data 
Scenario metadata provides information to help manage. The meta-data are collected as part of the BDD parsing and made available for different uses, e.g. grouping scenario, setting data-provider. It can be used for scenario selection by providing [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html).

Parameters of `@Test` and `@QAFDataProvider` can be used as meta-data and it will work same as working for test case written in Java. predefined meta-key available to use which are listed in table below. In addition to predefined meta-data, you can have your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.

There are predefined meta-key available to use which are listed in [meta-data](scenario.html#meta-data). In addition to predefined meta-data, you can have your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.

Meta-value can be string, number, boolean, list or map.

Meta data can be provided with following statements:
 * Feature
 * Scenario or Scenario Outline

Meta data provided to feature will be inherited by each scenario.

#### Example BDD2:
In BDD2 meta-data declaration starts with `@` followed by meta-key and meta-value separated with `:`.

```
	@enabled:true
	@channel:['web','mobile']
	Feature:  example feature

	@description:This is example scenario in BDD2
	@grp1 @grp2
	@author: Chirag Jayswal
	@TestID:12345
	Scenario: example
	...
	
```

Meta-data without value will be considered as groups in BDD2. in example above, `@grp1` and `@grp2` will be considered as groups `grp1` and `grp2`


#### Example BDD:
In BDD meta-data provided as JSON map of meta-key and meta-value with `Meta-data` statement.

```

   Feature: example feature
   Meta-data:{'enabled':true, 'channel':['web','mobile']}
   

   Scenario: example
   Meta-data:{'TestID':'12345', 'description':'Data driven test that uses csv file to provide data', 'groups':['grp1','grp2'],'author':'Chirag Jayswal'}
   ...
 
```

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

