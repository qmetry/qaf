---
title: Page launch strategy
sidebar: qaf_2_1_9-sidebar
permalink: qaf-2.1.9/page_launch_strategy.html
folder: latest
tags: [page,testng,java]
---


By default page strategy is launch only if required. You can set strategy by using setLaunchStrategy method.

Available launch strategies are *onlyIfRequired, alwaysRelaunchFromParent* and *alwaysRelaunchFromRoot* which are defined by enum LaunchStrategy.


To set specific strategy for all test one can set appropriate strategy in page constructor. In case of test specific requirement set appropriate strategy for the page object created in test method. For example

following code will set launch strategy for test case only:


```java
@Test
public void testLogin(Map<String, String> params) {
LoginPage loginPage = new LoginPage();
// Should not have any prior error message.loginPage.setLaunchStrategy(LaunchStrategy.alwaysRelaunchFromRoot);
---
---
```
