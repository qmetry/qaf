---
title: Keyword Driven Define Test Suite
sidebar: qaf_2_1_8_sidebar
permalink: qaf-2.1.8/keyword_driven_define_test_suite.html
folder: qaf-2.1.8
---

Keyword driven test suite consist of one or more Scenarios. Each scenario represents one test case.

## FORMATION OF SUITE

```
SCENARIO|<name of the scenario>|{"description":"<meaningful description>"}
<<steps>>
END||
  
SCENARIO|<name of the scenario>|{"description":"<meaningful description>"}
<<steps>>
END||
```

{% include tip.html content="To start with you can use available pre-defined low level steps and author scenario." %} 
