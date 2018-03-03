---
title: Scenario
sidebar: qaf_latest-sidebar
permalink: latest/scenario.html
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
|dataProviderClass|Fully qualified class name|required if you want to use custom data provider (since 2.1.12)|
|dataProvider|name of the data provider|required when you want to use custom data provider (since 2.1.12)|
 

## Data-driven Scenario
You can iterate your scenario with set of test data. Refer [make test data driven](maketest_data_driven.html), any of the @QAFDataProvider property you can set as meta-data. Below example demonstrates data-driven feature  


```

SCENARIO: Data-driven Example 
META-DATA: {"dataFile":"resources/data/testdata.csv","description":"Data driven test that uses csv file to provide data"}
	Given I am on fruits and colors activity
	When i select '${fruit}'
	Then the color should be '${color}'

END

```

Below is csv data file and first row is column names.

**testdata.csv**

```csv
fruit,color
grapes,green
banana,yellow

```


If you want to uses custom data set you need to provide data provider class and data provider name in meta-data.

```
SCENARIO: Custom Data provider Example 
META-DATA: {"dataProvider":"my-custom-dp", "dataProviderClass":"my.project.impl.CustomDataProvider","description":"Data driven test that uses custom data provider"}
	Given I am on fruits and colors activity
	When i select '${fruit}'
	Then the color should be '${color}'

END

```

 
```java

package my.project.impl;

import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.collections.Maps;

/**
 * @author chirag.jayswal
 *
 */
public class CustomDataProvider {
	
	@DataProvider(name="my-custom-dp")
	public static Object[][] dataProviderForBDD(){
		
		Map<Object, Object> rec1 = Maps.newHashMap();
		m.put("fruit", "grapes");
		m.put("color", "green");
		
		Map<Object, Object> rec2 = Maps.newHashMap();
		m.put("fruit", "banana");
		m.put("color", "yellow");
		
		return new Object[][]{{rec1},{rec2}};
	}
}

```