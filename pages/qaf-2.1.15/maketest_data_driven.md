---
title: Make Tests Data driven
sidebar: qaf_2_1_15-sidebar
permalink: qaf-2.1.15/maketest_data_driven.html
tags: [getting_started,datadriven]
folder: qaf-2.1.15
---

QAF enhances TestNG data provider by providing intercepter and in built data providers that supports different external data sources. 
To make any test data driven you can use `@QAFDataProvider` or `@Metadata` annotation on java test method, where test get executed for each data set provided in external data file. For BDD and KWD you can specify it as scenario meta-data.

## In-built data-providers
 - [CSV](csv.html)
 - [XML](xml.html)
 - [JSON](json.html)
 - [Excel](excel.html)
 - [Database](database.html)
 
## Meta data
Following are meta-data for test case used by QAF to specify data provider:

|Meta-data key|Type|Comments|
|-------|--------|---------|
|dataFile|Data-file path|csv, xml, json, xls  file for data driven scenario|
|sheetName|Text|Used for Excel file to provide sheet name for the data driven scenario|
|key|Text|Used with xml data, to specify node of xml tree or data table key for excel data table|
|SQL query|Text|To use database, sql query is required to get data from database.|
|dataProviderClass|Fully qualified class name|required if you want to use custom data provider (since 2.1.12)|
|dataProvider|name of the data provider|required when you want to use custom data provider (since 2.1.12)|
|filter|logical expression|Filter to apply on data set returned by the data-provider that returns List of Maps. (since 2.1.14)|
|indices|List|list of indices (base 0) to filter (since 2.1.14)|
|from|number|start index (base 1) for range filter (since 2.1.14)|
|to|number|end index (base 1) for range filter (since 2.1.14)|

### parameters in meta-value
You can use any property in value of meta-data for data provider. It will get resolved using configuration manager. In addition to  that following special  parameters will be available.
 * class - name of the java class
 * method - name of the java method
 * meta-key - any meta-key from test case meta-data

## Test data Filter
You can [filter specific test data](data_driven_filter.html) by providing logical expression or list of indices or range.

## Data provider Intercepter

Implementation of [QAFDataProviderIntercepter](javadoc/com/qmetry/qaf/automation/testng/dataprovider/QAFDataProviderIntercepter.html) interface can be registered as `QAFListener` to intercept test data provided by Data provider in data driven test case. you can use it to process or apply filter.

## Configure/Override DataProvider

Moreover, you can configure/override DataProvider Meta-data. To configure meta-data globally for all data driven test you can set property **"global.testdata"**. Below are examples:

```properties
global.testdata.<meta-key>=<meta-value>
 #
global.testdata.dataFile=resources/data/${class}/${method}.csv

 #setting data file and sheet name to use excel file
global.testdata.dataFile=resources/data/${class}.xls
global.testdata.sheetName=${method}

 # multiple meta-data as map
global.testdata={<meta-key>:<meta-value>}
global.testdata={'dataFile':'resources/data/${class}/${method}.csv'}
global.testdata={'dataFile': 'resources/data/testdata.xls', 'sheetName':'${class}','labelName':'${method}'}
global.testdata=dataFile = "resources/data/${class}.xls"; sheetName="${ method }"
global.testdata=key="${ method }.data"
```

In above example you can notice ${class} and ${method} parameters are used which you can use as per the requirement.

```
To set data provider parameters for individual test method you can provide property as below:
<tc_name>.testdata={<property>:<value>}
login.testdata={'dataFile': 'resources/data/testdata.xls','sheetName':'login'}
login.testdata= {'sqlQuery':'select col1, col2,col3 from tbl'}
```
Priority for meta-data to take effect is:

   * Meta-data set using test method specific test data property, i.e. "<tc_name>.testdata"
   * Meta-data set using global test data property, i.e. "gloabal.testdata"
   * Meta-data provided with test case using annotation or as scenario meta-data

## Compete Example
Consider following test data specified in CSV file.

| recId | username | password | isvalid | expected_msg |
|-------|---------|-------|--------|---------|
| Wrong password | chirag12 |  test123 | false | Invalid Username Or Password. Please Try Again.|
| Wrong User name and Wrong Password | test | wrongtest | false | Invalid Username Or Password. Please Try Again.|
| wrong Username | chirag	| abc123 | false | Invalid Username Or Password. Please Try Again. |


```
recId,username,password,isvalid,expected_msg
Wrong Password,admin,admin,false,Invalid Username Or Password. Please Try Again.
Wrong Username and Password,Admin,Admin,false,Invalid Username Or Password. Please Try Again.
Wrong Username,admin,admin123,false,Invalid Username Or Password. Please Try Again.
```


### java
```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.csv")
@Test(description = "login functionality test")
public void login(Map<String, Object> data) {
    LoginPage loginPage = new LoginPage();
    loginPage.launchPage(null);
    boolean status = loginUtil.doLogin(data.get("username"), data.get("password"))
    Validator.verifyThat(status, Matchers.equalTo(data.get("isvalid"));
    loginPage.getErrorMessage().verifyText(data.get("expected_msg"));
}
```
### BDD
```
Scenario: example
Meta-data: {"dataFile":"resources/data/logintestdata.csv"}
	Given use is on login page
	When login using '${username}' and '${password}'
	And store into 'status'
	Then verify that '${status}' is '${isvalid}'
	And verify error message '${expected_msg}'
```
## Method arguments (Java Only)
Map argument

```java
@QAFDataProvider (dataFile = "resources/data/logintestdata.csv")
@Test(description = "login functionality test")
public void login(Map<String, Object> data) {
   //implementation goes here
}
```
one or more complex arguments
```java
@QAFDataProvider (dataFile = "resources/data/logintestdata.csv")
@Test(description = "login functionality test")
public void login(LoginBean user) {
   //implementation goes here
}

@QAFDataProvider (dataFile = "resources/data/mytestdata.csv")
@Test(description = "login functionality test")
public void myTest(LoginBean user, Item item) {
   //implementation goes here
}
```
