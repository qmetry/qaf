---
title: KWD Custom Step Definition
sidebar: qaf_3_1_0-sidebar
permalink: qaf-3.1.0/KWD_Custom_Step_Definition.html
folder: qaf-3.1.0
tags: [kwd,scenario]
---

Steps can be defined in kwl

The Basic Step definition is following. 
 
```
STEP-DEF|<stepName>|{"description":"<meaningfull step description>",<meta-key>:<meta-value>}
<first stepName>|<step input parameters>|<step out parameters>
...
<nth stepName>|<step input parameters>|<step out parameters>
END||
```


While execution make sure required kwl files are mentioned in step.provider.pkg property as below

```properties
step.provider.pkg=com.test;scenarios
```
