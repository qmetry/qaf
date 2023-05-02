---
title: BDD2
sidebar: qaf_3_1_0-sidebar
permalink: qaf-3.1.0/bdd2.html
summary: "QAF BDD2 is derived from QAF BDD and gherkin. It supports meta-data from qaf bdd as tags and examples from gherkin."
folder: qaf-3.1.0
tags: [bdd,scenario]
---

## Features

BDD2 syntax is derived from QAF BDD, Jbehave and Gherkin. Following are features of BDD2:

  * Custom [Meta-Data](scenario-meta-data.html) that supports [meta-data filters](scenario_metadatata_filter_include_exclude_prop.html), [meta-data rules](meta-data-rules.html) and [formatter](scenario-meta-data.html#meta-data-formatter)
  * Parameter support in step argument
  * Data driven test using embedded or external test data
    * External test data from external source (CSV, XML, JSON, EXCEL, DB)
    * Embedded test data using Examples
  * Background support
  * Compatible with QAF-geherkin or any other gherkin editor.
  * Supported by TestNG runner using [BDDTestFactory2](bdd-configuration.html#factory-class) and by cucumber runner using [QAF-cucumber](qaf_cucumber.html)


## Meta-data 

Scenario [metadata](scenario-meta-data.html) provides information to help categorize and manage scenarios. The meta-data are collected as part of the BDD parsing and made available for different uses, e.g. grouping scenario, Scenario selection, setting priority, setting data-provider. 

Unlike BDD, in BDD2 meta-data are provided before scenario declaration  using `@` as key value pair separated with `:` sign. Meta-data not provided as key value pair will be considered as `groups`.
* There are predefined meta-key available to use which are listed [here](scenario-meta-data.html#pre-defined-meta-data-for-bdd).
* You can define your custom meta-key to categorize scenarios as per AUT. You can choose whatever names are most appropriate for the information they are trying to convey.
* Meta-data can be used for scenario selection by providing [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html).

```
@description:Data driven test that uses csv file to provide data
@group1 @group2
@author: Chirag Jayswal
SCENARIO: Scenario Example 
	Given I am on fruits and colors activity
	When i select 'grapes'
	Then the color should be 'green'
```

Meta-value can string, number, boolean or list.

```
@TestID:12345
@enabled:true
@channel:['web','mobile']
```

Meta data can be provided with following elements:
 * Feature
 * Scenario
 * Scenario Outline 
 * Examples
 
Meta data provided to feature will be inherited by each scenario or scenario outline.

## Data-driven Scenario
You can iterate your scenario with set of test data by providing examples with scenario outline. You also can provide data from qaf data provider by providing @QAFDataProvider property as meta-data.Refer [make test data driven](maketest_data_driven.html), any of the @QAFDataProvider property you can set as meta-data.

Below example demonstrates data-driven feature  using [csv data-provider](https://qmetry.github.io/qaf/latest/maketest_data_driven.html#in-built-data-providers)


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

Above scenario will use custom data provider defined in class `CustomDataProvider`. It will add `regression`  group and `Chirag Jayswal` as custom meta-data to specify `author`.
 
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

