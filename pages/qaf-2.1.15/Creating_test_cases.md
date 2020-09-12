---
title: Creating test cases - Java
sidebar: qaf_2_1_15-sidebar
permalink: qaf-2.1.15/Creating_test_cases.html
folder: qaf-2.1.15
---


You can create a new test case or suit by extending `TestNGTestCase`. More specific flavor for web services is `WSTestCase` and for web and mobile is `WebDriverTestCase`. Now you can create test same as in TestNG. If you are new to TestNG, here is the [documentation](http://testng.org/doc/documentation-main.html).


## Meta-data
QAF allows you to add [meta-data](scenario-meta-data.html) for test case. 
  - You can use `@MetaData` annotation or use your custom annotation to provide meta-data. 
  - All annotations parameters will be collected as meta data. It includes annotations on test method and class annotations. 
  - Meta data provided at class level will be inherited by test meta-data. 

[meta-data](scenario-meta-data.html) can be used for test case selection by providing [meta-data filter](scenario_metadatata_filter_include_exclude_prop.html) and in listeners. To enforce certain meta-data you can specify [meta-data rules](meta-data-rules.html).

## Data-driven tests
You can create and use TestNG data provider. QAF has test data [filtering](data_driven_filter.html) and [intercepting](javadoc/com/qmetry/qaf/automation/testng/dataprovider/QAFDataProviderIntercepter.html) capabilities along with [inbuilt external data providers](maketest_data_driven.html) support. 

## Grouping

Test can be grouped and can be configured to run specific group or for dependency.

## Setting dependency

You can set method dependency as supported by TestNG.

## Precondition and post condition

Use @before/afterTest annotation from TestNG. You can call getTestBase() to have test base object in such methods.


## Examples
 
 ```java
@MetaData("{'story':'testing for fun'}")
public class SampleTestSuite extends TestNGTestCase {
    @MetaData("{'author':'me'}")
    @Test
    public void test1() {
		//your code goes here
    }
}
```

On extending WebDriverTestCase following object will be available to use in your test.

<b>getTestBase()</b>

 - When extending `WebDriverTestCase` Get thread safe WebDriverTestBase Instance that provides webdriver object.

<b>getDriver()</b>

Provides a thread safe webdriver object same as getTestBase().getDriver().

<b>context</b>

Instance of ITestContext, test context which contains all the information for a given test run.

<b>props</b>

Instance of PropertyUtil, can be used to read property value from any of the properties file.


Below are example for web/mobile test case:

```java
@MetaData("{'story':'testing for fun'}")
public class SampleTestSuite extends WebDriverTestCase {
    @MetaData("{'author':'me'}")
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
@MetaData("{'story':'testing for fun'}")
public class SampleTestSuite extends WebDriverTestCase {
    
    @MetaData("{'author':'me'}")
    @Test
    public void test1() {
        MyTestPage page = new MyTestPage();
        page.launchPage(null);
        page.getFname().verifyText("expected text");
    }
}
```
