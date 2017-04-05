---
title: Managing resources for Different Environments
sidebar: qaf_2_1_10-sidebar
permalink: qaf-2.1.10/managing_resources_for_different_env.html
folder: qaf-2.1.9
---

Assume that AUT has multiple environments to target. Assume that there are few locator, translation data, and property difference for each environment and others are common. Consider the dir structure below, where we have common dir as well as environment specific dir. While providing property/locator/translation data, we will put common data under appropriate file in common dir and provide environment specific values in files under environment specific dir.

**resources**

**application.properties**

**common**

	common.properties
	locators.properties
	translation.hi
	translation.en 
	translation.fr 

**env1**

	env.properties
	locators.properties
	translation.hi
	translation.en
	translation.fr

**env2**

	env.properties
	locators.properties
	translation.hi
	translation.en
	translation.fr

Now while configuring run, you can provide from where to load resources using property **env.resources**.

```properties
env.resources=resources/common;resources/env1
```
