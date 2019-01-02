---
title: BDD syntax
sidebar: qaf_latest-sidebar
permalink: latest/bdd-syntax.html
summary: "QAF supports multiple BDD syntaxes."
folder: latest
tags: [bdd]
---

QAF supports multiple BDD syntaxes.

## QAF BDD2 Syntax
BDD2 syntax is supported by the `BDDTestFactory2`. It is derived from QAF BDD, Jbehave and Gherkin. 
```
@group1
@author:Chirag Jayswal
@channel:["web","mobile"]
Feature: A feature is a collection of scenarios
 
Narrative:
	In order to communicate effectively to the business some functionality
	As a development team
	I want to use Behavior-Driven Development
     
Background: 
 
	Given a step that is executed before each scenario 

@smoke @TestcaseId:12345
Scenario:  A scenario is a collection of executable steps of different type
 
	Given step represents a precondition to an event
	When step represents the occurrence of the event
	Then step represents the outcome of the event

@datafie:resources/${env}/testdata.txt
@regression 
Scenario:  Another scenario exploring different combination using data-provider
 
	Given a "${precondition}"
	When an event occurs
	Then the outcome should "${be-captured}"    


@regression 
Scenario Outline:  Another scenario exploring different combination using examples
 
	Given a "${precondition}"
	When an event occurs
	Then the outcome should "${be-captured}"    
 
Examples: 
	|TestcaseId|precondition|be-captured|
	|123461|abc|be captured    |
	|123462|xyz|not be captured|


```

## QAF BDD Syntax
QAF BDD syntax is supported by the `BDDTestFactory`.

```
Feature: A feature is a collection of scenarios
Meta-data: {'groups':['group1'], 'author':'Chirag Jayswal', 'channel':['web','mobile']}

Narrative:
	In order to communicate effectively to the business some functionality
	As a development team
	I want to use Behavior-Driven Development
     
Background: 
 
	Given a step that is executed before each scenario 

Scenario:  A scenario is a collection of executable steps of different type
Meta-data: {'groups':['smoke']}
 
	Given step represents a precondition to an event
	When step represents the occurrence of the event
	Then step represents the outcome of the event

Scenario:  Another scenario exploring different combination using data-provider
Meta-data: {'groups':['regression'],'datafie':'resources/${env}/testdata.txt'}
 
	Given a "${precondition}"
	When an event occurs
	Then the outcome should "${be-captured}" 

```

## Gherkin Syntax
Gherkin syntax is supported by the `GherkinScenarioFactory`.
QAF also supports parameters as argument and data-providers for examples, which are not currently available in Gherkin.

```
@group1
Feature: A feature is a collection of scenarios

     
Background: 
 
Given a step that is executed before each scenario 
 
@smoke
Scenario:  A scenario is a collection of executable steps of different type
 
Given step represents a precondition to an event
When step represents the occurrence of the event
Then step represents the outcome of the event

@regression
Scenario Outline:  Another scenario exploring different combinations using examples
 
Given a "<precondition>"
When an event occurs
Then the outcome should "<be-captured>"    
 
Examples: 
|precondition|be-captured|
|123461|abc|be captured    |
|123462|xyz|not be captured|

Scenario Outline:  Another scenario exploring different combinations using data provider
 
Given a "<precondition>"
When an event occurs
Then the outcome should "<be-captured>"    
 
Examples: {'datafile':'resources/${env}/testdata.txt'}

```