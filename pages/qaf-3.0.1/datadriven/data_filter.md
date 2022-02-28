---
title: Test data Filter
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/data_driven_filter.html
tags: [datadriven]
folder: qaf-3.0.1
prev: maketest_data_driven.html
---
Consider following test data return by data provider.

| TestcaseID | uname | password | id | name | price |
|============|=======|==========|====|======|=======|
| 12345 | user1 | @Test123# | 101 | Item-1 | 10.50 |
| 12346 | user1 | @Test123# | 102 | Item-2 | 50.0 |
| 12347 | user1 | @Test123# | 103 | Item-3 | 125.99 |
| 12348 | user2 | @Test123# | 104 | Item-4 | 99.99 |
| 12349 | user2 | @Test123# | 105 | Item-5 | 199.0 |
| 12350 | user3 | @Test123# | 106 | Item-6 | 15.0 |

## Filter
`filter` meta-key used to apply filter on data set returned by the data-provider that returns List of Maps. It must represent logical expression that returns true or false. You can use map key as context variable and test meta-data as parameter. For example above test data has "uname", "name", "id" and "price", you can have expression that filters records for price above 100 as below:

    - price>100
    - uname.equalsIgnoreCase('user1') && price>25
    - id==103
    
As you can use test case meta-data, method name as "method" and class name as "class" as parameters, Another example of using test case meta-data in filter assuming test case meta-data has meta-data "testCaseID". This is more useful when [configuring data provider globally](maketest_data_driven.html#configureoverride-dataprovider).

    filter="TestcaseID=='${testCaseID}'"
    filter="TestcaseID.equalsIgnoreCase('${method}')"



**Java**
```java
	@QAFDataProvider(..., filter="id==103")
	@Test
	public void editItem(Map<String, Object> data){
		//implementation
	}
```
**BDD**
```
Scenario: editItem
Meta-data: {<data-provider meta data>,'filter':'id==103'}
```
**BDD2**
```
<data-provider meta data>
@filter:id==103
Scenario: editItem
#implementation

```	

**Gherkin**
```

Scenario: editItem
#implementation

Examples: {<data-provider meta data>,'filter':'id==103'}

```	
  
## indices
Indices start from 0. In example below `indices` set to `[1,3]` will return second (id=102) and forth (id=104) records.

**Java**
```java
	@MetaData("{'indices':[1,3]}")
	@QAFDataProvider(...)
	@Test
	public void editItem(Map<String, Object> data){
		//implementation
	}
```
**BDD**
```
Scenario: editItem
Meta-data: {<data-provider meta data>,'indices':[1,3]}
```
**BDD2**
```
<data-provider meta data>
@indices:[1,3]
Scenario: editItem
#implementation

```	

**Gherkin**
```

Scenario: editItem
#implementation

Examples: {<data-provider meta data>,'indices':[1,3]}

```	
## Range
You can specify start index using `from` and end index using `to` to provide subset of records in data set base 1.

**Java**
```java
	@MetaData("{'from':3}") // will return subset starting from third record in dataset
	@MetaData("{'to':3}") // will return subset from first to third record (including first and third) in dataset
	@MetaData("{'from':2,'to':4}") // will return subset from second to forth record (including second and forth) in datset
```


**BDD**
```
	# will return subset starting from third record in dataset
	Meta-data: {<data-provider meta data>,'from':3}
```
```	
	# will return subset from first to third record (including first and third) in dataset
	Meta-data:{<data-provider meta data>,'to':3}
```	

```
	Meta-data:{<data-provider meta data>,'from':2,'to':4}
	# will return subset from second to forth record (including second and forth) in datset
```

**BDD2**
```
	# will return subset starting from third record in dataset
	<data-provider meta data>
	@from:3
```
```	
	# will return subset from first to third record (including first and third) in dataset
	<data-provider meta data>
	@to:3
```	

```
	<data-provider meta data>
	@from:2 @to:4
	# will return subset from second to forth record (including second and forth) in datset
```