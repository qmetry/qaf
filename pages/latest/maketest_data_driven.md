---
title: Make Tests Data driven
sidebar: qaf_latest-sidebar
permalink: latest/maketest_data_driven.html
tags: [getting_started,datadriven]
folder: latest
---

QAF enhances TestNG data provider by providing intercepter and in built data providers that supports different external data sources. 
## Meta data
Following are meta-data for test case used by QAF to specify data provider:

|Meta-data key|Type|Comments|
|-------|--------|---------|
|dataFile|Data-file path|csv, xml, json, xls  file for data driven scenario|
|sheetName|Text|Used for Excel file to provide sheet name for the data driven senario|
|key|Text|Used with xml data, to specify node of xml tree or data table key for excel data table|
|SQL query|Text|To use database, sql query is required to get data from database.|
|dataProviderClass|Fully qualified class name|required if you want to use custom data provider (since 2.1.12)|
|dataProvider|name of the data provider|required when you want to use custom data provider (since 2.1.12)|
|filter|logical expression|Filter to apply on data set returned by the data-provider that returns List of Maps.|
|indices|List|list of indices to filter|
|from|number|start index for range filter|
|to|number|end index for range filter|

## parameters in meta-value
You can use any property in value of meta-data for data provider. It will get resolved using configuration manager. In addition to  that following special  parameters will be available.
 * class - name of the java class
 * method - name of the java method
 * meta-key - any meta-key from test case meta-data

## Test data Filter
You can filter specific test data by providing logical expression or list of indices or range.

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

To make any test data driven you can use **@QAFDataProvider** or **@Metadata** annotation on java test method, where test get executed for each data set provided in external data file. QAF supports following file formats to provide data for data driven tests. For BDD and KWD you can specify it as scenario meta-data.

1. CSV
2. MS-EXCEL
3. JSON
4. XML
5. DATABASE
6. TXT

```java
@QAFDataProvider (dataFile = "resources/data/logintestdata.csv")
@QAFDataProvider(dataFile = "resources/data/testdata.xls")
@QAFDataProvider(dataFile = "resources/data/testdata.xls", sheetName="login")
@QAFDataProvider(dataFile = "resources/data/testdata.xls", key="login") //excel with data table
@QAFDataProvider(dataFile = "resources/data/logintestdata.json")
@QAFDataProvider(key="login.data") //xml key
@QAFDataProvider(dataFile = "resources/data/testdata.xml", key="login.data") //xml key

@QAFDataProvider(sqlQuery = "select col1, col2 from tbl")
@QAFDataProvider(dataFile = "resources/data/logintestdata.txt")
```

## CSV Data Provider

Following is the test where CSV data provider used to provide data set. The test gets executed 4 times as there are 4 data set in data file.

```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.csv")
@Test(description = "login functionality test")
public void login(Map<String, Object> data) {
    LoginUtil loginUtil = new LoginUtil();
    loginUtil.launchPage(null);
    Validator.verifyThat(loginUtil.doLogin(data.get("user_name"), data.get("password")),Matchers.equalTo(Boolean.valueOf(
    data.get("isvalid")));
}
```

property to set data file path  

```properties
test.login.datafile=resources/data/logintestdata.csv 
```

Below is csv data file for login test and first row is header.

**logintestdata.csv**

```csv
user_name,password,isvalid
ruchita.shah@gmail.com,pwdtest,true
ruchita.shah@gmail.com,wrongpwd,false
wronguser@domain.com,pwdtest,false
wronguser@domain.com,wrongpwd,false 
```

## Excel Data Provider
Following is the test where xls data provider used to provide data set. The test gets executed 3 times as there are 3 data set in data file.

```java
@QAFDataProvider(dataFile = "resources/logindata.xls", sheetName="LoginSheet", key="data")
@Test(description = "xls file as data provider ")
public void login(Map<String, Object> data) {
    doLogin(data.get("Username"),data.get("Password"), data.get("Isvalid"));
    assertLoginMsg(data.get("ExpectedMsg"))
}
```

Below is xls data file for login test and first row is header.

	
| data | recId | Username | Password | Isvalid | ExpectedMsg | |
|-------|--------|---------|-------|--------|---------|---------|
|      | Wrong password | chirag12 |  test123 | FALSE | Invalid Username Or Password. Please Try Again.| |
|      | Wrong User name and Wrong Password | test | wrongtest | FALSE | Invalid Username Or Password. Please Try Again.|	|
|      | wrong Username | chirag	| abc123 | FALSE | Invalid Username Or Password. Please Try Again. | data |

## JSON Data Provider

 Following is the test where JSON data provider used to provide data set. The test gets executed 3 times as there are 3 data set in data file.

 
```java

@QAFDataProvider(dataFile = "resources/logindata.json")
@Test(description = "json file as data provider ")
public void login(Map<String, Object> data) {
        doLogin(data.get("Username"),data.get("password"));
        assertLoginMsg(data.get("isvalid"))
}

````

property to set data file path

```properties
test.login.datafile=resources/data/logintestdata.json
```


Below is csv data file for login test and first row is header.

**logintestdata.json**

```
[
    { "Username" : "admin" , "password":"123abc123" , "isvalid":"false" },
    { "Username" : "Admin" , "password":"test" , "isvalid":"false" },
    { "Username" : "admin" , "password":"Chirag2193" , "isvalid":"true" }
]
```

## XML Data Provider

If we want to provide data using xml file below is the sample xml file:

```xml
<root>
    <!-- data for login test -->
    <login>
        <data>
            <user_name>ruchita.shah1989+02@gmail.com</user_name>
            <password>aravotest</password>
            <isvalid>true</isvalid>
        </data>
        <data>
            <user_name>ruchita.shah1989+02@gmail.com</user_name>
            <password>wrongpwd</password>
            <isvalid>false</isvalid>
        </data>
        <data>
            <user_name>wronguser@domain.com</user_name>
            <password>aravotest</password>
            <isvalid>false</isvalid>
        </data>
        <data>
            <user_name>wronguser@domain.com</user_name>
            <password>wrondpwd</password>
            <isvalid>false</isvalid>
        </data>
    </login>
    <!-- file may contain other data -->
</root>
```
The test case code will remain same only the change will be the parameter value of **QAFDataProvider** as below:

```java
@QAFDataProvider(key = "login.data")
@Test(description = "login functionality test")
public void login(Map<String, Object> data) {
    LoginUtil loginUtil = new LoginUtil();
    loginUtil.launchPage(null);
    Validator.verifyThat(loginUtil.doLogin(data.get("user_name"), data.get("password")),Matchers.equalTo(Boolean.valueOf(
    data.get("isvalid")));
}
```

## DATABASE Data Provider

Following is the testcase where sql query is required to get data from database.

```java
@QAFDataProvider(sqlQuery="select username,password,isvalid from login_table")
@Test(description = "Database as data provider ")
public void login(Map<String, Object> data) {
    doLogin(data.get("username"),data.get("password"));
    assertLoginMsg(data.get("isvalid"))
}
```

If user want to provide data using database below is the properties which user need to configure in property file as per the requirement.

```
db.driver.class=com.mysql.jdbc.Driver
db.connection.url=jdbc:mysql://localhost:3306/db
db.user=username
db.pwd=password

```

## Text Data Provider

```java
@QAFDataProvider(dataFile = "resources/logindata.txt")
@Test(description = "text file as data provider ")
public void login(Map<String, Object> data) {
    doLogin(data.get("Username"),data.get("Password"), data.get("Isvalid"));
    assertLoginMsg(data.get("ExpectedMsg"))
}
```

```
recId,Username,password,isValid,expectedMsg
Wrong Password,admin,admin,false,Invalid Username Or Password. Please Try Again.
Wrong Username and Password,Admin,Admin,false,Invalid Username Or Password. Please Try Again.
Wrong Username,admin,admin123,false,Invalid Username Or Password. Please Try Again.
```

