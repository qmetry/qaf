---
title: Assertion/Verification
sidebar: qaf_2_1_7b-sidebar
permalink: qaf-2.1.7b/assertion_verification.html
folder: qaf-2.1.7b
tags: [java,checkpoint]
---

**Assertion Service** provides commonly used assertion methods in web application tests. It automatically logs result of assertion/verification with appropriate message and screenshot. When using webdriver API for development different assertion and verification methods are available with extended webelement object itself.

For example, assuming that firstName is of type QAFExtendedWebElement and you want to do assertion/Verifation test on firstName field, then below are some examples for that.

```java
//verify element present in DOM
firstName.verifyPresent();
firstName.assertPresent();
  
//verify element is visible
firstName.verifyVisible();
firstName.assertVisible();
  
//verify Text of Element
firstName.verifyText("First User");
firstName.assertText("First User");
  
//verify Text of element with StringMatchers conditions
firstName.verifyText(StringMatcher.contains("First User"));
firstName.assertText(StringMatcher.contains("First User"),"Username Validation");
  
//verify attributes(e.g. class, value, enabled) of element
firstName.verifyAttribute("class","Expected Class");
  
//verify css classs
firstName.assertCssClass("text-default");
firstName.verifyCssClass("text-default");
  
//verify css style
firstName.verifyCssStyle("text-size","14");
firstName.assertCssStyle("text-size","14");
  
//verify element is enabled
firstName.verifyEnabled();
firstName.assertEnabled();
```

## Validator Class

Validator class provides assertion/verification methods that supports hamcrest matchers.
For e.g:

```java
Validator.verifyThat(actual,Matchers.equalTo(expected));
```

