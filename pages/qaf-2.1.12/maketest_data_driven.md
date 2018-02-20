---
title: Make Tests Data driven
sidebar: qaf_2_1_12-sidebar
permalink: qaf-2.1.12/maketest_data_driven.html
tags: [getting_started,datadriven]
folder: qaf-2.1.12
---

To make any test data driven you can use **QAFDataProvider** annotation on test method, where test get executed for each data set provided in external data file. QAF supports following file formats to provide data for data driven tests.

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
@QAFDataProvider(sqlQuery = "select col1, col2 from tbl")
@QAFDataProvider(dataFile = "resources/data/logintestdata.txt")
```

## CSV Data Provider

Following is the test where CSV data provider used to provide data set. The test gets executed 4 times as there are 4 data set in data file.

```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.csv")
@Test(description = "login functionality test")
public void login(Map<String, String> data) {
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
public void login(Map <String, String> data) {
    doLogin(data.get("Username"),data.get("Password"), data.get("Isvalid"));
    assertLoginMsg(data.get("ExpectedMsg"))
}
```

property to set data file path

```properties
test.login.datafile=resources/data/logintestdata.xls
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
public void login(Map <String, String> data) {
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
public void login(Map<String, String> data) {
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
public void login(Map <String, String> data) {
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
public void login(Map <String, String> data) {
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

## Configure/Override DataProvider

Moreover, you can configure/override QAFDataProvider parameters. To configure parameters globally for all data driven test you can set property **"global.testdata"**. Below are examples:

```properties
global.testdata=[<param>=value]
global.testdata=dataFile = "resources/data/${class}/${method}.csv"
global.testdata=dataFile= "resources/data/testdata.xls"; sheetName="${class}";labelName="${method}"
global.testdata=dataFile = "resources/data/${class}.xls"; sheetName="${ method }"
global.testdata=key="${ method }/data"
```

In above example you can notice ${class} and ${method} parameters are used which you can use as per the requirement.

```
To set data provider parameters for individual test method you can provide property as below:
<tc_name>.testdata=[<param>=value]
login.testdata=dataFile = "resources/data/testdata.xls"; sheetName="login"
login.testdata= sqlQuery = " select col1, col2,col3 from tbl "
```
Priority for parameters to take effect is:

   * Parameters set using test method specific test data property, i.e. "<tc_name>.testdata"
   * Parameters set using global test data property, i.e. "gloabal.testdata"
   * Parameters hardcoded in QAFDataProvider annotation itself
  

| data | recId | Username | Password | Isvalid | ExpectedMsg | |
|-------|--------|---------|-------|--------|---------|---------|
|      | Wrong password | chirag12 |  test123 | FALSE | Invalid Username Or Password. Please Try Again.| |
|      | Wrong User name and Wrong Password | test | wrongtest | FALSE | Invalid Username Or Password. Please Try Again.|	|
|      | wrong Username | chirag	| abc123 | FALSE | Invalid Username Or Password. Please Try Again. | data |

