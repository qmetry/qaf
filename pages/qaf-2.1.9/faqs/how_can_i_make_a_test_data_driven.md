---
title: How can I make a test Data driven?
sidebar: faq_sidebar
permalink: qaf-2.1.9/how_can_i_make_a_test_data_driven.html
folder: latest
---


To make any test data driven you can use QAFDataProvider annotation on test method. You can provide data in CSV, JSON, MS-Excel file or from database.


```java

@QAFDataProvider(dataFile = " resources/data/searchText.csv ")

@QAFDataProvider(dataFile = "resources/data/searchText.json")

@QAFDataProvider(dataFile = "resources/data/testdata.xls")

@QAFDataProvider(dataFile = "resources/data/testdata.xls", sheetName="TC2")

@QAFDataProvider(dataFile = "resources/data/testdata.xls", labelName="TC2")

@QAFDataProvider(sqlQuery = "select col1, col2 from tbl")
```
 

**To configure using property**

```properties

global.testdata=[<param>=value]

global.testdata=dataFile = "resources/data/${class}/${method}.csv"

global.testdata=dataFile = "resources/data/testdata.xls"; sheetName="${class}";labelName="${method}"

global.testdata=dataFile = "resources/data/${class}.xls"; sheetName="${ method }"

```
 

In above example you can notice ${class} and ${method} parameters are used which you can use as per your requirement.

**To set data provider parameters for individual test method you can provide property as below:**

```properties

<tc_name>.testdata =[<param>=value]

TC02.testdata=dataFile = "resources/data/testdata.xls"; sheetName="TC2"

TC01.testdata= sqlQuery = " select col1, col2 from tbl "

```
