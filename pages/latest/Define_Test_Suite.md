---
title: Define Test Suite
sidebar: latest-sidebar
permalink: latest/Define_Test_Suite.html
folder: latest
tags: [bdd,scenario]
---

Behavior driven test suite consist of one or more Scenarios. Each scenario represents one test case. One can provide optional related information as background or narrative or user-story related to scenarios authored in the suite.


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

{% include tip.html content="To start with you can use available pre-defined low level steps and author scenario." %}
