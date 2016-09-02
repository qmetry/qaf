---
title: Creating test cases
sidebar: qaf_2_1_8_sidebar
permalink: qaf-2.1.8/creatingtestcases.html
folder: qaf-2.1.8
tags: [getting_started,scenario]
---

## Creating test cases
You can create a new test case or suit by extending WebDriverTestCase class. Now you can create test same as in TestNG. If you are new to TestNG here is the [documentation.](http://testng.org/doc/documentation-main.html/) 

On extending WebDriverTestCase following object will be available to use in your test.

**getTestBase()**

Get thread safe WebDriverTestBase Instance that provides webdriver object.

**getDriver()**

Provides a thread safe webdriver object same as getTestBase().getDriver().

**context**

Instance of ITestContext, test context which contains all the information for a given test run.

**props**

Instance of PropertyUtil, can be used to read property value from any of the properties file.

**Example**

```
java
public class SampleTestSuite extends WebDriverTestCase {
    @Test
    public void test1() {
        getDriver().get("/");
        // QAFWebElement fname = getDriver().findElement("fname locator");
        QAFWebElement fname = new QAFExtendedWebElement("fname locator");
        fname.verifyText(StringMatcher.exactIgnoringCase("expected text"));
    }
}
```
**Using Test Page in Test case**

```java
public class SampleTestSuite extends WebDriverTestCase {
    @Test
    public void test1() {
        MyTestPage page = new MyTestPage();
        page.launchPage(null);
        page.getFname().verifyText("expected text");
    }
}
```

## Grouping

Test can be grouped and can configure to run specific group or for dependency.

## Setting dependency

You can set method dependency as supported by [TestNG.](http://testng.org/doc/documentation-main.html#dependent-methods)

## Precondition and post condition

Use @before/afterTest annotation from TestNG. You can call getTestBase() to have test base object in such methods.
