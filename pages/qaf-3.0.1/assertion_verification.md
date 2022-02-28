---
title: Assertion/Verification
sidebar: qaf_3_0_1-sidebar
permalink: qaf-3.0.1/assertion_verification.html
folder: qaf-3.0.1
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

## customize assertion verification message

You can customize assertion verification message using properties

```
element.<operation>.pass
element.<operation>.fail
element.not<operation>.pass
element.not<operation>.fail

```
Where operation is lower-case assert/verification operation. For example
In `verifyPresent` operation is present so key will be

```
element.present.pass
element.present.fail
```

In verifyNotPresent operation is  notpresent

```
element.notpresent.pass
element.notpresent.fail
```
arguments supported in message:
	{0} - description of the element
	{1} - if value match, expected value
	{2} - if value match, actual value

Default message values for operation without value match

```
  element.<operation>.pass = Expected {0} <operation> : Actual {0} <operation>
  element.<operation>.fail = Expected {0} <operation> : Actual {0} not <operation>
```

Message for `verifyPresent()` and `assertPresent()`

```
  element.present.pass = Expected {0} present : Actual {0} present
  element.present.fail = Expected {0} present : Actual {0} not present
```

** Not Operation **

```
  element.<notoperation>.pass = Expected {0} not <operation> : Actual {0} not <operation>
  element.<notoperation>.fail = Expected {0} not <operation> : Actual {0} <operation>
```

Message for `verifyNotPresent()` and `assertNotPresent`

```
  element.notpresent.pass = Expected {0} not present : Actual {0} not present
  element.notpresent.fail = Expected {0} not present : Actual {0} present
```


Default message values for operation with value match

```
	element.<operation>.pass = Expected {0} <operation> should not be {1} : Actual {0} <operation> is {2}
	element.<operation>.fail = "Expected {0} <operation> should be {1} : Actual {0} <operation> is {2}
```
Message for verifyText and assertText

```
	element.text.pass = Expected {0} text should not be {1} : Actual {0} text is {2}
	element.text.fail = Expected {0} text should be {1} : Actual {0} text is {2}
```