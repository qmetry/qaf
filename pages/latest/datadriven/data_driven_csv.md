---
title: CSV Data Provider
sidebar: qaf_latest-sidebar
permalink: latest/csv.html
tags: [datadriven]
folder: latest
prev: maketest_data_driven.html
---

CSV data provider supports character separated values including standard comma separated values. Each line in file represents one record except blank line or comment line.  The file extension can be `csv` or `txt`. 

Following are examples of CSV data file used to provide test data where first line is header. The test gets executed 4 times as there are 4 data set in data file.

## Character separated values
In order to use any character other than `,` as separator character, you need to specify comment in file `#col.seperator=<char>`. You can use `\` as escape character. For instance with `|` as separator char you can provide column value `abc | def` as `abc \| def`.  Example-1 below uses `|` as separator character. 


_Example-1:_
**logintestdata.csv**

```csv
#col.seperator=|

user_name|password|isvalid
ruchita.shah@gmail.com|pwdtest|true
ruchita.shah@gmail.com|wrongpwd|false
wronguser@domain.com|pwdtest|false
wronguser@domain.com|wrongpwd|false 
```


## Comma separated values:
 
Both `abc` and `"abc"` are supported to represent string `abc`. Quoted string will eliminate use of escape
char for separator char, for example with `,` as separator char you can provide column value `abc, def` as `"abc, def"`
QAF supports industry standard comma separated values with quoted string. As per RFC 4180 CSV implementation leading or trailing spaces  are
trimmed (ignored), fields with such spaces as meaningful data must be quoted. Double quotes are not allowed in unquoted fields according to
RFC 4180.

_Example-2:_
**logintestdata.csv**

```csv
#this is comment
user_name,password,isvalid
ruchita.shah@gmail.com,pwdtest,true
ruchita.shah@gmail.com,wrongpwd,false
wronguser@domain.com,pwdtest,false
wronguser@domain.com,wrongpwd,false 
```

## Usage
### Java
```java
@QAFDataProvider(dataFile = "resources/data/logintestdata.csv")

```
### BDD
```
Meta-data:{"dataFile":"resources/data/logintestdata.csv"}
```

### BDD2
```
@dataFile:resources/data/logintestdata.csv

```
Or

```
Examples: {"dataFile":"resources/data/logintestdata.csv"}

```

### Gherkin
```
Examples: {"dataFile":"resources/data/logintestdata.csv"}

```


