---
title: BDD2
sidebar: qaf_2_1_15-sidebar
permalink: qaf-2.1.15/bdd2.html
summary: "QAF BDD2 is derived from QAF BDD and gherkin. It supports meta-data from qaf bdd as tags and examples from gherkin."
folder: qaf-2.1.15
tags: [bdd,scenario]
---

## Comment

Comments can be placed any where in the bdd file. Comment can be single line or multiline. Single line comment starts with `#` or `!`.

Multi-line comments start with `"""` and end with `"""`.Multiline comment in scenario or background logged in report as info message, however comments outside will not logged in report.

```
 # this is example of single line comment
 # single line comment will be ignored by BDD parser
 ! this is also a comment
 
 """
 This is multi line comment
 will be logged in report if it is inside background or scenario.
 """  

```
## line-break
To break statement in multiple line you can use `_&` as line break.


## Meta-data 

* There are predefined meta-key available to use which are listed in [meta-data](scenario.html#meta-data).
* You can define your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.
* The meta-data are collected as part of the scenario parsing and made available for different uses, e.g. Scenario selection, setting priority

```
@description:Data driven test that uses csv file to provide data
@group1 @group2
@author: Chirag Jayswal
SCENARIO: Scenario Example 
	Given I am on fruits and colors activity
	When i select 'grapes'
	Then the color should be 'green'
```

## Data-driven Scenario
You can iterate your scenario with set of test data by providing examples with scenario outline. You also can provide data from qaf data provider by providing @QAFDataProvider property as meta-data.Refer [make test data driven](maketest_data_driven.html), any of the @QAFDataProvider property you can set as meta-data.

Below example demonstrates data-driven feature  


```
@author:Chirag Jayswal @regression 
@dataFile:resources/data/testdata.csv 
SCENARIO: Data-driven Example 
	Given I am on fruits and colors activity
	When i select '${fruit}'
	Then the color should be '${color}'

```

Below is csv data file and first row is column names.

**testdata.csv**

```csv
fruit,color
grapes,green
banana,yellow

```

Scenario outline with examples.
```
@regression 
@author:Chirag Jayswal
Scenario Outline: Data-driven Example 
	Given I am on fruits and colors activity
	When i select '${fruit}'
	Then the color should be '${color}'

Examples:
|fruit|color|
|grapes|green|
|banana|yellow|

```
You can also use groups/tags with `Examples`, however data-provider recommended over Examples with or without groups.

If you want to uses custom data set you need to provide data provider class and data provider name in meta-data.

```
@dataProvider:my-custom-dp 
@dataProviderClass:my.project.impl.CustomDataProvider
@regression @author:Chirag Jayswal
Scenario: Custom Data provider Example 
	Given I am on fruits and colors activity
	When i select '${fruit}'
	Then the color should be '${color}'

```
Above scenario will use custom data provider defined in class `CustomDataProvider`. It will add `regression`  group and `Chirag Jayswal` as custom meta-data `author`.
 
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
		
		return new Object[][] {% raw %}{{rec1},{rec2}}{% endraw %} ;
	}
}

```