---
title: Creating test cases
sidebar: qaf_2_1_9-sidebar
permalink: qaf-2.1.9/Creating_test_cases.html
folder: qaf-2.1.9
---


You can create a new test case or suit by extending **WebDriverTestCase** class. Now you can create test same as in TestNG. If you are new to TestNG, here is the [documentation](http://testng.org/doc/documentation-main.html). 

On extending WebDriverTestCase following object will be available to use in your test.

<b>getTestBase()</b>

Get thread safe WebDriverTestBase Instance that provides webdriver object.

<b>getDriver()</b>

Provides a thread safe webdriver object same as getTestBase().getDriver().

<b>context</b>

Instance of ITestContext, test context which contains all the information for a given test run.

<b>props</b>

Instance of PropertyUtil, can be used to read property value from any of the properties file.

<i><u>Example</u></i>

```java
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

<i><u>Using Test Page in Test case</u></i>

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

Test can be grouped and can be configured to run specific group or for dependency.

## Setting dependency

You can set method dependency as supported by TestNG.

## Precondition and post condition

Use @before/afterTest annotation from TestNG. You can call getTestBase() to have test base object in such methods.
