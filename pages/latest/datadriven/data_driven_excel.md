---
title: Excel Data Provider
sidebar: qaf_latest-sidebar
permalink: latest/excel.html
tags: [datadriven]
folder: latest
---
Following is the example of xls data file used to provide test data where first row is header. The test gets executed 3 times as there are 3 data set in data file. It assumes by default first sheet. If sheet is not first sheet you need to provide sheet name using meta-key `sheetName`.

## Sheet per test case

| recId | Username | Password | Isvalid | ExpectedMsg |
|-------|---------|-------|--------|---------|
| Wrong password | chirag12 |  test123 | FALSE | Invalid Username Or Password. Please Try Again.|
| Wrong User name and Wrong Password | test | wrongtest | FALSE | Invalid Username Or Password. Please Try Again.|
| wrong Username | chirag	| abc123 | FALSE | Invalid Username Or Password. Please Try Again. |

##Usage
`sheetName` only required if sheet is not first sheet
####Java
```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.xls", sheetName="LoginSheet")

```
####BDD
```
Meta-data:{"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet"}
```

####BDD2
```
@dataFile:resources/data/logintestdata.xls
@sheetName:LoginSheet

```
Or

```
Examples: {"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet"}

```

####Gherkin
```
Examples: {"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet"}

```



## Excel data table
With data table, you can provide test data for more than one test case in one sheet. `sheetName` only required if sheet is not first sheet.

Below is xls data file represents two data tables with key `login` and `key2`.
	
| login | recId | Username | Password | Isvalid | ExpectedMsg | |
|-------|--------|---------|-------|--------|---------|---------|
|       | Wrong password | chirag12 |  test123 | FALSE | Invalid Username Or Password. Please Try Again.| |
|       | Wrong User name and Wrong Password | test | wrongtest | FALSE | Invalid Username Or Password. Please Try Again.|	|
|       | wrong Username | chirag	| abc123 | FALSE | Invalid Username Or Password. Please Try Again. | login |
|       |       |      |      |      | | |
| Key2  | recId | Col1 | col2 | coln | | |
|-------|--------|---------|-------|--------| | |
|       | identifier-1 | col1-val1 |  col2-val1 | coln-val1| | |
|       | identifier-3 | col1-val2 | col2-val2 | coln-val1 | | |
|       | identifier-2 | col1-val3 | col2-val3 | coln-val1 | Key2 | |

###Usage

####Java
```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.xls", sheetName="LoginSheet", key="login")

```
####BDD
```
Meta-data:{"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet", "key":"login"}
```

####BDD2
```
@dataFile:resources/data/logintestdata.xls
@sheetName:LoginSheet
@key:login
```
Or

```
Examples: {"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet", "key":"login"}

```

####Gherkin
```
Examples: {"dataFile":"resources/data/logintestdata.xls", "sheetName":"LoginSheet", "key":"login"}

```


