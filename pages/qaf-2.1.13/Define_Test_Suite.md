---
title: Define Test Suite
sidebar: qaf_2_1_13-sidebar
permalink: qaf-2.1.13/Define_Test_Suite.html
folder: latest
tags: [bdd,scenario]
---

Behavior driven test suite consist of one or more Scenarios. Each scenario represents one test case. One can provide optional related information as background or narrative or user-story related to scenarios authored in the suite. Save your suite file with `.bdd` extension.


## Formation of Suite

```
[Narrative | Background | User-story]
Scenario: <name of the scenario>
<<steps>>
End
...
Scenario: <name of the scenario>
<<steps>>
End
```

## Example
File: suite1.bdd

```
Feature: google search

Scenario: Search InfoStrech
META-DATA: {'desc':'This is an example of scenario using QAF-BDD'}
	Given I am on Google Search Page
	When I search for "git qmetry"
	Then I get at least 5 results
	And it should have "QMetry Automation Framework" in search results
END
```
Find more example [here](https://github.com/qmetry/qaf/tree/master/test/resources/scenarios).

{% include tip.html content="To start with you can use available pre-defined low level steps and author scenario." %}
