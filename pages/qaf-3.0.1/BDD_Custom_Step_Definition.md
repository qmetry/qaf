---
title: BDD Custom Step Definition
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/BDD_Custom_Step_Definition.html
folder: qaf-3.0.1
tags: [bdd,scenario]
---

Steps can be defined in bdl

The Basic Step definition is following. 
 
```
STEP-DEF:<meaningfull step description>
 
    [Keyword] <first step description with parameters>
 
    …
 
    [Keyword] <nth step description>
 
END
```

While execution make sure required bdl files are mentioned in step.provider.pkg property as below

```properties
step.provider.pkg=com.test;scenarios

```
Refer [describe test step](Describe_Test_Step.html)
