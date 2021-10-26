---
title: Test case meta-data
sidebar: qaf_3_0_0-sidebar
permalink: qaf-3.0.0/scenario-meta-data.html
summary: "Testcase meta-data provides information to help manage a test case.The meta-data are collected as part of the BDD parsing."
folder: qaf-3.0.0
tags: [bdd,scenario, meta-data]
---
## Meta-data 
Meta-data with test case provides information to help manage test cases. In Java test case is defined with `@Test` annotation on method and in BDD test case is defined with `Scenario` statement. Each meta-data provided as combination of `meta-key` and `meta-value`. The meta-data are collected as part of the BDD parsing and made available for different uses, e.g. grouping scenario, setting data-provider. It can be used for scenario selection by providing [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html) and in listeners.


Meta-value can be `string`, `number`, `boolean`, `list` or `map`.

Meta data can be provided with following statements:
 * Feature
 * Scenario or Scenario Outline

### Meta-data rules
[Meta data rules](meta-data-rules.html) can be defiened to enforce meta-data and it's possible value for the project
Meta data provided to feature will be inherited by each scenario.

### Meta-data formatter
Since 2.1.15, Meta data values can be formatted by providing formmater. To provide formattor for any meta-value, you need to provide property with `metadata.formatter` prefix and meta-key suffix. You can set format as supported by MessageFormat that accepts one argument for format to apply on meta-value.
```
metadata.formatter.storyKey=<a href="${jira.url}/{0}">{0}</a>
```
When you provide metadata with meta-key `storyKey` on test case, for example in BDD, `@storyKey:PRJ-123` in report story id will be formatted with link. This is applicable for BDD, Java, Keyword driven test case.

### Example BDD2:
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

### Example BDD:
In BDD meta-data provided as JSON map of meta-key and meta-value with `Meta-data` statement.

```
Feature: example feature
Meta-data:{'enabled':true, 'channel':['web','mobile'], 'storyKey':'PRJ-123'}

Scenario: example
Meta-data:{'TestID':'12345', 'description':'This is example scenario in BDD', 'groups':['grp1','grp2'],'author':'Chirag Jayswal'}
...

```
### Example Java:
In Java meta-data provided as JSON map of meta-key and meta-value with `@MetaData` annotation. Any annotation provided on test method including `@Test` and `@QAFDataProvider` represents meta-data.

```
@Test(description="This is example scenario in BDD",groups={"grp1","grp2"})
@MetaData("{'TestID':'12345', 'storyKey':'PRJ-123', 'channel':['web','mobile'] ,'author':'Chirag Jayswal'}}"
public void example(){

}

```

### Example Gherkin:
In Gherkin meta-data declaration starts with `@` followed by value. Gherkin syntax supports only groups which is called as tags in gherkin.


```
@grp1
Feature:  example feature

@grp1 @grp2
Scenario: example
...

```


## Pre-Defined Meta-data for BDD
Parameters of `@Test` and `@QAFDataProvider` can be used as meta-data and it will work same as working for test case written in Java. Refer [meta-data for data-providers](maketest_data_driven.html#meta-data) and predefined meta-key available to use which are listed in table below. In addition to predefined meta-data, you can have your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey. In examples above, `TestID` and `channel` represents custom meta-data.

|Pre-Defined Meta-Keys|Type|Comments|
|-------|--------|---------|
|description|Text|Text to describe scenario in detail|
|enabled| True or false|Switch to consider scenario executable|
|groups|Array of String|List of groups of test case, i.e. smoke, regression, P1 etc.|
|priority|Number|Defines the order in which scenario should be executed. Higher the priority, earlier it will execute|
|dependsOnGroups|Array of String|Scenarios of Groups, to be executed before|
|dependsOnMethods|Array of String|Scenarios to be executed before|
|dataProviderClass|Fully qualified class name|required if you want to use custom data provider (since 2.1.12)|
|dataProvider|name of the data provider|required when you want to use custom data provider (since 2.1.12)|


