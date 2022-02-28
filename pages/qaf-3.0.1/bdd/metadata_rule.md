---
title: Metadata rules
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/meta-data-rules.html
summary: "Meta data rules can be defiened to enforce meta-data and it's possible value for the project"
folder: qaf-3.0.1
tags: [bdd,scenario, meta-data]
---

@since 2.1.15

Meta-data rule provides a way to enforce [meta-data](scenario-meta-data.html) and it's possible value for the project. 
You can define required and optional meta-data for scenario along with its possible values. 
You can specify meta data rules by using `metadata.rules` property. 

During **dryrun** metadata get validated by applying metadata rules.

### Metadata rule
 `metadata.rules` accepts json list of Metadata rule in following structre:
```
MetadataRule =
	{
	String key;
	MetaDataRule depends;
	List<Object> values;
	Boolean required;
	}
 ```
 
 ### Basic Example
 
 Below is basic example with one rule.
 ```
 metadata.rules=[{"key":"groups","values":["P1","P2","P3","sanity","regression","module1"],"required":true}]
 ```
 
  In above example, there is only one rule that enforece user to specify `groups` in testcase/scenario with possible vaules `["P1","P2","P3","sanity","regression","module1"]`. `"required":true` means you must provide `groups`.
  If any other group specified meta-data rule will fail.
  
 ### Complex Example
 
 Here is another example, which difines mulitple rules.
 
 ```
 metadata.rules=[\
    {"key":"groups","values":["P1","P2","P3"],"required":false},\
    {"key":"testCaseID","values":["TC-(\\\\d)+"],"required":true},\
    {"key":"type","values":["sanity","regression"],"required":true},\
    {"key":"storyId","values":["PRJ-(\\\\d)+"],"required":true},\
    {"key":"channel","values":["web","mobile","api"],"required":false},\
    {"key":"module","values":["M1","M2","M3"],"required":false},\
    {"key":"M1Type","values":["M1Sub1","M1Sub2","M1Sub3"],\
  	 "depends":{"key":"module", "vaules":["M1"]}\
  	}\
  ]
  ```
  In above example `testCaseID` and `storyId` specified with pattern. `M1Type` is dependent on `module` value `M1` that means 
  you can define `M1Type` only when `module` is `M1`.
  
  
