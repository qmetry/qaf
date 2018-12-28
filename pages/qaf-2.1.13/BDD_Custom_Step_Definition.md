---
title: BDD Custom Step Definition
sidebar: qaf_latest-sidebar
permalink: latest/BDD_Custom_Step_Definition.html
folder: latest
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
