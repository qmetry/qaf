---
title: XML Data Provider
sidebar: qaf_3_1_0-sidebar
permalink: qaf-3.1.0/xml.html
tags: [datadriven]
folder: qaf-3.1.0
prev: maketest_data_driven.html
---


If we want to provide data using xml file below is the sample xml file. The test gets executed 4 times as there are 4 data set in data file.


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
The test case code will remain same only the change will be in meta-data as below. You need to specify xml file using `dataFile` **only if it is not in** [configured resources](managing_resources_for_different_env.html). 

## Usage
### Java
```java

@QAFDataProvider(dataFile = "data/logintestdata.xml", key = "login.data")

```
### BDD
```
Meta-data:{"dataFile":"data/logintestdata.xml", "key":"login.data"}
```

### BDD2
```
@dataFile:data/logintestdata.xml
@key:login.data
```
Or

```
Examples: {"dataFile":"data/logintestdata.xml", "key":"login.data"}

```

### Gherkin
```
Examples: {"dataFile":"data/logintestdata.xml", "key":"login.data"}

```


